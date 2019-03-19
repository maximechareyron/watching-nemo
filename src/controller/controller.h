#ifndef CONTROLLER_H
#define CONTROLLER_H

struct coordinates {
  int x;
  int y;
};

struct size {
  int width;
  int height;
};
  
struct view {
  int id;
  char* name;
  struct coordinates start;
  struct size size;
};

struct aquarium() {
  struct coordinates start;
  struct size size;
};

//Controller
void init_controller();
void finalize_controller();
  
void load_aquarium();
void load_log();

#endif
