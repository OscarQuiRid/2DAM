package com.example.practica04_login_ddbb;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.practica04_login_ddbb.activity_profile.SALT;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvCreateAccount, tvLostAccount;
    private DataBaseInterna dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar los elementos de la interfaz
        TextInputLayout tilUsername = findViewById(R.id.textInputUserName);
        TextInputLayout tilPassword = findViewById(R.id.textInputPassword);
        etUsername = tilUsername.getEditText();
        etPassword = tilPassword.getEditText();
        btnLogin = findViewById(R.id.btn_login);
        tvCreateAccount = findViewById(R.id.textView_createAccount);
        tvLostAccount = findViewById(R.id.textView_recovery);
        dbHelper = new DataBaseInterna(this);
        HashUtils.updateExistingPasswords(dbHelper, SALT);

        // Solicitar datos de usuarios al iniciar la aplicación
        new SolicitarDatosUsuariosTask().execute();

        // Configurar el botón de login
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            new LoginTask().execute(username, password);
        });
        /*
        // Configurar el enlace para crear una cuenta
        tvCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_create_account.class);
            startActivity(intent);
        });

        // Configurar el enlace para recuperar la cuenta
        tvLostAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_lost_account.class);
            startActivity(intent);
        });*/
    }

    // Tarea asíncrona para manejar el login
    private class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DataBaseInterna.TABLE_USER_LOGIN,
                    new String[]{"password"},
                    "username = ?",
                    new String[]{username},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String storedHashedPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                cursor.close();
                if (HashUtils.verifyPassword(password, SALT, storedHashedPassword)) {
                    return "<estado>ok</estado>";
                } else {
                    return "<estado>ko</estado>";
                }
            }
            return "ERROR_404";
        }

        @Override
        protected void onPostExecute(String result) {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String hashedPassword = HashUtils.hashPassword(password, SALT);
            String errorTime = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());
            String errorCode;

            if (result != null && result.contains("<estado>ok</estado>")) {
                // Guardar el usuario, contraseña y fecha de nacimiento en la base de datos
                ContentValues values = new ContentValues();
                values.put("username", username);
                values.put("password", hashedPassword);
                values.put("fechaNacimiento", "2023-01-01"); // Reemplazar con la fecha de nacimiento real
                dbHelper.getWritableDatabase().insert(DataBaseInterna.TABLE_USER_LOGIN, null, values);

                Intent intent = new Intent(MainActivity.this, activity_profile.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                if (result != null && result.startsWith("ERROR_")) {
                    errorCode = result; // Mostrar el código de error HTTP
                } else if (result != null && result.contains("<estado>ko</estado>")) {
                    errorCode = "KO"; // Mostrar "KO" si las credenciales son incorrectas
                } else {
                    errorCode = "Error404";
                }

                // Guardar el error en la base de datos
                ContentValues values = new ContentValues();
                values.put("username", username);
                values.put("password", password); // Guardar la contraseña sin hash
                values.put("errorTime", errorTime);
                values.put("errorCode", errorCode);
                dbHelper.getWritableDatabase().insert(DataBaseInterna.TABLE_LOGIN_ERROR, null, values);

                Intent intent = new Intent(MainActivity.this, activity_error_login.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("errorTime", errorTime);
                intent.putExtra("errorCode", errorCode);
                startActivity(intent);
            }
        }
    }

    // Tarea asíncrona para solicitar datos de usuarios
    private class SolicitarDatosUsuariosTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Conection conexion = new Conection();
            conexion.solicitarDatosUsuarios(dbHelper);
            return null;
        }
    }
}