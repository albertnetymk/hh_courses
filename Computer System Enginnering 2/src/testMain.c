/** @file testMain.c
 *		Test file for this kernel
 *		@author Albert Netymk
 */

#include "api.h"
#include "kernel.h"
#include "kernel_hwdep.h"
#include "memory.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>


#define TASK_TERMINATE_DEADLINE 2
#define TASK_MAILBOX_DEADLINE 3
#define TASK_WAIT_1_DEADLINE 10
#define TASK_WAIT_2_DEADLINE 11
#define TASK_NO_WAIT_1_DEADLINE 20
#define TASK_NO_WAIT_2_DEADLINE 21
#define TASK_DEADLINE_1_DEADLINE 30
#define TASK_DEADLINE_2_DEADLINE 31
#define TASK_TICKS_MASTER_DEADLINE 41
#define TASK_TICKS_SLAVE_DEADLINE 40

#define WAIT1 0xAA
#define WAIT2 0x55

#define NOWAIT1 "helloworld"
#define NOWAIT2 "HelloWorld"

#define DEADLINE_TEST_1 32
#define DEADLINE_TEST_2 35

#define TICKS_TEST_1 60
#define TICKS_TEST_2 35

// testing for memory leak
void task_memory(void);
// testing for terminate
void task_terminate(void);

// testing for mailbox
void task_mailbox(void);
// testing for blocked message
void task_wait_1(void);
void task_wait_2(void);

// testing for unblocked message
void task_no_wait_1(void);
void task_no_wait_2(void);

// testing for deadline
void task_deadline_1(void);
void task_deadline_2(void);

// testing for ticks
void task_ticks_master(void);
void task_ticks_slave(void);

// blcoked messages
mailbox *mailb1;
// unblcoked messages
mailbox *mailb2;

int main(void)
{
	// the size of memory, measured by the number of task_terminate
	int size;
	if(init_kernel() != OK) {
		while(1);
	}
	// this task will run when this application exits, which means it
	// can check if there is memory leak
	if(create_task(&task_memory, 100) != OK) {
		while(1);
	}
	size = 0;
	while(create_task(&task_terminate, 1) == OK) {  
		size++;
	}
	if(size == 20) {
	     int a = 0;
	}
	while(size > 0) {
	    removeTask();
	    size--;
	}

	// begin of the application
	mailb1 = create_mailbox(2, sizeof(int));
	if(mailb1 == NULL) {
		while(1);
	}
	if(create_task(&task_terminate, TASK_TERMINATE_DEADLINE) != OK) {
		while(1);
	}
	if(create_task(&task_mailbox, TASK_MAILBOX_DEADLINE) != OK) {
		while(1);
	}
	if(create_task(&task_wait_1, TASK_WAIT_1_DEADLINE) != OK) {
		while(1);
	}
	if(create_task(&task_wait_2, TASK_WAIT_2_DEADLINE) != OK) {
		while(1);
	}
	
	if(create_task(&task_no_wait_1, TASK_NO_WAIT_1_DEADLINE) != OK) {
		while(1);
	}
	if(create_task(&task_no_wait_2, TASK_NO_WAIT_2_DEADLINE) != OK) {
		while(1);
	}
	if(create_task(&task_deadline_1, TASK_DEADLINE_1_DEADLINE) != OK) {
		while(1);
	}
	if(create_task(&task_deadline_2, TASK_DEADLINE_2_DEADLINE) != OK) {
		while(1);
	}
	
	if(create_task(&task_ticks_master, TASK_TICKS_MASTER_DEADLINE) != OK) {
		while(1);
	}
	
	run();
}

void task_memory(void)
{
	int size = 0;
	while(create_task(&task_terminate, 1000) == OK) {
		size++;
	}
	 if(size == 20) {
	        int a = 0;
	 }
	terminate();
}
	
/**
 * testing for terminate()
 */
void task_terminate(void)
{
	int data = 1;
	terminate();
}

void task_mailbox(void)
{
	if(no_message(mailb1) != 0) {
		while(1);
	}
	terminate();
}

void task_wait_1(void)
{
	int data1 = WAIT1;
	if(send_wait(mailb1, &data1) == DEADLINE_REACHED) {
		while(1);
	}
	if(no_message(mailb1) != 1) {
		while(1);
	}
	set_deadline(TASK_WAIT_1_DEADLINE-2);
	if(receive_wait(mailb1, &data1) == DEADLINE_REACHED) {
		while(1);
	}
	if(data1 != WAIT2) {
		while(1);
	}
	terminate();
}

void task_wait_2(void)
{
	int data2;
	if(no_message(mailb1) != 1) {
		while(1);
	}
	set_deadline(TASK_WAIT_1_DEADLINE-1);
	if(receive_wait(mailb1, &data2) == DEADLINE_REACHED) {
		while(1);
	}
	if(data2 != WAIT1) {
		while(1);
	}
	if(no_message(mailb1) != 0) {
		while(1);
	}
	data2 = WAIT2;
	if(send_wait(mailb1, &data2) == DEADLINE_REACHED) {
		while(1);
	}
	if(no_message(mailb1) != 0) {
		while(1);
	}
	terminate();
}

void task_no_wait_1(void)
{
	char *data1 = NOWAIT1;
	if(remove_mailbox(mailb1) != OK) {
		while(1);
	}
	mailb2 = create_mailbox(10, sizeof(NOWAIT1));
	if(mailb2 == NULL) {
		while(1);
	}

	if(send_no_wait(mailb2, data1) != OK) {
		while(1);
	}

	set_deadline(TASK_NO_WAIT_2_DEADLINE+1);

	if(receive_no_wait(mailb2, data1) != OK) {
		while(1);
	}
	if(strcmp(data1, NOWAIT2) != 0) {
		while(1);
	}
	terminate();
}

void task_no_wait_2(void)
{
	char *data2 = (char *) albert_malloc(sizeof(NOWAIT1));
	if(receive_no_wait(mailb2, data2) != OK) {
		while(1);
	}
	if(strcmp(data2, NOWAIT1) != 0) {
		while(1);
	}

	data2 = NOWAIT2;
	if(send_no_wait(mailb2, data2) != OK) {
		while(1);
	}

	terminate();
}
/**
 * testing for deadline() and set_deadline()
 * task_deadline_1() and task_deadline_2() are firstly created,
 * with deadline TASK_DEADLINE_1_DEADLINE(30) and
 * TASK_DEADLINE_2_DEADLINE(31), respectively
 */
void task_deadline_1(void)
{
	set_deadline(DEADLINE_TEST_1);
	if(deadline() != DEADLINE_TEST_1) {
		while(1);
	}
	terminate();
}
void task_deadline_2(void)
{
	set_deadline(DEADLINE_TEST_2);
	if(deadline() != DEADLINE_TEST_2) {
		while(2);
	}
	terminate();
}

/**
 * testing set_ticks() and ticks()
 */
void task_ticks_master(void)
{
	if(create_task(&task_ticks_slave, TASK_TICKS_SLAVE_DEADLINE) != OK) {
		while(1);
	}
	set_ticks(TICKS_TEST_1);
	if(ticks() != TICKS_TEST_1) {
		while(1);
	}
	set_ticks(TICKS_TEST_2);
	if(ticks() != TICKS_TEST_2) {
		while(1);
	}
	terminate();
}
void task_ticks_slave(void)
{
	int data1 = 1;
	if(no_message(mailb2) != 0) {
		while(1);
	}
	// clean up
	if(remove_mailbox(mailb2) != OK) {
		while(1);
	}

	terminate();
}
