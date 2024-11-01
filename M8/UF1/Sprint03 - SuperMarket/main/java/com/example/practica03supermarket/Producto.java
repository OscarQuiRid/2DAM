package com.example.practica03supermarket;

import android.graphics.Bitmap;

public class Producto {
    private String nombre; // Nombre de la verdura
    private Bitmap imagen;  // Imagen asociada (Bitmap)

    public Producto(String nombre, Bitmap imagen) {
        this.nombre = nombre;
        this.imagen = imagen; // Inicializa el Bitmap
    }

    public String getNombre() {
        return nombre;
    }

    public Bitmap getImagen() {
        return imagen; // Método para obtener el Bitmap
    }
}
