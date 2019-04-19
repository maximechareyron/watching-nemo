#ifndef PARSER_H
#define PARSER_H


// Struct to move in another file
struct controller_config
{
  int port;
  int display_timeout_value;
  int fish_update_interval;
};


int parse_config_file(const char *filename, struct controller_config *config);
int parse_aquarium_file(const char *filename);


#endif // PARSER_H
