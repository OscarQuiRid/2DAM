#include <iostream>
#include <ctime>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>

using namespace std;

int main() {
    // Crear el socket del servidor
    int servidor_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (servidor_fd == -1) {
        cerr << "Error al crear el socket del servidor." << endl;
        return 1;
    }

    // Configurar la dirección y el puerto del servidor
    sockaddr_in direccion_servidor;
    direccion_servidor.sin_family = AF_INET;
    direccion_servidor.sin_addr.s_addr = INADDR_ANY;
    direccion_servidor.sin_port = htons(9000);

    // Enlazar el socket al puerto
    if (::bind(servidor_fd, (struct sockaddr*)&direccion_servidor, sizeof(direccion_servidor)) == -1) {
        cerr << "Error al enlazar el socket al puerto." << endl;
        close(servidor_fd);
        return 1;
    }

    // Poner el socket en modo de escucha
    if (listen(servidor_fd, 5) == -1) {
        cerr << "Error al poner el socket en modo escucha." << endl;
        close(servidor_fd);
        return 1;
    }

    cout << "Servicio iniciado. Escuchando en el puerto 9000..." << endl;

    while (true) {
        // Aceptar una conexión del cliente
        sockaddr_in direccion_cliente;
        socklen_t tamano_direccion = sizeof(direccion_cliente);
        int cliente_fd = accept(servidor_fd, (sockaddr*)&direccion_cliente, &tamano_direccion);

        if (cliente_fd == -1) {
            cerr << "Error al aceptar la conexión del cliente." << endl;
            continue;
        }

        // Obtener la dirección IP del cliente
        char ip_cliente[INET_ADDRSTRLEN];
        inet_ntop(AF_INET, &direccion_cliente.sin_addr, ip_cliente, INET_ADDRSTRLEN);
        cout << "Conexión aceptada desde el cliente " << ip_cliente << endl;

        // Obtener la fecha y hora actuales
        time_t ahora = time(0);
        tm* tiempo_local = localtime(&ahora);
        char buffer[80];
        strftime(buffer, sizeof(buffer), "%d/%m/%Y %H:%M:%S", tiempo_local);

        // Enviar la fecha y hora al cliente
        send(cliente_fd, buffer, strlen(buffer), 0);
        cout << "Hora y fecha enviadas: " << buffer << endl;

        // Cerrar la conexión con el cliente
        close(cliente_fd);
        cout << "Esperando una nueva conexión..." << endl;
    }

    // Cerrar el socket del servidor
    close(servidor_fd);
    return 0;
}