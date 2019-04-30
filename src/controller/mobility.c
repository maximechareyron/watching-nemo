#include <inttypes.h>
#include <math.h>
#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#include <string.h>

#include "tools.h"
#include "mobility.h"
#include "fish.h"
#include "aquarium.h"


#ifdef WIN32
#include <windows.h>
#define msleep(ms) Sleep (ms)
#else
#include <unistd.h>
#define msleep(ms) usleep ((ms)*1000)
#endif


static void *mobility_functions[NO_MOBILITY] = {random_path};

static void *mobility_params[NO_MOBILITY] = {random_param_init};
static char *mobility_names[NO_MOBILITY] = {"RandomWayPoint"};

#define MOBILITY_RUNNING 0
#define MOBILITY_STOPPED 1

pthread_t update_thread;

static int mobility_status = MOBILITY_STOPPED;

struct size aquarium_size;

int mobility_from_name(char *name) {
  for (enum mobility_function f = 0; f != NO_MOBILITY; f++) {
    if (strcmp(name, mobility_names[f]) == 0) {
      return f;
    }
  }
  return NO_MOBILITY;
}


void *updater(void *param) {
  printf("Mobiliy started\n");
  while (mobility_status == MOBILITY_RUNNING) {
    fishs_update();
    msleep(DT);
  }
  return NULL;
}


void mobility_init(struct aquarium *aquarium) {
  srand(time(NULL));
  aquarium_size = aquarium->size;
  mobility_status = MOBILITY_RUNNING;
  pthread_create(&update_thread, NULL, &updater, NULL);
}

void mobility_finalize() {
  mobility_status = MOBILITY_STOPPED;
  pthread_join(update_thread, NULL);
}

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

void *no_mobility(struct fish* fish) {
  return NULL;
}

void *random_path(struct fish* fish) {
  struct random_path_param *p = fish->param;

  printf("time to arrival : %ld, pos : %dx%d\n", p->time_to_arrival, fish->coordinates.x, fish->coordinates.y);
  if (p->time_to_arrival <= DT) {
    p->time_to_arrival = RANDOM_TIME_MAX;
    random_point(&p->end_point);
    printf("New dest : %dx%d\n", p->end_point.x, p->end_point.y);
    return NULL;
  }
  
  time_ms k =  p->time_to_arrival/DT;
  fish->coordinates.x += (p->end_point.x - fish->coordinates.x)/k;
  fish->coordinates.y += (p->end_point.y - fish->coordinates.y)/k;
  p->time_to_arrival -= DT;
  return NULL;
}

void random_param_init(struct fish *fish) {
  struct random_path_param *p = malloc(sizeof(struct random_path_param));
  p->time_to_arrival = 0;
  fish->param = p;
}

void *no_mobility_param_init(struct fish *fish) {
  return NULL;
}

void call_mobility_function(struct fish *fish) {
  printf("mobility call\n");
  if (fish->state == STARTED) {
    fish->mobility_function(fish);
  }
}


void setup_mobility(struct fish* fish, char *mobility) {
  printf("Mobility : %s\n", mobility);
  int f = mobility_from_name(mobility);
  printf("%d\n", f);
  if (f == NO_MOBILITY) {
    fish->mobility_function = &no_mobility;
    no_mobility_param_init(fish);
    return;
  }
  fish->mobility_function = mobility_functions[f];
  void *(*param_init)(struct fish*) = mobility_params[f];
  param_init(fish);
}
