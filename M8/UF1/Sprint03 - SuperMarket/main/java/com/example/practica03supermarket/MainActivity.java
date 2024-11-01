package com.example.practica03supermarket;

import android.content.Intent; // Asegúrate de incluir esto
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton; // Importar el ImageButton
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView; // Vista para mostrar la lista de productos
    private ProductoAdapter productoAdapter; // Adaptador que conecta la lista de productos con el RecyclerView
    private List<Producto> listaProductos; // Lista de todos los productos disponibles
    private List<Producto> listaProductosSeleccionados = new ArrayList<>(); // Lista de productos seleccionados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Llama al método onCreate de la superclase
        EdgeToEdge.enable(this); // Habilita el modo de pantalla completa
        setContentView(R.layout.activity_main); // Establece el diseño de la actividad

        // Configura la vista principal para manejar márgenes en dispositivos con barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); // Obtiene los márgenes de las barras de sistema
            // Aplica los márgenes obtenidos a la vista principal
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets; // Devuelve los insets aplicados
        });

        // Inicializa el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCesta); // Encuentra el RecyclerView en el layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Establece el tipo de layout como lineal

        listaProductos = cargarProductos(); // Llama al método para cargar la lista de productos
        // Crea una instancia del adaptador y le pasa la lista de productos y el método para actualizar la lista seleccionada
        productoAdapter = new ProductoAdapter(listaProductos, this::actualizarProductosSeleccionados);
        recyclerView.setAdapter(productoAdapter); // Establece el adaptador en el RecyclerView

        // Configura el ImageButton para abrir la cesta de la compra
        ImageButton imageButton = findViewById(R.id.imageButton); // Encuentra el botón de imagen en el layout
        imageButton.setOnClickListener(v -> abrirCesta()); // Define la acción al hacer clic en el botón
    }

    // Método para actualizar la lista de productos seleccionados
    private void actualizarProductosSeleccionados(List<Producto> productosSeleccionados) {
        listaProductosSeleccionados = productosSeleccionados; // Actualiza la lista de productos seleccionados
    }

    // Método para abrir la actividad de la cesta de la compra
    private void abrirCesta() {
        Intent intent = new Intent(MainActivity.this, MainActivity_cesta.class); // Crea un Intent para abrir la nueva actividad
        // Pasa la lista de productos seleccionados a la nueva actividad
        intent.putParcelableArrayListExtra("productosSeleccionados", new ArrayList<>(listaProductosSeleccionados));
        startActivity(intent); // Inicia la nueva actividad
    }

    // Método para cargar los productos disponibles
    private List<Producto> cargarProductos() {
        List<Producto> productos = new ArrayList<>(); // Inicializa la lista de productos
        Bitmap bitmapVerduras = BitmapFactory.decodeResource(getResources(), R.drawable.bitmap_verduras); // Carga la imagen de verduras

        // Añadiendo productos con sus respectivos precios y recortes de imagen
        productos.add(new Producto("Rabano", RecorteBitmap.obtenerVerdura(55, 30, 100, 100, bitmapVerduras), 2.5)); // Añade el primer producto
        productos.add(new Producto("Champiñon", RecorteBitmap.obtenerVerdura(440, 30, 100, 100, bitmapVerduras), 3.0)); // Añade el segundo producto
        productos.add(new Producto("Calabaza", RecorteBitmap.obtenerVerdura(565, 30, 100, 100, bitmapVerduras), 1.8)); // Añade el tercer producto
        productos.add(new Producto("Ajo", RecorteBitmap.obtenerVerdura(690, 150, 100, 100, bitmapVerduras), 4)); // Añade el cuarto producto
        productos.add(new Producto("Pimiento", RecorteBitmap.obtenerVerdura(825, 150, 100, 100, bitmapVerduras), 3.5)); // Añade el quinto producto
        productos.add(new Producto("Cebolla", RecorteBitmap.obtenerVerdura(180, 150, 100, 100, bitmapVerduras), 1.2)); // Añade el sexto producto
        productos.add(new Producto("Tomate", RecorteBitmap.obtenerVerdura(690, 245, 100, 100, bitmapVerduras), 2.0)); // Añade el séptimo producto
        productos.add(new Producto("Zanahoria", RecorteBitmap.obtenerVerdura(565, 150, 100, 100, bitmapVerduras), 1.5)); // Añade el octavo producto
        productos.add(new Producto("Lechuga", RecorteBitmap.obtenerVerdura(55, 245, 100, 100, bitmapVerduras), 0.8)); // Añade el noveno producto
        productos.add(new Producto("Maiz", RecorteBitmap.obtenerVerdura(180, 245, 100, 100, bitmapVerduras), 1.9)); // Añade el décimo producto
        productos.add(new Producto("Berenjena", RecorteBitmap.obtenerVerdura(435, 245, 100, 100, bitmapVerduras), 3.3)); // Añade el undécimo producto
        productos.add(new Producto("Mango", RecorteBitmap.obtenerVerdura(50, 345, 100, 100, bitmapVerduras), 2.8)); // Añade el duodécimo producto
        productos.add(new Producto("Piña", RecorteBitmap.obtenerVerdura(690, 345, 100, 100, bitmapVerduras), 5.5)); // Añade el decimotercer producto

        return productos; // Devuelve la lista de productos cargada
    }
}
