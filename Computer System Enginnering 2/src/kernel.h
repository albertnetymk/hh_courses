/** @file kernel.h
 *       This file contains most of the typedefinitions and structs.
 *        It also contains some global variables and definitions.
 */
#ifndef KERNEL_H
#define KERNEL_H

// Debug option
//#define       _DEBUG

/*********************************************************/
/** Global variabels and definitions                     */
/*********************************************************/

#include <stdlib.h>


#define CONTEXT_SIZE    13
#define STACK_SIZE      100


#define TRUE    1
#define FALSE   !TRUE

#define RUNNING 1
#define INIT    !RUNNING

#define FAIL    0
#define SUCCESS 1
#define OK              1

#define DEADLINE_REACHED        0
#define NOT_EMPTY               0

#define SENDER          +1
#define RECEIVER        -1


typedef int			 exception;
typedef int             bool;
typedef unsigned int    uint;
typedef int             action;

// Task Control Block, TCB
typedef struct
{
        uint    Context[CONTEXT_SIZE];
        uint    *SP;
        void    (*PC)();
        uint    SPSR;
        uint    StackSeg[STACK_SIZE];
        uint    DeadLine;
} TCB;



// Message items
typedef struct msgobj
{
        void            *pData;
        exception       Status;
        struct _listobj *pBlock;
        struct msgobj   *pPrevious;
        struct msgobj   *pNext;
} msg;

// Mailbox structure
typedef struct {
        msg             *pHead;
        msg             *pTail;
        int             nDataSize;
        int             nMaxMessages;
        int             nMessages;
        int             nBlockedMsg;
} mailbox;


// Generic list item
typedef struct _listobj {
         TCB            *pTask;
         uint           nTCnt;
         msg            *pMessage;
         struct _listobj		 *pPrevious;
         struct _listobj		 *pNext;
} listobj;


// Generic list
typedef struct
{
         listobj        *pHead;
         listobj        *pTail;
} list;


// Function prototypes


// Task administration
/**
*/
int             init_kernel(void);
exception       create_task( void (* body)(), uint deadline );
void            terminate( void );
void            run( void );

// Communication
/**  */
mailbox*        create_mailbox(int nof_msg, int size_of_msg);
int             no_messages( mailbox* mBox );

exception       send_wait( mailbox* mBox, void* pData );
exception       receive_wait( mailbox* mBox, void* pData );

exception       send_no_wait( mailbox* mBox, void* pData );
int             receive_no_wait( mailbox* mBox, void* pData );


// Timing
exception       wait( uint nTicks );
void            set_ticks( uint no_of_ticks );
uint            ticks( void );
uint            deadline( void );
void            set_deadline( uint nNew );

//Interrupt
extern void     isr_off(void);
extern void     isr_on(void);
extern void     SaveContext( void );    // Stores DSP registers in TCB pointed to by Running
extern void     LoadContext( void );    // Restores DSP registers from TCB pointed to by Running

#endif
