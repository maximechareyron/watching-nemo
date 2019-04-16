#ifndef AQUARIUM_H
#define AQUARIUM_H

#include "utils.h"

struct coordinates;
struct size;

struct aquarium {
  struct size size;
};

void aquarium_init(struct aquarium *aquarium, int width, int height);
void aquarium_finalize(struct aquarium *aquarium);
void aquarium_print(struct aquarium *aquarium);

#endif
