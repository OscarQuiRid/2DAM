package com.example.practica04_login_ddbb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class activity_error_login extends AppCompatActivity {

    private DataBaseInterna dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_login);

        dbHelper = new DataBaseInterna(this);

        // Configurar el botón de volver
        Button botonVolver = findViewById(R.id.boton_cerrarSesion);
        botonVolver.setOnClickListener(v -> {
            finish();
        });

        // Mostrar los datos de error en el TableLayout
        TableLayout tableLayoutMostrarErrores = findViewById(R.id.tableLayout_MostrarErrores);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DataBaseInterna.TABLE_LOGIN_ERROR, null, null, null, null, null, null);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        while (cursor.moveToNext()) {
            TableRow tableRow = new TableRow(this);

            TextView usernameTextView = new TextView(this);
            usernameTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            usernameTextView.setPadding(8, 8, 8, 8);

            TextView passwordTextView = new TextView(this);
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            if (password.length() > 10) {
                password = password.substring(0, 10) + "..."; // Limitar la longitud de la contraseña
            }
            passwordTextView.setText(password);
            passwordTextView.setPadding(8, 8, 8, 8);

            TextView errorTimeTextView = new TextView(this);
            String errorTime = cursor.getString(cursor.getColumnIndexOrThrow("errorTime"));
            try {
                Date date = new Date(Long.parseLong(errorTime));
                errorTimeTextView.setText(dateFormat.format(date));
            } catch (NumberFormatException e) {
                errorTimeTextView.setText(errorTime);
            }
            errorTimeTextView.setPadding(8, 8, 8, 8);

            TextView errorCodeTextView = new TextView(this);
            errorCodeTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("errorCode")));
            errorCodeTextView.setPadding(8, 8, 8, 8);

            tableRow.addView(usernameTextView);
            tableRow.addView(passwordTextView);
            tableRow.addView(errorTimeTextView);
            tableRow.addView(errorCodeTextView);

            tableLayoutMostrarErrores.addView(tableRow);
        }
        cursor.close();
    }
}