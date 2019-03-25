#ifndef FISH_H
#define FISH_H

struct coordinates;

struct fish
{
  int id;
  char* name;
  struct coordinates *coordinates;
  void *(*mobility_function)(void*);
};


#endif // FISH_H
