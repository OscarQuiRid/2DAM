package com.example.tiendaropaprojecto;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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

        // acceso al carro de la compra
        findViewById(R.id.button_carrito).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CarritoActivity.class);
            startActivity(intent);
        });

        // acceso al home
        findViewById(R.id.button_home).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // acceso a favoritos
        findViewById(R.id.button_heart).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, favoritosActivity.class);
            startActivity(intent);
        });

        // acceso a profile
        findViewById(R.id.button_profile).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PorfileActivity.class);
            startActivity(intent);
        });

    }
}