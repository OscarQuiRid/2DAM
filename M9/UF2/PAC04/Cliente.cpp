#include <iostream>
#include <string>
#include <vector>
#include <netinet/in.h>
#include <unistd.h>
#include <tinyxml2.h>
#include <arpa/inet.h>
#include <sstream>

// Función para recibir el menú del servidor
void receive_menu(int client_socket, tinyxml2::XMLDocument &doc) {
    char buffer[4096] = {0};
    // Lee el menú enviado por el servidor
    read(client_socket, buffer, 4096);
    // Parsea el menú recibido en el documento XML
    doc.Parse(buffer);
    std::cout << "Menú recibido del servidor." << std::endl;
}

// Función para mostrar el menú en la consola
void display_menu(tinyxml2::XMLDocument &doc) {
    tinyxml2::XMLElement* menu = doc.RootElement();
    int index = 1;
    // Itera sobre las categorías y platos en el menú
    for (tinyxml2::XMLElement* category = menu->FirstChildElement(); category != nullptr; category = category->NextSiblingElement()) {
        std::cout << category->Name() << ":" << std::endl;
        for (tinyxml2::XMLElement* dish = category->FirstChildElement(); dish != nullptr; dish = dish->NextSiblingElement()) {
            std::cout << index++ << ". " << dish->GetText() << " [0]" << std::endl;
        }
    }
}

// Función para contar el número total de platos en el menú
int count_dishes(tinyxml2::XMLDocument &doc) {
    tinyxml2::XMLElement* menu = doc.RootElement();
    int count = 0;
    // Itera sobre las categorías y platos en el menú
    for (tinyxml2::XMLElement* category = menu->FirstChildElement(); category != nullptr; category = category->NextSiblingElement()) {
        for (tinyxml2::XMLElement* dish = category->FirstChildElement(); dish != nullptr; dish = dish->NextSiblingElement()) {
            count++;
        }
    }
    return count;
}

// Función para actualizar el menú con las cantidades seleccionadas por el cliente
void update_menu(tinyxml2::XMLDocument &doc, std::vector<int> &quantities) {
    tinyxml2::XMLElement* menu = doc.RootElement();
    int index = 0;
    // Itera sobre las categorías y platos en el menú
    for (tinyxml2::XMLElement* category = menu->FirstChildElement(); category != nullptr; category = category->NextSiblingElement()) {
        for (tinyxml2::XMLElement* dish = category->FirstChildElement(); dish != nullptr; ) {
            if (quantities[index] > 0) {
                // Actualiza el texto del plato con la cantidad seleccionada
                std::stringstream ss;
                ss << dish->GetText() << " [" << quantities[index] << "]";
                dish->SetText(ss.str().c_str());
                dish = dish->NextSiblingElement();
            } else {
                // Elimina el plato si la cantidad es 0
                tinyxml2::XMLElement* toDelete = dish;
                dish = dish->NextSiblingElement();
                category->DeleteChild(toDelete);
            }
            index++;
        }
    }
}

// Función para enviar la comanda al servidor
void send_order(int client_socket, tinyxml2::XMLDocument &doc, int table_number) {
    tinyxml2::XMLPrinter printer;
    // Convierte el documento XML a una cadena de texto
    doc.Print(&printer);
    std::string order = printer.CStr();
    // Añade el número de mesa al principio de la comanda
    std::stringstream ss;
    ss << "Mesa: " << table_number << "\n" << order;
    std::string order_with_table = ss.str();
    // Envía la comanda al servidor
    send(client_socket, order_with_table.c_str(), order_with_table.size(), 0);
    std::cout << "Comanda enviada al servidor." << std::endl;
}

int main() {
    int client_socket;
    struct sockaddr_in serv_addr;

    // Crea un socket para el cliente
    if ((client_socket = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        std::cout << "Error al crear el socket." << std::endl;
        return -1;
    }
    std::cout << "Socket creado." << std::endl;

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(9100);

    // Convierte la dirección IP del servidor a formato binario
    if (inet_pton(AF_INET, "127.0.0.1", &serv_addr.sin_addr) <= 0) {
        std::cout << "Dirección inválida / Dirección no soportada." << std::endl;
        return -1;
    }
    std::cout << "Dirección IP convertida." << std::endl;

    // Conecta el socket del cliente al servidor
    if (connect(client_socket, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0) {
        std::cout << "Conexión fallida." << std::endl;
        return -1;
    }
    std::cout << "Conectado al servidor." << std::endl;

    while (true) {
        int table_number;
        std::cout << "Introduce el número de mesa (0 para volver a introducir, 'exit' para salir): ";
        std::string input;
        std::cin >> input;
        if (input == "exit") {
            std::cout << "Saliendo del cliente." << std::endl;
            break;
        }
        table_number = std::stoi(input);
        if (table_number == 0) {
            continue;
        }

        tinyxml2::XMLDocument doc;
        // Recibe el menú del servidor
        receive_menu(client_socket, doc);
        // Muestra el menú en la consola
        display_menu(doc);

        // Cuenta el número total de platos en el menú
        int total_dishes = count_dishes(doc);
        std::vector<int> quantities(total_dishes, 0);

        int choice, quantity;
        // Bucle para que el cliente seleccione los platos y cantidades
        while (true) {
            std::cout << "Introduce el número del plato (0 para finalizar el pedido): ";
            std::cin >> choice;
            if (choice == 0) break;
            if (choice < 1 || choice > total_dishes) {
                std::cout << "Número de plato inválido. Por favor, introduce un número entre 1 y " << total_dishes << "." << std::endl;
                continue;
            }
            std::cout << "Introduce la cantidad: ";
            std::cin >> quantity;
            if (quantity < 0) {
                std::cout << "La cantidad no puede ser negativa." << std::endl;
                continue;
            }
            if (quantity > 1000) {
                std::cout << "La cantidad no puede ser mayor a 1000." << std::endl;
                continue;
            }
            quantities[choice - 1] = quantity;
        }

        // Actualiza el menú con las cantidades seleccionadas
        update_menu(doc, quantities);
        // Envía la comanda al servidor
        send_order(client_socket, doc, table_number);

        char buffer[1024] = {0};
        // Lee la respuesta del servidor
        read(client_socket, buffer, 1024);
        std::cout << "Respuesta del servidor: " << buffer << std::endl;
    }

    // Cierra el socket del cliente
    close(client_socket);
    std::cout << "Socket cerrado." << std::endl;
    return 0;
}