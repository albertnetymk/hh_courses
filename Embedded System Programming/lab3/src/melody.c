#include "sound.h"
#include "melody.h"

#define a 440
#define b 494
#define c 262
#define d 294
#define e 330
#define f 349
#define g 392
#define h 311

#define x 500
#define y 250
#define z 1250
#define SS 2000
#define t 1500

#define SPAN 10
static void playDianaHackedRecursion(Melody *self, int index);
static void playDianaNonStopRecursion(Melody *self, int index);
static void playDianaAntiPatternRecursion(Melody *self, int index);

static int freq[50] = {e, e, e, e, e, e, e, f, f, f,
	f, g, f, d, e, e, e, e, e, e,
	e, f, f, f, f, g, f, d, g, g,
	g, g, a, a, a, a, a, a, a, h, 
	h, h, c, c, a, a, g, h, d, c};

static int duration[50] = {x, x, x, x, x, y, z, x, x, x, 
	x, x, y, z, x, x, x, x, x, y,
	z, x, x, x, x, x, y, z, x, x, 
	x, x, x, y, z, x, x, x, x, x, 
	y, z, SS, SS, t, x, t, x, x, t};

/**
 * There is no stop between two tones.
 * This method exists only because of the limitation of the current
 * CPU. This architecture only supports 3 threads.
 */
void playDianaNonStop(Melody *self, int index)
{
	ASYNC(self->s, playRecursion, freq[index]);
	ASYNC(self, playDianaNonStopRecursion, index+1);
}

/**
 * This method works the best, for it's the hacked version. It takes
 * the advantage of this particular architecture. Therefore, it's
 * not very readable.
 */
void playDianaHacked(Melody *self, int index)
{
	ASYNC(self->s, playHacked, freq[index]);
	ASYNC(self, playDianaHackedRecursion, index+1);
}

void playDianaAntiPattern(Melody *self, int index)
{
	ASYNC(self, playDianaAntiPatternRecursion, index);
}

/**
 * This method is one good example to test how well the reader
 * understands TinyTimber. Because of the limitation of this
 * architecture, each step must be carefully calculated and estimated
 * in order for it to work.
 */
static void playDianaAntiPatternRecursion(Melody *self, int index)
{
	if(index == 50) {
		index = 0;
	}
	BEFORE(MSEC(duration[index]), self->s, setFrequency, freq[index]);
	BEFORE(MSEC(duration[index]), self->s, setStatus, 1);
	// This is the culprit.
	BEFORE(MSEC(duration[index]), self->s, playRecursion, freq[index]);

	WITHIN(MSEC(duration[index]), MSEC(10), self->s, setStatus, 0);
	AFTER(MSEC(duration[index] + 10), self, 
			playDianaAntiPatternRecursion, index+1);
}

static void playDianaNonStopRecursion(Melody *self, int index)
{
	if(index == 50) {
		index = 0;
	}
	BEFORE(MSEC(duration[index]), self->s, setFrequency, freq[index]);

	AFTER(MSEC(duration[index] + 10), self, 
			playDianaNonStopRecursion, index+1);
}

static void playDianaHackedRecursion(Melody *self, int index)
{
	if(index == 50) {
		index = 0;
	}
	// Priority. Has to be "BEFORE".
	BEFORE(MSEC(duration[index]), self->s, setFrequency, freq[index]);
	BEFORE(MSEC(duration[index]), self->s, setStatus, 1);

	// I can't perceive the difference between WITHIN and AFTER in
	// here.
	// WITHIN(MSEC(duration[index]), MSEC(10), self->s, setStatus, 0);
	AFTER(MSEC(duration[index]), self->s, setStatus, 0);

	AFTER(MSEC(duration[index] + SPAN), self, 
			playDianaHackedRecursion, index+1);
}
