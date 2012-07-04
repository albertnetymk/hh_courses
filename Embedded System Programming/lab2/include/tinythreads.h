#ifndef _tinythreads_h
#define _tinythreads_h

struct thread_block;
typedef struct thread_block *thread;

struct mutex_block {
    int locked;
    thread waitQ;
};
typedef struct mutex_block mutex;

#define MUTEX_INIT {0,0}
#define DISABLE()       cli()
#define ENABLE()        sei()

#define UNLOCKED 0
#define LOCKED 1
void spawn(void (*function)(int), int arg);
void yield(void);
void lock(mutex *m);
void unlock(mutex *m);
#endif
