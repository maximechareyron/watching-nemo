#ifndef VIEW_H
#define VIEW_H

#define NUMBER_MAX_VIEW 64

struct view;

void views_init();
void views_print();
void views_save(FILE *f);
void views_finalize();

void view_add(char *name, int x, int y, int width, int height);
void view_print(struct view *view);
void view_save(FILE *f, struct view *view);
void view_remove(struct view *view);
void view_remove_by_name(const char *name);
int view_get_number();

void print_view_added();
void print_view_deleted(int id);
#endif
