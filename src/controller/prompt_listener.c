#include <stdio.h>
#include <string.h>

#include "controller.h"
#include "prompt_listener.h"


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
  if (strncmp(line, "load", 4) == 0) {
    load_aquarium(line + 5);
  } else if (strcmp(line, "show aquarium") == 0) {
    controller_print(NULL);
  }
}


void create_prompt_listener()
{
  char line[MAX_INPUT_SIZE];

  printf("$ ");
  while (fgets(line, MAX_INPUT_SIZE, stdin) != NULL) {
    handle_overflow(line);
    printf("%s", line);
    parse_command(line);
    printf("$ ");
  }
}


//Prompt interaction
void show_aquarium() {}
void add_view() {}
void del_view() {}
//void save_aquarium() {}
