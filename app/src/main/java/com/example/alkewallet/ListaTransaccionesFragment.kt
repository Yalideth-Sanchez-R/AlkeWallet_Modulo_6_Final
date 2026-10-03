package com.example.alkewallet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.alkewallet.data.AppDatabase
import com.example.alkewallet.data.network.AlkeWalletCliente
import com.example.alkewallet.model.Transaccion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaTransaccionesFragment : Fragment() {

    // Variable para guardar el ListView visual
    private lateinit var listaTransaccionesHome: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. Inflamos el diseño XML del fragmento
        val view = inflater.inflate(R.layout.fragment_lista_transacciones, container, false)

        listaTransaccionesHome = view.findViewById(R.id.listaTransaccionesHome)

        return view
    }

    // Fuerza a Room a consultar de nuevo la base de datos cada vez que regresa de Depositar o Enviar dinero.
    override fun onResume() {
        super.onResume()
        obtenerDatosdeInternet()
    }
    private fun obtenerDatosdeInternet() {
        val db = AppDatabase.getDatabase(requireContext())
        val transaccionDao = db.transaccionDao()
        val usuarioDao = db.usuarioDao()

        // Recupera de forma exacta el correo del usuario que inició sesión en SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("AlkeWalletPrefs", Context.MODE_PRIVATE)
        val emailUsuarioActivo = sharedPreferences.getString("Email_usuario_logueado", "") ?: ""

        // Se abre el ciclo de vida de la corrutina
        lifecycleScope.launch {
            try {
                // 1. LLAMADO A LA API REST (Ejecución en segundo plano)
                val listaMovimientos = withContext(Dispatchers.IO) {
                    AlkeWalletCliente.apiService.obtenerTransacciones()
                }

                //if (listaMovimientos.isNotEmpty()) {
                  //  withContext(Dispatchers.IO) {
                    //    for (transaccion in listaMovimientos) {
                            // Amarramos los datos estáticos de la API al usuario de la sesión activa
                      //      transaccion.emailUsuario = emailUsuarioActivo
                        //    transaccionDao.registrarTransaccion(transaccion)
                        //}
                    //}
                //}

                // Traer TODO el historial real guardado en Room para ESTE usuario específico
                val movimientosRealesLocal = withContext(Dispatchers.IO) {
                    transaccionDao.obtenerHistorial(emailUsuarioActivo)
                }

                // Mapea el listado combinado para el ArrayAdapter estándar
                val transaccionTexto = movimientosRealesLocal.map {
                    "${it.tipoMovimiento}: $${it.monto} \n${it.descripcion} - ${it.fechaHora}"
                }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    transaccionTexto
                )
                listaTransaccionesHome.adapter = adapter

            } catch (e: Exception) {
                // 💡 CAPTURA DE ERROR DE RED: Si falla el internet o el permiso, cae aquí pacíficamente
                val historialLocal = withContext(Dispatchers.IO) {
                    // Rescata los movimientos guardados en Room filtrados por el usuario activo
                    transaccionDao.obtenerHistorial(emailUsuarioActivo)
                }

                if (historialLocal.isNotEmpty()) {
                    val historialTexto = historialLocal.map {
                        "${it.tipoMovimiento}: $${it.monto} \n${it.descripcion} - ${it.fechaHora}"
                    }

                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        historialTexto
                    )
                    listaTransaccionesHome.adapter = adapter

                    Toast.makeText(
                        requireContext(),
                        "Mostrando historial local sin conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Sin conexión y sin historial guardado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
