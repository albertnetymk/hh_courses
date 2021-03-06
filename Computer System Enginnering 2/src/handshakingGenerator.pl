#!/usr/bin/perl
use strict;
use warnings;

# the number of clients
my $number = $ARGV[0];
# my $number = 1000;
my $i;
print '/** @file handShaking.c', "\n";
print '*		One application running on this kernel', "\n";
print '*		@author Albert Netymk', "\n";
print '*/', "\n";
print "\n";
print '#include "api.h"', "\n";
print '#include "kernel.h"', "\n";
print '#include "kernel_hwdep.h"', "\n";
print '#include "memory.h"', "\n";
print '#include <stdio.h>', "\n";
print '#include <stdlib.h>', "\n";
print "\n";
print '#define TASK_SERVER_DEADLINE 20000', "\n";
print '#define NUMBER ', $number, "\n";
print "\n";
print '// function declaration', "\n";
print 'void task_server(void);', "\n";
for($i=0; $i<$number; $i++) {
	print 'void task_client_', $i, '(void);', "\n";
}
print "\n";
print 'mailbox *mailbox_wait[', $number, '];', "\n";
print 'mailbox *mailbox_no_wait[', $number, '];', "\n";

print 'int CLIENT[', $number, '];', "\n";
print 'int DEADLINE[', $number, '];', "\n";
print "\n";

print 'int main(void)', "\n";
print '{', "\n";
print '	int i;', "\n";
print '	for(i=0; i<NUMBER; ++i) {', "\n";
print '		// the message', "\n";
print '		CLIENT[i] = rand()%100+1;', "\n";
print '	}', "\n";
print '	for(i=0; i<NUMBER; ++i) {', "\n";
print '		DEADLINE[i] = i*10+10;', "\n";
print '	}', "\n";
print "\n";
print '	if(init_kernel() != OK) {', "\n";
print '		while(1);', "\n";
print '	}', "\n";
print '	for(i=0; i<', $number, '; ++i) {', "\n";
print '		mailbox_wait[i] = create_mailbox(1, sizeof(int));', "\n";
print '		if(mailbox_wait[i] == NULL) {', "\n";
print '			while(1);', "\n";
print '		}', "\n";
print '		mailbox_no_wait[i] = create_mailbox(1, sizeof(int));', "\n";
print '		if(mailbox_no_wait[i] == NULL) {', "\n";
print '			while(1);', "\n";
print '		}', "\n";
print '	}', "\n";
print '	if(create_task(&task_server, TASK_SERVER_DEADLINE) != OK) {', "\n";
print '		while(1);', "\n";
print '	}', "\n";
for($i=0; $i<$number; $i++) {
	print '	if(create_task(&task_client_', $i, ', DEADLINE[', $i, ']) != OK) {', "\n";
	print '		while(1);', "\n";
	print '	}', "\n";
}
print '	run();', "\n";
print '}', "\n";

print 'void task_server(void)', "\n";
print '{', "\n";

print '	int i, j, flag_total, data[', $number, '], flag[', $i, '];', "\n";

print '	for(i=0; i<', $number, '; ++i) {', "\n";
print '		flag[i] = 0;', "\n";
print '	}', "\n";

print '	while(1) {', "\n";
print '		while(1) {', "\n";
print '			for(i=0; i<', $number, '; ++i) {', "\n";
print '				if(no_message(mailbox_wait[i]) == 0) {', "\n";
print '					flag[i] = 0;', "\n";
print '				} else {', "\n";
print '					flag[i] = 1;', "\n";
print '					receive_wait(mailbox_wait[i], &data[i]);', "\n";
print '				}', "\n";
print '			}', "\n";
print '			flag_total = 0;', "\n";
print '			for(i=0; i<', $number, '; ++i) {', "\n";
print '				flag_total = flag_total + flag[i];', "\n";
print '			}', "\n";
print '			if(flag_total >= 1) {', "\n";
print '				break;', "\n";
print '			} else {', "\n";
print '				continue;', "\n";
print '			}', "\n";
print '		}', "\n";
print '		for(i=0; i<', $number, '; ++i) {', "\n";
print '			if(flag[i] == 1) {', "\n";
print '				data[i]++;', "\n";
print '				if(send_no_wait(mailbox_no_wait[i], &data[i]) == FAIL) {', "\n";
print '					terminate();	// this should not happen', "\n";
print '				}', "\n";
print '			}', "\n";
print '		}', "\n";

print '		while(flag_total >= 1) {', "\n";
print '			for(i=0; i<', $number, '; ++i) {', "\n";
print '				if(flag[i] == 1) {', "\n";
print '					int data_receive;', "\n";
print '					for(j=0; j<5; ++j) {', "\n";
print '						if(no_message(mailbox_wait[i]) == 0) {', "\n";
print '							wait(1);', "\n";
print '							continue;', "\n";
print '						} else {', "\n";
print '							receive_wait(mailbox_wait[i], &data_receive);', "\n";
print '							if(data_receive == data[i] + 1) {', "\n";
print '								break;', "\n";
print '							} else {', "\n";
print '								// the communication has been compromised', "\n";
print '								while(1);', "\n";
print '							}', "\n";
print '						}', "\n";
print '					}', "\n";
print '					if(j == 5) { ', "\n";
print '						flag[i] = 0;	// drop this session', "\n";
print '					} else {', "\n";
print '						flag[i] = 0;   // we can begin communication now', "\n";
print '					}', "\n";
print '				}', "\n";
print '			}', "\n";
print '		}', "\n";
print '	}', "\n";
print '}', "\n";
for($i=0; $i<$number; $i++) {
	print 'void task_client_', $i, '(void)', "\n";
	print '{', "\n";
	print '	int i, data_send, data_receive, result;', "\n";
	print '	data_send = CLIENT[', $i, '];', "\n";
	print '	for(i=0; i<5; ++i) {', "\n";
	print '		result = send_wait(mailbox_wait[', $i, '], &data_send);', "\n";
	print '		if(result == DEADLINE_REACHED) {', "\n";
	print '			set_deadline(ticks() + DEADLINE[', $i, ']);', "\n";
	print '			continue;', "\n";
	print '		} else if(result == FAIL){', "\n";
	print '			terminate();		// can not create the message', "\n";
	print '		} else {', "\n";
	print '			break;', "\n";
	print '		}', "\n";
	print '	}', "\n";
	print '	if(i == 5) {', "\n";
	print '		terminate();	// the server is down', "\n";
	print '	} ', "\n";
	print '	// the server accepts our request', "\n";
	print '	for(i=0; i<5; ++i) {', "\n";
	print '		if(receive_no_wait(mailbox_no_wait[', $i, '], &data_receive) == FAIL) {', "\n";
	print '			set_deadline(ticks() + DEADLINE[', $i, ']);', "\n";
	print '			wait(1);', "\n";
	print '		} else {', "\n";
	print ' 		break;', "\n";
	print '		}', "\n";
	print '	}', "\n";
	print '	if(i == 5) {', "\n";
	print '		terminate();	// the server is down', "\n";
	print '	}', "\n";
	print '	if( data_receive == data_send + 1) {', "\n";
	print '		data_send = data_receive + 1;', "\n";
	print '		for(i=0; i<5; ++i) {', "\n";
	print '			if(send_wait(mailbox_wait[', $i, '], &data_send) == DEADLINE_REACHED) {', "\n";
	print '				set_deadline(ticks() + DEADLINE[', $i, ']);', "\n";
	print '				continue;', "\n";
	print '			} else {', "\n";
	print '				break;', "\n";
	print '			}', "\n";
	print '		}', "\n";
	print '		if(i == 5) {', "\n";
	print '			terminate();	// the server is down', "\n";
	print '		}', "\n";
	print '		terminate();     // communication begins', "\n";
	print '	} else {', "\n";
	print '		terminate();	// the communication has been compromised', "\n";
	print '	}', "\n";
	print '}', "\n";
}
