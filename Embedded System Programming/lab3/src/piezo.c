#include "piezo.h"

void turnOn(Piezo *self, int nothing)
{
	PORTB |= 0x20;
}

void turnOff(Piezo *self, int nothing)
{
	PORTB &= 0xDF;
}

void testPiezo(Piezo *self, int flag)
{
	if(flag) {
		turnOn(self, NOTHING);
	} else {
		turnOff(self, NOTHING);
	}
	AFTER(MSEC(500), self, testPiezo, !flag);
}
