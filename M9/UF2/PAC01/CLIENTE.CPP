#include <iostream>
#include <string>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>

#define PUERTO 8080

using namespace std;

int main() {
    int descriptor_socket = 0;
    struct sockaddr_in direccion_servidor;
    string mensaje;
    char buffer[1024] = {0};

    // Crear el sòcol
    if ((descriptor_socket = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        cout << "Error al crear el sòcol\n";
        return -1;
    }

    direccion_servidor.sin_family = AF_INET;
    direccion_servidor.sin_port = htons(PUERTO);

    // Convertir l'adreça IP a binari
    if (inet_pton(AF_INET, "127.0.0.1", &direccion_servidor.sin_addr) <= 0) {
        cout << "Adreça no vàlida o no suportada\n";
        return -1;
    }

    // Connectar-se al servidor
    if (connect(descriptor_socket, (struct sockaddr *)&direccion_servidor, sizeof(direccion_servidor)) < 0) {
        cout << "Error al connectar-se al servidor\n";
        return -1;
    }
    cout << "Connexió establerta amb el servidor.\n";

    // Demanar a l'usuari que introdueixi el missatge
    cout << "Introdueix el missatge per enviar al servidor: ";
    // Usamos getline para leer toda la línea, incluyendo espacios
    getline(cin, mensaje);

    // Enviar missatge al servidor
    send(descriptor_socket, mensaje.c_str(), mensaje.length(), 0);
    cout << "Enviant missatge: " << mensaje << "\n";

    // Rebre resposta del servidor
    read(descriptor_socket, buffer, 1024);
    cout << "Resposta del servidor: " << buffer << "\n";

    // Tancar el sòcol
    close(descriptor_socket);
    return 0;
}