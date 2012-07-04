/** @file handShaking.c
*		One application running on this kernel
*		@author Albert Netymk
*/

#include "api.h"
#include "kernel.h"
#include "kernel_hwdep.h"
#include "memory.h"
#include <stdio.h>
#include <stdlib.h>

#define TASK_SERVER_DEADLINE 20000
// the number of clients
#define NUMBER 2

// function declaration
void task_server(void);
void task_client(void);

mailbox *mailbox_wait[NUMBER];
mailbox *mailbox_no_wait[NUMBER];
int CLIENT[NUMBER];
int DEADLINE[NUMBER];

int main(void)
{
	int i;
	for(i=0; i<NUMBER; ++i) {
		// the message
		CLIENT[i] = rand()%100+1;
	}
	for(i=0; i<NUMBER; ++i) {
		DEADLINE[i] = i*10+10;
	}

	if(init_kernel() != OK) {
		while(1);
	}
	for(i=0; i<NUMBER; ++i) {
		mailbox_wait[i] = create_mailbox(1, sizeof(int));
		if(mailbox_wait[i] == NULL) {
			while(1);
		}
		mailbox_no_wait[i] = create_mailbox(1, sizeof(int));
		if(mailbox_no_wait[i] == NULL) {
			while(1);
		}
	}
	if(create_task(&task_server, TASK_SERVER_DEADLINE) != OK) {
		while(1);
	}
	for(i=0; i<NUMBER; ++i) {
		if(create_task(&task_client, DEADLINE[i]) != OK) {
			while(1);
		}
	}
	run();
}
void task_server(void)
{
	int i, j, flag_total, data[NUMBER], flag[NUMBER];
	for(i=0; i<NUMBER; ++i) {
		flag[i] = 0;
	}
	while(1) {
		while(1) {
			for(i=0; i<NUMBER; ++i) {
				if(no_message(mailbox_wait[i]) == 0) {
					flag[i] = 0;
				} else {
					flag[i] = 1;
					receive_wait(mailbox_wait[i], &data[i]);
				}
			}
			flag_total = 0;
			for(i=0; i<NUMBER; ++i) {
				flag_total = flag_total + flag[i];
			}
			if(flag_total >= 1) {
				break;
			} else {
				continue;
			}
		}
		for(i=0; i<NUMBER; ++i) {
			if(flag[i] == 1) {
				data[i]++;
				if(send_no_wait(mailbox_no_wait[i], &data[i]) == FAIL) {
					terminate();	// this should not happen
				}
			}
		}
		while(flag_total >= 1) {
			for(i=0; i<NUMBER; ++i) {
				if(flag[i] == 1) {
					int data_receive;
					for(j=0; j<5; ++j) {
						if(no_message(mailbox_wait[i]) == 0) {
							wait(1);
							continue;
						} else {
							receive_wait(mailbox_wait[i], &data_receive);
							if(data_receive == data[i] + 1) {
								break;
							} else {
								// the communication has been compromised
								while(1);
							}
						}
					}
					if(j == 5) { 
						flag[i] = 0;	// drop this session
					} else {
						flag[i] = 0;   // we can begin communication now
					}
				}
			}
		}
	}
}
void task_client(void)
{
	int i, data_send, data_receive, result, index;
	uint deadline_local;
	mailbox *mailbox_wait_local, *mailbox_no_wait_local;
	// read the "argument"
	index = (deadline()-10)/10;

	deadline_local = DEADLINE[index];
	mailbox_wait_local = mailbox_wait[index];
	mailbox_no_wait_local = mailbox_no_wait[index];

	data_send = CLIENT[index];
	for(i=0; i<5; ++i) {
		result = send_wait(mailbox_wait_local, &data_send);
		if(result == DEADLINE_REACHED) {
			set_deadline(ticks() + deadline_local);
			continue;
		} else if(result == FAIL){
			terminate();		// can not create the message
		} else {
			break;
		}
	}
	if(i == 5) {
		terminate();	// the server is down
	} 
	// the server accepts our request
	for(i=0; i<5; ++i) {
		if(receive_no_wait(mailbox_no_wait_local, &data_receive) == FAIL) {
			set_deadline(ticks() + deadline_local);
			wait(1);
		} else {
 		break;
		}
	}
	if(i == 5) {
		terminate();	// the server is down
	}
	if( data_receive == data_send + 1) {
		data_send = data_receive + 1;
		for(i=0; i<5; ++i) {
			if(send_wait(mailbox_wait_local, &data_send) == DEADLINE_REACHED) {
				set_deadline(ticks() + deadline_local);
				continue;
			} else {
				break;
			}
		}
		if(i == 5) {
			terminate();	// the server is down
		}
		terminate();     // communication begins
	} else {
		terminate();	// the communication has been compromised
	}
}
