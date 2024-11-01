package com.example.practica03supermarket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private List<Producto> listaProductos;

    public ProductoAdapter(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view_fruits, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.nombreTextView.setText(producto.getNombre());
        holder.imagenImageView.setImageBitmap(producto.getImagen()); // Cambia a setImageBitmap
        holder.cantidadEditText.setText("0");  // Inicia la cantidad en 0
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        ImageView imagenImageView;
        Button botonMas;
        Button botonMenos;
        TextInputEditText cantidadEditText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.textView);
            imagenImageView = itemView.findViewById(R.id.imageView2);
            botonMas = itemView.findViewById(R.id.button);
            botonMenos = itemView.findViewById(R.id.button2);
            cantidadEditText = itemView.findViewById(R.id.cantidadBox).findViewById(R.id.cantidad);

            // Configurar botones para incrementar y decrementar el valor de cantidad
            botonMas.setOnClickListener(v -> ajustarCantidad(1));
            botonMenos.setOnClickListener(v -> ajustarCantidad(-1));
        }

        private void ajustarCantidad(int incremento) {
            int cantidadActual = Integer.parseInt(cantidadEditText.getText().toString());
            int nuevaCantidad = Math.max(cantidadActual + incremento, 0);  // Evita cantidades negativas
            cantidadEditText.setText(String.valueOf(nuevaCantidad));
        }
    }
}