package com.example.alkewallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.alkewallet.data.AppDatabase;
import com.example.alkewallet.model.Usuario;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Activity_Home_Page extends AppCompatActivity {
    // 1. Se declaran los componentes visuales y el controlador
    private TextView txtSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        // 2. Se inicializa el controlador y se vincula el textView con el XML
        txtSaldo = findViewById(R.id.txtMontoBalanceHome);

        // Configuración para el diseño EdgeToEdge nativo
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 3. Métodos onClick enlazados con el XML
    public void irASendMoney(View view) {
        Intent intent = new Intent(this, Activity_Send_Money.class);
        startActivity(intent);
    }

    public void irARequestMoney(View view) {
        Intent intent = new Intent(this, Activity_Request_Money.class);
        startActivity(intent);
    }

    public void irAPerfil(View view) {
        Intent intent = new Intent(this, Activity_Profile_Page.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reclama de forma segura la instancia de la base de datos de Room
        AppDatabase db = AppDatabase.Companion.getDatabase(this);

        // Rescata el correo del usuario logueado almacenado en las SharedPreferences de la sesión activa
        String emailSesionActiva = getSharedPreferences("AlkeWalletPrefs", Context.MODE_PRIVATE)
                .getString("Email_usuario_logueado", "");

        // Ejecuta la consulta de base de datos dentro del hilo secundario clásico
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Busca únicamente al usuario dueño de la sesión actual por su email
                Usuario usuarioActual = db.usuarioDao().obtenerPorEmail(emailSesionActiva);

                if (usuarioActual != null) {
                    double saldoActual = usuarioActual.getSaldo();

                    // Sincroniza la actualización visual de vuelta en el hilo principal de la interfaz
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                            simbolos.setGroupingSeparator('.');
                            simbolos.setDecimalSeparator(',');

                            DecimalFormat formateador = new DecimalFormat("$#,##0.00", simbolos);
                            txtSaldo.setText(formateador.format(saldoActual));
                        }
                    });
                }
            }
        }).start(); // Encendido correcto del Thread secundario para evitar congelamientos
    }
}