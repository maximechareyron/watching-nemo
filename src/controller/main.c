#include <pthread.h>
#include <stdint.h>
#include <stdlib.h>

#include "controller.h"
#include "client_listener.h"
#include "parser.h"
#include "prompt_listener.h"


int main()
{
  struct controller_config c;
  //parse_config_file("controller.cfg", &c);

  struct controller controller;
  init_controller(&controller);
  
  //pthread_t t;
  //pthread_create(&t, NULL, create_client_listener, (void*)(intptr_t)c.port);
  
  //create_prompt_listener();

  return EXIT_SUCCESS;
}
