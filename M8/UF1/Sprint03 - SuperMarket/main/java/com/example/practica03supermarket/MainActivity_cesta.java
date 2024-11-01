package com.example.practica03supermarket;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import java.util.List;

public class MainActivity_cesta extends AppCompatActivity implements ProductoAdapterCesta.OnCantidadCambiadaListener {
    private RecyclerView recyclerViewCesta; // Vista para mostrar la lista de productos en la cesta
    private ProductoAdapterCesta productoAdapterCesta; // Adaptador que conecta la lista de productos con el RecyclerView
    private List<Producto> listaProductosSeleccionados; // Lista de productos seleccionados pasados de la actividad anterior
    private TextView totalCestaTextView; // TextView para mostrar el total de la cesta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Llama al método onCreate de la superclase
        EdgeToEdge.enable(this); // Habilita el modo de pantalla completa
        setContentView(R.layout.activity_main_cesta); // Establece el diseño de la actividad

        // Configura la vista principal para manejar márgenes en dispositivos con barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); // Obtiene los márgenes de las barras de sistema
            // Aplica los márgenes obtenidos a la vista principal
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets; // Devuelve los insets aplicados
        });

        // Inicializa el RecyclerView para la cesta
        recyclerViewCesta = findViewById(R.id.recyclerViewCesta); // Encuentra el RecyclerView en el layout
        recyclerViewCesta.setLayoutManager(new LinearLayoutManager(this)); // Establece el tipo de layout como lineal
        totalCestaTextView = findViewById(R.id.total_cesta); // Encuentra el TextView para mostrar el total

        // Recuperar la lista de productos seleccionados desde MainActivity
        listaProductosSeleccionados = getIntent().getParcelableArrayListExtra("productosSeleccionados"); // Obtiene la lista de productos

        // Verifica que la lista de productos no sea nula
        if (listaProductosSeleccionados != null) {
            // Crea una instancia del adaptador y le pasa la lista de productos
            productoAdapterCesta = new ProductoAdapterCesta(listaProductosSeleccionados, this);
            recyclerViewCesta.setAdapter(productoAdapterCesta); // Establece el adaptador en el RecyclerView
            actualizarTotalCesta(); // Muestra el total de la cesta
        } else {
            // Si no hay productos seleccionados, muestra el total como 0.00
            totalCestaTextView.setText("0.00");
        }
    }

    @Override
    public void onCantidadCambiada() {
        // Método llamado cuando se cambia la cantidad de un producto
        actualizarTotalCesta(); // Actualiza el total al cambiar la cantidad
    }

    private void actualizarTotalCesta() {
        double totalCesta = 0.0; // Inicializa el total de la cesta
        // Recorre la lista de productos seleccionados
        for (Producto producto : listaProductosSeleccionados) {
            int cantidad = producto.getCantidad(); // Obtiene la cantidad del producto
            totalCesta += producto.getPrecioTotal(cantidad); // Suma el precio total del producto al total
        }
        // Muestra el total formateado a 2 decimales en el TextView
        totalCestaTextView.setText(String.format("%.2f", totalCesta));
    }
}
