package com.example.practica03supermarket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.ByteArrayOutputStream;

public class Producto implements Parcelable {
    private String nombre; // Nombre del producto
    private Bitmap imagen; // Imagen del producto
    private double precioUnitario; // Precio por unidad del producto
    private int cantidad; // Cantidad del producto en la cesta

    // Constructor de la clase Producto
    public Producto(String nombre, Bitmap imagen, double precioUnitario) {
        this.nombre = nombre; // Asigna el nombre del producto
        this.imagen = imagen; // Asigna la imagen del producto
        this.precioUnitario = precioUnitario; // Asigna el precio unitario
        this.cantidad = 0; // Inicializa la cantidad a 0
    }

    // Constructor que recibe un objeto Parcel para crear un Producto a partir de datos serializados
    protected Producto(Parcel in) {
        nombre = in.readString(); // Lee el nombre del producto
        // Leer el Bitmap como un byte array
        byte[] byteArray = in.createByteArray(); // Crea un byte array a partir del Parcel
        imagen = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length); // Decodifica el byte array a Bitmap
        precioUnitario = in.readDouble(); // Lee el precio unitario
        cantidad = in.readInt(); // Lee la cantidad
    }

    // Creador de Parcelable para crear instancias de Producto desde un Parcel
    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in); // Crea un nuevo Producto a partir del Parcel
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size]; // Crea un array de Producto del tamaño especificado
        }
    };

    @Override
    public int describeContents() {
        return 0; // No se utilizan características especiales en el Parcel
    }

    // Método para escribir los datos del Producto en un Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre); // Escribe el nombre del producto en el Parcel
        // Convertir el Bitmap a un byte array y escribirlo
        if (imagen != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream(); // Crea un flujo de salida para convertir a byte array
            imagen.compress(Bitmap.CompressFormat.PNG, 100, stream); // Comprime la imagen en formato PNG
            byte[] byteArray = stream.toByteArray(); // Convierte la imagen a byte array
            dest.writeByteArray(byteArray); // Escribe el byte array en el Parcel
        } else {
            dest.writeByteArray(null); // Escribe nulo si el Bitmap es nulo
        }
        dest.writeDouble(precioUnitario); // Escribe el precio unitario en el Parcel
        dest.writeInt(cantidad); // Escribe la cantidad en el Parcel
    }

    // Métodos getter para acceder a los atributos del Producto
    public String getNombre() {
        return nombre; // Retorna el nombre del producto
    }

    public Bitmap getImagen() {
        return imagen; // Retorna la imagen del producto
    }

    public double getPrecioUnitario() {
        return precioUnitario; // Retorna el precio unitario del producto
    }

    public int getCantidad() {
        return cantidad; // Retorna la cantidad del producto
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad; // Establece la cantidad del producto
    }

    public double getPrecioTotal(int cantidad) {
        return precioUnitario * cantidad; // Calcula y retorna el precio total multiplicando precio unitario por cantidad
    }
}

