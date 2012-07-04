#include <c6x.h>
#include "c6211dsk.h"
#include "robotnic.h"
#include "timers.h"
#include "camera.h"
#include <stdlib.h>

#define LED_0_ON 0x0e000000
#define LED_1_ON 0x0d000000
#define LED_2_ON 0x0b000000

#define LED_0_OFF 0x01000000
#define LED_1_OFF 0x02000000
#define LED_2_OFF 0x04000000

#define LED_ON 0x08000000
#define LED_OFF 0x07000000

// retrieve RGB from one integer
#define GET_R(x) (((*(x)) >> 0) & 0xFF)
#define GET_G(x) (((*(x)) >> 8) & 0xFF)
#define GET_B(x) (((*(x)) >> 16) & 0xFF)

// defining the minimal pixel value processed, used in normalization
#define DARK 20

// the threshold for similar color
#define THRESHOLD 400

// in pixel unit
// criteria for one valid column; at least this pixel in this column
// which indicates that when the box is too far, it will not be
// recognized
#define MIN 10
#define REPEAT	10
#define BOXWIDTH 120

// define the center range for taking pictures and hitting
#define CENTERRANGE 10

// the product of distance and the angle in radian is one constant
#define REALHEIGHT 2660

#define RED	0
#define BLUE	1
// in circle unit
// the distance between the camera and the center of rotating
#define HEAD	2

// TODO not used so far
// convert between two different units
#define PIXEL2CIRCLE	0.1

// rotate 90 degrees counter-clockwise
#define NINTY	13

#define BOUNDARYSIZE	2

// the distance between two adjacent boxes
#define BOXDISTANCE 20

// control the camera orientation
#define LEFT 255
#define FRONT 45
#define RIGHT 50

// for controlling the pod
#define UP 250
#define DOWN 100

// extra distance to march in order to make sure the robot is oriented
// correctly
#define EXTRA 10
#define VALIDATEIMAGELOOPS	3
#define IMPACTLOOPS 8
#define FOCUSLOOPS 2
#define TUNINGLOOPS 2
#define ALIGNUPLOOPS 3

// function declaration
void ConfigImageType(ImageType *Config);
void InitDSPReg(void);
void normalization();
void colorSegment(int reference);
void numberOfBoxes();
void alignUp();
void centerOfGravity();
void validateImage(int color);
void march(int distance);
void rotate(int distance);
void focus();
void tuning();
void initPosition();

// global variables
int in_pre_0, in_pre_1;
int encode_0, encode_1;

// define the direction the robot is marching, 1 means the object is
// on the left, -1 means the object is on the right
int direction;

ImageType PictRGB;

// reference point
unsigned int reference_b;
unsigned int reference_r;

// number of white pixels along the row and column direction
unsigned char *num_row, *num_col;

// center of gravity
int *x,*y;

// indicating the box boundary
int boundary[2*BOUNDARYSIZE];
unsigned char y_0, y_1;
int num_box;

// value of the picture, 1 or 0
int result;

int distance;

int flag;

unsigned char all_black;
void ConfigImageType(ImageType *Config)
{

	Config->Image = (unsigned int *)0x80050000;
	Config->Hight = 240;
	Config->Width = 320;
	Config->StartVertical = 1;
	Config->StartHorizontal = 1;
	Config->Decimation = 0;         /* 0,2,4,8 */
	Config->Resolution = QVGA;      /* VGA, QVGA*/
	Config->ScanMode = INTERLACED;  /* INTERLACED */
	Config->GrabbMode = GRABB_RGB;  /* GRABB_RGB, GRABB_YUV */

}

/**
 * Initial the HD
 */
void InitDSPReg(void)
{

	CSR=0x100;			             /* disable all interrupts            */
	IER=1;                           /* disable all interrupts except NMI */
	ICR=0xffff;                      /* clear all pending interrupts      */
	*(unsigned volatile int *)EMIF_GCR = 0x3300; /* EMIF global control   */
	*(unsigned volatile int *)EMIF_CE0 = 0x30;   /* EMIF CE0 control       */
	*(unsigned volatile int *)EMIF_CE3 = 0x77f3c523;  /* EMIF CE3 control*/
	*(unsigned volatile int *)EMIF_CE1 = CE1_32; /*EMIF CE1 control, 32bit async */
	*(unsigned volatile int *)EMIF_SDCTRL = 0x07117000; /* EMIF SDRAM control     */
	*(unsigned volatile int *)EMIF_SDRP = 0x61a;       /* EMIF SDRM refresh period */
	*(unsigned volatile int *)EMIF_SDEXT = 0x54519; /* EMIF SDRAM extension    */
}

/**
 * Normalize RGB vectors in this image, and expand them to [0 255].
 * The original image is overwritten.
 * image, r, g, b all have the same size(row x column)
 * The range of values in image is [0 255].
 * The range of values in r, g, and b is [0 255], which are normalized
 * vector expanded to [0 255] range.
 */
void normalization()
{
	int R,G,B;
	int intensity;
	int i,j;
	all_black = 1;
	for(i=0;i<PictRGB.Hight;++i){
		for(j=0;j<PictRGB.Width;++j){
			R=GET_R(PictRGB.Image+i*PictRGB.Width+j);
			G=GET_G(PictRGB.Image+i*PictRGB.Width+j);
			B=GET_B(PictRGB.Image+i*PictRGB.Width+j);

			intensity=R+G+B;
			// eliminate dark pixel
			if(intensity < DARK){
				*(PictRGB.Image+i*PictRGB.Width+j) = 0;
				continue;
			}
			// indicating the image contains at least one white pixel
			all_black =0;

			R = 255*R/intensity;
			G = 255*G/intensity;
			B = 255*B/intensity;
			// overwrite the original image
			*(PictRGB.Image+i*PictRGB.Width+j)=((unsigned char)B << 16) |
				((unsigned char)G<<8) | ((unsigned char)R<<0);
		}
	}
}

/**
 * Calculate the distance according to the reference point. The result
 * is saved in num_row, and num_col.
 * The range of values in image is [0 255].
 * The range of values in reference is [0 255].
 */
void colorSegment(int color)
{
	int i,j,k;
	unsigned char r, g, b;
	// reference value
	unsigned char r_0, g_0,b_0;
	// distance in normalized color space
	int distance;
	// initialization
	for(i=0;i<PictRGB.Hight;++i){
		num_row[i] = 0;
	}
	for(j=0;j<PictRGB.Width;++j){
		num_col[j] = 0;
	}

	if(color == RED){
		r_0=GET_R(&reference_r);
		// g_0=GET_G(reference_b);
		b_0=GET_B(&reference_r);

		for(i=0;i<PictRGB.Hight;++i){
			for(j=0;j<PictRGB.Width;++j){
				r=GET_R(PictRGB.Image+i*PictRGB.Width+j);
				// g=GET_G(PictRGB.Image+i*PictRGB.Width+j);
				b=GET_B(PictRGB.Image+i*PictRGB.Width+j);

				distance = (r-r_0)*(r-r_0)+(b-b_0)*(b-b_0);
				if(distance<THRESHOLD){
					// white color
					num_row[i]++;
					num_col[j]++;
					// *(PictRGB.Image+i*PictRGB.Width+j) = 0x00FFFFFF;
				} else {
					// *(PictRGB.Image+i*PictRGB.Width+j) = 0;
				}
			}
		}
	} else {
		r_0=GET_R(&reference_b);
		// g_0=GET_G(reference_b);
		b_0=GET_B(&reference_b);

		for(i=y_0;i<=y_1;++i){
			for(j=0;j<BOUNDARYSIZE;++j){
				for(k=boundary[2*j]; k<=boundary[2*j+1]; ++k){
					r=GET_R(PictRGB.Image+i*PictRGB.Width+k);
					// g=GET_G(PictRGB.Image+i*PictRGB.Width+j);
					b=GET_B(PictRGB.Image+i*PictRGB.Width+k);

					distance = (r-r_0)*(r-r_0)+(b-b_0)*(b-b_0);
					if(distance<THRESHOLD+1000){
						// white color
						num_row[i]++;
						num_col[k]++;
						*(PictRGB.Image+i*PictRGB.Width+k) = 0x00FFFFFF;
					} else {
						*(PictRGB.Image+i*PictRGB.Width+k) = 0;
					}

				}
			}
		}
	}
}

/**
 * Determine how many boxes in this picture. The boundaries(x and y) are set.
 */
void numberOfBoxes()
{
	int i, box;
	i=0;
	for(box=0; box<BOUNDARYSIZE; ++box) {
		while(1) {
			if(i>=PictRGB.Width) {
				break;
			}
			while(i<PictRGB.Width&&num_col[i]<MIN) {
				i++;
			}
			boundary[2*box]=i;
			while(i<PictRGB.Width && num_col[i]>MIN) {
				i++;
			}
			if(i - boundary[2*box] > REPEAT) {
				break;
			} else {
				i++;
				continue;
			}
		}
		i = boundary[2*box];
		while(1) {
			if(i>=PictRGB.Width) {
				break;
			}
			while(i<PictRGB.Width && num_col[i]>MIN) {
				i++;
			}
			boundary[2*box+1]=i;
			while(i<PictRGB.Width&&num_col[i]<MIN) {
				i++;
			}
			if(i - boundary[2*box+1] > REPEAT) {
				break;
			} else {
				i++;
				continue;
			}
		}
		i = boundary[2*box+1];
	}
	if(boundary[0]==PictRGB.Width) {
		// the picture is black
		num_box = 0;
	} else if ( boundary[1]== PictRGB.Width || boundary[2]==PictRGB.Width) {
		num_box = 1;
	} else if( boundary[3]== PictRGB.Width
				&& (boundary[1]-boundary[0]<BOXWIDTH/2 || boundary[3]-boundary[2]<BOXWIDTH/2) ) {
		num_box=1;
	} else {
		num_box = 2;
	}

	i = 0;
	while(1) {
		if(i>=PictRGB.Hight) {
			break;
		}
		while(i<PictRGB.Hight && num_row[i] < 80) {
			i++;
		}
		y_0 = i;
		while(i<PictRGB.Hight && num_row[i] > 80) {
			i++;
		}
		if(i - y_0 > REPEAT) {
			break;
		} else {
			i++;
			continue;
		}
	}
	i=y_0;
	while(1) {
		if(i>=PictRGB.Hight) {
			break;
		}
		while(i<PictRGB.Hight && num_row[i] > 80) {
			i++;
		}
		y_1 = i;
		while(i<PictRGB.Hight && num_row[i] < 80) {
			i++;
		}
		if(i - y_1 > REPEAT) {
			break;
		} else {
			i++;
			continue;
		}
	}
}

/**
 * adjust the car so that it aligns with the boxes according to the difference of the height of the two boxes
 * @pre the number of boxes are more than 1
 */
void alignUp()
{
	int i;
	for(i=0; i<ALIGNUPLOOPS; ++i) {
		if(num_col[boundary[2*num_box-1]] > num_col[boundary[0]]) {
			rotate(-NINTY/abs(NINTY));
		} else if(num_col[boundary[2*num_box-1]] < num_col[boundary[0]]) {
			rotate(NINTY/abs(NINTY));
		} else {
			break;
		}
		validateImage(RED);
		if(num_box <= 1) {
			break;
		}
	}

}

/**
 * Calculate the center of gravity, and save it to (x,y).
 * Check the number on this box, and save it to result.
 */
void centerOfGravity()
{
	int sum, n;
	int hlength = 0;
	int i;
	if (num_box == 1) {
		int a,b;
		//num of object is one.
		sum=0;
		n=0;
		if(boundary[1]-boundary[0]>BOXWIDTH/2) {
			a=boundary[0];
			b=boundary[1];
		} else {
			a=boundary[2];
			b=boundary[3];
		}
		// calculate x
		for(i=a; i <=b; ++i){
			sum = sum + num_col[i]*i;
			n = n + num_col[i];
		}
		*x=sum/n;

		// calculate the horizontal length
		// go to left
		i=*x;
		while(i >= 0 && num_col[i] > 0) {
			hlength++;
			i--;
		}
		// go to right
		i=*x+1;
		while(i < PictRGB.Width && num_col[i] > 0) {
			hlength++;
			i++;
		}
		if((b-a)/hlength>5) {
			result = 1;
		} else {
			result = 0;
		}
	} else if(num_box == 2) {
		int a, b, c, d;
		if(direction == 1) {
			a = boundary[0];
			b = boundary[1];
			c = boundary[2];
			d = boundary[3];
		} else {
			a = boundary[2];
			b = boundary[3];
			c = boundary[0];
			d = boundary[1];
		}
		sum=0;
		n=0;
		// calculate x
		for(i=a; i <=b; ++i){
			sum = sum + num_col[i]*i;
			n = n + num_col[i];
		}
		*x=sum/n;

		// calculate the horizontal length
		// go to left
		i=*x;
		while(i >= 0 && num_col[i] > 0) {
			hlength++;
			i--;
		}
		// go to right
		i=*x+1;
		while(i < PictRGB.Width && num_col[i] > 0) {
			hlength++;
			i++;
		}
		if((b-a)/hlength>5) {
				// calculate x
				for(i=c; i <=d; ++i){
					sum = sum + num_col[i]*i;
					n = n + num_col[i];
				}
				*x=sum/n;
				// calculate the horizontal length
				// go to left
				i=*x;
				while(i >= 0 && num_col[i] > 0) {
					hlength++;
					i--;
				}
				// go to right
				i=*x+1;
				while(i < PictRGB.Width && num_col[i] > 0) {
					hlength++;
					i++;
				}
				if((d-c)/hlength>5) {
					result = 1;
				} else {
					result = 0;
				}
		} else {
				result = 0;
		}
	}
}

/**
 * Based on the argument, this function will focus on red or blue color.
 */
void validateImage(int color)
{
	int i;
	if(color == 0) {
		for(i=0; i<VALIDATEIMAGELOOPS; ++i) {
			delay_msec(350);
			GrabbImageRGB(PictRGB, GRABB_SYNCHRONOUS);
			normalization();
			if(all_black != 1) {
				break;
			}
		}
		colorSegment(RED);
		numberOfBoxes();
	} else {
		// ensure that validateImage(RED) was called before
		if(num_box == -1) {
			validateImage(RED);
		}
		colorSegment(BLUE);
		centerOfGravity();
		num_box = -1;
	}
}

int leftOrRight()
{
	int i;
	int max = num_col[0];
	int max_index = 0;
	for(i=1; i<PictRGB.Width; ++i) {
		if(max < num_col[i]){
			max = num_col[i];
			max_index = i;
		}
	}
	if(max_index<PictRGB.Width/2-CENTERRANGE) {
		// LED_1  LEFT
		// *(unsigned volatile int *) IO_PORT |= LED_1;
		return -1;
	} else if ( max_index > PictRGB.Width/2 + CENTERRANGE) {
		// LED_3  RIGHT
		// *(unsigned volatile int *) IO_PORT |= LED_3;
		return 1;
	} else {
		// LED_2  MID
		// *(unsigned volatile int *) IO_PORT |= LED_2;
		return 0;
	}
}

int getDistance()
{
	if(y_1 < y_0) {
		return 20;
	} else {
		return REALHEIGHT/(y_1-y_0);
	}
}

void march(int distance)
{
	if(abs(distance) < 50) {
		if(distance > 0) {
			// forward
			encode_0 = distance;
			encode_1 = distance;
			Motor_0(220);
			Motor_1(230);
			while(1) {
				if(encode_0 == 0 && encode_1 == 0) {
					Motor_0(0);
					Motor_1(0);
					break;
				}
				if(encode_0 == 0) {
					Motor_0(0);
				}
				if(encode_1 == 0) {
					Motor_1(0);
				}
			}
		} else if (distance < 0) {
			// backward
			encode_0 = -distance;
			encode_1 = -distance;
			Motor_0(-220);
			Motor_1(-230);
			while(1) {
				if(encode_0 == 0 && encode_1 == 0) {
					Motor_0(0);
					Motor_1(0);
					break;
				}
				if(encode_0 == 0) {
					Motor_0(0);
				}
				if(encode_1 == 0) {
					Motor_1(0);
				}
			}
		}
	} else {
		march(20);
	}
}

void rotate(int distance)
{
	if(distance > 0) {
		// forward
		encode_0 = distance;
		encode_1 = distance;
		Motor_0(-200);
		Motor_1(210);
		while(1) {
			if(encode_0 == 0 && encode_1 == 0) {
				Motor_0(0);
				Motor_1(0);
				break;
			}
			if(encode_0 == 0) {
				Motor_0(0);
			}
			if(encode_1 == 0) {
				Motor_1(0);
			}
		}
	} else if (distance < 0) {
		// backward
		encode_0 = -distance;
		encode_1 = -distance;
		Motor_0(200);
		Motor_1(-210);
		while(1) {
			if(encode_0 == 0 && encode_1 == 0) {
				Motor_0(0);
				Motor_1(0);
				break;
			}
			if(encode_0 == 0) {
				Motor_0(0);
			}
			if(encode_1 == 0) {
				Motor_1(0);
			}
		}
	}
}

/**
 * Move the robot back and forth so that the object falls in the
 * center range. If the car is not parallel to the alignment of boxes,
 * alignUp function will be called.
 */
void focus()
{
	int i;
	for(i=0;i<FOCUSLOOPS;++i) {
		validateImage(RED);
		if(num_box == 0) {
			march(5*direction);
			continue;
		} else if(num_box > 1) {
		//	alignUp();
		}
		validateImage(BLUE);
		if( abs(*x - (int)PictRGB.Width/2) < CENTERRANGE/2 ) {
			break;
		} else {
			if((*x - (int)PictRGB.Width/2) < 0) {
				march(NINTY/abs(NINTY));
			} else {
				march(-NINTY/abs(NINTY));
			}
		}
	}
}

/**
 * This function is meant to be called when the object is quite close
 * to the car.
 */
void tuning()
{
	int i;
	for(i=0; i<TUNINGLOOPS; ++i) {
		validateImage(1);
		if(*x < ((int)PictRGB.Width - CENTERRANGE)/2 ) {
			rotate(NINTY/abs(NINTY));
		} else if (*x > ((int)PictRGB.Width + CENTERRANGE)/2 ) {
			rotate(-NINTY/abs(NINTY));
		} else {
			break;
		}
	}
}

/**
 * Timer0_ISR Used for decrease the circle.
 */
interrupt void timer0_isr(void)
{
	int in_0, in_1;
	in_0 = Dig_In_0();
	in_1 = Dig_In_1();
	if(Dig_In_4() == 0 && Dig_In_5() == 0 && flag == 0) {
		flag = 1;
		encode_0 = encode_1 = 0;
	}
	if(encode_0 > 0) {
		if(in_0 != in_pre_0 ){
			*(unsigned volatile int *)IO_PORT ^= LED_0_ON; // Display 1 on LEDs
			encode_0--;
			in_pre_0 = in_0;
		} else {
			*(unsigned volatile int *)IO_PORT |= LED_0_OFF; // Turn off all user LEDs
		}
	} // End - IF
	if(encode_1 > 0) {
		if(in_1 != in_pre_1 ){
			*(unsigned volatile int *)IO_PORT ^= LED_1_ON; // Display 1 on LEDs
			encode_1--;
			in_pre_1 = in_1;
		} else {
			*(unsigned volatile int *)IO_PORT |= LED_1_OFF; // Turn off all user LEDs
		}
	} //End - IF
}

void initPosition()
{
}

void lab4()
{
	int i;
	Servo_0(FRONT);

	while(1) {
		for(i=0; i<3; ++i) {
			GrabbImageRGB(PictRGB,GRABB_SYNCHRONOUS);
			normalization();
			colorSegment(reference_b);
			centerOfGravity();
			if(*x != -1) {
				break;
			}
		}

		if(i==3) {
			// there is no object in this view
			rotate(-3);
			continue;
		}
		result = leftOrRight();

		if(result == -1) {
			rotate(1);
		} else if(result == 1) {
			rotate(-1);
		}
	}
}

void main(void){
	unsigned char AGC_Control;
	signed char BlueGain, RedGain;
	int box;
	int i;
	int debug;


	CSR=0x100;			             /* disable all interrupts            */
	IER=1;                           /* disable all interrupts except NMI */
	ICR=0xffff;                      /* clear all pending interrupts      */
	*(unsigned volatile int *)EMIF_GCR = 0x3300; /* EMIF global control   */
	*(unsigned volatile int *)EMIF_CE0 = 0x30;   /* EMIF CE0 control       */
	*(unsigned volatile int *)EMIF_CE3 = 0x7771c323;   /* EMIF CE3 control*/
	*(unsigned volatile int *)EMIF_CE1 = 0xffffff03; /* EMIF CE1 control, 8bit async */
	*(unsigned volatile int *)EMIF_SDCTRL = 0x07117000; /* EMIF SDRAM control     */
	*(unsigned volatile int *)EMIF_SDRP = 0x61a;       /* EMIF SDRM refresh period */
	*(unsigned volatile int *)EMIF_SDEXT = 0x54519; /* EMIF SDRAM extension    */

	// initialization
	in_pre_0 = Dig_In_0();
	in_pre_1 = Dig_In_1();
	encode_0 = 0;
	encode_1 = 0;
	direction = 1;
	reference_b = (((unsigned char)132)<<16) |
		(((unsigned char)65)<<8) | (((unsigned char)56)<<0);

	reference_r = (((unsigned char)25)<<16) |
		(((unsigned char)75)<<8) | (((unsigned char)144)<<0);

	x = malloc( sizeof(int) );
	y = malloc( sizeof(int) );
	if(x == NULL || y == NULL) {
		while(1) {
			x = 0;
		}
	}

	ConfigImageType(&PictRGB);
	num_row=malloc((int)PictRGB.Hight*sizeof(char));
	num_col=malloc((int)PictRGB.Width*sizeof(char));

	if(num_row == NULL || num_col == NULL) {
		while(1) {
			x = 0;
		}
	}

	AGC_Control=5;
	BlueGain = 11;
	RedGain = -80;

	// Hardware Reset
	ResetCamera();
	// Software Reset
	OV7620_ResetChip();
	// Init Camera
	InitCamera(PictRGB);

	// TODO what is the difference between this and the above
	// initialization

	// Init the Hardware
	InitDSPReg();
	Start_Timer0();

	// Using Bandfilter for indoor
	// OV7620_BandFilterEnable(1);

	// Auto Adjust - to use the manual settings this has to be turned
	// off. The camera is by default enabled, 1 = enable, 0 = disable
	OV7620_AutoAdjustMode(0);
	// Set Registers
	OV7620_AGCGainControl(AGC_Control); // range [0 255]
	OV7620_BlueGainControl(BlueGain);// range [-127 127]
	OV7620_RedGainControl(RedGain);// range [-127 127]

	All_DC_Motor_Off();
	*(unsigned volatile int *) IO_PORT = LED_OFF;

	while(1) {
		/*
		   Servo_0(LEFT);
		   Servo_0(FRONT);
		  */

		/*
		   march(20);
		   march(-35);
		  */

		/*
		   rotate(NINTY);
		   rotate(-NINTY);
		  */
		/*
		Servo_0(FRONT);
		validateImage(RED);
		validateImage(BLUE);
		*/
		/*
		// Choose grabbing format, GRABB_SYNCHRONOUS, GRABB_ASYNCHRONOUS
		GrabbImageRGB(PictRGB,GRABB_SYNCHRONOUS);
		// Read registers
		//OV7620_AGCGainControlRead(&AGC_Control);
		//OV7620_BlueGainControlRead(&BlueGain);
		//OV7620_RedGainControlRead(&RedGain);

		normalization();
		colorSegment(reference_r);
		centerOfGravity();
		*/

		initPosition();
		direction = 1;
		while(1) {
			All_DC_Motor_Off();
			march(BOXDISTANCE*direction);
		 	for(box=0; box<4; ++box) {
		 		Servo_0(LEFT);
		 		focus();
		 		if(result == 1) {
		 			tuning();
					// the image is one, no need to turn, go to next box
					march(BOXDISTANCE*direction);
					continue;
				}
				// eliminate the distance between the camera and the
				// rotating center
				march(HEAD);
				rotate(NINTY);
				Servo_0(FRONT);
				validateImage(RED);
				validateImage(BLUE);
				if(result == 1) {
					tuning();
					rotate(-NINTY);
					march(BOXDISTANCE*direction);
					continue;
				}
				distance = getDistance();
				march(distance-20);
				//  the image is zero, prepare for impact
				Servo_2(DOWN);
				for(i=0; i<IMPACTLOOPS; ++i) {
					validateImage(RED);
					validateImage(BLUE);
					tuning();
					if(result == 1) {
						break;
					}
					distance = getDistance();
					flag = 0;
					march(distance+EXTRA);
					march(-20);

				}
				Servo_2(UP);
				// rotate 90 degrees clockwise
				rotate(-NINTY);
				// go to next box
				march(BOXDISTANCE*direction);
			}
			if(direction == 1) {
				direction = -1;
			} else {
				direction = 1;
			}
		}

	}
}
