#ifndef FISH_H
#define FISH_H

#include "tools.h"
#include "mobility.h"
#include "queue.h"

enum fish_state {
		 STARTED,
		 NOT_STARTED
};



struct fish
{
  char* name;
  struct coordinates coordinates;
  struct size size;
  void *(*mobility_function)(struct fish*, time_ms);
  void *param;
  enum fish_state state;
  
  TAILQ_ENTRY(fish) queue_entries;
};

int fish_add(char* name, int x, int y, int w, int h, void *(*mobility_function)(struct fish*, time_ms dt));
struct fish *fish_find(char *name);
void fish_update(struct fish *fish);
int fish_start(char *name);
int fish_remove(char *name);


void fishs_init();
void fishs_finalize();
void fishs_print();
void fishs_update();


#endif // FISH_H
