#include <stdlib.h>
#include <stdio.h>

#include "controller.h"
#include "fish.h"
#include "queue.h"

struct fish
{
  int id;
  char* name;
  struct coordinates *coordinates;
  void *(*mobility_function)(void*);

  
  TAILQ_ENTRY(fish) queue_entries;
};


TAILQ_HEAD(fish_queue, fish);

struct fish_queue* fishs;

void fishs_init() {
  TAILQ_INIT(fishs);
}

void fish_add(int id, char* name, int x, int y, void *(*mobility_function)(void*)) {
  struct fish *fish = malloc(sizeof(struct fish));
  fish->coordinates = malloc(sizeof(struct coordinates));
  fish->coordinates->x = x;
  fish->coordinates->y = y;
  fish->id = id;
  fish->name = name;
  
  TAILQ_INSERT_TAIL(fishs, fish, queue_entries);
}

void fish_remove(struct fish *fish) {
  TAILQ_REMOVE(fishs, fish, queue_entries);
  free(fish->coordinates);
  free(fish);
}

void fish_print(struct fish *fish) {
  if (fish != NULL) {
    printf("%dx%d\n", fish->coordinates->x, fish->coordinates->y);
  }
}
