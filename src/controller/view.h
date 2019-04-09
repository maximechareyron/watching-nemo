#ifndef VIEW_H
#define VIEW_H

#define NUMBER_MAX_VIEW 64

struct view;

void views_init();
void views_print();
void views_finalize();

void view_add(int id, int x, int y, int width, int height);
void view_print(struct view *view);
void view_remove(struct view *view);

void print_view_added();
void print_view_deleted(int id);
#endif
