#include "primes.h"

static int is_prime(unsigned int number)
{
	if(number < 2) {
		return 0;
	}
	for (int i = 2; i < number; ++i) {
		if (number % i == 0) {
			return 0;
		}
	}
	return 1;
}

int primes(PrimeCalculator *self, unsigned int x)
{
	if(is_prime(x)) {
		// If SYNC is used, current thread will execute this method.
		// In this case, during the waiting time, 100 MSEC, this
		// thread will do other stuff.
		// SYNC(self->lcd, writeInt, x);
		
		// If ASYNC is used, current thread will be freed as soon as
		// possible, can be used for the more "important" message.
		ASYNC(self->lcd, writeInt, x);
	}
	AFTER(MSEC(100), self, primes, x+1);
}
