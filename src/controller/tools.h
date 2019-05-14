#ifndef UTILS_H
#define UTILS_H


#include <pthread.h>

#ifdef WIN32
#include <windows.h>
#define msleep(ms) Sleep (ms)
#else
#define msleep(ms) usleep ((ms)*1000)
#endif


extern int terminate_program;
extern int sock;


struct coordinates
{
  int x;
  int y;
};

struct size
{
  int width;
  int height;
};

#endif
