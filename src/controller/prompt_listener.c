#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "controller.h"
#include "prompt_listener.h"
#include "view.h"


#define MAX_INPUT_SIZE 256


void handle_overflow(char *buffer)
{
  char *p = strchr(buffer, '\n');
  if (p == NULL) {
    buffer[MAX_INPUT_SIZE - 1] = '\0';

    char c;
    while ((c = fgetc(stdin)) != '\n' && c != EOF); // Empty stdin buffer
  } else {
    *p = '\0';
  }
}


void parse_command(char *line)
{
  if (strncmp(line, "load ", 5) == 0) {
    if (load_aquarium(line + 5)) {
      printf("    -> aquarium loaded (%d display view)!\n", view_get_number());
    }
  } else if (strcmp(line, "show aquarium") == 0) {
    controller_print();
  } else if (strncmp(line, "save ", 5) == 0) {
    const int nb_views = view_get_number();
    if (save_aquarium(line + 5)) {
      printf("    -> aquarium saved (%d display view)!\n", nb_views);
    }
  } else if (strncmp(line, "add view ", 9) == 0) {
    char name[MAX_INPUT_SIZE];
    int x, y, width, height;

    if (sscanf(line + 9, "%s %dx%d+%d+%d", name, &x, &y, &width, &height) == 5) {
      view_add(name, x, y, width, height);
      puts("    -> view added.");
    } else {
      fputs("Invalid view.\n", stderr);
    }
  } else if (strncmp(line, "del view ", 9) == 0) {
    view_remove_by_name(line + 9);
    printf("    -> view %s deleted.\n", line + 9);
  } else if (strcmp(line, "exit") == 0) {
    // TODO: Free memory
    exit(0);
  } else {
    printf("Unknown command.\n");
  }
}


void create_prompt_listener()
{
  char line[MAX_INPUT_SIZE];

  printf("$ ");
  while (fgets(line, MAX_INPUT_SIZE, stdin) != NULL) {
    handle_overflow(line);
    parse_command(line);
    printf("$ ");
  }
}

