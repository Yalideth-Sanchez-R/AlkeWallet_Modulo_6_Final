package com.example.alkewallet;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.alkewallet.data.AppDatabase;
import com.example.alkewallet.model.Transaccion;
import com.example.alkewallet.model.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity_Request_Money extends AppCompatActivity {

    // 1. Se declaran los componentes visuales e instancias
    private EditText etxtCantidadIngresar;
    private Button btnConfirmarIngreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_money);

        // Configuración para el diseño EdgeToEdge nativo
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Vinculación tradicional mediante findViewById (Idéntico a tu Proyecto 5 original)
        etxtCantidadIngresar = findViewById(R.id.etxtCantidadIngresar);
        btnConfirmarIngreso = findViewById(R.id.btnConfirmarIngreso);
    }

    // 2. Método para la flecha de volver atrás
    public void volverAlHome(View view) {
        finish();
    }

    // 3. Método conectado al onclick del botón 'Ingresar Dinero'
    public void ingresarDinero(View view) {
        String textoMonto = etxtCantidadIngresar.getText().toString().trim();

        // Validación A: Si la caja de texto está vacía
        if (textoMonto.isEmpty()) {
            etxtCantidadIngresar.setError("Ingrese un monto");
            return;
        }

        try {
            String montoLimpio = textoMonto.replace(".", "");

            // Validación B: Si ingresó un valor igual o menor a cero
            if (Double.parseDouble(montoLimpio) <= 0) {
                etxtCantidadIngresar.setError("Ingrese un monto mayor a 0");
                return;
            }

            // Si paso las validaciones, se procesa el número decimal
            double monto = Double.parseDouble(montoLimpio);
            String desdeDonde = "Cuenta Bancaria";

            // Reclama la base de datos de Room
            AppDatabase db = AppDatabase.Companion.getDatabase(this);

            // Rescata el correo del usuario dueño de la sesión activa
            String emailSesionActiva = getSharedPreferences("AlkeWalletPrefs", Context.MODE_PRIVATE)
                    .getString("Email_usuario_logueado", "");

            // Encapsula las operaciones de Room de forma segura en un hilo secundario tradicional
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Busca al usuario dueño de la sesión actual
                    Usuario usuarioActual = db.usuarioDao().obtenerPorEmail(emailSesionActiva);

                    if (usuarioActual != null) {
                        double nuevoSaldo = usuarioActual.getSaldo() + monto;

                        usuarioActual.setSaldo(nuevoSaldo);

                        // Actualiza el perfil del usuario en Room
                        db.usuarioDao().actualizarUsuario(usuarioActual);

                        // Genera la marca de tiempo exacta para el historial
                        String fechaSistema = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

                        // Registra la transacción pasando los valores puros requeridos en orden por Java
                        Transaccion nuevaTransaccion = new Transaccion(
                                0, // Auto-incrementable por Room
                                usuarioActual.getEmail(),
                                monto,
                                "Ingreso desde Cuenta Bancaria",
                                "INGRESO",
                                fechaSistema
                        );
                        db.transaccionDao().registrarTransaccion(nuevaTransaccion);

                        // Retorna de forma segura al hilo de la interfaz de usuario para avisar el éxito
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Activity_Request_Money.this, "¡Ingreso realizado con éxito!", Toast.LENGTH_SHORT).show();
                                finish(); // Cierra el formulario y actualiza la pantalla anterior
                            }
                        });
                    }
                }
            }).start(); // Encendido correcto del hilo secundario (.start())

        } catch (NumberFormatException e) {
            etxtCantidadIngresar.setError("Ingrese sólo números válidos");
        }
    }
}