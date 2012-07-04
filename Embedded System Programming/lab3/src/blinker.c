#include "blinker.h"

static void blink(Blinker *self, int flag)
{
	if(self->period > 0) {
		if(flag) {
			SYNC(self->lcd, segmentOn, self->segment);
		} else {
			SYNC(self->lcd, segmentOff, self->segment);
		}
		AFTER(MSEC(self->period), self, blink, !flag);
		// BEFORE(MSEC(self->period), self, blink, nothing);
	}
}

int startBlinking(Blinker *self, int nothing)
{
	blink(self, NOTHING);
}

int stopBlinking(Blinker *self, int nothing)
{
	self->period = 0;
}

int setPeriod(Blinker *self, int period)
{
	self->period = period;
}
