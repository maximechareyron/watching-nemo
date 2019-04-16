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

void load_aquarium(char **log, char *aquarium_name, struct aquarium *aquarium);
void save_aquarium(struct aquarium *aquarium, char *aquarium_name);

void parse_log(char *log, int size, struct aquarium *a);
char *parse_int(char *p, int *result, char *end);
	       
#endif
