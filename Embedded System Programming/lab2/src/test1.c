#include <avr/io.h>
#include <avr/interrupt.h>
#include <util/delay.h>
#include <math.h>
#include "tinythreads.h"


void writeLong(long x);
mutex m = MUTEX_INIT;
int num[10][4] = { { 0x1, 0x5, 0x5, 0x1 },
		   { 0x0, 0x1, 0x1, 0x0 },
		   { 0x1, 0x1, 0xE, 0x1 },
		   { 0x1, 0x1, 0xB, 0x1 },
		   { 0x0, 0x5, 0xB, 0x0 },
		   { 0x1, 0x4, 0xB, 0x1 },
		   { 0x1, 0x4, 0xF, 0x1 },
		   { 0x1, 0x1, 0x1, 0x0 },
		   { 0x1, 0x5, 0xF, 0x1 },
		   { 0x1, 0x5, 0xB, 0x1 } };

void DRCLR(int pos)
{
	if (pos == 0) {
		LCDDR2 &= 0x0f;
		LCDDR7 &= 0x0f;
		LCDDR12 &= 0x0f;
		LCDDR17 &= 0x0f;
	}

	if (pos == 1) {
		LCDDR2 &= 0xf0;
		LCDDR7 &= 0xf0;
		LCDDR12 &= 0xf0;
		LCDDR17 &= 0xf0;
	}

	if (pos == 2) {
		LCDDR1 &= 0x0f;
		LCDDR6 &= 0x0f;
		LCDDR11 &= 0x0f;
		LCDDR16 &= 0x0f;
	}

	if (pos == 3) {
		LCDDR1 &= 0xf0;
		LCDDR6 &= 0xf0;
		LCDDR11 &= 0xf0;
		LCDDR16 &= 0xf0;
	}

	if (pos == 4) {
		LCDDR0 &= 0x0f;
		LCDDR5 &= 0x0f;
		LCDDR10 &= 0x0f;
		LCDDR15 &= 0x0f;
	}

	if (pos == 5) {
		LCDDR0 &= 0xf0;
		LCDDR5 &= 0xf0;
		LCDDR10 &= 0xf0;
		LCDDR15 &= 0xf0;
	}
}

void writeChar(char ch, int pos)
{
	while (ch > 9) {
		// Decimal
		ch -= 10;
	}
	DRCLR(pos);
	switch (pos) {
	case 0:
		LCDDR2 |= num[ch][0] << 4;
		LCDDR7 |= num[ch][1] << 4;
		LCDDR12 |= num[ch][2] << 4;
		LCDDR17 |= num[ch][3] << 4;
		break;
	case 1:
		LCDDR2 |= num[ch][0];
		LCDDR7 |= num[ch][1];
		LCDDR12 |= num[ch][2];
		LCDDR17 |= num[ch][3];
		break;
	case 2:
		LCDDR1 |= num[ch][0] << 4;
		LCDDR6 |= num[ch][1] << 4;
		LCDDR11 |= num[ch][2] << 4;
		LCDDR16 |= num[ch][3] << 4;
		break;
	case 3:
		LCDDR1 |= num[ch][0];
		LCDDR6 |= num[ch][1];
		LCDDR11 |= num[ch][2];
		LCDDR16 |= num[ch][3];
		break;
	case 4:
		LCDDR0 |= num[ch][0] << 4;
		LCDDR5 |= num[ch][1] << 4;
		LCDDR10 |= num[ch][2] << 4;
		LCDDR15 |= num[ch][3] << 4;
		break;
	case 5:
		LCDDR0 |= num[ch][0];
		LCDDR5 |= num[ch][1];
		LCDDR10 |= num[ch][2];
		LCDDR15 |= num[ch][3];
		break;
	}
}

int is_prime(long number)
{
	for (int i = 2; i < number; ++i) {
		if (number % i == 0) {
			return 0;
		}
	}
	return 1;
}

int pp;
void printAt(long num, int pos)
{
	lock(&m);
	pp = pos;
	pp++;
	writeChar((num % 100) / 10, pp);
	writeChar(num % 10, pp-1);
	unlock(&m);
}

void computePrimes(int pos)
{
	long n;
	for (n = 1;; n++) {
		if (is_prime(n)) {
			printAt(n, pos);
		}
		_delay_ms(5000);
	}
}

void prime(int x)
{
	for (long i = x; i < 999999; i++) {
		if (is_prime(i)) {
			writeLong(i);
		}
		_delay_ms(5000);
	}
}

void writeLong(long x)
{
	int i;
	int bit;

	for (i = 5; i >= 0; i--) {
		bit = x / pow(10, i);
		writeChar(bit, i);
		x -= bit * pow(10, i);
	}
}

/**
 * Interrupt for joystick
 */
ISR(SIG_PIN_CHANGE1)
{
	DISABLE();
	// Check pressed status
	if ((PINB & PORTB) != PORTB) {    //determine whether bit 7 of PINB is 1 
		yield();
	}
	ENABLE();
}

ISR(SIG_OUTPUT_COMPARE1A)
{
	DISABLE();
	yield();
	ENABLE();
}

int main()
{
	// Initialization
	LCDCRA = 0x80;
	LCDCRB = 0xb7;
	TCCR1B = 0x04;
	// PORTB |= 0x80;

	//configuration for interrupt by joystick
	PORTB = 0x80;           //Enable portB
	EIMSK = 0x80;           //Pin change interrupt enable 1
	PCMSK1 = 0x80;          //logical interrupt source PCINT15 enable

	//configration for Timer
	//OCR1A = 0X0186;       //a period of 50MS              (8000000/256)*0.05=390=0X186
	// OCR1A = 0X3D09; //a period of 0.5s
	// TODO the value doesn't seem very right.
	OCR1A = 8000000/256*0.0001;
	TCNT1 = 0x0;    //start timer from 0
	TCCR1B = 0x0D;  //set the timer to CTC (bit 3 = 1) and use prescaling factor of 1024 (bit2,1,0 = 101)
	TIMSK1 = 0x02;  //enable timer output compare A

	spawn(computePrimes, 0);
	computePrimes(3);
}
