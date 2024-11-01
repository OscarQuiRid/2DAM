package com.example.practica03supermarket;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class view_fruits extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Activa el modo "Edge to Edge" para utilizar todo el espacio de la pantalla.
        setContentView(R.layout.activity_view_fruits); // Establece el diseño de la actividad utilizando el archivo XML correspondiente.

        // Configura el comportamiento de los márgenes del sistema para el diseño principal.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); // Obtiene los márgenes del sistema (barra de estado, barra de navegación).
            // Aplica los márgenes obtenidos al diseño principal para evitar que el contenido se superponga a estas barras.
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets; // Devuelve los márgenes aplicados.
        });
    }
}
