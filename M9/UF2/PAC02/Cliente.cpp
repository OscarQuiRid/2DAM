#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <iostream>
#include <thread>
#include <cstring>

#define SERVER_IP "127.0.0.1"  // IP del servidor
#define PORT 12345

void receive_messages(int clientSocket) {
    char buffer[1024];
    int bytesRead;

    while ((bytesRead = recv(clientSocket, buffer, sizeof(buffer), 0)) > 0) {
        buffer[bytesRead] = '\0';
        std::cout << buffer << std::endl;
    }

    std::cout << "Conexión cerrada por el servidor." << std::endl;
    close(clientSocket);
    exit(0);
}

int main() {
    int clientSocket;
    struct sockaddr_in serverAddr;
    std::string username;

    // Crear el socket del cliente
    clientSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (clientSocket == -1) {
        std::cerr << "Error al crear el socket del cliente." << std::endl;
        return 1;
    }

    // Configurar la dirección del servidor
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.s_addr = inet_addr(SERVER_IP);
    serverAddr.sin_port = htons(PORT);

    // Conectar al servidor
    if (connect(clientSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == -1) {
        std::cerr << "Error al conectar al servidor." << std::endl;
        close(clientSocket);
        return 1;
    }

    std::cout << "Conectado al servidor." << std::endl;

    // Solicitar el nombre del usuario
    std::cout << "Por favor, introduce tu nombre: ";
    std::getline(std::cin, username);

    // Enviar el nombre al servidor
    send(clientSocket, username.c_str(), username.length(), 0);

    // Crear un hilo para recibir los mensajes
    std::thread(receive_messages, clientSocket).detach();

    // Enviar mensajes
    std::string message;
    while (true) {
        std::cout << "Escribe un mensaje: ";
        std::getline(std::cin, message);

        // Adjuntar el mensaje al formato "usuario: mensaje"
        std::string fullMessage = username + ": " + message;
        if (send(clientSocket, fullMessage.c_str(), fullMessage.length(), 0) == -1) {
            std::cerr << "Error al enviar el mensaje." << std::endl;
            break;
        }
    }

    close(clientSocket);
    return 0;
}