#include <iostream>
#include <vector>
#include <string>
#include <ctime>
#include <hpdf.h>

using namespace std;

// Clase Producto
class Producto {
public:
    string nombre;
    string categoria;
    double precio;

    Producto(const string& nombre, const string& categoria, double precio)
        : nombre(nombre), categoria(categoria), precio(precio) {}
};

// Función para obtener la fecha actual
string obtenerFechaActual() {
    time_t t = time(nullptr);
    tm* timePtr = localtime(&t);

    char buffer[100];
    strftime(buffer, sizeof(buffer), "%d/%m/%Y", timePtr);
    return string(buffer);
}

// Función para mostrar el informe en consola
void mostrarInforme(const vector<Producto>& productos) {
    cout << "===============================\n";
    cout << "        Informe de productes\n";
    cout << "Data: " << obtenerFechaActual() << "\n";
    cout << "===============================\n";

    cout << "Nom         | Categoria   | Preu\n";
    cout << "------------|-------------|-------\n";

    for (const auto& producto : productos) {
        if (producto.precio > 10.0) { // Aplicar el filtre
            cout << producto.nombre << " | " << producto.categoria << " | " << producto.precio << " €\n";
        }
    }

    cout << "===============================\n";
    cout << "Generat automàticament\n";
    cout << "===============================\n";
}

// Función para generar el informe en PDF
void generarPDF(const vector<Producto>& productos) {
    HPDF_Doc pdf = HPDF_New(nullptr, nullptr);
    if (!pdf) {
        cerr << "Error al crear el PDF" << endl;
        return;
    }

    HPDF_Page page = HPDF_AddPage(pdf);
    HPDF_Page_SetSize(page, HPDF_PAGE_SIZE_A4, HPDF_PAGE_PORTRAIT);

    HPDF_Page_BeginText(page);
    HPDF_Page_SetFontAndSize(page, HPDF_GetFont(pdf, "Helvetica", nullptr), 16);
    HPDF_Page_TextOut(page, 50, 800, "Informe de productes");

    HPDF_Page_SetFontAndSize(page, HPDF_GetFont(pdf, "Helvetica", nullptr), 12);
    HPDF_Page_MoveTextPos(page, 0, -30);
    HPDF_Page_TextOut(page, 50, 770, "Nom");
    HPDF_Page_TextOut(page, 150, 770, "Categoria");
    HPDF_Page_TextOut(page, 250, 770, "Preu");

    int y = 750;
    for (const auto& producto : productos) {
        if (producto.precio > 10.0) {
            HPDF_Page_MoveTextPos(page, 0, -20);
            HPDF_Page_TextOut(page, 50, y, producto.nombre.c_str());
            HPDF_Page_TextOut(page, 150, y, producto.categoria.c_str());
            HPDF_Page_TextOut(page, 250, y, to_string(producto.precio).c_str());
            y -= 20;
        }
    }

    HPDF_Page_EndText(page);

    HPDF_SaveToFile(pdf, "informe_productes.pdf");
    HPDF_Free(pdf);
    cout << "Informe exportat a informe_productes.pdf\n";
}

// Función principal
int main() {
    // Dades simulades
    vector<Producto> productos = {
               Producto("Llibre E", "Ciencia", 10.0),
        Producto("Llibre F", "Tecnología", 20.0),
        Producto("Llibre G", "Historia", 13.5),
        Producto("Llibre H", "Matemáticas", 11.0),
        Producto("Llibre I", "Arte", 14.0),
        Producto("Llibre J", "Ficción", 7.5),
        Producto("Llibre K", "Literatura", 12.0),
        Producto("Llibre L", "Cultura", 16.5),
        Producto("Llibre M", "Filosofía", 18.0),
        Producto("Llibre N", "Educación", 9.5),
        Producto("Llibre O", "Ciencia", 17.0),
        Producto("Llibre P", "Tecnología", 22.5),
        Producto("Llibre Q", "Historia", 14.5),
        Producto("Llibre R", "Matemáticas", 10.5),
        Producto("Llibre S", "Arte", 19.0),
        Producto("Llibre T", "Ficción", 8.0),
        Producto("Llibre U", "Literatura", 13.0),
        Producto("Llibre V", "Cultura", 15.5),
        Producto("Llibre W", "Filosofía", 17.5),
        Producto("Llibre X", "Educación", 11.5),
        Producto("Llibre Y", "Ciencia", 16.0),
        Producto("Llibre Z", "Tecnología", 21.0),
        Producto("Llibre AA", "Historia", 18.5),
        Producto("Llibre AB", "Matemáticas", 12.0),
        Producto("Llibre AC", "Arte", 14.5),
        Producto("Llibre AD", "Ficción", 9.0),
        Producto("Llibre AE", "Literatura", 16.0),
        Producto("Llibre AF", "Cultura", 19.5),
        Producto("Llibre AG", "Filosofía", 20.0),
        Producto("Llibre AH", "Educación", 10.5),
        Producto("Llibre AI", "Ciencia", 18.0),
        Producto("Llibre AJ", "Tecnología", 23.0),
        Producto("Llibre AK", "Historia", 13.0),
        Producto("Llibre AL", "Matemáticas", 11.5),
        Producto("Llibre AM", "Arte", 15.0),
        Producto("Llibre AN", "Ficción", 8.0),
        Producto("Llibre AO", "Literatura", 14.0),
        Producto("Llibre AP", "Cultura", 17.0),
        Producto("Llibre AQ", "Filosofía", 21.5),
        Producto("Llibre AR", "Educación", 12.0),
        Producto("Llibre AS", "Ciencia", 19.0),
        Producto("Llibre AT", "Tecnología", 24.0),
        Producto("Llibre AU", "Historia", 15.5),
        Producto("Llibre AV", "Matemáticas", 13.0)
    };

    int opcion;
    do {
        cout << "\nMenú:\n";
        cout << "1. Veure productes\n";
        cout << "2. Aplicar filtre i veure informe\n";
        cout << "3. Exportar informe a PDF\n";
        cout << "4. Sortir\n";
        cout << "Tria una opció: ";
        cin >> opcion;

        switch (opcion) {
            case 1:
                cout << "Productes disponibles:\n";
                for (const auto& producto : productos) {
                    cout << producto.nombre << " | " << producto.categoria << " | " << producto.precio << " €\n";
                }
                break;
            case 2:
                mostrarInforme(productos);
                break;
            case 3:
                generarPDF(productos);
                break;
            case 4:
                cout << "Sortint...\n";
                break;
            default:
                cout << "Opció no vàlida. Intenta-ho de nou.\n";
        }
    } while (opcion != 4);

    return 0;
}