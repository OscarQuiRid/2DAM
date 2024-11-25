#include <iostream>
#include <unistd.h>
#include <sys/wait.h>

using namespace std;

int main() {
    int x = 0;

    // Creació de dos processos fills
    for (int i = 0; i < 2; ++i) {
        if (fork() == 0) { // Si estem en el procés fill
            x += i + 1; // Modifiquem la variable local 'x'
            cout << "Procés fill " << i << ": x = " << x << endl;
            return 0; // El procés fill acaba aquí
        }
    }

    // El procés pare espera que els dos fills acabin
    wait(NULL); 
    wait(NULL);

    // El procés pare imprimeix el valor de 'x'
    cout << "Procés pare: x = " << x << endl;

    return 0;
}