#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <limits.h>
#include <string.h>

#include "controller.h"
#include "aquarium.h"
#include "view.h"
/*
int main(int argc, char* argv[]) {
  
  return 0;
}*/


//Controller
void init_controller(struct controller *controller) {
  char *log;
  controller->views = malloc(sizeof(struct view) * NUMBER_MAX_VIEW);
    
  int log_size = load_log(&log);
  parse_log(log, log_size, &controller->aquarium, &controller->nb_view, controller->views);

  controller_print(controller);

  //free(controller->log);
}

void finalize_controller(struct controller *controller) {
  for (int v = 0; v < controller->nb_view; v++) {
    view_free(controller->views[v]);
  }
  free(controller->aquarium);
  free(controller->views);
}

int load_log(char **log) {
  int size = 0;
  char cwd[PATH_MAX];
  if (getcwd(cwd, sizeof(cwd)) == NULL) {
    perror("getcwd() error");
    exit(EXIT_FAILURE);
  }
  strncat(cwd, "/aquarium", sizeof(cwd)-1);
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
  return size;
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
void parse_log(char *log, int size, struct aquarium **a, int *nb_view, struct view **views) {
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

  *nb_view = 0;
  
  while (p != end && *nb_view < NUMBER_MAX_VIEW) {
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
    view_init(views + *nb_view, v_id, v_x, v_y, v_width, v_height);
    p++;
    (*nb_view)++;
  }
}


void controller_print(struct controller *controller) {
  aquarium_print(controller->aquarium);
  printf("Nombre de vue : %d\n", controller->nb_view);
  for (int v = 0; v < controller->nb_view; v++) {
    printf("\t");
    view_print(controller->views[v]);
  }
}
