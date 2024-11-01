package com.example.practica03supermarket;

import android.graphics.Bitmap;

public class RecorteBitmap {
    // Método estático para recortar una sección de una imagen dada
    public static Bitmap obtenerVerdura(int x, int y, int ancho, int alto, Bitmap bitmap) {
        // Crea y devuelve un nuevo bitmap recortado de la imagen original
        return Bitmap.createBitmap(bitmap, x, y, ancho, alto);
    }
}
