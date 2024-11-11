#include <iostream>
#include <thread>
#include <windows.h>

using namespace std;

void funcion1(){
    for (int i = 0; i < 5; i++)
    {
        string m = "hilo 1 vuelta " + to_string(i) + "\n"
        cout << m;
        Sleep(500);
    }
    
}

void funcion2(){
    for (int i = 0; i < 5; i++)
    {
        string m = "hilo 2 vuelta " + to_string(i) + "\n"
        cout << m;
        Sleep(500);
    }
    
}

void funcion3(){
    for (int i = 0; i < 5; i++)
    {
        string m = "hilo 3 vuelta " + to_string(i) + "\n"
        cout << m;
        Sleep(500);
    }
    
}

int mai(){
    // funcion 
    thread hilo1(funcion1);
    thread hilo2(funcion2);
    thread hilo3(funcion3);

    // join espera que terminen los hilos
    hilo1.join();
    hilo2.join();
    hilo3.join();

    cout >> "programa multithreading";

}
/*
#include <iostream>
#include <thread>
#include <chrono>
#include <vector>
#include <random>

using namespace std;

const int trackLength = 50; // Longitud de la pista
const int numRacers = 3;     // Número de corredores
const int maxVertical = 20;  // Altura de la consola

void printTrack(const vector<int>& positions) {
    system("cls"); // Limpiar la consola (usado en Windows)

    // Imprimir la pista
    for (int i = 0; i < numRacers; ++i) {
        cout << "Corredor " << i + 1 << ": ";
        for (int j = 0; j < trackLength; ++j) {
            if (j < positions[i]) {
                cout << "="; // Avance del corredor
            } else {
                cout << " "; // Espacio vacío
            }
        }
        cout << " |" << endl; // Marca el final de la pista
    }

    // Imprimir la línea de meta
    cout << string(trackLength, '-') << ">" << endl; // Línea de meta
}

int main() {
    vector<int> positions(numRacers, 0); // Posiciones de los corredores
    random_device rd; // Para generar números aleatorios
    mt19937 gen(rd());
    uniform_int_distribution<> dist(1, 3); // Distribución para la velocidad

    // Carrera
    while (*max_element(positions.begin(), positions.end()) < trackLength) {
        // Actualizar las posiciones de los corredores
        for (int i = 0; i < numRacers; ++i) {
            positions[i] += dist(gen); // Aumentar la posición aleatoriamente
            if (positions[i] > trackLength) {
                positions[i] = trackLength; // No exceder la longitud de la pista
            }
        }

        // Imprimir el estado actual de la carrera
        printTrack(positions);
        
        // Esperar un poco para simular el tiempo de la carrera
        this_thread::sleep_for(chrono::milliseconds(300));
    }

    // Anunciar el ganador
    for (int i = 0; i < numRacers; ++i) {
        if (positions[i] >= trackLength) {
            cout << "¡Corredor " << i + 1 << " ha ganado!" << endl;
        }
    }

    return 0;
}

*/