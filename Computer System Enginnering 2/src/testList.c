/**
 * Test file for the three linkedlists.
 */
#include "genericLinkedList.h"
#include <stdio.h>
void task1() {
}
int main() {
	list *tmpListR = newList();
	list *tmpListW = newList();
	list *tmpListT = newList();
	// insert six objects into ready list
	TCB *tmpTCB_6 = newTCBObj(&task1, 600);
	insertR(tmpListR, tmpTCB_6);
	TCB *tmpTCB_1 = newTCBObj(&task1, 100);
	insertR(tmpListR, tmpTCB_1);
	TCB *tmpTCB_3 = newTCBObj(&task1, 300);
	insertR(tmpListR, tmpTCB_3);
	TCB *tmpTCB_2 = newTCBObj(&task1, 200);
	insertR(tmpListR, tmpTCB_2);
	TCB *tmpTCB_4 = newTCBObj(&task1, 400);
	insertR(tmpListR, tmpTCB_4);
	TCB *tmpTCB_5 = newTCBObj(&task1, 500);
	insertR(tmpListR, tmpTCB_5);
	printf("The size of the list is %d.\n", getSize(tmpListR));
	printList(tmpListR);
	printf("The size of the list is %d.\n", getSize(tmpListW));
	printList(tmpListW);
	printf("The size of the list is %d.\n", getSize(tmpListT));
	printList(tmpListT);
	listobj *iterator = tmpListR->pHead->pNext;
	int i = 1;
	while(iterator != tmpListR->pTail) {
		iterator->nTCnt = i;
		iterator = iterator->pNext;
		i++;
	}
	move(tmpListR, tmpListW, 1);
	move(tmpListR, tmpListW, 1);
	move(tmpListR, tmpListW, 1);
	move(tmpListR, tmpListW, 1);
	printf("The size of the list is %d.\n", getSize(tmpListR));
	printList(tmpListR);
	printf("The size of the list is %d.\n", getSize(tmpListW));
	printList(tmpListW);
	printf("The size of the list is %d.\n", getSize(tmpListT));
	printList(tmpListT);
	move(tmpListW, tmpListT, -1);
	move(tmpListW, tmpListT, -1);
	move(tmpListW, tmpListT, -1);
	move(tmpListW, tmpListT, -1);
	printList(tmpListR);
	printList(tmpListW);
	printList(tmpListT);
	removeFirst(tmpListR);
	removeFirst(tmpListW);
	removeFirst(tmpListT);
	printf("The size of the list is %d.\n", getSize(tmpListR));
	printList(tmpListR);
	printf("The size of the list is %d.\n", getSize(tmpListW));
	printList(tmpListW);
	printf("The size of the list is %d.\n", getSize(tmpListT));
	printList(tmpListT);
	return 0;
}
