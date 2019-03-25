#include <stdlib.h>
#include <stdio.h>

#include "controller.h"
#include "view.h"


void view_init(struct view **view, int id, int x, int y, int width, int height) {
  *view = malloc(sizeof(struct view));
  (*view)->size = malloc(sizeof(struct size));
  (*view)->start = malloc(sizeof(struct coordinates));
  (*view)->size->width = width;
  (*view)->size->height = height;
  (*view)->start->x = x;
  (*view)->start->y = y;
  (*view)->id = id;
}

void view_free(struct view *view) {
  free(view->size);
  free(view->start);
  free(view);
}

void view_print(struct view *view) {
  if (view != NULL) {
    printf("View %d\t: Start : %dx%d\tDim %dx%d\n", view->id, view->start->x, view->start->y, view->size->width, view->size->height);
  }
}
