#ifndef FISH_H
#define FISH_H

#include "utils.h"
#include "mobility.h"
#include "queue.h"

enum fish_state {
		 STARTED,
		 NOT_STARTED
};



struct fish
{
  int id;
  char* name;
  struct coordinates coordinates;
  struct size size;
  void *(*mobility_function)(struct fish*, time_ms);
  void *param;
  enum fish_state state;
  
  TAILQ_ENTRY(fish) queue_entries;
};

struct fish *fish_find(int id);
void fish_update(struct fish *fish);
void fishs_update();



#endif // FISH_H
