#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>

#include "fish.h"
#include "view.h"
#include "queue.h"
#include "mobility.h"
#include "tools.h"
#include "string.h"

TAILQ_HEAD(fish_queue, fish);

struct fish_queue* fishs;
static pthread_mutex_t mutex_fishs;

static pthread_mutexattr_t recursive;


void fishs_init() {
  printf("Fishs init\n");
  fishs = malloc(sizeof(struct fish_queue));
  TAILQ_INIT(fishs);
  pthread_mutexattr_init(&recursive);
  pthread_mutexattr_settype(&recursive, PTHREAD_MUTEX_RECURSIVE);
  pthread_mutex_init(&mutex_fishs, &recursive);
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
  pthread_mutex_destroy(&mutex_fishs);
  pthread_mutexattr_destroy(&recursive);
}

void fishs_update() {
  pthread_mutex_lock(&mutex_fishs);
    if (!TAILQ_EMPTY(fishs)) {
      struct fish* f;
      for (f = TAILQ_FIRST(fishs); f != TAILQ_LAST(fishs, fish_queue); f = TAILQ_NEXT(f, queue_entries)) {
	fish_update(f);
      }
      fish_update(f);
    }
  pthread_mutex_unlock(&mutex_fishs);
}

void fish_update(struct fish *fish) {
  call_mobility_function(fish);
}

int fish_add(char* name, int x, int y, int w, int h, char *mobility) {
  pthread_mutex_lock(&mutex_fishs);
  if (fish_find(name) != NULL) {
    pthread_mutex_unlock(&mutex_fishs);
    return 0;
  }
  struct fish *fish = malloc(sizeof(struct fish));
  fish->coordinates.x = x;
  fish->coordinates.y = y;
  fish->size.width = w;
  fish->size.height = h;
  strcpy(fish->name, name);

  setup_mobility(fish, mobility);

  fish->state = NOT_STARTED;

  TAILQ_INSERT_TAIL(fishs, fish, queue_entries);
  pthread_mutex_unlock(&mutex_fishs);
  return 1;
}

int fish_remove(char *name) {
  pthread_mutex_lock(&mutex_fishs);
  struct fish *fish;
  if ((fish = fish_find(name)) == NULL) {
    pthread_mutex_unlock(&mutex_fishs);
    return 0;
  }
  TAILQ_REMOVE(fishs, fish, queue_entries);
  free(fish);
  pthread_mutex_unlock(&mutex_fishs);
  return 1;
}

int fish_start(char *name) {
  pthread_mutex_lock(&mutex_fishs);
  struct fish *f = fish_find(name);
  if (f == NULL || f->state == STARTED) {
    pthread_mutex_unlock(&mutex_fishs);
    return 0;
  }
  f->state = STARTED;
  pthread_mutex_unlock(&mutex_fishs);
  return 1;
}

struct fish *fish_find(char *name) {
  pthread_mutex_lock(&mutex_fishs);
  struct fish *f;
  if (!TAILQ_EMPTY(fishs)) {
    for (f = TAILQ_FIRST(fishs); f != TAILQ_LAST(fishs, fish_queue); f = TAILQ_NEXT(f, queue_entries)) {
      if (strcmp(f->name,name) == 0) {
	pthread_mutex_unlock(&mutex_fishs);
	return f;
      }
    }
    if (strcmp(f->name,name) == 0) {
      pthread_mutex_unlock(&mutex_fishs);
      return f;
    }
  }
  pthread_mutex_unlock(&mutex_fishs);
  return NULL;
}

int fish_is_visible(struct view *view, struct fish* fish) {
  return (fish->coordinates.x >= view->start.x && fish->coordinates.y >= view->start.y && fish->coordinates.x < view->start.x + view->size.width && fish->coordinates.x < view->start.y + view->size.height);
}



void fish_print(struct fish *fish) {
  if (fish != NULL) {
    char * s = "Mobility\n";
    printf("%s at %dx%d, %dx%d, %s\n", fish->name, fish->coordinates.x, fish->coordinates.y, fish->size.width, fish->size.height, s);
  }
}

void fishs_print() {
  pthread_mutex_lock(&mutex_fishs);
    if (!TAILQ_EMPTY(fishs)) {
      struct fish* f;
      for (f = TAILQ_FIRST(fishs); f != TAILQ_LAST(fishs, fish_queue); f = TAILQ_NEXT(f, queue_entries)) {
	fish_print(f);
      }
      fish_print(f);
    }
  pthread_mutex_unlock(&mutex_fishs);
}

void fish_send(struct view *view, char *response, struct fish *fish) {
  if (!fish_is_visible(view, fish)) {
    return;
  }  
  strcat(response, "[");
  char s[MAX_BUFFER_SIZE];
  sprintf(s, "%s at %dx%d, %dx%d, %s\n", fish->name, fish->coordinates.x - view->start.x, fish->coordinates.y - view->start.y, fish->size.width, fish->size.height, fish->mobility_name);
  strcat(response, s);
  strcat(response, "] ");
}

void fishs_send(struct view *client_view, char *response) {
    strcat(response, "list ");
    pthread_mutex_lock(&mutex_fishs);
    if (!TAILQ_EMPTY(fishs)) {
      struct fish* f;
      for (f = TAILQ_FIRST(fishs); f != TAILQ_LAST(fishs, fish_queue); f = TAILQ_NEXT(f, queue_entries)) {
	fish_send(client_view, response, f);
      }
      fish_send(client_view, response, f);
    }
  pthread_mutex_unlock(&mutex_fishs);
}

void fishs_lock() {
  pthread_mutex_lock(&mutex_fishs);
}

void fishs_unlock() {
  pthread_mutex_unlock(&mutex_fishs);
}
