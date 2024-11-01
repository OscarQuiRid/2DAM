package com.example.practica03supermarket;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductoAdapter productoAdapter;
    private List<Producto> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recycledViewMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaProductos = cargarProductos();
        productoAdapter = new ProductoAdapter(listaProductos);
        recyclerView.setAdapter(productoAdapter);
    }

    private List<Producto> cargarProductos() {
        List<Producto> productos = new ArrayList<>();
        Bitmap bitmapVerduras = BitmapFactory.decodeResource(getResources(), R.drawable.bitmap_verduras);
        productos.add(new Producto("rabano", RecorteUtil.obtenerVerdura(55, 30, 100, 100, bitmapVerduras)));
        productos.add(new Producto("Champiñon", RecorteUtil.obtenerVerdura(440, 30, 100, 100, bitmapVerduras)));
        productos.add(new Producto("Calabaza", RecorteUtil.obtenerVerdura(565, 30, 100, 100, bitmapVerduras)));
        productos.add(new Producto("Ajo", RecorteUtil.obtenerVerdura(690, 150, 100, 100, bitmapVerduras)));
        productos.add(new Producto("Pimiento", RecorteUtil.obtenerVerdura(825, 150, 100, 100, bitmapVerduras)));
        productos.add(new Producto("Cebolla", RecorteUtil.obtenerVerdura(180, 150, 100, 100, bitmapVerduras)));
        productos.add(new Producto("Tomate", RecorteUtil.obtenerVerdura(690, 245, 100, 100, bitmapVerduras)));
        productos.add(new Producto("Zanahoria", RecorteUtil.obtenerVerdura(565, 150, 100, 100, bitmapVerduras)));
        productos.add(new Producto("Lechuga", RecorteUtil.obtenerVerdura(55, 245, 100, 100, bitmapVerduras)));
        productos.add(new Producto("Maiz", RecorteUtil.obtenerVerdura(180, 245, 100, 100, bitmapVerduras)));
        productos.add(new Producto("berenjena", RecorteUtil.obtenerVerdura(435, 245, 100, 100, bitmapVerduras)));
        productos.add(new Producto("Mango", RecorteUtil.obtenerVerdura(50, 345, 100, 100, bitmapVerduras)));
        productos.add(new Producto("Piña", RecorteUtil.obtenerVerdura(690, 345, 100, 100, bitmapVerduras)));
        return productos;
    }
}