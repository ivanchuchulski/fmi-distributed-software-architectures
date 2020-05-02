
/*
 * main.c
 *
 *  Created on: Apr 17, 2015
 *      Author: lisp
 */

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <netdb.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <string.h>

#include <signal.h>

#define _sys_err(x) do { perror(x); exit(1); } while(0)

#define NAME       257
#define BUFSIZE    257
#define MAXPENDING 1

void _handle_child_sig(int sigNum) {

	printf("received signal: %d\n", sigNum);

}

struct sigaction _sa;

void _handle_child_sig2(int sigNum, siginfo_t *info, void *ptr) {

	printf("[note] received signal: %d\n", sigNum);

	printf("[note] signal comes from process: %lu\n",
		(unsigned long) info->si_pid);

}

void handle_client_conn(int cfd);

int main(int argc, char *argv[]) {

	// port number to bind to...
	unsigned short port;

	// our local hostname is...
	char servhost[NAME];

	// Internet sockets...
	struct sockaddr_in sock;

	// our host info
	struct hostent *server;

	int serverSocket;
	int clientSocket;

	// who am i?
	int pid;

	if (argc != 2) {

		fprintf(stderr, "command line usage: echoServer3 <port>\n");
		exit(1);

	}

	port = atoi(argv[1]);

	//
	// NOTE: historic way of signal handling...
	// signal(SIGCHLD, _handle_child_sig);
	//

	memset(&_sa, 0, sizeof(struct sigaction));

	// _sa.sa_handler = _handle_child_sig;
	_sa.sa_sigaction = _handle_child_sig2;
	_sa.sa_flags = SA_SIGINFO;
	sigaction(SIGCHLD, &_sa, NULL);

	// do we have sockets available;
	if ((serverSocket = socket(AF_INET, SOCK_STREAM, 0)) < 0)
		_sys_err("echoServer3: socket()");

	// our server address is;
	gethostname(servhost, sizeof(servhost));
	if ((server = gethostbyname(servhost)) == NULL) {

		fprintf(stderr, "[halt] %s: unknown host\n", servhost);
		exit(1);

	}


	// prepare for server socket;
	sock.sin_family = AF_INET;
	sock.sin_port = htons(port);
	memcpy(&sock.sin_addr, server -> h_addr, server -> h_length);

	// actually rise server socket;
	if (bind(serverSocket, (struct sockaddr *) &sock, sizeof(sock)) < 0)
		_sys_err("echoServ3: bind()");


	// and start listening for client connections;
	if (listen(serverSocket, MAXPENDING) < 0)
		_sys_err("schoServ3: listen()");

	while (1) {

		// start accepting client connections;
		if ((clientSocket = accept(serverSocket, NULL, NULL)) < 0)
			continue;
		// _sys_err("server: accept");

		// NOTE: called once, but returns twice!
		if ((pid = fork()) < 0)
			_sys_err("echoServer3: fork()");

		if (pid == 0) {

			// hey! I'm child...
			handle_client_conn(clientSocket);
			exit(0);

		} else {

			printf("[note] client container created with pid: %d\n", pid);
			fflush(stdout);
			// parent (master daemon) continues...
			close(clientSocket);

		}

	}

	return (0);
}

void handle_client_conn(int cfd) {

	// some input buffering 'll go here.
	char buf[BUFSIZE];

	// from out client FD socket to stream.
	FILE *fp, *_out_fp;

	// stdio.h helps us to make things stream oriented;
	if (!(fp = fdopen(cfd, "r"))) {

		fprintf(stderr, "[halt] Can't convert socket FD to input stream FD!\n");
		exit(1);

	}

	if (!(_out_fp = fdopen(cfd, "w"))) {

		fprintf(stderr, "[halt] Can't convert socket FD to output stream FD!\n");
		exit(1);

	}

	while (fgets(buf, BUFSIZE, fp)) {

		printf("[info] client said: %s", buf);
		fflush(stdout);

		fprintf(_out_fp, "%s", buf);
		fflush(_out_fp);

	}

	// that was it. let's close (decrement FD reference count) our fds...
	fclose(fp);
	fclose(_out_fp);
	close(cfd);

}
