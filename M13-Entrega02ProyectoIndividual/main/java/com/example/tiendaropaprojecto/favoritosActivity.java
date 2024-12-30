package com.example.tiendaropaprojecto;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class favoritosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.favoritos_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // vuelve al main activity
        findViewById(R.id.button_return).setOnClickListener(v -> {
            Intent intent = new Intent(favoritosActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // acceso al home
        findViewById(R.id.button_home).setOnClickListener(v -> {
            Intent intent = new Intent(favoritosActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // acceso a favoritos
        findViewById(R.id.button_heart).setOnClickListener(v -> {
            Intent intent = new Intent(favoritosActivity.this, favoritosActivity.class);
            startActivity(intent);
        });

        // acceso a profile
        findViewById(R.id.button_profile).setOnClickListener(v -> {
            Intent intent = new Intent(favoritosActivity.this, PorfileActivity.class);
            startActivity(intent);
        });
    }
}