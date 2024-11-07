package com.example.practica03supermarket;

import android.graphics.Bitmap;

public class RecorteUtil {
    public static Bitmap obtenerVerdura(int x, int y, int ancho, int alto, Bitmap bitmap) {
        return Bitmap.createBitmap(bitmap, x, y, ancho, alto);
    }
}