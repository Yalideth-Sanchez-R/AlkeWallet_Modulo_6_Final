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


public class Activity_Login_Page extends AppCompatActivity {


    // 1. Declaración de los componentes visuales del xml
    private EditText txtInputEmailLogin;
    private EditText txtInputClaveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);

        // 2. Inicialización tradicional mediante findViewById
        txtInputEmailLogin = findViewById(R.id.txtInputEmailLogin);
        txtInputClaveLogin = findViewById(R.id.txtInputClaveLogin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    // 3. Método conectado al onClick del botón "INICIAR SESIÓN" en el XML
    public void irAHomePage(View view) {
        String email = txtInputEmailLogin.getText().toString().trim();
        String clave = txtInputClaveLogin.getText().toString().trim();

        // Validaciones básicas de campos vacíos
        if (email.isEmpty()) {
            txtInputEmailLogin.setError("El correo es requerido");
            return;
        }
        if (clave.isEmpty()) {
            txtInputClaveLogin.setError("La contraseña es requerida");
            return;
        }

        // Instancia de Room para verificar las credenciales
        AppDatabase db = AppDatabase.Companion.getDatabase(this);

        // Guardamos de forma inmediata el correo en la sesión activa de SharedPreferences
        getSharedPreferences("AlkeWalletPrefs", Context.MODE_PRIVATE)
                .edit()
                .putString("Email_usuario_logueado", email)
                .apply();

        // Ejecución en segundo plano para no congelar la UI
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Busca si el usuario existe en Room
                Usuario usuarioLogueado = db.usuarioDao().obtenerPorEmail(email);

                // GESTIÓN CASO ESPECIAL: Si es el usuario viejo Amanda y entra por primera vez a Room
                if (usuarioLogueado == null && email.equalsIgnoreCase("amanda@alkewallet.com")) {
                    double saldoInicialAmanda = 150000.0;
                    usuarioLogueado = new Usuario(
                            0, // Auto-incremento
                            "11.111.111-1",
                            "Amanda",
                            "Wallet",
                            "amanda@alkewallet.com",
                            clave,
                            clave,
                            saldoInicialAmanda
                    );
                    db.usuarioDao().registrarUsuario(usuarioLogueado);
                }

                // Evaluación de destino según el saldo del usuario
                if (usuarioLogueado != null) {

                    // 💡 VALIDACIÓN CRÍTICA: Verifica que la clave introducida sea la correcta
                    if (!usuarioLogueado.getContraseña().equals(clave)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Activity_Login_Page.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return; // Frena la ejecución para que no verifique el saldo si se equivocó de clave
                    }

                    // Si la clave coincide, procede con la lógica de saldo normal
                    double saldo = usuarioLogueado.getSaldo();

                    if (saldo == 0.0) {
                        // CASO 1: Es un usuario nuevo con saldo 0
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(Activity_Login_Page.this, Activity_HomePage_Empty_Case.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        // CASO 2: Es un usuario viejo con saldo cargado
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(Activity_Login_Page.this, Activity_Home_Page.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Activity_Login_Page.this, "Usuario no registrado. Regístrese primero.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    // 4. Método conectado al onClick del enlace "Registrarse" en el XML
    public void irAlRegistro(View view) {
        Intent intent = new Intent(this, Activity_Singup_Page.class);
        startActivity(intent);
    }
}