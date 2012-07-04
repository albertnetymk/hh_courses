#include <math.h>
#include "lcd.h"

static int blink_flag = 0;

void writeLong(long x)
{
	int i;
	int bit;

	for (i = 5; i >= 0; i--) {
		bit = x / pow(10, i);
		writeChar(bit, i);
		x -= bit * pow(10, i);
	}
	// bit = x/(int)pow(10, 5);
	// writeChar(x, 0);

}

int writeDigit(LCD *self, int digitPos)
{
	writeChar(digitPos / 10, digitPos % 10);
	return 0;
}

int writeInt(LCD *self, unsigned int val)
{
	writeLong(val);
	return 0;
}

int segmentOn(LCD *self, int segment)
{
	switch (segment) {
		case 8:
			LCDDR8 = 0x01;
			break;
		case 13:
			LCDDR13 |= 0x01;
			break;
	}
}

int segmentOff(LCD *self, int segment)
{
	switch (segment) {
		case 8:
			LCDDR8 = 0x00;
			break;
		case 13:
			LCDDR13 &= 0x00;
			break;
	}
}

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

static void DRCLR(int pos)
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
