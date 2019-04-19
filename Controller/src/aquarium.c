#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include "aquarium.h"
#include "parser.h"
#include "tools.h"
#include "view.h"


struct aquarium aquarium;


void aquarium_init(int width, int height)
{
  aquarium.size.width = width;
  aquarium.size.height = height;
  
  views_init();
}


int aquarium_load(char *aquarium_name)
{
  char cwd[PATH_MAX];
  
  if (getcwd(cwd, sizeof(cwd)) == NULL) {
    perror("getcwd");
    return 0;
  }
  
  strncat(cwd, "/", sizeof(cwd) - 1);
  strncat(cwd, aquarium_name, sizeof(cwd) - 1);
  //printf("Aquarium path : %s\n", cwd);

  return parse_aquarium_file(cwd);
}


// Save the aquarium in a log file
int aquarium_save(char *aquarium_name)
{
  char cwd[PATH_MAX];

  if (getcwd(cwd, sizeof(cwd)) == NULL) {
    perror("getcwd() error");
    return 0;
  }

  strncat(cwd, "/", sizeof(cwd)-1);
  strncat(cwd, aquarium_name, sizeof(cwd)-1);
  //printf("Aquarium path : %s\n", cwd);

  FILE *f = fopen(cwd, "w");
  if (f != NULL) {
    fputs("", f);
  }

  const struct size size = aquarium_get_size();
  
  fprintf(f, "%dx%d\n", size.width, size.height);
  views_save(f);
  aquarium_finalize();
  fclose(f);
  
  return 1;
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
