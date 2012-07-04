/*
 * 
 * TinyTimber.h 
 *
 */

#ifndef _TINYTIMBER_
#define _TINYTIMBER_

//      Header file tailored to AVR systems
#include <avr/interrupt.h>

//      Abstract type, used in the definition of Object.
struct thread_block;

//      Base class of reactive objects. Every reactive object in a TinyTimber 
//      system must be of a class that inherits this class.
typedef struct {
    struct thread_block *ownedBy, *wantedBy;
} Object;

//      Abstract type, used in the definition of Msg
struct msg_block;

//      Type that identifies outstanding messages
typedef struct msg_block *Msg;

//      Unit value of type Msg
#define NullMsg \
        (Msg)0
// More readable when the second argument is not used.
#define NOTHING 0

//      Base type for methods. Every method in a TinyTimber system should take 
//      a first argument that is a subclass of class Object.
typedef int (*Method)(Object*, int);

//      Initialization macro for class Object. 
#define initObject() \
        { 0, 0 }

//      Abstract type of time values.
typedef signed long Time;

//  void WITHIN(Time bl, Time dl, T *to, int (*meth)(T*, A), A arg);
//      Asynchronously invoke method m on object obj with argument a, baseline 
//      offset b, and relative deadline d. Type T must be a struct type that 
//      inherits from Object, while A can be any int-sized type.
//      Offsets bl and dl allow a new execution window for the asynchronous 
//      call to be defined on basis of the current one:
//         new baseline = current baseline,    bl < 0
//                      = max(current baseline + b, current time), otherwise
//         new deadline = current deadline,    dl < 0
//                      = new baseline + d,    otherwise
//      A dl value of zero is interpreted as the infinity.
#define WITHIN(bl, dl, to, meth, arg) \
        async(bl, dl, (Object*)to, (Method)meth, (int)arg)

//  void AFTER(Time bl, T *to, int (*meth)(T*, A), A arg);
//      Asynchronously invoke method m on object obj with argument a and a 
//      baseline offset of b. 
//      Identical to WITHIN(b, -1, obj, m, a).
#define AFTER(bl, to, meth, arg) \
        async(bl, -1, (Object*)to, (Method)meth, (int)arg)

//  void BEFORE(Time dl, T *to, int (*meth)(T*, A), A arg);
//      Asynchronously invoke method m on object obj with argument a and a
//      relative deadline of d. 
//      Identical to WITHIN(-1, d, obj, m, a). 
#define BEFORE(dl, to, meth,arg) \
        async(-1, dl, (Object*)to, (Method)meth, (int)arg)

//  void ASYNC(T *to, int (*meth)(T*, A), A arg);
//      Asynchronously invoke method m on object obj with argument a. 
//      Identical to WITHIN(-1, -1, obj, m, a).
#define ASYNC(to, meth, arg) \
        async(-1, -1, (Object*)to, (Method)meth, (int)arg)

//  int SYNC( T *obj, int (*m)(T*,A), A a );
//      Synchronously invoke method m on object obj with argument a. Type T 
//      must be a struct type that inherits from Object, while A can be any 
//      int-sized type. If completion of the call would result in deadlock, 
//      -1 is returned; otherwise the result is the value returned by m.
#define SYNC(to, meth, arg) \
        sync((Object*)to, (Method)meth, (int)arg)

//      Declare a system startup handler whose body is stmt.
#define STARTUP(stmt) \
  int main(void) { initialize(); stmt; idle(); }

//      Declare an interrupt-handler for vector whose body is stmt.
#define INTERRUPT(vector, stmt) \
        ISR(vector) { mark(); stmt; schedule(); }

//      Return the baseline of the currently executing method call.
#define BASELINE() \
        current_baseline()

#if defined(__AVR_ATmega169__)    // AVR ATmega169 Butterfly dependencies 
//      Construct a Time value form an argument given in milliseconds.
#define MSEC(x) \
  ((Time)(x) * 31 + ((x) / 4))
//      Construct a Time value form an argument given in seconds.
#define SEC(x) \
  (((x) * (Time)31250)) 
//      Construct a Time with a value 1/31250 of a second, the resolution of butterfly
#define RESOLUTION(x) \
        ((Time)(x))
#elif defined(__AVR_ATmega32__)    // AVR ATmega32 dependencies
//      Construct a Time value form an argument given in microseconds.
#define USEC(x) \
        ((Time)(x))
//      Construct a Time value form an argument given in milliseconds.
#define MSEC(x) \
        ((Time)(x * (Time)1000))
//      Construct a Time value form an argument given in seconds.
#define SEC(x) \
        ((x * (Time)1000000)) 
#endif

//      Abort an outstanding asynchronous message (if not yet active)
#define ABORT(msg) \
        abort_msg(msg);
        
// -------------------------------------------------------------------
// No externally significant information below this line
// -------------------------------------------------------------------
#define INLINE_3
#define INLINE_2
#define INLINE_1 inline

INLINE_2 void initialize(void);
INLINE_2 void idle(void);


INLINE_3 Msg async(Time bl, Time dl, Object *to, Method m, int arg);   
INLINE_3 int sync(Object *to, Method m, int arg);                       

#endif
