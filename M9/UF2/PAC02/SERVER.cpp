#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <iostream>
#include <thread>
#include <vector>
#include <algorithm>
#include <mutex>

#define PORT 12345

std::mutex clients_mutex; // Mutex para proteger la lista de clientes

void handle_client(int clientSocket, std::vector<int>& clients) {
    char buffer[1024];
    int bytesRead;

    while ((bytesRead = recv(clientSocket, buffer, sizeof(buffer), 0)) > 0) {
        buffer[bytesRead] = '\0';
        std::cout << "Mensaje recibido del cliente " << clientSocket << ": " << buffer << std::endl;

        // Retransmitir el mensaje a todos los clientes conectados
        {
            std::lock_guard<std::mutex> lock(clients_mutex);
            for (int client : clients) {
                if (client != clientSocket) { // No enviar al remitente
                    if (send(client, buffer, bytesRead, 0) == -1) {
                        std::cerr << "Error al enviar mensaje al cliente " << client << std::endl;
                    }
                }
            }
        }
    }

    // Cliente desconectado
    std::cout << "Cliente " << clientSocket << " desconectado." << std::endl;
    {
        std::lock_guard<std::mutex> lock(clients_mutex);
        clients.erase(std::remove(clients.begin(), clients.end(), clientSocket), clients.end());
    }
    close(clientSocket);
}

int main() {
    int serverSocket, clientSocket;
    struct sockaddr_in serverAddr, clientAddr;
    socklen_t clientAddrLen = sizeof(clientAddr);
    std::vector<int> clients;

    // Crear el socket
    serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (serverSocket == -1) {
        std::cerr << "Error al crear el socket." << std::endl;
        return 1;
    }

    // Configurar la dirección del servidor
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.s_addr = INADDR_ANY;
    serverAddr.sin_port = htons(PORT);

    // Enlazar el socket con el puerto
    if (bind(serverSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == -1) {
        std::cerr << "Error en bind." << std::endl;
        close(serverSocket);
        return 1;
    }

    // Escuchar conexiones
    if (listen(serverSocket, 3) == -1) {
        std::cerr << "Error en escuchar." << std::endl;
        close(serverSocket);
        return 1;
    }

    std::cout << "Esperando conexiones a la sala de chat..." << std::endl;

    // Aceptar conexiones de clientes
    while ((clientSocket = accept(serverSocket, (struct sockaddr*)&clientAddr, &clientAddrLen)) != -1) {
        std::cout << "Cliente conectado: " << clientSocket << std::endl;
        {
            std::lock_guard<std::mutex> lock(clients_mutex);
            clients.push_back(clientSocket);
        }

        // Crear un hilo para gestionar la comunicación con este cliente
        std::thread(handle_client, clientSocket, std::ref(clients)).detach();
    }

    close(serverSocket);
    return 0;
}