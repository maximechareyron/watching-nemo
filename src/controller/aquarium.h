#ifndef AQUARIUM_H
#define AQUARIUM_H

struct coordinates;
struct size;

struct aquarium {
  struct size *size;
};

void aquarium_init(struct aquarium **aquarium, int width, int height);
void aquarium_print(struct aquarium *aquarium);

#endif
