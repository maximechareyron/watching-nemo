#ifndef CONTROLLER_H
#define CONTROLLER_H
struct aquarium;

struct controller {
  struct aquarium *aquarium;
  char *log;
};

//Controller
void init_controller();
void finalize_controller();
void controller_print(struct controller *controller);

void load_aquarium(char *aquarium_name);
void save_aquarium(char *aquarium_name);

void parse_log(char *log, int size);
char *parse_int(char *p, int *result, char *end);
	       
#endif
