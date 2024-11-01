package com.example.practica03supermarket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductoAdapterCesta extends RecyclerView.Adapter<ProductoAdapterCesta.ViewHolder> {
    private List<Producto> listaProductosSeleccionados; // Lista de productos seleccionados en la cesta
    private OnCantidadCambiadaListener listener; // Listener para manejar cambios en la cantidad

    // Constructor que recibe la lista de productos seleccionados y el listener
    public ProductoAdapterCesta(List<Producto> listaProductosSeleccionados, OnCantidadCambiadaListener listener) {
        this.listaProductosSeleccionados = listaProductosSeleccionados; // Inicializa la lista de productos seleccionados
        this.listener = listener; // Inicializa el listener
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla la vista para cada ítem de la lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view_fruits, parent, false);
        return new ViewHolder(view); // Devuelve el ViewHolder asociado a la vista
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = listaProductosSeleccionados.get(position); // Obtiene el producto en la posición actual
        holder.nombreTextView.setText(producto.getNombre()); // Asigna el nombre del producto
        holder.imagenImageView.setImageBitmap(producto.getImagen()); // Asigna la imagen del producto
        holder.precioUnitarioTextView.setText(String.format("%.2f", producto.getPrecioUnitario())); // Muestra el precio unitario

        // Obtener y mostrar la cantidad actual del producto
        int cantidad = producto.getCantidad();
        holder.cantidadTextView.setText(String.valueOf(cantidad)); // Muestra la cantidad
        holder.precioTotalTextView.setText(String.format("%.2f", producto.getPrecioTotal(cantidad))); // Muestra el precio total

        // Configura el botón "+" para aumentar la cantidad
        holder.botonMas.setOnClickListener(v -> {
            producto.setCantidad(cantidad + 1); // Aumenta la cantidad en 1
            actualizarVista(holder, producto); // Actualiza la vista con la nueva cantidad
            listener.onCantidadCambiada(); // Notifica a la actividad para que actualice el total
        });

        // Configura el botón "-" para disminuir la cantidad (sin permitir que sea menor a 0)
        holder.botonMenos.setOnClickListener(v -> {
            if (cantidad > 0) { // Solo disminuye si la cantidad es mayor que 0
                producto.setCantidad(cantidad - 1); // Disminuye la cantidad en 1
                actualizarVista(holder, producto); // Actualiza la vista con la nueva cantidad
                listener.onCantidadCambiada(); // Notifica a la actividad para que actualice el total
            }
        });
    }

    // Método para actualizar la vista con la nueva cantidad y precio total
    private void actualizarVista(ViewHolder holder, Producto producto) {
        int nuevaCantidad = producto.getCantidad(); // Obtiene la nueva cantidad
        holder.cantidadTextView.setText(String.valueOf(nuevaCantidad)); // Actualiza el TextView de cantidad
        holder.precioTotalTextView.setText(String.format("%.2f", producto.getPrecioTotal(nuevaCantidad))); // Actualiza el precio total
    }

    @Override
    public int getItemCount() {
        return listaProductosSeleccionados.size(); // Devuelve el número de productos en la cesta
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView; // Vista para el nombre del producto
        ImageView imagenImageView; // Vista para la imagen del producto
        TextView precioUnitarioTextView; // Vista para el precio unitario
        TextView precioTotalTextView; // Vista para el precio total
        TextView cantidadTextView; // Vista para mostrar la cantidad
        Button botonMas; // Botón para aumentar la cantidad
        Button botonMenos; // Botón para disminuir la cantidad

        public ViewHolder(@NonNull View itemView) {
            super(itemView); // Llama al constructor del ViewHolder padre
            // Inicializa las vistas a partir del itemView
            nombreTextView = itemView.findViewById(R.id.textView); // Asegúrate de que este ID sea correcto
            imagenImageView = itemView.findViewById(R.id.imageView2); // Asegúrate de que este ID sea correcto
            precioUnitarioTextView = itemView.findViewById(R.id.precio_unitario); // Asegúrate de que este ID sea correcto
            precioTotalTextView = itemView.findViewById(R.id.precio_total_articulo); // Asegúrate de que este ID sea correcto
            cantidadTextView = itemView.findViewById(R.id.cantidadBox).findViewById(R.id.cantidad); // Asegúrate de que este ID sea correcto
            botonMas = itemView.findViewById(R.id.button); // Botón para aumentar la cantidad
            botonMenos = itemView.findViewById(R.id.button2); // Botón para disminuir la cantidad
        }
    }

    // Interfaz para manejar cambios en la cantidad de productos
    public interface OnCantidadCambiadaListener {
        void onCantidadCambiada(); // Método que se llama al cambiar la cantidad
    }
}

