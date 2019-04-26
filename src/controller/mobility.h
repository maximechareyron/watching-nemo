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

enum mobility_function {
			RANDOM_WAY_POINT,
			NO_MOBILITY
};

void mobility_init(struct aquarium *);
void mobility_finalize();

void *random_path(struct fish*);
void random_param_init(struct fish *fish);

void *no_mobility(struct fish* fish);
void *no_mobility_param_init(struct fish *fish);


int mobility_from_name(char *name);
void call_mobility_function(struct fish*);
void setup_mobility(struct fish* fish, char *mobility);

#endif
