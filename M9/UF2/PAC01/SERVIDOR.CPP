#include <iostream>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>

#define PUERTO 8080
using namespace std;

int main() {
    int descriptor_servidor, nuevo_socket;
    struct sockaddr_in direccion;
    int opcion = 1;
    int longitud_direccion = sizeof(direccion);
    char buffer[1024] = {0};
    const char *respuesta = "Hola des del servidor!";

    // Crear el sòcol
    if ((descriptor_servidor = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("Error al crear el sòcol");
        exit(EXIT_FAILURE);
    }

    // Configurar el sòcol (només SO_REUSEADDR)
    if (setsockopt(descriptor_servidor, SOL_SOCKET, SO_REUSEADDR, &opcion, sizeof(opcion)) < 0) {
        perror("Error a setsockopt");
        exit(EXIT_FAILURE);
    }

    direccion.sin_family = AF_INET;
    direccion.sin_addr.s_addr = INADDR_ANY;
    direccion.sin_port = htons(PUERTO);

    // Lligar el sòcol al port
    if (bind(descriptor_servidor, (struct sockaddr *)&direccion, sizeof(direccion)) < 0) {
        perror("Error a bind");
        exit(EXIT_FAILURE);
    }

    // Escoltar connexions entrants
    if (listen(descriptor_servidor, 3) < 0) {
        perror("Error a listen");
        exit(EXIT_FAILURE);
    }

    cout << "Esperant connexions...\n";

    // Acceptar una connexió
    if ((nuevo_socket = accept(descriptor_servidor, (struct sockaddr *)&direccion, (socklen_t*)&longitud_direccion)) < 0) {
        perror("Error a accept");
        exit(EXIT_FAILURE);
    }

    // Llegir el missatge del client
    read(nuevo_socket, buffer, 1024);
    cout << "Missatge rebut del client: " << buffer << "\n";

    // Enviar resposta al client
    send(nuevo_socket, respuesta, strlen(respuesta), 0);
    cout << "Resposta enviada al client.\n";

    // Tancar el sòcol
    close(nuevo_socket);
    close(descriptor_servidor);
    return 0;
}