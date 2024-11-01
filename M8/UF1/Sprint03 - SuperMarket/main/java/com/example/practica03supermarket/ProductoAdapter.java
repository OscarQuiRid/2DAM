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
import java.util.ArrayList;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {
    private List<Producto> listaProductos; // Lista de productos a mostrar
    private List<Producto> productosSeleccionados; // Lista para productos seleccionados
    private OnProductosSeleccionadosListener listener; // Listener para actualizar la selección

    // Constructor que recibe la lista de productos y el listener
    public ProductoAdapter(List<Producto> listaProductos, OnProductosSeleccionadosListener listener) {
        this.listaProductos = listaProductos; // Inicializa la lista de productos
        this.listener = listener; // Inicializa el listener
        this.productosSeleccionados = new ArrayList<>(); // Inicializa la lista de productos seleccionados
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
        Producto producto = listaProductos.get(position); // Obtiene el producto en la posición actual
        holder.nombreTextView.setText(producto.getNombre()); // Asigna el nombre del producto
        holder.imagenImageView.setImageBitmap(producto.getImagen()); // Asigna la imagen del producto
        holder.precioUnitarioTextView.setText(String.format("%.2f", producto.getPrecioUnitario())); // Muestra el precio unitario
        holder.cantidadTextView.setText("0"); // Inicializa la cantidad a 0
        holder.actualizarPrecioTotal(producto); // Actualiza el precio total al iniciar

        // Al pulsar el botón +, aumentar la cantidad
        holder.botonMas.setOnClickListener(v -> holder.ajustarCantidad(1));

        // Al pulsar el botón -, disminuir la cantidad (sin permitir menos de 0)
        holder.botonMenos.setOnClickListener(v -> holder.ajustarCantidad(-1));
    }

    @Override
    public int getItemCount() {
        return listaProductos.size(); // Devuelve el número de productos en la lista
    }

    public List<Producto> getProductosSeleccionados() {
        return productosSeleccionados; // Método para obtener la lista de productos seleccionados
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView; // Vista para el nombre del producto
        ImageView imagenImageView; // Vista para la imagen del producto
        TextView precioUnitarioTextView; // Vista para el precio unitario
        TextView precioTotalTextView; // Vista para el precio total
        Button botonMas; // Botón para aumentar la cantidad
        Button botonMenos; // Botón para disminuir la cantidad
        TextView cantidadTextView; // Vista para mostrar la cantidad

        public ViewHolder(@NonNull View itemView) {
            super(itemView); // Llama al constructor del ViewHolder padre
            // Inicializa las vistas a partir del itemView
            nombreTextView = itemView.findViewById(R.id.textView);
            imagenImageView = itemView.findViewById(R.id.imageView2);
            precioUnitarioTextView = itemView.findViewById(R.id.precio_unitario);
            precioTotalTextView = itemView.findViewById(R.id.precio_total_articulo);
            botonMas = itemView.findViewById(R.id.button);
            botonMenos = itemView.findViewById(R.id.button2);
            cantidadTextView = itemView.findViewById(R.id.cantidadBox).findViewById(R.id.cantidad); // Ahora es un TextView

            // Inicializa la cantidad a 0
            cantidadTextView.setText("0");
        }

        // Método para ajustar la cantidad del producto
        private void ajustarCantidad(int incremento) {
            int cantidadActual = Integer.parseInt(cantidadTextView.getText().toString()); // Obtiene la cantidad actual
            int nuevaCantidad = Math.max(cantidadActual + incremento, 0); // Calcula la nueva cantidad (sin permitir valores negativos)
            cantidadTextView.setText(String.valueOf(nuevaCantidad)); // Actualiza la vista de cantidad

            // Actualiza la cantidad en el objeto Producto
            Producto producto = listaProductos.get(getAdapterPosition()); // Obtiene el producto correspondiente al ViewHolder
            producto.setCantidad(nuevaCantidad); // Actualiza la cantidad en el producto

            // Actualiza el precio total en el ViewHolder
            actualizarPrecioTotal(producto);

            // Actualiza la lista de productos seleccionados
            if (nuevaCantidad > 0) {
                if (!productosSeleccionados.contains(producto)) {
                    productosSeleccionados.add(producto); // Añade el producto si no está ya seleccionado
                }
            } else {
                productosSeleccionados.remove(producto); // Remueve el producto si la cantidad es 0
            }
            listener.onProductosSeleccionadosChanged(productosSeleccionados); // Notifica al listener sobre los cambios
        }

        // Método para actualizar el precio total en la vista
        private void actualizarPrecioTotal(Producto producto) {
            int cantidad = Integer.parseInt(cantidadTextView.getText().toString()); // Obtiene la cantidad actual
            double precioTotal = producto.getPrecioTotal(cantidad); // Calcula el precio total
            precioTotalTextView.setText(String.format("%.2f", precioTotal)); // Muestra el precio total formateado
        }
    }

    // Interfaz para manejar cambios en la lista de productos seleccionados
    public interface OnProductosSeleccionadosListener {
        void onProductosSeleccionadosChanged(List<Producto> productosSeleccionados); // Método que se llama al cambiar los productos seleccionados
    }
}

