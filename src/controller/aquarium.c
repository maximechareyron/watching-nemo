#include <stdlib.h>
#include <stdio.h>

#include "controller.h"
#include "aquarium.h"

void aquarium_init(struct aquarium **aquarium, int width, int height) {
  *aquarium = malloc(sizeof(struct aquarium));
  (*aquarium)->size = malloc(sizeof(struct size));
  (*aquarium)->size->width = width;
  (*aquarium)->size->height = height;
}

void aquarium_free(struct aquarium *aquarium) {
  free(aquarium->size);
}
void aquarium_print(struct aquarium *aquarium) {
  printf("Aquarium : Dim %dx%d\n", aquarium->size->width, aquarium->size->height);
}

