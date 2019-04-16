#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <limits.h>
#include <string.h>

#include "queue.h"
#include "controller.h"
#include "aquarium.h"
#include "view.h"
#include "utils.h"

//Controller 
void init_controller(struct controller *controller) {
  char *log;

  controller->aquarium = malloc(sizeof(struct aquarium));
  load_aquarium(&log, "aquarium", controller->aquarium);
  controller_print(controller);  
  save_aquarium(controller->aquarium, "aquarium2");
}

void finalize_controller(struct controller *controller) {
  aquarium_finalize(controller->aquarium);
}

void load_aquarium(char **log, char *aquarium_name, struct aquarium *aquarium) {
  int size = 0;
  char cwd[PATH_MAX];
  if (getcwd(cwd, sizeof(cwd)) == NULL) {
    perror("getcwd() error");
    exit(EXIT_FAILURE);
  }
  strncat(cwd, "/", sizeof(cwd)-1);
  strncat(cwd, aquarium_name, sizeof(cwd)-1);
  printf("Aquarium path : %s\n", cwd);
  FILE *f = fopen(cwd, "r");
  if (f == NULL) { 
    perror("Error loading log");
    exit(EXIT_FAILURE); 
  }
  fseek(f, 0, SEEK_END);
  size = ftell(f);
  fseek(f, 0, SEEK_SET);
  *log = (char *)malloc(size+1);
  if (size != fread(*log, sizeof(char), size, f)) { 
    perror("Error reading log");
    exit(EXIT_FAILURE);
  }
  parse_log(*log, size, aquarium);
}

//Save the aquarium in a log file
void save_aquarium(struct aquarium *aquarium, char *aquarium_name) {
  char cwd[PATH_MAX];
  if (getcwd(cwd, sizeof(cwd)) == NULL) {
    perror("getcwd() error");
    exit(EXIT_FAILURE);
  }
  strncat(cwd, "/", sizeof(cwd)-1);
  strncat(cwd, aquarium_name, sizeof(cwd)-1);
  printf("Aquarium path : %s\n", cwd);
  FILE *f = fopen(cwd, "w");
  if (f != NULL) {
    fputs("", f);
  }
  fprintf(f, "%dx%d\n", aquarium->size.width, aquarium->size.height);
  views_save(f);
  aquarium_finalize(aquarium);
  fclose(f);
}


//parse a number and give the next char of *p after this number
char* parse_int(char* p, int *result, char *end) {
  *result = 0;
  while (p!= end && *p >= '0' && *p <= '9') {
    *result = *result * 10 + (*p-'0');
    p++;
  }
  return p;
}

//parse
void parse_log(char *log, int size, struct aquarium *a) {
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
  aquarium_init(a, a_width, a_height);

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

    printf("%d, %d, %d, %d, %d\n", v_id, v_x, v_y, v_width, v_height); 
    view_add(v_id, v_x, v_y, v_width, v_height);
    p++;
  }
}


void controller_print(struct controller *controller) {
  aquarium_print(controller->aquarium);
  views_print();
}
