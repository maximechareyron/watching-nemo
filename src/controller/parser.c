#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "parser.h"


#define MAX_PARAM_LENGTH 32


int parse_config_file(const char *filename, struct controller_config *config)
{
  FILE *fp = NULL;
  char *line = NULL;
  size_t len = 0;
  
  if ((fp = fopen(filename, "r")) == NULL) {
    perror("fopen");
    return 0;
  }

  while (getline(&line, &len, fp) != -1) {
    if (line[0] == '#') {
      continue;
    }

    char name[MAX_PARAM_LENGTH];
    char value[MAX_PARAM_LENGTH];
    
    if (sscanf(line, "%s = %s", name, value) == 2) {
      if (strcmp(name, "controller-port") == 0) {
	config->port = atoi(value);
      } else if (strcmp(name, "display-timeout-value") == 0) {
	config->display_timeout_value = atoi(value);
      } else if (strcmp(name, "fish-update-interval") == 0) {
	config->fish_update_interval = atoi(value);
      }
    }
  }

  free(line);
  fclose(fp);

  return 1;
}
