#ifndef MOBILITY_H
#define MOBILITY_H
#include <time.h>
#include "tools.h"


//Time in ms between each update
#define DT 100 //in ms
#define RANDOM_TIME_MAX 5000 //in ms
typedef long time_ms; 

struct fish; 
struct aquarium;

struct random_path_param {
  time_ms time_to_arrival;
  struct coordinates end_point;
};

void mobility_init(struct aquarium *);
void mobility_finalize();

void *random_path(struct fish*);
void random_param_init(struct random_path_param *p);

void call_mobility_function(struct fish*);
#endif
