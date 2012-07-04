/**
 * api of this kernel
 */
#ifndef _API_H
#define _API_H
#include "kernel.h"
exception init_kernel();
exception create_task(void(*task_body)(), uint deadline);
void run();
void terminate();
mailbox* create_mailbox(int nof_msg, int size_of_msg);
exception remove_mailbox(mailbox * mBox);
exception send_wait(mailbox* mBox, void* Data);
exception receive_wait(mailbox* mBox, void* Data);
exception send_no_wait(mailbox* mBox, void* Data);
exception receive_no_wait(mailbox* mBox, void* Data);
exception wait(uint nTicks);
void set_ticks(uint nTicks);
uint ticks(void);
uint deadline(void);
void set_deadline(uint deadline);
void TimerInt(void);
int no_message(mailbox *mBox);

void removeTask();
#endif
