#ifndef MOBILITY_H
#define MOBILITY_H
#include <time.h>

//Time in ms between each update
#define DT = 100;

typedef long time_ms; 

struct fish; 

void random_path(struct fish*, time_ms dt);

#endif
