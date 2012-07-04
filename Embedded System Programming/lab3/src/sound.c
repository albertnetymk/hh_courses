#include "sound.h"

void setFrequency(Sound *self, int freq)
{
	self->f = freq;
}

void setStatus(Sound *self, int status)
{
	self->status = status;
}

void playHacked(Sound *self, int state)
{
	if(self->status) {
		if(state) {
			ASYNC(self->p, turnOn, 0);
		} else {
			ASYNC(self->p, turnOff, 0);
		}
	}
	// Use deadline to increase the priority.
	// AFTER(RESOLUTION(31250/(self->f))/2, self, playHacked, !state);
	WITHIN(RESOLUTION(31250/(self->f)), MSEC(10), self, 
			playHacked, !state);
}

/**
 * If this method exits, it's quite "expensive" invoke it. That's the
 * reason why this one doesn't work in this case.
 */
void playRecursion(Sound *self, int state)
{
	if(self->status) {
		if(state) {
			ASYNC(self->p, turnOn, 0);
		} else {
			ASYNC(self->p, turnOff, 0);
		}
		WITHIN(RESOLUTION(31250/(self->f)), MSEC(10), 
				self, playRecursion, !state);
	}
}
