#ifndef _sound_h
#define _sound_h

#include "TinyTimber.h"
#include "piezo.h"

typedef struct {
	Object super;
	Piezo *p;
	int f;
	int status;
} Sound;

#define initSound(p, f) { initObject(), p, f, 1}

void setFrequency(Sound *self, int freq);
void setStatus(Sound *self, int status);
void playHacked(Sound *self, int state);
void playRecursion(Sound *self, int state);
#endif
