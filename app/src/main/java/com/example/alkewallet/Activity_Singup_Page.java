package com.example.alkewallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.alkewallet.data.AppDatabase;
import com.example.alkewallet.model.Usuario;

public class Activity_Singup_Page extends AppCompatActivity {

    // 1. Declaración de los componentes visuales tradicionales
    private EditText txtInputNombreSingup;
    private EditText txtInputApellidoSingup;
    private EditText txtInputEmailSingup;
    private EditText txtInputClaveSingup;
    private EditText txtInputReingresoClaveSingup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_singup_page);

        // 2. Inicialización tradicional mediante findViewById (Idéntico a tu Proyecto 5)
        txtInputNombreSingup = findViewById(R.id.txtInputNombreSingup);
        txtInputApellidoSingup = findViewById(R.id.txtInputApellidoSingup);
        txtInputEmailSingup = findViewById(R.id.txtInputEmailSingup);
        txtInputClaveSingup = findViewById(R.id.txtInputClaveSingup);
        txtInputReingresoClaveSingup = findViewById(R.id.txtInputReingresoClaveSingup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void confirmarRegistro(View view) {
        String nombre = txtInputNombreSingup.getText().toString().trim();
        String apellido = txtInputApellidoSingup.getText().toString().trim();
        String email = txtInputEmailSingup.getText().toString().trim();
        String clave = txtInputClaveSingup.getText().toString().trim();
        String reingresoClave = txtInputReingresoClaveSingup.getText().toString().trim();

        // Limpiar errores visuales previos
        txtInputNombreSingup.setError(null);
        txtInputApellidoSingup.setError(null);
        txtInputEmailSingup.setError(null);
        txtInputClaveSingup.setError(null);
        txtInputReingresoClaveSingup.setError(null);

        // Validación básica de campos vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || clave.isEmpty() || reingresoClave.isEmpty()) {
            Toast.makeText(this, "Error: Todos los campos del formulario deben ser completados correctamente", Toast.LENGTH_SHORT).show();

            if (nombre.isEmpty()) txtInputNombreSingup.setError("El nombre es requerido");
            if (apellido.isEmpty()) txtInputApellidoSingup.setError("El apellido es requerido");
            if (email.isEmpty()) txtInputEmailSingup.setError("El email es requerido");
            if (clave.isEmpty()) txtInputClaveSingup.setError("La contraseña es requerida");
            if (reingresoClave.isEmpty()) txtInputReingresoClaveSingup.setError("Ingrese nuevamente la contraseña");
            return;
        }

        // Validación estricta del formato de email
        if (!email.contains("@") || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtInputEmailSingup.setError("Ingrese un correo electrónico válido (Ejemplo: usuario@email.com)");
            return;
        }

        // Validación de coincidencia de contraseñas
        if (!clave.equals(reingresoClave)) {
            txtInputReingresoClaveSingup.setError("Las contraseñas no coinciden");
            Toast.makeText(this, "Las contraseñas ingresadas deben ser iguales", Toast.LENGTH_SHORT).show();
            return;
        }

        // Instancia segura de la base de datos de Room
        AppDatabase db = AppDatabase.Companion.getDatabase(this);

        // Operación de inserción en segundo plano de forma asíncrona
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Registra al usuario nuevo con saldo inicial de 0.0
                Usuario nuevoUsuario = new Usuario(
                        0, // Auto-incrementable por Room
                        "22.222.222-2",
                        nombre,
                        apellido,
                        email,
                        clave,
                        reingresoClave,
                        0.0
                );

                db.usuarioDao().registrarUsuario(nuevoUsuario);

                // Vuelve al hilo de la interfaz de usuario para confirmar y redirigir al Login
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Activity_Singup_Page.this, "¡Registro realizado con éxito! Inicie sesión.", Toast.LENGTH_SHORT).show();

                        // Redirige al login de forma limpia
                        Intent intent = new Intent(Activity_Singup_Page.this, Activity_Login_Page.class);
                        startActivity(intent);
                        finish(); // Destruye la pantalla de registro
                    }
                });
            }
        }).start(); // Encendido correcto del hilo secundario (.start())
    }

    // 4. Método conectado al enlace "¿Ya tienes cuenta?" en tu XML
    public void irALoginDesdeSingUp(View view) {
        Intent intent = new Intent(this, Activity_Login_Page.class);
        startActivity(intent);
        finish();
    }
}