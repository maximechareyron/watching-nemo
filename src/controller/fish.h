#ifndef FISH_H
#define FISH_H

#include "tools.h"
#include "mobility.h"
#include "queue.h"

#define MAX_BUFFER_SIZE 256

enum fish_state {
		 STARTED,
		 NOT_STARTED
};

struct fish
{
  char name[MAX_BUFFER_SIZE];
  struct coordinates coordinates;
  struct size size;
  void *(*mobility_function)(struct fish*);
  void *param;
  enum fish_state state;
  
  TAILQ_ENTRY(fish) queue_entries;
};

int fish_add(char* name, int x, int y, int w, int h, char *mobility);
struct fish *fish_find(char *name);
void fish_update(struct fish *fish);
int fish_start(char *name);
int fish_remove(char *name);


void fishs_init();
void fishs_finalize();
void fishs_print();
void fishs_update();
void fishs_lock();
void fishs_unlock();

//update fishs pthread
void *updater(void *param);

#endif // FISH_H
