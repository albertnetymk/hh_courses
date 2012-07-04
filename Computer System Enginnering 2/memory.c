#include "kernel.h"
#include "kernel_hwdep.h"
#include <stdlib.h>


void *albert_malloc(uint size)
{

	int status;
	void *returnValue;
	status = set_isr(ISR_OFF);
	returnValue = malloc(size);
	set_isr(status);
	return returnValue;
}
// set pointer to null after freeing it
void albert_free(void *pointer)
{
	int status;
	status = set_isr(ISR_OFF);
	free(pointer);
	pointer = NULL;
	set_isr(status);
}
