package com.example.tiendaropaprojecto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CarritoActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.carrito_activity); // Inflate the layout XML
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // vuelve al main activity
        findViewById(R.id.button_return).setOnClickListener(v -> {
            Intent intent = new Intent(CarritoActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // vuelve al main activity
        findViewById(R.id.seguir).setOnClickListener(v -> {
            Intent intent = new Intent(CarritoActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // te envia a una vista diciendo que has finalizado la compra y te la añade a tus datos.
        // FALTA POR HACER ESTA VISTA Y LA AÑADE A UNA BASE DE DATOS DE PEDIDOS.
        findViewById(R.id.finalizar).setOnClickListener(v -> {
            Intent intent = new Intent(CarritoActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

}