#include <arpa/inet.h>
#include <errno.h>
#include <fcntl.h>
#include <netinet/in.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <unistd.h>

#include "client_listener.h"
#include "queue.h"

#define MAX_BUFFER_SIZE 256


struct client
{
  int socket;
  char name[256];

  LIST_ENTRY(client) queue_entries;
};

LIST_HEAD(client_queue, client);

struct client_queue *clients;

int sock;
int sockets[10];
int socket_nb = 0;



__attribute__ ((destructor))
void clear_connections()
{
  for (int i = 0 ; i < socket_nb ; i++) {
    close(sockets[i]);
  }
  close(sock);
}


//init
void *create_client_listener(void *p)
{  
  const int port = (intptr_t)p;
  int opt = 1;
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
      sockets[socket_nb++] = client_sock;
      printf("Connection established with client %s\n", inet_ntoa(client_sin.sin_addr));

      struct client *client = malloc(sizeof(struct client));
      client->socket = client_sock;
      client->name[0] = '\0';
    }

    
    char buffer[MAX_BUFFER_SIZE];
    ssize_t nb_bytes;
    for (int i = 0 ; i < socket_nb ; i++) {
      if ((nb_bytes = recv(sockets[i], buffer, 256, 0)) == -1) {
	if (errno != EAGAIN) {
	  perror("recv");
	}
      } else {
	buffer[nb_bytes] = '\0';
	printf("Received from %d: %s", sockets[i], buffer);
      }
    }
  }

  return NULL;
}

//Commands
void hello() {}
void get_fishes() {}
void get_fishes_continuously() {}
void ls() {}
void ping() {}
void add_fish() {}
void del_fish() {}
void start_fish() {}

//Communication
//void send() {}
