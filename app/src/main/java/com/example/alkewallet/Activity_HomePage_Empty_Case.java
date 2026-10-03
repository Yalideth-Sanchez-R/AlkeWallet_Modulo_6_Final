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

public class Activity_HomePage_Empty_Case extends AppCompatActivity {

    // 1. Se declaran los componentes visuales tradicionales
    private TextView txtMontoBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page_empty_case);

        // Configuración para el diseño EdgeToEdge nativo
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Vinculación tradicional mediante findViewById
        txtMontoBalance = findViewById(R.id.txtMontoBalance);
    }

    // Métodos onClick de los botones del XML
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

        // 1. Reclama la base de datos de Room instalada
        AppDatabase db = AppDatabase.Companion.getDatabase(this);

        // 2. Rescata el correo del usuario logueado almacenado en SharedPreferences
        String emailSesionActiva = getSharedPreferences("AlkeWalletPrefs", Context.MODE_PRIVATE)
                .getString("Email_usuario_logueado", "");
        // 3. Ejecuta la consulta de saldo dentro del hilo secundario tradicional
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Busca los datos actualizados del usuario en Room
                Usuario usuarioActual = db.usuarioDao().obtenerPorEmail(emailSesionActiva);

                if (usuarioActual != null) {
                    double saldoActual = usuarioActual.getSaldo();

                    // CASO A: Si el usuario ya ingresó dinero (Saldo > 0), salta automáticamente al Home principal
                    if (saldoActual > 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(Activity_HomePage_Empty_Case.this, Activity_Home_Page.class);
                                startActivity(intent);
                                finish(); // Destruye la pantalla vacía para no regresar con el botón "Atrás"
                            }
                        });
                    } else {
                        // CASO B: Si el saldo sigue en 0, formatea el texto en el hilo de la interfaz de usuario
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                                simbolos.setGroupingSeparator('.');
                                simbolos.setDecimalSeparator(',');

                                DecimalFormat formateador = new DecimalFormat("$#,##0.00", simbolos);
                                txtMontoBalance.setText(formateador.format(saldoActual));
                            }
                        });
                    }
                }
            }
        }).start(); // Encendido correcto del hilo secundario
    }
}