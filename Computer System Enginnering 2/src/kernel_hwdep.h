#ifndef KERNEL_HWDEP_H
#define KERNEL_HWDEP_H

#define CSR_BIT 0x80
#define ISR_OFF 0x80
#define ISR_ON 0x0

/*MED SFR_STARTADDRESS       0x7ff0000*/
#define rTEST (*(volatile unsigned*)(0x7ff0000))

/*------------- Timer 0 -----------------------*/
#define rTDAT0 (*(volatile unsigned short*)(0x7ff9000))
#define rTPRE0 (*(volatile unsigned char *)(0x7ff9002))/* Prescale timer 8-9, ~400 ms*/
#define rTCON0 (*(volatile unsigned char *)(0x7ff9003))

/*------------ Interrupt Control-------------- */
#define rSYSCON (*(volatile unsigned char *)(0x7ffd003))
#define rINTMOD (*(volatile unsigned*)(0x7ffc000))
#define rINTPND (*(volatile unsigned*)(0x7ffc004))
#define rINTMSK (*(volatile unsigned*)(0x7ffc008))

//void Init_IRQ_TINT0(void);
unsigned int set_isr( unsigned int newCSR );
extern unsigned int Get_psr(void);
extern void Set_psr(unsigned int PSR);
void timer0_start(void);

#endif
