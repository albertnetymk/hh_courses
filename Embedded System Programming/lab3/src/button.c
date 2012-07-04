#include "button.h"

void button(Button *self, int nothing)
{
	if ((PINB & PORTB) != PORTB) {
		SYNC(self->lcd, segmentOn, 8);
	}else {
		SYNC(self->lcd, segmentOff, 8);
	}
}
