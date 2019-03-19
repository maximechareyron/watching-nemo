#ifndef CLIENT_H
#define CLIENT_H

struct coordinates {
  int x;
  int y;
};

struct fish {
  int id;
  char* name;
  struct coordinates coodinates;
};

struct view {
  int id;
  char* name;
  struct coordinates start;
  struct coordinates length;
};

struct client {
  int id;
  struct view *view;
  struct fish* *fishes;
};

void load_aquarium();

void create_prompt_listener();
void create_client_listener();

void show_aquarium();
void add_view();
void del_view();
void save_aquarium();

//Communication
void send();

#endif
