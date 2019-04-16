#ifndef MOBILITY_H
#define MOBILITY_H
#include <time.h>

//Time in ms between each update
#define DT 100 //in ms
#define RANDOM_TIME_MAX 5000 //in ms
typedef long time_ms; 

struct fish; 

void random_path(struct fish*, time_ms dt);

void call_mobility_function(struct fish*);
#endif
