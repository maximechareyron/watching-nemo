#include <arpa/inet.h>
#include <errno.h>
#include <fcntl.h>
#include <netinet/in.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <time.h>
#include <unistd.h>

#include "client_listener.h"
#include "queue.h"
#include "view.h"
#include "fish.h"

#define MAX_BUFFER_SIZE 256
#define TIME_BEFORE_DISCONNECTION 50 // s


struct client
{
  int socket;
  char name[256];
  time_t time_of_last_action;

  LIST_ENTRY(client) queue_entries;
};

LIST_HEAD(client_queue, client);

struct client_queue *clients;


void add_new_client(int socket)
{
  struct client *client = malloc(sizeof(struct client));
  client->socket = socket;
  client->name[0] = '\0';
  client->time_of_last_action = time(NULL);
  LIST_INSERT_HEAD(clients, client, queue_entries);
}


void remove_client(struct client *client)
{
  close(client->socket);
  LIST_REMOVE(client, queue_entries);
  printf("\nClient %d disconnected\n$ ", client->socket);
  fflush(stdout);
  free(client);
}


__attribute__((destructor))
void destroy_listener()
{
  while (!LIST_EMPTY(clients)) {
    remove_client(LIST_FIRST(clients));
  }
  free(clients);
}


void parse_client_message(char *message, struct client *client)
{
  if (strncmp(message, "hello", 5) == 0) {
    if (strncmp(message, "hello as in ", 12) == 0) {
      if (view_set_available(message + 12)) {
	strcpy(client->name, message + 12);
      }
    } else if (strcmp(message, "hello") == 0) {
      const char *name = view_find_available();
      if (name != NULL) {
	strcpy(client->name, name);	
      }
    }

    if (client->name[0] == '\0') {
      send(client->socket, "no greeting", 11, 0);
    } else {
      char response[MAX_BUFFER_SIZE];
      const int response_length = snprintf(response, MAX_BUFFER_SIZE - 1, "greeting %s", client->name);
      send(client->socket, response, response_length, 0);
    }
  } else if (strcmp(message, "log out") == 0) {
    remove_client(client);
    send(client->socket, "bye", 3, 0);
  } else if (strncmp(message, "ping ", 5) == 0) {
    char response[MAX_BUFFER_SIZE];
    const int response_length = snprintf(response, MAX_BUFFER_SIZE - 1, "pong %s", message + 5);
    send(client->socket, response, response_length, 0);
    client->time_of_last_action = time(NULL);
  } else if (strcmp(message, "getFishes") == 0) {
    struct view *client_view = view_find(client->name);
    fishs_print(); //TODO replace with fishs send
    send(client->socket, "Not yet implemented", 19, 0);
  } else if (strcmp(message, "ls") == 0) {
    send(client->socket, "Not yet implemented", 19, 0);
  } else if (strcmp(message, "getFishesContinuously") == 0) {
    send(client->socket, "Not yet implemented", 19, 0);
  } else if (strncmp(message, "addFish ", 8) == 0) {
    char name[MAX_BUFFER_SIZE], moving_algorithm[MAX_BUFFER_SIZE];
    int x, y, width, height;
    if (sscanf(message + 8, "%s at %dx%d,%dx%d, %s", name, &x, &y, &width, &height, moving_algorithm) == 6) {
      if (fish_add(name, x, y, width, height, moving_algorithm)) {
	send(client->socket, "OK", 2, 0);
      } else {
	send(client->socket, "NOK", 3, 0);
      }	
    }
  } else if (strncmp(message, "delFish ", 8) == 0) {
    char name[MAX_BUFFER_SIZE];
    if (sscanf(message + 8, "%s", name) == 1) {
      if (fish_remove(name)) {
	send(client->socket, "OK", 2, 0);
      } else {
	send(client->socket, "NOK", 3, 0);
      }	
    }
  } else if (strncmp(message, "startFish ", 10) == 0) {
    char name[MAX_BUFFER_SIZE];
    if (sscanf(message + 10, "%s", name) == 1) {
      if (fish_start(name)) {
	send(client->socket, "OK", 2, 0);
      } else {
	send(client->socket, "NOK", 3, 0);
      }	
    }
  }
}


//init
void *create_client_listener(void *p)
{  
  const int port = (intptr_t)p;
  int opt = 1;
  int sock;
  struct sockaddr_in sin;
  sin.sin_addr.s_addr = htonl(INADDR_ANY);   
  sin.sin_family = AF_INET;
  sin.sin_port = htons(port);
  
  if ((sock = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
    perror("socket");
    exit(EXIT_FAILURE);
  }

  fcntl(sock, F_SETFL, O_NONBLOCK);

  if (setsockopt(sock, SOL_SOCKET, SO_REUSEADDR | SO_REUSEPORT, 
		 &opt, sizeof(opt))) { 
    perror("setsockopt"); 
    exit(EXIT_FAILURE); 
  }
  
  if (bind(sock, (struct sockaddr *)&sin, sizeof(sin)) == -1) {
    perror("bind");
    exit(EXIT_FAILURE);
  }

  if (listen(sock, 5) == -1) {
    perror("listen");
    exit(EXIT_FAILURE);
  }

  clients = malloc(sizeof(struct client_queue));
  LIST_INIT(clients);
  
  while (1) {
    struct sockaddr_in client_sin;
    socklen_t len = sizeof(client_sin);
    const int client_sock = accept(sock, (struct sockaddr *)&client_sin, &len);
    if (client_sock == -1) {
      if (errno != EAGAIN) {
	perror("accept");
	continue;
      } 
    } else {
      fcntl(client_sock, F_SETFL, O_NONBLOCK);
      printf("\nConnection established with client %s, id = %d\n$ ", inet_ntoa(client_sin.sin_addr), client_sock);
      fflush(stdout);
      add_new_client(client_sock);
    }

    
    char buffer[MAX_BUFFER_SIZE];
    ssize_t nb_bytes;
    struct client *client = NULL;
    struct client *tmp = NULL;
    
    LIST_FOREACH_SAFE(client, clients, queue_entries, tmp) {
      if ((nb_bytes = recv(client->socket, buffer, MAX_BUFFER_SIZE, 0)) == -1) {
	if (errno != EAGAIN) {
	  perror("recv");
	}

	if (time(NULL) - client->time_of_last_action > TIME_BEFORE_DISCONNECTION) {
	  remove_client(client);
	}
      } else {
	if (nb_bytes == 0) {
	  remove_client(client);
	} else {
	  buffer[nb_bytes - 1] = '\0';
	  //	printf("Received from %d: %s", client->socket, buffer);
	  parse_client_message(buffer, client);
	}
	client->time_of_last_action = time(NULL);
      }
    }
  }

  return NULL;
}
