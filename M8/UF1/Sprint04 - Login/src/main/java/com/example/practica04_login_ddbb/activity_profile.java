package com.example.practica04_login_ddbb;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class activity_profile extends AppCompatActivity {

    private TextView textWelcomeProfile;
    private TextInputEditText etPassword, etFechaNacimiento;
    private ImageButton btnTogglePasswordVisibility;
    private Button btnEdit, btnCerrarSesion;
    private DataBaseInterna dbHelper;
    private boolean isEditing = false;
    private boolean isPasswordVisible = false;
    public static final String SALT = "some_random_salt"; // Use a secure random salt in production

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textWelcomeProfile = findViewById(R.id.text_welcome_profile);
        etPassword = findViewById(R.id.etPassword);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        btnTogglePasswordVisibility = findViewById(R.id.btnTogglePasswordVisibility);
        btnEdit = findViewById(R.id.button);
        btnCerrarSesion = findViewById(R.id.boton_cerrarSesion);
        dbHelper = new DataBaseInterna(this);

        // Recuperar el username del Intent
        String username = getIntent().getStringExtra("username");

        if (username != null) {
            // Recuperar los datos del usuario de la base de datos
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DataBaseInterna.TABLE_USER_LOGIN,
                    new String[]{"username", "password", "fechaNacimiento"},
                    "username = ?",
                    new String[]{username},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String hashedPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String fechaNacimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaNacimiento"));

                // Mostrar los datos en la interfaz
                textWelcomeProfile.setText("Welcome, " + username);
                etPassword.setText(hashedPassword); // Display hashed password for demonstration
                etFechaNacimiento.setText(fechaNacimiento);

                cursor.close();
            }
        } else {
            // Handle the case where username is null
            textWelcomeProfile.setText("Welcome, Guest");
            etPassword.setText("");
            etFechaNacimiento.setText("");
        }

        btnEdit.setOnClickListener(v -> {
            if (isEditing) {
                // Guardar los cambios en la base de datos
                String newPassword = etPassword.getText().toString();
                String hashedPassword = HashUtils.hashPassword(newPassword, SALT);
                String newFechaNacimiento = etFechaNacimiento.getText().toString();

                ContentValues values = new ContentValues();
                values.put("password", hashedPassword);
                values.put("fechaNacimiento", newFechaNacimiento);

                dbHelper.getWritableDatabase().update(DataBaseInterna.TABLE_USER_LOGIN, values, "username = ?", new String[]{username});

                // Bloquear los campos
                etPassword.setEnabled(false);
                etFechaNacimiento.setEnabled(false);
                btnEdit.setText("Edit");
                isEditing = false;
            } else {
                // Desbloquear los campos
                etPassword.setEnabled(true);
                etFechaNacimiento.setEnabled(true);
                btnEdit.setText("Save");
                isEditing = true;
            }
        });

        btnTogglePasswordVisibility.setOnClickListener(v -> {
            if (isPasswordVisible) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnTogglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnTogglePasswordVisibility.setImageResource(R.drawable.ic_visibility);
            }
            isPasswordVisible = !isPasswordVisible;
            etPassword.setSelection(etPassword.getText().length());
        });

        etFechaNacimiento.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(activity_profile.this,
                    (view, year, month, dayOfMonth) -> etFechaNacimiento.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        btnCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(activity_profile.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}