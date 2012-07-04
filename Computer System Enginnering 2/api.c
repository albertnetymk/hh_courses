#include "kernel.h"
#include "kernel_hwdep.h"
#include "api.h"
#include "memory.h"
#include <string.h>
#include <limits.h>

//Global variables
TCB *Running;
list *readylist;
list *timerlist;
list *waitinglist;
uint tickCounter;

// one linked list, the underlying data structure used by this kernel
static list *newList(void);
static TCB *newTCBObj(void (*PC)(), unsigned int deadLine);
static int insertR(list *sentList, TCB *TCBObj);
static void removeFirst(list *sentList);
static void removeList(list *sentList);
static void moveFirst(list *listFrom, list *listTo, int sortedby);
static void move(listobj *object, list *listFrom, list *listTo,
		int sortedby);
static listobj *newListObj(TCB *TCBObj);
static void insert(list *sentList, listobj *element,int sortedby);
static int isEmpty(list *sentList);

// private function declaration
static void removeFirstMsg(mailbox *mBox);
static void appendMsg(mailbox *mBox, msg *tmpMsg, int type);
static void removeMsg(mailbox *mBox, msg *tmpMsg);

/** This is an idle task when nothing is happening */
static void task_idle(void)
{
	while(1);
}

/**
 * This function initializes the kernel and its data
 * structures and leaves the kernel in start-up mode. The
 * init_kernel call must be made before any other call is
 * made to the kernel.
 *		@return Description of this function status
 */
exception init_kernel()
{
	tickCounter = 0;
	//Create 3 linked lists
	timerlist = (list *)newList();
	if(timerlist == NULL) {
		return FAIL;
	}
	readylist = (list *)newList();
	if(readylist == NULL) {
		return FAIL;
	}
	waitinglist = (list *)newList();
	if(waitinglist == NULL) {
		return FAIL;
	}
	//create an idle task with infinite deadline
	create_task(&task_idle,UINT_MAX);
	return OK;
}

/**
 * This function creates a task. If the call is made in startup
 * mode, i.e. the kernel is not running, only the
 * necessary data structures will be created. However, if
 * the call is made in running mode, it will lead to a
 * rescheduling and possibly a context switch.
 * Argument
 * @param *task_body A pointer to the C function holding the code of the task.
 * @param deadline The kernel will try to schedule the task so it will meet this deadline.
 * @return Description of the functions status, i.e. FAIL/OK.
 */
exception create_task(void(*task_body)(), uint deadline)
{
	/*
	 * Two steps are needed to create one task. Firstly, one TCB is
	 * constructed. Then the TCB is inserted into the list.
	 */
	TCB *task;
	task = (TCB *)newTCBObj(task_body, deadline);
	if(task == NULL) {
		return FAIL;
	}

	if(Running == NULL) {
		// Startup mode
		if(insertR(readylist, task) == FAIL) {
			// clean the first step
			albert_free(task);
			return FAIL;
		}
		return OK;
		// don't need to update running pointer
	} else {
		volatile int first = 1;
		// disable interrupts
		isr_off();
		SaveContext();
		if(first) {
			//set not first execution
			first = 0;
			if(insertR(readylist, task) == FAIL) {
				// clean the first step
				albert_free(task);
				return FAIL;
			}
			// update running pointer
			Running = readylist->pHead->pNext->pTask;
			// if the new created task has tighter deadline,
			// LoadContext() will redirect us to the new task,
			// otherwise we will just go to the line below the
			// SaveContext()
			LoadContext();
		}
	}
	return OK;
}

/**
  This function starts the kernel and thus the system of
  created tasks. Since the call will start the kernel it will
  leave control to the task with tightest deadline.
  Therefore, it must be placed last in the application
  initialization code. After this call the system will be in
  running mode. */
void run()
{
	// Initialize interrupt timer here
	timer0_start();
	// set Running to point to the task with the tightest deadline
	Running = readylist->pHead->pNext->pTask;
	LoadContext();
}

/**
  This call will terminate the running task. All data
  structures for the task will be removed. Thereafter,
  another task will be scheduled for execution. */
void terminate()
{
	// terminate can only be called by the running task, which is in
	// the first position of ready list
	removeFirst(readylist);
	Running = readylist->pHead->pNext->pTask;
	// go to the next task
	LoadContext();
}

/**
 * This call will create a Mailbox. The Mailbox is a FIFO
 * communication structure used for asynchronous and synchronous
 * communication between tasks.
 *		@param nof_msg: maximum number of messages the mailbox can
 *		hold
 *		@param size_of_msg: the size of one message in the mailbox
 */
mailbox *create_mailbox(int nof_msg, int size_of_msg)
{
	mailbox *mBox = (mailbox *) albert_malloc(1*sizeof(mailbox));
	if(mBox == NULL) {
		return NULL;
	}
	mBox->pHead = (msg *) albert_malloc(1*sizeof(msg));
	if(mBox->pHead == NULL) {
		albert_free(mBox);
		return NULL;
	}
	mBox->pTail = (msg *) albert_malloc(1*sizeof(msg));
	if(mBox->pTail == NULL) {
		albert_free(mBox->pHead);
		albert_free(mBox);
		return NULL;
	}
	mBox->pHead->pNext = mBox->pTail;
	mBox->pTail->pPrevious = mBox ->pHead;
	mBox->nDataSize = size_of_msg;
	mBox->nMaxMessages = nof_msg;
	// only one of these variables will be used at one time
	mBox->nMessages = 0;
	mBox->nBlockedMsg = 0;
	return mBox;
}

/**
  This call will remove the Mailbox if it is empty and return
  OK. Otherwise no action is taken and the call will return
  NOT_EMPTY. */
exception remove_mailbox(mailbox *mBox)
{
	if(mBox->nMessages == 0 && mBox->nBlockedMsg == 0) {
		albert_free(mBox->pHead);
		albert_free(mBox->pTail);
		albert_free(mBox);
		return OK;
	} else {
		return NOT_EMPTY;
	}
}

/**
 * This call will send a message to the specified mailbox. If there is
 * a receiving task(which must be a blocked task) waiting for a
 * message on the specified mailbox, send_wait will deliver it and the
 * receiving task will be moved to the ready list. Otherwise, if there
 * is not a receiving task waiting for a message on the specified
 * mailbox, the sending task will be blocked. During the blocking
 * period of the task its deadline might be reached. At that point
 * the blocked task will be resumed with the exception:
 * DEADLINE_REACHED. Note: Blocked and unblocked messages shall
 * not be mixed in the same mailbox.
 */
exception send_wait(mailbox *mBox, void *Data)
{
	volatile int first;
	int returnValue;
	returnValue = FAIL;
	first = 1;
	isr_off();
	SaveContext();
	if(first) {
		first = 0;
		if(no_message(mBox) != 0
				&& mBox->pHead->pNext->Status == RECEIVER) {
			// someone is waiting in this mailbox
			memcpy(mBox->pHead->pNext->pData, Data, mBox->nDataSize);
			// this must be a block message
			// cut the relation between the task and the message
			mBox->pHead->pNext->pBlock->pMessage = NULL;
			move(mBox->pHead->pNext->pBlock,
					waitinglist, readylist, 1);
			// update running pointer
			Running = readylist->pHead->pNext->pTask;
			removeFirstMsg(mBox);
		} else {
			msg *tmpMsg;
			// we need to put one mail in this mailbox
			// we assume that there is enough room this message
			// the new message
			tmpMsg = (msg *) albert_malloc(sizeof(msg));
			if(tmpMsg == NULL) {
				return FAIL;
			}

			// initialize values in this message
			tmpMsg->pData = Data;
			tmpMsg->Status = SENDER;
			tmpMsg->pBlock = readylist->pHead->pNext;
			// associate the message to the object
			readylist->pHead->pNext->pMessage = tmpMsg;

			appendMsg(mBox, tmpMsg, 0);
			moveFirst(readylist, waitinglist, 0);
			// update running pointer
			Running = readylist->pHead->pNext->pTask;
		}
		LoadContext();
	} else {
		if(tickCounter >= Running->DeadLine) {
			msg *tmpMsg = readylist->pHead->pNext->pMessage;
			removeMsg(mBox, tmpMsg);
			readylist->pHead->pNext->pMessage = NULL;
			returnValue = DEADLINE_REACHED;
		} else {
			returnValue = OK;
		}
	}
	return returnValue;
}

/**
 * This call will attempt to receive a message from the specified
 * Mailbox. If there is a blocked or unblocked message waiting for
 * receiving on the specified mailbox, this function will collect it.
 * If the message was of block type, the sending task will be moved to
 * the ready list. Otherwise, if there is not a send Message (of either
 * type) waiting on the specified mailbox, the receiving task will be
 * blocked. In both cases (blocked or not blocked) a new task schedule
 * is done and possibly a context switch. During the blocking period of
 * the task its deadline might be reached. At that point in time the
 * blocked task will be resumed with the exception: DEADLINE_REACHED.
 */
exception receive_wait(mailbox *mBox, void *Data)
{
	volatile int first;
	int returnValue;
	returnValue = FAIL;
	first = 1;
	isr_off();
	SaveContext();
	if(first) {
		first = 0;
		if(no_message(mBox) != 0
				&& mBox->pHead->pNext->Status == SENDER) {
			// there are some messages in this mailbox
			memcpy(Data, mBox->pHead->pNext->pData, mBox->nDataSize);
			if(mBox->nBlockedMsg != 0) {
				// this is a block message
				// cut the relation between the task and the message
				mBox->pHead->pNext->pBlock->pMessage = NULL;
				move(mBox->pHead->pNext->pBlock,
						waitinglist, readylist, 1);
				// update running pointer
				Running = readylist->pHead->pNext->pTask;
				removeFirstMsg(mBox);
			} else {
				// this is an unblocked message
				removeFirstMsg(mBox);
			}
		} else {
			msg *tmpMsg;
			// we need to put one mail in this mailbox
			// we assume that there is enough room this message
			// the new message
			tmpMsg = (msg *) albert_malloc(sizeof(msg));
			if(tmpMsg == NULL) {
				return FAIL;
			}
			// initialize values in this message
			tmpMsg->pData = Data;
			tmpMsg->Status = RECEIVER;
			tmpMsg->pBlock = readylist->pHead->pNext;
			// associate the message to the object
			readylist->pHead->pNext->pMessage = tmpMsg;

			appendMsg(mBox, tmpMsg, 0);
			moveFirst(readylist, waitinglist, 0);
			// update running pointer
			Running = readylist->pHead->pNext->pTask;
		}
		LoadContext();
	} else {
		if(tickCounter >= Running->DeadLine) {
			msg *tmpMsg = readylist->pHead->pNext->pMessage;
			removeMsg(mBox, tmpMsg);
			readylist->pHead->pNext->pMessage = NULL;
			returnValue = DEADLINE_REACHED;
		} else {
			returnValue = OK;
		}
	}
	return returnValue;
}

/**
 * This call will send a message to the specified mailbox. The sending
 * task will continue execution after the call. When the mailbox is
 * full, the oldest message will be dropped. The send_no_wait call will
 * probably unblocked one task, which imply a new scheduling and
 * possibly a context switch. Note: blocked and unblocked messages
 * shall not be mixed in the same Mailbox.
 */
exception send_no_wait(mailbox *mBox, void *Data)
{
	volatile int first = 1;
	isr_off();
	SaveContext();
	if(first) {
		first = 0;
		if(no_message(mBox) != 0
				&& mBox->pHead->pNext->Status == RECEIVER) {
			memcpy(mBox->pHead->pNext->pData, Data, mBox->nDataSize);
			// this must be a block message
			// cut the relation between the task and the message
			mBox->pHead->pNext->pBlock->pMessage = NULL;
			move(mBox->pHead->pNext->pBlock,
					waitinglist, readylist, 1);
			// update running pointer
			Running = readylist->pHead->pNext->pTask;
			removeFirstMsg(mBox);
		} else {
			msg *tmpMsg;
			// we need to put one mail in this mailbox
			if(mBox->nMaxMessages <= mBox->nMessages) {
				// cut the relation between the task and the message
				mBox->pHead->pNext->pBlock->pMessage = NULL;
				removeFirstMsg(mBox);
			}
			// the new message
			tmpMsg = (msg *) albert_malloc(sizeof(msg));
			if(tmpMsg == NULL) {
				return FAIL;
			}

			tmpMsg->pData = albert_malloc(mBox->nDataSize);
			if(tmpMsg->pData == NULL) {
				albert_free(tmpMsg);
				return FAIL;
			}
			memcpy(tmpMsg->pData, Data, mBox->nDataSize);
			tmpMsg->Status = SENDER;
			tmpMsg->pBlock = NULL;

			appendMsg(mBox, tmpMsg, 1);
		}
		LoadContext();
	}
	return OK;
}

/**
 * This call will attempt to receive a message from the specified
 * mailbox. The calling task will continue execution after this call.
 * When there is no send message available, or if the mailbox is
 * empty, the function will return FAIL. Otherwise, OK is returned.
 * The call might imply a new scheduling and possibly a context
 * switch. This function is very different from three other IPC
 * functions. In fact, it makes perfect sense that this function will
 * not create any message, for it can not get the reply while this
 * task is alive,
 */
exception  receive_no_wait(mailbox *mBox, void *Data)
{
	volatile int first = 1;
	isr_off();
	SaveContext();
	if(first) {
		first = 0;
		if(no_message(mBox) != 0
				&& mBox->pHead->pNext->Status == SENDER) {
			memcpy(Data, mBox->pHead->pNext->pData,
					mBox->nDataSize);
			if(mBox->nBlockedMsg != 0) {
				// this is a block message
				// cut the relation between the task and the message
				mBox->pHead->pNext->pBlock->pMessage = NULL;
				move(mBox->pHead->pNext->pBlock,
						waitinglist, readylist, 1);
				// update running pointer
				Running = readylist->pHead->pNext->pTask;
				removeFirstMsg(mBox);
			} else {
				// this is an unblocked message
				removeFirstMsg(mBox);
			}
		} else {
			return FAIL;
		}
		LoadContext();
	}
	return OK;
}

/**
  This call will block the calling task until the given
  number of ticks has expired. */
exception wait(uint nTicks)
{
	volatile int first;
	int returnValue;
	first = 1;
	returnValue = FAIL;
	// disable interrupt
	isr_off();
	SaveContext();
	if(first == 1) {
		first = 0;
		readylist->pHead->pNext->nTCnt = tickCounter + nTicks;
		moveFirst(readylist, timerlist, -1);
		// update running pointer
		Running = readylist->pHead->pNext->pTask;
		LoadContext();
	} else {
		if(Running->DeadLine >= tickCounter) {
			returnValue = DEADLINE_REACHED;
		} else {
			returnValue = OK;
		}
	}
	return returnValue;
}

/**
  This call will set the tick counter to the given value. */
void set_ticks(uint nTicks)
{
	tickCounter = nTicks;
}

/**
  This call will return the current value of the tick counter */
uint ticks(void)
{
	return tickCounter;
}

/**
  This call will return the deadline of the specified task */
uint deadline(void)
{
	return Running->DeadLine;
}

/**
  This call will set the deadline for the calling task. The
  task will be rescheduled and a context switch might
  occur. */
void set_deadline(uint deadline)
{
	volatile int first = 1;
	// disable interrupts
	isr_off();
	SaveContext();
	if(first) {
		first = 0;
		Running->DeadLine = deadline;
		// to invoke the reschedule
		moveFirst(readylist, readylist, 1);
		// update running pointer
		Running = readylist->pHead->pNext->pTask;
		LoadContext();
	}
}

/**
  This function is not available for the user to call.
  It is called by an ISR (Interrupt Service Routine)
  invoked every tick. Note, context is automatically saved
  prior to call and automatically loaded on function exit. */
void TimerInt(void)
{
	listobj *iterator;
	tickCounter++;
	// it is possible that the task in timerlist expires, but we don't
	// need to check that, it is the user's job to check that and
	// avoid that.
	iterator = timerlist->pHead->pNext;
	// for timer list
	while(iterator != timerlist->pTail) {
		if(iterator->nTCnt <= tickCounter) {
			iterator = iterator->pNext;
			moveFirst(timerlist, readylist, 1);
			// update running pointer
			Running = readylist->pHead->pNext->pTask;
		} else {
			break;
		}

	}
	// for waiting list
	iterator = waitinglist->pHead->pNext;
	while(iterator != waitinglist->pTail) {
		if(iterator->pTask->DeadLine <= tickCounter) {
			iterator = iterator->pNext;
			// since this one will be in the first position in readylist,
			// and after LoadContext(), this task will run, and then we
			// will go to send_wait()
			moveFirst(waitinglist, readylist, 1);
			// update running pointer
			Running = readylist->pHead->pNext->pTask;
		} else {
			break;
		}

	}
}

/**
 * return the number messages in this mailbox
 */
int no_message(mailbox *mBox)
{
	return mBox->nMessages + mBox->nBlockedMsg;
}

/**
 * This function is hardware dependent. All relevant
 * registers are saved to the TCB of the currently running
 * task.
 */
void SaveContext (void);

/**
  This function is hardware dependent. All relevant
  registers are restored from the TCB of the currently
  running task to the CPU registers. */
void LoadContext (void);

/**
  This function is not available for the user to call.
  It is an ISR (Interrupt Service Routine) invoked every
  tick. Note. It calls the C-Function TimerInt(). */
void timer0_isr (void);

/**
 * remove the first message in this mailbox
 * when this function is invoked, such assumption, that at least one
 * message is in this mailbox, is built.
 */
static void removeFirstMsg(mailbox *mBox)
{
	if(mBox->nBlockedMsg != 0) {
		// this mailbox only contains blocked messages, if any
		msg *tmpMsg = mBox->pHead->pNext;
		// unlink it from this mailbox
		mBox->pHead->pNext->pNext->pPrevious = mBox->pHead;
		mBox->pHead->pNext = mBox->pHead->pNext->pNext;
		// free its storage
		albert_free(tmpMsg);
		(mBox->nBlockedMsg)--;
	} else {
		// this mailbox only contains unblocked messages, if any
		msg *tmpMsg = mBox->pHead->pNext;
		// unlink it from this mailbox
		mBox->pHead->pNext->pNext->pPrevious = mBox->pHead;
		mBox->pHead->pNext = mBox->pHead->pNext->pNext;
		// free its storage
		// since this one unblocked message, one extra storage is used
		// for the data
		albert_free(tmpMsg->pData);
		albert_free(tmpMsg);
		(mBox->nMessages)--;
	}
}

/**
 * append one message to the end of this mailbox. type indicates that it is blocked(0) or unblocked(1)
 */
static void appendMsg(mailbox *mBox, msg *tmpMsg, int type)
{
	tmpMsg->pPrevious = mBox->pTail->pPrevious;
	tmpMsg->pNext = mBox->pTail;
	mBox->pTail->pPrevious->pNext = tmpMsg;
	mBox->pTail->pPrevious = tmpMsg;
	if(type == 1) {
		(mBox->nMessages)++;
	} else {
		(mBox->nBlockedMsg)++;
	}

}

/**
*/
static void removeMsg(mailbox *mBox, msg *tmpMsg)
{
	// unlink it from the list
	tmpMsg->pPrevious->pNext = tmpMsg->pNext;
	tmpMsg->pNext->pPrevious = tmpMsg->pPrevious;
	// remove the corresponding message
	albert_free(tmpMsg);
	(mBox->nBlockedMsg)--;
}

/**
 * this function is meant to be called for debugging
 */
void removeTask()
{
	removeFirst(readylist);
}

/** Creates a empty list.
 *       Allocates memory and defines the basic structure for the list
 *       @return Returns the pointer to the empty list.
 */
static list *newList(void)
{
	list *retList;
	listobj *pHead, *pTail;
	retList = (list *) albert_malloc(1*sizeof(list));
	if(retList==NULL) {
		return NULL;
	}
	pHead=(listobj *) albert_malloc(1*sizeof(listobj));
	if(pHead==NULL) {
		albert_free(retList);
		return NULL;
	}
	pTail=(listobj *) albert_malloc(1*sizeof(listobj));
	if(pTail==NULL) {
		albert_free(pHead);
		albert_free(retList);
		return NULL;
	}
	retList->pHead=pHead;
	retList->pTail=pTail;

	retList->pHead->pNext=retList->pTail;
	// we do not need to care about the pPrevious in pTail, for we
	// will update them when we insert the first element
	return retList;
}

/**
 * Create a new list object. This function is not intended to be
 * called by the user. It is called when one TCB object is inserted
 * into one list
 *		@param: TCBObj: the tasked wrapped insied one list object
 *		@return: the list object
 */
static listobj *newListObj(TCB *TCBObj)
{
	listobj *tmpListObj;
	tmpListObj = (listobj *) albert_malloc(1*sizeof(listobj));
	if(tmpListObj == NULL) {
		return NULL;
	}
	tmpListObj->pTask = TCBObj;
	tmpListObj->pMessage = NULL;
	return tmpListObj;
}

/**
 * Create one TCB.
 *		@param: PC, program counter, pointing to the next execution
 *		@param: deadLine, deadline for this task
 *		@return: TCB object
 */
static TCB *newTCBObj(void (*PC)(), unsigned int deadLine)
{
	TCB *tmpTCBObj;
	tmpTCBObj = (TCB *) albert_malloc(1*sizeof(TCB)); //Allocate memory for the TCB object
	if(tmpTCBObj == NULL) {
		// printf("Memory failure in newTCBObj: can't allocate memory for tmpTCBObj.\n");
		return NULL;
	}
	tmpTCBObj->SPSR = 0;
	tmpTCBObj->PC = PC;
	tmpTCBObj->SP = &(tmpTCBObj->StackSeg[STACK_SIZE-1]);
	tmpTCBObj->DeadLine=deadLine;
	return tmpTCBObj;
}

/** Insert ready list
 *		@param sentList: the ready list
 * 		@param TCBObj: the element to be inserted
 */
static int insertR(list *sentList, TCB *TCBObj)
{
	listobj *tmpListObj = newListObj(TCBObj);
	if(tmpListObj == NULL) {
		return FAIL;
	}
	insert(sentList, tmpListObj,1);
	return OK;
}

#ifdef DEBUG
static void printList(list *sentList)
{
	if(isEmpty(sentList)) {
		// printf("The list is empty.\n");
	}
	else
	{
		listobj *tmpListObj = sentList->pHead->pNext;
		while(tmpListObj!=sentList->pTail)
		{
			/*
			printf("******************************\n");
			printf("NEW LIST OBJECT!!!!\n");
			printf("******************************\n");
			printf("This address = %p\n",tmpListObj);
			printf("nTCNT = %d\n",tmpListObj->nTCnt);
			printf("Message: %d\n", \
			    tmpListObj->pMessage->nDada);
			printf("Address pPrevius = %p\n",\
			    tmpListObj->pPrevious);
			printf("Address pNext = %p\n",tmpListObj->pNext);
			printf("Deadline: %d\n",\
			    tmpListObj->pTask->DeadLine);
			printf("PC: %p\n",tmpListObj->pTask->PC);
			*/
			tmpListObj = tmpListObj->pNext;
		}
	}
}
#endif

/**
 * This function unlink the first element in the list and frees memory
 * for it.
 * @param sentList the target list
 */
static void removeFirst(list *sentList)
{
	/*
	 * This function is reversing the effect of create_task().
	 * Considering that two steps are needed to construct one task, we
	 * will need two steps to remove the task. Firstly, remove the
	 * TCB. Secondly, remove the listobj.
	 */
	// we are guaranteed that this list is not empty when this
	// function is called
	listobj *firstObj = sentList->pHead->pNext;
	// unlink it from the list
	sentList->pHead->pNext = firstObj->pNext;
	firstObj->pNext->pPrevious = sentList->pHead;

	albert_free(firstObj->pTask);
	// we should not free the message, for message can only be freed
	// when its corresponding task reaches deadline or this message
	// get its reply
	albert_free(firstObj);
}

/** Removes the list and all list elements it contains.
 *       @param sentList The list you would like to remove
 *       @bug If message doesent exist the program crashes
 */
static void removeList(list *sentList)
{
	while(!isEmpty(sentList)) {
		removeFirst(sentList);
	}
	albert_free(sentList->pHead);
	albert_free(sentList->pTail);
	albert_free(sentList);
}

/** Unlink the first element from one list and inserts into another.
 *  Caller is responsible for guaranting that there is at least one
 *  element in the listFrom
 *       @param listFrom The list to remove from
 *       @param listTo The list to insert into
 *       @param sortedby How the listTo should be sorted, 1 sorts by
 *       deadline(ex. readylist) -1 sorts by nTCnt
 */
static void moveFirst(list *listFrom, list *listTo, int sortedby)
{
	// get the first element
	listobj *first = listFrom->pHead->pNext;
	// unlink it from listFrom
	listFrom->pHead->pNext = first->pNext;
	first->pNext->pPrevious = first->pPrevious;

	// insert it into listTo
	insert(listTo, first, sortedby);
}

/** Unlink the specified element from one list and inserts into another.
 *  Caller is responsible for guaranting that there is at least one
 *  element in the listFrom
 *       @param object The object to move
 *       @param listFrom The list to remove from
 *       @param listTo The list to insert into
 *       @param sortedby How the listTo should be sorted, 1 sorts by
 *       deadline(ex. readylist) -1 sorts by nTCnt
 */
static void move(listobj *object, list *listFrom, list *listTo,
		int sortedby)
{
	// unlink it from listFrom
	object->pPrevious->pNext = object->pNext;
	object->pNext->pPrevious = object->pPrevious;

	// insert it into listTo
	insert(listTo, object, sortedby);
}
/** Insert element into one list
 *		@param sentList: the target list
 *		@param element: the element to be inserted
 *		@param sortedby: sort this list by deadline if it is
 *		1, otherwise sort it by nTCnt
 */
static void insert(list *sentList, listobj *element,int sortedby)
{

	if(isEmpty(sentList))
	{
		// printf("The list is empty, inserting in first position\n");
		sentList->pHead->pNext = element;
		sentList->pTail->pPrevious = element;
		element->pPrevious=sentList->pHead;
		element->pNext=sentList->pTail;
	}
	else
	{
		// there are some elements in this list, we need to find the
		// right position
		listobj *iterator = sentList->pHead->pNext;
		while(iterator != sentList->pTail)
		{
			//Checks if we need to sort by deadline or nTCnt
			if(sortedby == 1)
			{
				if((element->pTask->DeadLine) < (iterator->pTask->DeadLine)) {
					break;
				}
			}
			else
			{
				if(element->nTCnt < iterator->nTCnt) {
					break;
				}
			}
			iterator = iterator->pNext;
		}
		element->pNext = iterator;
		element->pPrevious = iterator->pPrevious;
		iterator->pPrevious->pNext = element;
		iterator->pPrevious = element;
	}

}

static int isEmpty(list *sentList)
{
	if(sentList->pHead->pNext == sentList->pTail) {
		return TRUE;
	} else {
		return FALSE;
	}
}

static int getSize(list *tmpList)
{
	int i;
	listobj *iter;
	i = 0;
	iter = tmpList->pHead->pNext;
	while(iter != tmpList->pTail)
	{
		iter = iter->pNext;
		i++;
	}
	return i;
}
