#include <inttypes.h>
#include <math.h>
#include <stdio.h>
#include <time.h>
#include <stdlib.h>

#include "mobility.h"
#include "fish.h"
#include "aquarium.h"


struct size aquarium_size;

struct random_path_param {
  time_ms time_to_arrival;
  struct coordinates end_point;
};

time_ms get_time (void) {
  struct timespec spec;
  clock_gettime(CLOCK_REALTIME, &spec);
  time_ms t = spec.tv_nsec / 1000000 + spec.tv_sec*1000;
  return t;
}

void random_point(struct coordinates *c) {
  c->x = rand() % aquarium_size.width;
  c->y = rand() % aquarium_size.height;
}

void mobility_init(struct aquarium *aquarium) {
  srand(time(NULL));
  aquarium_size = aquarium->size;
  fishs_update();
  //TODO set timer to recall
}

void mobility_finalize() {
  //TODO stop periodic function
}

void random_path(struct fish* fish, time_ms dt) {
  struct random_path_param *p = fish->param;
  if (p->time_to_arrival <= dt) {
    p->time_to_arrival = RANDOM_TIME_MAX;
    random_point(&p->end_point);
    return;
  }
  
  time_ms k =  dt/p->time_to_arrival;
  fish->coordinates.x += k*(p->end_point.x - fish->coordinates.x);
  fish->coordinates.y += k*(p->end_point.y - fish->coordinates.y);
  p->time_to_arrival -= dt;
}

void call_mobility_function(struct fish *fish) {
  fish->mobility_function(fish, DT);
}
