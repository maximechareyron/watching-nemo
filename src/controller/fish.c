#include <stdlib.h>
#include <stdio.h>

#include "controller.h"
#include "fish.h"
#include "queue.h"

enum fish_state {
		 STARTED,
		 NOT_STARTED
};

struct fish
{
  int id;
  char* name;
  struct coordinates *coordinates;
  struct size *size;
  void *(*mobility_function)(void*);
  void *param;
  enum fish_state state;
  
  TAILQ_ENTRY(fish) queue_entries;
};


TAILQ_HEAD(fish_queue, fish);

struct fish_queue* fishs;

void fishs_init() {
  TAILQ_INIT(fishs);
}

void fish_add(int id, char* name, int x, int y, int w, int h, void *(*mobility_function)(void*)) {
  struct fish *fish = malloc(sizeof(struct fish));
  fish->coordinates = malloc(sizeof(struct coordinates));
  fish->coordinates->x = x;
  fish->coordinates->y = y;
  fish->size->width = w;
  fish->size->height = h;
  fish->id = id;
  fish->name = name;
  fish->mobility_function = mobility_function;
  fish->state = NOT_STARTED;
  
  TAILQ_INSERT_TAIL(fishs, fish, queue_entries);
}

void fish_remove(struct fish *fish) {
  TAILQ_REMOVE(fishs, fish, queue_entries);
  free(fish->coordinates);
  free(fish);
}

void fish_start(int id) {
  struct fish *f = fish_find(id);
  f->state = STARTED;
}

struct fish *fish_find(int id) {
  struct fish *f;
  if (!TAILQ_EMPTY(fishs)) {
    for (f = TAILQ_FIRST(fishs); f != TAILQ_LAST(fishs, fish_queue); f = TAILQ_NEXT(f, queue_entries)) {
      if (f->id == id)
	return f;
    }
    if (f->id == id)
      return f;
  }
  return NULL;
}

				    

void fish_print(struct fish *fish) {
  if (fish != NULL) {
    printf("%dx%d\n", fish->coordinates->x, fish->coordinates->y);
  }
}
