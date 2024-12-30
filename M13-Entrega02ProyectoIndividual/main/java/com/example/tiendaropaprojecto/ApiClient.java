package com.example.tiendaropaprojecto;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2/ProyectoTienda/";
    private static ApiClient instance;
    private final ApiService apiService;

    private ApiClient(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public interface ApiService {
        @FormUrlEncoded
        @POST("api.php?accion=registrarCliente")
        Call<ApiResponse> registrarCliente(@Field("correo") String correo, @Field("contraseña") String contraseña);

        @FormUrlEncoded
        @POST("api.php?accion=actualizarCliente")
        Call<ApiResponse> actualizarCliente(@Field("clienteID") int clienteID, @Field("nombre") String nombre, @Field("apellidos") String apellidos, @Field("telefono") String telefono, @Field("direccion") String direccion);

        @GET("api.php?accion=obtenerCliente")
        Call<Cliente> obtenerCliente(@Query("clienteID") int clienteID);

        @GET("api.php?accion=obtenerProductos")
        Call<List<Producto>> obtenerProductos();

        @GET("api.php?accion=obtenerCategorias")
        Call<List<Categoria>> obtenerCategorias();

        @GET("api.php?accion=obtenerPromociones")
        Call<List<Promocion>> obtenerPromociones();

        @FormUrlEncoded
        @POST("api.php?accion=realizarPedido")
        Call<ApiResponse> realizarPedido(@Field("clienteID") int clienteID, @Field("productos") String productos, @Field("metodoPagoID") int metodoPagoID);

        @FormUrlEncoded
        @POST("api.php?accion=actualizarEstadoPedido")
        Call<ApiResponse> actualizarEstadoPedido(@Field("pedidoID") int pedidoID, @Field("estadoPedidoID") int estadoPedidoID);
    }

    public static class ApiResponse {
        private String status;
        private String message;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Cliente {
        private int clienteID;
        private String nombre;
        private String apellidos;
        private String telefono;
        private String direccion;
        private String correoElectronico;

        public int getClienteID() {
            return clienteID;
        }

        public void setClienteID(int clienteID) {
            this.clienteID = clienteID;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellidos() {
            return apellidos;
        }

        public void setApellidos(String apellidos) {
            this.apellidos = apellidos;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getCorreoElectronico() {
            return correoElectronico;
        }

        public void setCorreoElectronico(String correoElectronico) {
            this.correoElectronico = correoElectronico;
        }
    }

    public static class Producto {
    @SerializedName("ProductoID")
    private int id;
    @SerializedName("Nombre")
    private String nombre;
    @SerializedName("Precio")
    private double precio;
    @SerializedName("Descripcion")
    private String descripcion;
    @SerializedName("CategoriaID")
    private double idCategoria;
    @SerializedName("ImagenURL")
    private String imagenURLproducto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(double idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getImagenURLproducto() {
        return imagenURLproducto;
    }

    public void setImagenURLproducto(String imagenURLproducto) {
        this.imagenURLproducto = imagenURLproducto;
    }
}

    public static class Categoria {
        private int id;
        private String nombre;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }

    public class Promocion {
        @SerializedName("PromocionID")
        private int id;
        @SerializedName("Nombre")
        private String nombre;
        @SerializedName("Descripcion")
        private String descripcion;
        @SerializedName("EstaActiva")
        private boolean estaActiva;
        @SerializedName("ImagenURL")
        private String urlImagen;

        // Getters y setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public boolean isEstaActiva() {
            return estaActiva;
        }

        public void setEstaActiva(boolean estaActiva) {
            this.estaActiva = estaActiva;
        }

        public String getUrlImagen() {
            return urlImagen;
        }

        public void setUrlImagen(String urlImagen) {
            this.urlImagen = urlImagen;
        }
    }
}