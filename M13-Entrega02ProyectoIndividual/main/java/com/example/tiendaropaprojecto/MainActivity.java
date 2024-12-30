package com.example.tiendaropaprojecto;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView promocionesImageView;
    private RecyclerView productosRecyclerView;

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

        // Añadir ScrollView programáticamente
        ScrollView scrollView = new ScrollView(this);
        scrollView.setId(R.id.body);
        ConstraintLayout bodyLayout = findViewById(R.id.body);
        bodyLayout.addView(scrollView);

        // Inicializar vistas
        promocionesImageView = findViewById(R.id.promociones);
        productosRecyclerView = findViewById(R.id.productos);

        // Configurar RecyclerView con columnas dinámicas
        int numeroDeColumnas = calcularNumeroDeColumnas();
        GridLayoutManager layoutManager = new GridLayoutManager(this, numeroDeColumnas);
        productosRecyclerView.setLayoutManager(layoutManager);

        // Instancia del cliente API
        ApiClient apiClient = ApiClient.getInstance(this);

        // Llamada a las promociones
        apiClient.getApiService().obtenerPromociones().enqueue(new Callback<List<ApiClient.Promocion>>() {
            @Override
            public void onResponse(Call<List<ApiClient.Promocion>> call, Response<List<ApiClient.Promocion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ApiClient.Promocion> promociones = response.body();
                    if (!promociones.isEmpty()) {
                        String imageUrl = promociones.get(0).getUrlImagen();
                        Picasso.get().load(imageUrl).into(promocionesImageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d("MainActivity", "Imagen cargada exitosamente");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("MainActivity", "Error al cargar la imagen", e);
                            }
                        });
                    }
                } else {
                    Log.e("API_ERROR", "Error en la respuesta de la API");
                }
            }

            @Override
            public void onFailure(Call<List<ApiClient.Promocion>> call, Throwable t) {
                Log.e("API_ERROR", "Error en la llamada a la API", t);
            }
        });

        // Llamada a los productos
        apiClient.getApiService().obtenerProductos().enqueue(new Callback<List<ApiClient.Producto>>() {
            @Override
            public void onResponse(Call<List<ApiClient.Producto>> call, Response<List<ApiClient.Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ApiClient.Producto> productos = response.body();
                    ProductosAdapter adapter = new ProductosAdapter(productos, MainActivity.this);
                    productosRecyclerView.setAdapter(adapter);
                } else {
                    Log.e("API_ERROR", "Error en la respuesta de la API");
                }
            }

            @Override
            public void onFailure(Call<List<ApiClient.Producto>> call, Throwable t) {
                Log.e("API_ERROR", "Error en la llamada a la API", t);
            }
        });

        // Configurar botones de navegación
        findViewById(R.id.button_carrito).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CarritoActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button_home).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button_heart).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, favoritosActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button_profile).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PorfileActivity.class);
            startActivity(intent);
        });
    }

    private int calcularNumeroDeColumnas() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int anchoColumna = 193;
        return (int) (dpWidth / anchoColumna);
    }
}