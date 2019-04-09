#ifndef CONTROLLER_H
#define CONTROLLER_H
struct aquarium;

struct coordinates {
  int x;
  int y;
};

struct size {
  int width;
  int height;
};

struct controller {
  struct aquarium *aquarium;
  char *log;
};

//Controller
void init_controller();
void finalize_controller();
void controller_print(struct controller *controller);

int load_log(char **log, char *aquarium_name);
void parse_log(char *log, int size, struct aquarium **a);
char *parse_int(char *p, int *result, char *end);
	       
#endif
