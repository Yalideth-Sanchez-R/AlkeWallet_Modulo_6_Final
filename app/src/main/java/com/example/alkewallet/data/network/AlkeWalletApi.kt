package com.example.alkewallet.data.network

import android.credentials.CreateCredentialRequest
import com.example.alkewallet.model.Transaccion
import com.example.alkewallet.model.Usuario
import okhttp3.Response
import retrofit2.http.*

interface AlkeWalletApi {

    // 📡 PETICIONES GET (Consultar Datos)

    // 1.  Leer todas las transacciones
    @GET ("/transacciones")
    suspend fun obtenerTransacciones(): List<Transaccion>

    // 📡 PETICIONES PUT (Actualizar Datos)

    @PUT("/transacciones/id/{id}")
    suspend fun actualizarIdTransaccion(
        @Path ("id") id: Int,
        @Body IdActualizado: Transaccion
    ):  Transaccion

    // 🔄 PETICIONES POST (Crear / Registrar Datos)

    @POST ("/user/profile")
    suspend fun CrearUsuario(
        @Body usuario: Usuario
    ): Usuario
}
