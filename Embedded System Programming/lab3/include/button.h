#include "TinyTimber.h"
#include "lcd.h"

#ifndef _button_h
#define _button_h

typedef struct {
	Object super;
	LCD *lcd;
} Button;

#define initButton(lcd) { initObject(), lcd }
#define CONFJOY { PORTB = 0x80; PCMSK1 = 0x80; EIMSK = 0x80; }

void button(Button *self, int nothing);
#endif
