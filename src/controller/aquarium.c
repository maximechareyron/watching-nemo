#include <stdlib.h>
#include <stdio.h>

#include "aquarium.h"
#include "controller.h"
#include "utils.h"
#include "view.h"


struct aquarium aquarium;


void aquarium_init(int width, int height)
{
  aquarium.size.width = width;
  aquarium.size.height = height;
  
  views_init();
}

void aquarium_finalize()
{
  views_finalize();
}

void aquarium_print()
{
  printf("%dx%d\n", aquarium.size.width, aquarium.size.height);
}

void print_aquarium_saved(int nb_view)
{
  printf("\t Aquarium saved!( %d display view)\n", nb_view);
}


struct size aquarium_get_size()
{
  return aquarium.size;
}
