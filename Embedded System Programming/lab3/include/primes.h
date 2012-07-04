#include "TinyTimber.h"
#include "lcd.h"

#ifndef _primes_h
#define _primes_h
typedef struct {
	Object super;
	LCD *lcd;
} PrimeCalculator;

#define initPrimeCalculator(lcd) { initObject(), lcd }

int primes(PrimeCalculator *self, unsigned int x);
#endif
