package com.example.tiendaropaprojecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

// En tu adaptador
public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder> {

    private List<ApiClient.Producto> productos;
    private Context context;

    public ProductosAdapter(List<ApiClient.Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_productos, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        ApiClient.Producto producto = productos.get(position);
        holder.textViewNombre.setText(producto.getNombre());
        holder.textViewPrecio.setText(producto.getPrecio() + " €");
        Picasso.get().load(producto.getImagenURLproducto()).into(holder.imgProducto);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void updateProductos(List<ApiClient.Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewPrecio;
        ImageButton imgProducto;
        ImageButton btnFavorito;
        ImageButton btnComprar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.nombre);
            textViewPrecio = itemView.findViewById(R.id.precio);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            btnFavorito = itemView.findViewById(R.id.favorito);
            btnComprar = itemView.findViewById(R.id.comprar);
        }
    }
}