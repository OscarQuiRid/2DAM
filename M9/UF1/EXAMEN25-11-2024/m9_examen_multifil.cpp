#include <iostream>
#include <thread>

using namespace std;

void inpar() {
    for (int i = 1; i <= 9; i += 2) {
        cout << "Imparell: " << i << endl;
    }
}

void par() {
    for (int i = 2; i <= 10; i += 2) {
        cout << "Parell: " << i << endl;
    }
}

int main() {
    // Crear dos hilos, uno para imprimir impares y otro para imprimir pares
    thread t1(inpar);
    thread t2(par);

    // Esperar a que ambos hilos terminen antes de continuar
    t1.join();
    t2.join();

    cout << "Tots els fils han acabat." << endl;
    return 0;
}