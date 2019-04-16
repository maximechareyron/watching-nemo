#ifndef CONTROLLER_H
#define CONTROLLER_H


void init_controller();
void finalize_controller();
void controller_print();

int load_aquarium(char *aquarium_name);
int save_aquarium(char *aquarium_name);

void parse_log(char *log, int size);
char *parse_int(char *p, int *result, char *end);


#endif
