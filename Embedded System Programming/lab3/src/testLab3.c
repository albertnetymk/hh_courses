#include <avr/io.h>
#include "lcd.h"
#include "primes.h"
#include "blinker.h"
#include "button.h"
#include "sound.h"
#include "melody.h"

/*

   This file is just for you to check that you can compile a program
   together with tinytimber. In your programs you will have a .h and a .c
   file per reactive object and then the application .c file.

 */

// this part will typicaly be in other files, the .h for a reactive object
typedef struct {
	Object super;
} Is;

#define initIs() { initObject() }
#define CONFLCD { LCDCRB = 0xb7; LCDFRR = 0x10; LCDCCR = 0x0f; LCDCRA = 0x80; }
// end of what you would have in the reactive object .h

// this parts will typicaly be in other files, the .c for a reactive object
int showAll(Is *self, int nothing)
{
	LCDCRA = 0x80;
	LCDCRB = 0xb7;

	LCDDR0 = 0x11;
	LCDDR5 = 0x88;
	LCDDR15 = 0x33;

	LCDDR1 = 0x11;
	LCDDR6 = 0x88;
	LCDDR16 = 0x33;

	LCDDR2 = 0x11;
	LCDDR7 = 0x88;
	LCDDR17 = 0x33;
}
// end of what you would have in the reactive object .c

//  This is the application:
// Is is = initIs();
// STARTUP(CONFLCD;ASYNC(&is,showAll,0););
// LCD lcd = initLCD();
// PrimeCalculator calculator = initPrimeCalculator(&lcd);
// Blinker blinker = initBlinker(&lcd, 13, 500);
// Button joystick = initButton(&lcd);
// STARTUP(CONFLCD;
//              CONFJOY;
//              ASYNC(&blinker,startBlinking,NOTHING);
//              ASYNC(&calculator, primes, NOTHING);
//              ASYNC(&lcd, writeDigit, NOTHING);
//              );
// INTERRUPT(SIG_PIN_CHANGE1, SYNC(&joystick, button, NOTHING));
LCD lcd = initLCD();
Piezo piezo = initPiezo();
PrimeCalculator calculator = initPrimeCalculator(&lcd);

Sound sound = initSound(&piezo, 100);
Melody melody = initMelody(&sound);
STARTUP(CONFLCD;
	CONFPIEZO;
	// ASYNC(&sound, play, NOTHING);
	//ASYNC(&lcd, writeInt, 1);
	//ASYNC(&piezo, testPiezo, NOTHING);
	// ASYNC(&lcd, segmentOn, 13);
	// ASYNC(&melody, playDianaNonStop, 0);
	// ASYNC(&melody, playDianaAntiPattern, 0);
	ASYNC(&melody, playDianaHacked, 0);
	ASYNC(&calculator, primes, 65000);
	);
