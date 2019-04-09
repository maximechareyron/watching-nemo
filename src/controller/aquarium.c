#include <stdlib.h>
#include <stdio.h>

#include "controller.h"
#include "aquarium.h"
#include "view.h"

void aquarium_init(struct aquarium **aquarium, int width, int height) {
  *aquarium = malloc(sizeof(struct aquarium));
  (*aquarium)->size = malloc(sizeof(struct size));
  (*aquarium)->size->width = width;
  (*aquarium)->size->height = height;

  
  views_init();
}

void aquarium_finalize(struct aquarium *aquarium) {
  views_finalize();
  free(aquarium->size);
}

void aquarium_print(struct aquarium *aquarium) {
  printf("%dx%d\n", aquarium->size->width, aquarium->size->height);
}

void print_aquarium_saved(int nb_view) {
  printf("\t Aquarium saved!( %d display view)\n", nb_view);
}


