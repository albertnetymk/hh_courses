/**
 * Program: test0.c
 * Purpose: to check how this code is translated to llvm
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
	int *a;
	int b;
} name;

typedef struct {
	name x;
} test;

int main()
{
	name *x = (name *)malloc(sizeof(name));
	x->a=0;
	x->b=0;
	test *y = (test *)malloc(sizeof(test));
	y->x=*x;
	return 0;
}
