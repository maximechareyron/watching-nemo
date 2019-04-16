#ifndef AQUARIUM_H
#define AQUARIUM_H


#include "tools.h"


struct aquarium {
  struct size size;
};

void aquarium_init(int width, int height);
void aquarium_finalize();
void aquarium_print();
struct size aquarium_get_size();

#endif
