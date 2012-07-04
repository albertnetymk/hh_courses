/*
 *
 * TinyTimber.c
 *
 */

#include "TinyTimber.h"

#if defined(__AVR_ATmega169__)    // AVR ATmega169 Butterfly dependencies ---------------------

#include <setjmp.h>
#include <avr/io.h>

#define STACKSIZE       96
#define NMSGS           8
#define NTHREADS        3

#define STATUS()        (SREG & 0x80)
#define DISABLE()       { irqstatus = STATUS(); cli(); }
#define ENABLE()        if (irqstatus) { sei(); }
#define SLEEP()         { SMCR = 0x01; __asm__ __volatile__ ("sleep" ::); }
#define CONFLCD { LCDCRB = 0xb7; LCDFRR = 0x10; LCDCCR = 0x0f; LCDCRA = 0x80; }
#define INIT()          { CLKPR = 0x80; CLKPR = 0x00; \
			  TCNT1 = 0x0000; TCCR1B = 0x04; TIMSK1 = 0x01; }
// No system clock prescaling
// Normal mode, clk/256 prescaling, enable timer overflow interrupts
#define PANIC()         { CONFLCD; LCDDR0 = 0xFF; LCDDR1 = 0xFF; LCDDR2 = 0xFF; while (1) { SLEEP(); } }

// Light up the display...
#define SETSTACK(buf, a) { *((unsigned int *)(buf) + 8) = (unsigned int)(a) + STACKSIZE - 4; \
			   *((unsigned int *)(buf) + 9) = (unsigned int)(a) + STACKSIZE - 4; }
#define SETPC(buf, a)   * ((unsigned int *)((unsigned char *)(buf) + 21)) = (unsigned int)(a)

#define TCMP_INTERRUPT  ISR(SIG_OUTPUT_COMPARE1A)
#define TOVFL_INTERRUPT ISR(SIG_OVERFLOW1)
#define TIMERGET(x)     (x) = ((Time)overflows << 16) | (unsigned int)TCNT1; \
	if (TIFR1 & 0x01) { (x) = ((Time)(overflows + 1) << 16) | (unsigned int)TCNT1; }
#define TIMERSET(x)     { if ((x) && (overflows == HIGH16((x)->baseline))) { \
				  OCR1A = LOW16((x)->baseline);	\
				  TIMSK1 |= 0x02; \
			  } else{ \
				  TIMSK1 &= ~0x02; } \
}
#define TDELTA          0L
#define HIGH16(x)       (int)((x) >> 16)
#define LOW16(x)        (unsigned int)((x) & 0xffff)
#define MAX(a, b)        ((a) - (b) <= 0 ? (b) : (a))
#define INF(a)          ((a) == 0 ? 0x7fffffffL : (a))

#elif defined(__AVR_ATmega32__)    // AVR ATmega32 dependencies -------------------------------

#include <setjmp.h>
#include <avr/io.h>

#define STACKSIZE       150
#define NMSGS           20
#define NTHREADS        3

#define STATUS()        (SREG & 0x80)
#define DISABLE()       { irqstatus = STATUS(); cli(); }
#define ENABLE()        if (irqstatus) { sei(); }
#define SLEEP()         { MCUCR = 0x80; __asm__ __volatile__ ("sleep" ::); }
#define INIT()          { TCNT1 = 0x0000; TCCR1B = 0x02; TIMSK = 0x04; }
// Normal mode, clk/8 prescaling, enable timer overflow interrupts
#define PANIC()         { while (1) { SLEEP(); } }
//
#define SETSTACK(buf, a) { *((unsigned int *)(buf) + 8) = (unsigned int)(a) + STACKSIZE - 4; \
			   *((unsigned int *)(buf) + 9) = (unsigned int)(a) + STACKSIZE - 4; }
#define SETPC(buf, a)   * ((unsigned int *)((unsigned char *)(buf) + 21)) = (unsigned int)(a)

#define TCMP_INTERRUPT  ISR(SIG_OUTPUT_COMPARE1A)
#define TOVFL_INTERRUPT ISR(SIG_OVERFLOW1)
#define TIMERGET(x)     (x) = ((Time)overflows << 16) | (unsigned int)TCNT1; \
	if (TIFR & 0x04) { (x) = ((Time)(overflows + 1) << 16) | (unsigned int)TCNT1; }
#define TIMERSET(x)     { if ((x) && (overflows == HIGH16((x)->baseline))) { \
				  OCR1A = LOW16((x)->baseline);	\
				  TIMSK |= 0x10; \
			  } else{ \
				  TIMSK &= ~0x10; } \
}
#define TDELTA          0L
#define HIGH16(x)       (int)((x) >> 16)
#define LOW16(x)        (unsigned int)((x) & 0xffff)
#define MAX(a, b)        ((a) - (b) <= 0 ? (b) : (a))
#define INF(a)          ((a) == 0 ? 0x7fffffffL : (a))

#endif  // Target dependencies ---------------------------------------------------------------

#define NULL                 0L

INLINE_1 void mark(void);                                               
INLINE_2 void schedule(void);                                           

INLINE_1 Time current_baseline(void);                                   

INLINE_1 void abort_msg(Msg m);


typedef struct thread_block *Thread;

struct msg_block {
	Msg next;            // for use in linked lists
	Time baseline;       // event time reference point
	Time deadline;       // absolute deadline (=priority)
	Object *to;          // receiving object
	Method method;       // code to run
	int arg;             // argument to the above
};

struct thread_block {
	Thread next;         // for use in linked lists
	Msg msg;             // message under execution
	Object *waitsFor;    // deadlock detection link
	jmp_buf context;     // machine state
};

struct stack {
	unsigned char stack[STACKSIZE];
};

struct msg_block messages[NMSGS];
struct thread_block threads[NTHREADS];
struct stack stacks[NTHREADS];

struct thread_block thread0;

Msg msgPool = messages;
Msg msgQ = NULL;
Msg timerQ = NULL;
Time timestamp = 0;
// 0 means disable interrupt
char irqstatus = 0;
int overflows = 0;

Thread threadPool = threads;
Thread activeStack = &thread0;
Thread current = &thread0;

/* queue manager */
INLINE_2 void enqueueByDeadline(Msg p, Msg *queue)
{
	Msg prev = NULL, q = *queue;
	while (q && (q->deadline - p->deadline <= 0)) {
		prev = q;
		q = q->next;
	}
	p->next = q;
	if (prev == NULL) {
		*queue = p;
	} else{
		prev->next = p;
	}
}

INLINE_2 void enqueueByBaseline(Msg p, Msg *queue)
{
	Msg prev = NULL, q = *queue;
	while (q && (q->baseline - p->baseline <= 0)) {
		prev = q;
		q = q->next;
	}
	p->next = q;
	if (prev == NULL) {
		*queue = p;
	} else{
		prev->next = p;
	}
}

INLINE_2 Msg dequeue(Msg *queue)
{
	Msg m = *queue;
	if (m) {
		*queue = m->next;
	} else{
		PANIC(); // Empty queue, kernel panic!!!
	}
	return m;
}

INLINE_2 void insert(Msg m, Msg *queue)
{
	m->next = *queue;
	*queue = m;
}

INLINE_2 void push(Thread t, Thread *stack)
{
	t->next = *stack;
	*stack = t;
}

INLINE_2 Thread pop(Thread *stack)
{
	Thread t = *stack;
	*stack = t->next;
	return t;
}

INLINE_2 int remove(Msg m, Msg *queue)
{
	Msg prev = NULL, q = *queue;
	while (q && (q != m)) {
		prev = q;
		q = q->next;
	}
	if (q) {
		if (prev) {
			prev->next = q->next;
		} else{
			*queue = q->next;
		}
		return 1;
	}
	return 0;
}

/* context switching */
INLINE_2 void dispatch(Thread next)
{
	if (setjmp(current->context) == 0) {
		current = next;
		longjmp(next->context, 1);
	}
}

void run(void)
{
	while (1) {
		Msg this = current->msg = dequeue(&msgQ);

		ENABLE();
		sync(this->to, this->method, this->arg);
		DISABLE();
		insert(this, &msgPool);

		Msg oldMsg = activeStack->next->msg;
		if (!msgQ || (oldMsg && (msgQ->deadline - oldMsg->deadline > 0))) {
			push(pop(&activeStack), &threadPool);
			Thread t = activeStack; // can't be NULL, may be &thread0
			while (t->waitsFor) {
				t = t->waitsFor->ownedBy;
			}
			dispatch(t);
		}
	}
}

INLINE_2 void idle(void)
{
	schedule();
	ENABLE();
	while (1) {
		SLEEP();
	}
}

/* interrupt helpers */
INLINE_1 void mark(void)
{
	TIMERGET(timestamp);
}

INLINE_2 void schedule()
{
	irqstatus = 1;
	Msg curMsg = activeStack->msg;
	if (msgQ && threadPool && ((!curMsg) || (msgQ->deadline - curMsg->deadline < 0))) {
		push(pop(&threadPool), &activeStack);
		dispatch(activeStack);
	}
}

/* timer interrupts */
TOVFL_INTERRUPT {
	Time now;
	// Each increment of overflow represents one increase of 2^16 in
	// the smallest scale.
	overflows++;
	// It's possible that the task is scheduled after x*2^16 minimal
	// scale.
	TIMERGET(now);
	while (timerQ && (timerQ->baseline - now - TDELTA < 0)) {
		enqueueByDeadline(dequeue(&timerQ), &msgQ);
	}
	TIMERSET(timerQ);
	schedule();
	if(current->next && current->next != &thread0) {
		dispatch(current->next);
	} else {
		dispatch(activeStack);
	}
}

TCMP_INTERRUPT {
	Time now;
	TIMERGET(now);
	while (timerQ && (timerQ->baseline - now - TDELTA < 0)) {
		enqueueByDeadline(dequeue(&timerQ), &msgQ);
	}
	TIMERSET(timerQ);
	schedule();
}

/* communication primitives */
INLINE_3 Msg async(Time bl, Time dl, Object *to, Method meth, int arg)
{
	Msg m;
	Time now;

	DISABLE();
	m = dequeue(&msgPool);
	m->to = to;
	m->method = meth;
	m->arg = arg;

	TIMERGET(now);
	if (!irqstatus) {           // async from an interrupt-handler
		m->baseline = bl < 0 ? timestamp : MAX(now, timestamp + bl);
		m->deadline = dl < 0 ? timestamp + INF(0) : timestamp + INF(dl);
	} else {                    // ordinary async call
		m->baseline = bl < 0 ? current->msg->baseline : MAX(now, current->msg->baseline + bl);
		m->deadline = dl < 0 ? current->msg->deadline : m->baseline + INF(dl);
	}

	if (m->baseline - now - TDELTA > 0) { // baseline has not yet passed
		enqueueByBaseline(m, &timerQ);
		TIMERSET(timerQ);
	} else {                        // m is immediately schedulable
		enqueueByDeadline(m, &msgQ);
		if (irqstatus && threadPool && (msgQ->deadline - activeStack->msg->deadline < 0)) {
			push(pop(&threadPool), &activeStack);
			dispatch(activeStack);
		}
	}

	ENABLE();
	return m;
}

INLINE_3 int sync(Object *to, Method meth, int arg)
{
	Thread t;
	int result;

	DISABLE();
	t = to->ownedBy;
	if (t) { // to is already locked
		while (t->waitsFor) {
			t = t->waitsFor->ownedBy;
		}
		if (t == current) { // Deadlock!
			ENABLE();
			return -1;
		}
		if (to->wantedBy) {
			to->wantedBy->waitsFor = NULL;
		}
		to->wantedBy = current;
		current->waitsFor = to;
		dispatch(t);
		if (current->msg == NULL) { // Aborted!
			ENABLE();
			return 0;
		}
	}
	to->ownedBy = current;
	ENABLE();

	result = meth(to, arg);

	DISABLE();
	to->ownedBy = NULL;
	t = to->wantedBy;
	if (t) { // we have run on someone's behalf
		to->wantedBy = NULL;
		t->waitsFor = NULL;
		dispatch(t);
	}
	ENABLE();
	return result;
}

INLINE_1 Time current_baseline()
{
	return(STATUS() ? current->msg->baseline : timestamp);
}

INLINE_1 void abort_msg(Msg m)
{
	DISABLE();
	if (remove(m, &timerQ) || remove(m, &msgQ)) {
		insert(m, &msgPool);
	} else{
		Thread t = activeStack;
		while (t) {
			if ((t != current) && (t->msg == m) && (t->waitsFor == m->to)) {
				t->msg = NULL;
				insert(m, &msgPool);
				break;
			}
			t = t->next;
		}
	}
	ENABLE();
}

/* initialization */
void initialize(void)
{
	int i;

	for (i = 0; i < NMSGS - 1; i++) {
		messages[i].next = &messages[i + 1];
	}
	messages[NMSGS - 1].next = NULL;

	for (i = 0; i < NTHREADS - 1; i++) {
		threads[i].next = &threads[i + 1];
	}
	threads[NTHREADS - 1].next = NULL;

	for (i = 0; i < NTHREADS; i++) {
		// TODO Do we need to save it in here, for they are
		// overwritten immediately afterwards.
		// setjmp(threads[i].context);
		SETSTACK(&threads[i].context, &stacks[i]);
		SETPC(&threads[i].context, run);
		threads[i].waitsFor = NULL;
	}

	thread0.next = NULL;
	thread0.waitsFor = NULL;
	thread0.msg = NULL;

	INIT();
}
