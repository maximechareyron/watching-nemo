#ifndef VIEW_H
#define VIEW_H

#define NUMBER_MAX_VIEW 64

struct coordinates;
struct size;

struct view {
  int id;
  char* name;
  struct coordinates *start;
  struct size *size;
};


void view_init(struct view **view, int id, int x, int y, int width, int height);
void view_print(struct view *view);
void view_free(struct view *view);

#endif
