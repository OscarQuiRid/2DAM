#include <iostream>
#include <thread>
#include <chrono>
#include <vector>
#include <random>
#include <algorithm>

using namespace std;
// EN LINUX HACE BIEN EL PRINT DE LA CARRERA, LOS PRINT EN WINDOWS COMO EL AMOR DE TU EX.
// MUESTRA ALGUNOS ERRORES PERO COMPILA Y ES TOTALMENTE COMPATIBLE CON WINDOS Y MAC/LINUX.
// WINDOWS
//compilar g++ -std=c++11 MAIN.cpp -o main.exe
//start ./main.exe
// LINUX O MAC
//compilar g++ -std=c++11 MAIN.cpp -o main
//start ./main


const int trackLength = 50; // Longitud de la pista
const int numRacers = 3;     // Número de animales en la carrera

// Estructura para representar a un animal
struct Animal {
    string name;             // Nombre del animal
    int position;            // Posición actual del animal

    // Constructor de la estructura Animal
    Animal(const string& n, int p) : name(n), position(p) {}
};

// Función para limpiar la consola
void clearConsole() {
#ifdef _WIN32
    system("cls"); // Comando para Windows
#else
    system("clear"); // Comando para macOS y Linux
#endif
}

// Función para imprimir el estado de la carrera
void printTrack(const vector<Animal>& animals) {
    clearConsole(); // Limpiar la consola para actualizar la visualización

    // Imprimir la pista para cada animal
    for (const auto& animal : animals) {
        cout << animal.name << ": "; // Mostrar el nombre del animal
        for (int j = 0; j < trackLength; ++j) {
            if (j < animal.position) {
                cout << "="; // Mostrar avance del animal
            } else {
                cout << " "; // Espacio vacío
            }
        }
        cout << " |" << endl; // Marca el final de la pista
    }

    // Imprimir la línea de meta
    cout << string(trackLength, '-') << ">" << endl; // Línea de meta
}

// Función que simula el movimiento de un animal
void runRace(Animal& animal) {
    random_device random; // Dispositivo para generar números aleatorios
    mt19937 generado(random()); // Generador de números aleatorios
    uniform_int_distribution<> dist(1, 5); // Distribución aleatoria para el movimiento

    // Mientras el animal no llegue a la meta
    while (animal.position < trackLength) {
        // Aumentar la posición del animal con un avance aleatorio
        animal.position += dist(generado); // Avance aleatorio
        if (animal.position > trackLength) {
            animal.position = trackLength; // No exceder la longitud de la pista
        }

        // Imprimir el estado actual de la carrera
        printTrack(vector<Animal>{animal}); // Imprimir el estado del animal actual
        this_thread::sleep_for(chrono::milliseconds(300)); // Esperar un poco para simular el tiempo
    }
}

int main() {
    // Definimos los animales participantes de la carrera
    vector<Animal> animals = {
        {"Tortuga", 0}, // Tortuga, comenzando en la posición 0
        {"Liebre", 0},  // Liebre, comenzando en la posición 0
        {"Perro", 0}    // Perro, comenzando en la posición 0
    };

    vector<thread> threads; // Vector para almacenar los hilos

    // Crear y lanzar un hilo para cada animal
    for (int i = 0; i < numRacers; ++i) {
        threads.emplace_back(runRace, ref(animals[i])); // Iniciar el hilo con la función runRace
    }

    // Esperar que todos los hilos terminen
    for (auto& t : threads) {
        t.join(); // Esperar que el hilo termine
    }

    // Encontrar al ganador
    int winnerIndex = 0; // Inicializar el índice del ganador
    for (int i = 1; i < numRacers; ++i) {
        if (animals[i].position > animals[winnerIndex].position) {
            winnerIndex = i; // Actualizar el índice del ganador
        }
    }

    cout << "¡" << animals[winnerIndex].name << " ha ganado la carrera!" << endl; // Mostrar el ganador

    return 0; // Fin del programa
}
