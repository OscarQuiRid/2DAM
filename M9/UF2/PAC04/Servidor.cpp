#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <thread>
#include <mutex>
#include <vector>
#include <netinet/in.h>
#include <unistd.h>
#include <iomanip>
#include <ctime>
#include <tinyxml2.h>

// Mutex para proteger el acceso al archivo de comandas
std::mutex file_mutex;
// Contador de órdenes para generar identificadores únicos
int order_counter = 1;

// Función que envía el menú al cliente
void send_menu(int client_socket) {
    tinyxml2::XMLDocument doc;
    // Carga el archivo de la carta
    if (doc.LoadFile("carta.xml") != tinyxml2::XML_SUCCESS) {
        std::cerr << "Error al cargar carta.xml" << std::endl;
        return;
    }

    // Convierte el documento XML a una cadena de texto
    tinyxml2::XMLPrinter printer;
    doc.Print(&printer);
    std::string menu = printer.CStr();
    // Envía el menú al cliente
    send(client_socket, menu.c_str(), menu.size(), 0);
    std::cout << "Menú enviado al cliente." << std::endl;
}

// Función que maneja la conexión con un cliente
void handle_client(int client_socket) {
    std::cout << "Conexión establecida con el cliente." << std::endl;

    // Envía el menú al cliente
    send_menu(client_socket);

    // Lee la comanda enviada por el cliente
    char buffer[4096] = {0};
    read(client_socket, buffer, 4096);
    std::string order(buffer);
    std::cout << "Comanda recibida del cliente." << std::endl;

    // Extrae el número de mesa de la comanda
    std::istringstream iss(order);
    std::string line;
    std::getline(iss, line);
    int table_number = std::stoi(line.substr(6)); // Asumiendo que el formato es "Mesa: X"

    // Resto de la comanda
    std::string order_content((std::istreambuf_iterator<char>(iss)), std::istreambuf_iterator<char>());

    // Bloquea el acceso al archivo de comandas
    std::lock_guard<std::mutex> guard(file_mutex);
    tinyxml2::XMLDocument doc;
    tinyxml2::XMLElement* root;

    // Abre el archivo de comandas y carga su contenido
    std::ifstream file("comandes.xml");
    if (file.good()) {
        doc.LoadFile("comandes.xml");
        root = doc.RootElement();
    } else {
        // Si el archivo no existe, crea un nuevo documento XML
        root = doc.NewElement("comandes");
        doc.InsertFirstChild(root);
    }
    file.close();

    // Obtiene la fecha actual
    std::time_t t = std::time(nullptr);
    std::tm* now = std::localtime(&t);
    char date[7];
    std::strftime(date, sizeof(date), "%d%m%y", now);

    // Genera un identificador único para la comanda
    std::stringstream ss;
    ss << "ORD-" << date << "-" << std::setw(4) << std::setfill('0') << order_counter++;
    std::string order_id = ss.str();

    // Crea un nuevo elemento XML para la comanda y lo añade al documento
    tinyxml2::XMLElement* orderElement = doc.NewElement("comanda");
    orderElement->SetAttribute("id", order_id.c_str());
    orderElement->SetAttribute("mesa", table_number);
    orderElement->SetText(order_content.c_str());
    root->InsertEndChild(orderElement);

    // Guarda el documento XML actualizado en el archivo
    doc.SaveFile("comandes.xml");
    std::cout << "Pedido generado con ID: " << order_id << " y número de mesa: " << table_number << std::endl;

    // Envía el identificador de la comanda al cliente
    send(client_socket, order_id.c_str(), order_id.size(), 0);
    std::cout << "ID del pedido enviado al cliente." << std::endl;

    // Cierra la conexión con el cliente
    close(client_socket);
    std::cout << "Conexión cerrada con el cliente." << std::endl;
}

// Función que inicia el servidor y escucha conexiones en el puerto especificado
void start_server(int port) {
    int server_fd, new_socket;
    struct sockaddr_in address;
    int opt = 1;
    int addrlen = sizeof(address);

    // Crea un socket para el servidor
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("socket failed");
        exit(EXIT_FAILURE);
    }

    // Configura el socket para reutilizar la dirección
    if (setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt))) {
        perror("setsockopt");
        exit(EXIT_FAILURE);
    }

    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(port);

    // Asocia el socket a la dirección y puerto especificados
    if (bind(server_fd, (struct sockaddr *)&address, sizeof(address)) < 0) {
        perror("bind failed");
        exit(EXIT_FAILURE);
    }

    // Configura el socket para escuchar conexiones entrantes
    if (listen(server_fd, 3) < 0) {
        perror("listen");
        exit(EXIT_FAILURE);
    }

    std::cout << "Servidor iniciado en el puerto " << port << std::endl;

    // Bucle principal que acepta y maneja conexiones de clientes
    while (true) {
        if ((new_socket = accept(server_fd, (struct sockaddr *)&address, (socklen_t*)&addrlen)) < 0) {
            perror("accept");
            exit(EXIT_FAILURE);
        }
        std::cout << "Nueva conexión aceptada." << std::endl;
        // Crea un nuevo hilo para manejar la conexión con el cliente
        std::thread(handle_client, new_socket).detach();
    }
}

int main() {
    // Inicia el servidor en el puerto 9100
    start_server(9100);
    return 0;
}