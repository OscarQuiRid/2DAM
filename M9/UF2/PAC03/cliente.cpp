// cliente.cpp
#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>

using namespace std;

int main() {
    // Crear el socket del cliente
    int cliente_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (cliente_fd == -1) {
        cerr << "Error al crear el socket del cliente." << endl;
        return 1;
    }

    // Configurar la dirección del servidor al que se conectará el cliente
    sockaddr_in direccion_servidor;
    direccion_servidor.sin_family = AF_INET;
    direccion_servidor.sin_port = htons(9000);

    // Convertir la dirección IP del servidor (localhost)
    if (inet_pton(AF_INET, "127.0.0.1", &direccion_servidor.sin_addr) <= 0) {
        cerr << "Dirección IP inválida." << endl;
        close(cliente_fd);
        return 1;
    }

    // Conectar al servidor
    if (connect(cliente_fd, (sockaddr*)&direccion_servidor, sizeof(direccion_servidor)) == -1) {
        cerr << "Error al conectar con el servidor." << endl;
        close(cliente_fd);
        return 1;
    }

    cout << "Conexión establecida con el servicio." << endl;

    // Recibir la fecha y hora del servidor
    char buffer[80];
    int bytes_recibidos = recv(cliente_fd, buffer, sizeof(buffer) - 1, 0);
    if (bytes_recibidos == -1) {
        cerr << "Error al recibir datos del servidor." << endl;
    } else {
        buffer[bytes_recibidos] = '\0'; // Asegurar que el mensaje recibido esté terminado en null
        cout << "Hora y fecha recibidas: " << buffer << endl;
    }

    // Cerrar la conexión
    close(cliente_fd);
    return 0;
}
