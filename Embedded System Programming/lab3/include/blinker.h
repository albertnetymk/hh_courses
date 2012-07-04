#ifndef _blinker_h
#define _blinker_h

//#include "TinyTimber.h"
#include "lcd.h"

typedef struct {
	Object super;
	LCD *lcd;
	int segment;
	int period;
} Blinker;

#define initBlinker(lcd, segment, period) { initObject(), lcd, segment, period};

int startBlinking(Blinker *self, int nothing);
int stopBlinking(Blinker *self, int nothing);
int setPeriod(Blinker *self, int period);
#endif
