/**
 * malloc and free wrapper, to make them atomic
 */
#ifndef _MEMORY_H
#define _MEMORY_H
extern void *albert_malloc(uint size);
extern void albert_free(void *pointer);
#endif
