#include <stdlib.h>
#include <stdio.h>

#include "controller.h"
#include "queue.h" 
#include "view.h"
#include "tools.h"

struct view {
  int id;
  char* name;
  struct coordinates start;
  struct size size;

  TAILQ_ENTRY(view) queue_entries;
};



TAILQ_HEAD(view_queue, view);

struct view_queue* views;
int nb_view = 0;

void views_init() {
  views = malloc(sizeof(struct view_queue));
  TAILQ_INIT(views);
}

void view_add(int id, int x, int y, int width, int height) {
  struct view *view = malloc(sizeof(struct view));
  view->size.width = width;
  view->size.height = height;
  view->start.x = x;
  view->start.y = y;
  view->id = id;

  TAILQ_INSERT_TAIL(views, view, queue_entries);
  nb_view++;
}

void view_remove(struct view *view) {
  TAILQ_REMOVE(views, view, queue_entries);
  nb_view--;
  free(view);
}

void view_print(struct view *view) {
  if (view != NULL) {
    printf("N%d %dx%d+%d+%d\n", view->id, view->start.x, view->start.y, view->size.width, view->size.height);
  }
}

void view_save(FILE *f, struct view *view) {
  if (view != NULL) {
    fprintf(f, "N%d %dx%d+%d+%d\n", view->id, view->start.x, view->start.y, view->size.width, view->size.height);
  }
}

void views_print() {
  if (!TAILQ_EMPTY(views)) {
    for (struct view* v = TAILQ_FIRST(views); v != TAILQ_LAST(views, view_queue); v = TAILQ_NEXT(v, queue_entries)) {
      view_print(v);
    }
    view_print(TAILQ_LAST(views, view_queue));
  }
}

void views_save(FILE *f) {
  if (!TAILQ_EMPTY(views)) {
    for (struct view* v = TAILQ_FIRST(views); v != TAILQ_LAST(views, view_queue); v = TAILQ_NEXT(v, queue_entries)) {
      view_save(f, v);
    }
    view_save(f, TAILQ_LAST(views, view_queue));
  }
}

void views_finalize() {
  while (!TAILQ_EMPTY(views)) {
    struct view * view = TAILQ_FIRST(views);
    view_remove(view);
  }
  free(views);
}

void print_view_added() {
  printf("\t-> view added\n");
}

void print_view_deleted(int id) {
  printf("\t-> view N%d deleted\n", id);
}
