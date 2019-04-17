#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <limits.h>
#include <string.h>

#include "queue.h"
#include "controller.h"
#include "aquarium.h"
#include "view.h"
#include "tools.h"


void init_controller()
{
  //  load_aquarium("aquarium");
  //controller_print();  
  //save_aquarium("aquarium2");
}


void finalize_controller()
{
  aquarium_finalize();
}


int load_aquarium(char *aquarium_name)
{
  int size = 0;
  char cwd[PATH_MAX];
  char *log;
  
  if (getcwd(cwd, sizeof(cwd)) == NULL) {
    perror("getcwd() error");
    return 0;
  }
  
  strncat(cwd, "/", sizeof(cwd)-1);
  strncat(cwd, aquarium_name, sizeof(cwd)-1);
  //printf("Aquarium path : %s\n", cwd);
  FILE *f = fopen(cwd, "r");
  if (f == NULL) { 
    fputs("Error loading log\n", stderr);
    return 0; 
  }
  
  fseek(f, 0, SEEK_END);
  size = ftell(f);
  fseek(f, 0, SEEK_SET);
  log = malloc(size + 1);

  if (size != fread(log, sizeof(char), size, f)) { 
    fputs("Error reading log\n", stderr);
    return 0;
  }
  parse_log(log, size);

  return 1;
}


// Save the aquarium in a log file
int save_aquarium(char *aquarium_name)
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


// Parse a number and give the next char of *p after this number
char* parse_int(char* p, int *result, char *end)
{
  *result = 0;
  while (p!= end && *p >= '0' && *p <= '9') {
    *result = *result * 10 + (*p-'0');
    p++;
  }
  return p;
}


void parse_log(char *log, int size)
{
  char *p = log;
  char *end = log+size;
  int a_width = 0;
  p = parse_int(p, &a_width, end);
  if (*p != 'x') {
    perror("Parsing : format error 1");
    exit(EXIT_FAILURE);
  }
  p++;
  int a_height = 0;
  p = parse_int(p, &a_height, end);
  if (*p != '\n') {
    perror("Parsing : format error 2");
    exit(EXIT_FAILURE);
  }
  p++;
  aquarium_init(a_width, a_height);

  int count_view = 0;
  while (p != end && count_view < NUMBER_MAX_VIEW) {
    p++;
    int v_id = 0;
    p = parse_int(p, &v_id, end);
    if (*p != ' ') {
      perror("Parsing : format error 3");
      exit(EXIT_FAILURE);
    }
    p++;

    int v_x = 0;
    p = parse_int(p, &v_x, end);
    if (*p != 'x') {
      perror("Parsing : format error 4");
      exit(EXIT_FAILURE);
    }
    p++;


    int v_y = 0;
    p = parse_int(p, &v_y, end);
    if (*p != '+') {
      perror("Parsing : format error 5");
      exit(EXIT_FAILURE);
    }
    p++;
    
    int v_width = 0;
    p = parse_int(p, &v_width, end);
    if (*p != '+') {
      perror("Parsing : format error 6");
      exit(EXIT_FAILURE);
    }
    p++;
    
    int v_height = 0;
    p = parse_int(p, &v_height, end);
    if (*p != '\n') {
      perror("Parsing : format error 7");
      exit(EXIT_FAILURE);
    }

    //printf("%d, %d, %d, %d, %d\n", v_id, v_x, v_y, v_width, v_height); 

    char name[256];
    snprintf(name, 255, "N%d", v_id);
    view_add(name, v_x, v_y, v_width, v_height);
    p++;
  }
}


void controller_print()
{
  aquarium_print();
  views_print();
}
