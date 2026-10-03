package com.example.alkewallet;

import android.content.Context;
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
import com.example.alkewallet.model.Transaccion;
import com.example.alkewallet.model.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity_Send_Money extends AppCompatActivity {
    private EditText txtMonto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_money);

        // 2. Vinculación tradicional mediante el ID real de tu XML
        txtMonto = findViewById(R.id.etxtCantidadEnviar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void volverAlHome(View view) {

        finish();
    }
    public void enviarDinero(View view) {
        String textoMonto = txtMonto.getText().toString().trim();

        if (textoMonto.isEmpty()) {
            txtMonto.setError("Ingrese un monto");
            return;
        }

        try {
            String montoLimpio = textoMonto.replace(".", "");

            // Validación B: Si ingresó un valor igual o menor a cero
            if (Double.parseDouble(montoLimpio) <= 0) {
                txtMonto.setError("Ingrese un monto mayor a 0");
                return;
            }

            double monto = Double.parseDouble(montoLimpio);

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

                        // VALIDACIÓN CRÍTICA DE SALDO DISPONIBLE EN ROOM
                        if (monto > usuarioActual.getSaldo()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtMonto.setError("Saldo insuficiente para realizar esta transacción");
                                }
                            });
                            return; // Frena el flujo si no tiene dinero suficiente
                        }

                        // Realiza el descuento matemático
                        double nuevoSaldo = usuarioActual.getSaldo() - monto;
                        usuarioActual.setSaldo(nuevoSaldo);

                        // Actualiza el perfil del usuario en Room con su nuevo saldo disminuido
                        db.usuarioDao().actualizarUsuario(usuarioActual);

                        // Genera la marca de tiempo exacta para el historial
                        String fechaSistema = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

                        // Registra la transacción de egreso pasando los valores puros requeridos en orden por Java
                        Transaccion nuevaTransaccion = new Transaccion(
                                0, // Auto-incrementable por Room
                                usuarioActual.getEmail(),
                                monto,
                                "Envío de dinero",
                                "ENVIO",
                                fechaSistema
                        );
                        db.transaccionDao().registrarTransaccion(nuevaTransaccion);

                        // Retorna de forma segura al hilo de la interfaz de usuario para avisar el éxito
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Activity_Send_Money.this, "¡Envío exitoso!", Toast.LENGTH_SHORT).show();
                                finish(); // Cierra el formulario de egreso y actualiza la navegación
                            }
                        });
                    }
                }
            }).start(); // Encendido correcto del hilo secundario (.start())

        } catch (NumberFormatException e) {
            txtMonto.setError("Ingrese sólo números válidos");
        }
    }
}