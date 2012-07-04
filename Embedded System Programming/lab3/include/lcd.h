#ifndef _lcd_h
#define _lcd_h

#include "TinyTimber.h"
typedef struct {
	Object super;
} LCD;

#define initLCD() { initObject()}
int writeDigit(LCD *self, int digitPos);
void writeLong(long x);
int writeInt(LCD *self, unsigned int val);
int segmentOn(LCD *self, int segment);
int segmentOff(LCD *self, int segment);
void writeChar(char ch, int pos);
#endif
