#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "fish.h"
#include "queue.h"
#include "mobility.h"
#include "tools.h"


TAILQ_HEAD(fish_queue, fish);

struct fish_queue* fishs;

void fishs_init() {
  printf("Fishs init\n");
  fishs = malloc(sizeof(struct fish_queue));
  TAILQ_INIT(fishs);
}

void fishs_finalize() {
  if (fishs == NULL) {
    return;
  }
  while (!TAILQ_EMPTY(fishs)) {
    struct fish * fish = TAILQ_FIRST(fishs);
    fish_remove(fish->name);
  }
  free(fishs);
  fishs = NULL;
}

void fishs_update() {
    if (!TAILQ_EMPTY(fishs)) {
      struct fish* f;
      for (f = TAILQ_FIRST(fishs); f != TAILQ_LAST(fishs, fish_queue); f = TAILQ_NEXT(f, queue_entries)) {
	fish_update(f);
      }
      fish_update(f);
    }   
}

void fish_update(struct fish *fish) {
  call_mobility_function(fish);
}

int fish_add(char* name, int x, int y, int w, int h, void *(*mobility_function)(struct fish*, time_ms dt)) {
  if (fish_find(name) != NULL)
    return 0;
  struct fish *fish = malloc(sizeof(struct fish));
  fish->coordinates.x = x;
  fish->coordinates.y = y;
  fish->size.width = w;
  fish->size.height = h;
  fish->name = name;
  fish->mobility_function = mobility_function;
  fish->state = NOT_STARTED;
  
  TAILQ_INSERT_TAIL(fishs, fish, queue_entries);
  return 1;
}

int fish_remove(char *name) {
  struct fish *fish;
  if ((fish = fish_find(name)) == NULL)
    return 0;
  TAILQ_REMOVE(fishs, fish, queue_entries);
  free(fish);
  return 1;
}

int fish_start(char *name) {
  struct fish *f = fish_find(name);
  if (f == NULL || f->state == STARTED)
    return 0;
  f->state = STARTED;
  return 1;
}

struct fish *fish_find(char *name) {
  struct fish *f;
  if (!TAILQ_EMPTY(fishs)) {
    for (f = TAILQ_FIRST(fishs); f != TAILQ_LAST(fishs, fish_queue); f = TAILQ_NEXT(f, queue_entries)) {
      if (strcmp(f->name,name) == 0)
	return f;
    }
    if (strcmp(f->name,name) == 0)
      return f;
  }
  return NULL;
}
				    
void fish_print(struct fish *fish) {
  if (fish != NULL) {
    printf("%s at %dx%d, %dx%d, %s\n", fish->name, fish->coordinates.x, fish->coordinates.y, fish->size.width, fish->size.height, "Mobility");
  }
}

void fishs_print() {
    if (!TAILQ_EMPTY(fishs)) {
      struct fish* f;
      for (f = TAILQ_FIRST(fishs); f != TAILQ_LAST(fishs, fish_queue); f = TAILQ_NEXT(f, queue_entries)) {
	fish_print(f);
      }
      fish_print(f);
    }   
}
