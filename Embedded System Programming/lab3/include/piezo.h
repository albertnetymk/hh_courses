#include "TinyTimber.h"

#ifndef _piezo_h
#define _piezo_h
typedef struct {
	Object super;
} Piezo;

#define initPiezo() { initObject() }
#define CONFPIEZO { DDRB = 0x20; }

void turnOn(Piezo *self, int nothing);
void turnOff(Piezo *self, int nothing);
void testPiezo(Piezo *self, int nothing);

#endif
