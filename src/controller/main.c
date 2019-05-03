#include <pthread.h>
#include <signal.h>
#include <stdint.h>
#include <stdlib.h>

#include "client_listener.h"
#include "parser.h"
#include "prompt_listener.h"


pthread_t t;

// Avoid memory leak due to the client thread
// Maybe find a more beautiful way to do this
__attribute__((destructor))
void kill_thread()
{
  pthread_kill(t, SIGKILL);
  pthread_join(t, NULL);
}


int main()
{
  struct controller_config c;
  parse_config_file("controller.cfg", &c);

  // TODO: Handle memory leaks related to the thread
  pthread_create(&t, NULL, create_client_listener, (void*)(intptr_t)c.port);
  
  create_prompt_listener();
  
  return EXIT_SUCCESS;
}
