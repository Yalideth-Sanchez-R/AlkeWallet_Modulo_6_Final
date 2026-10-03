package com.example.alkewallet.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tabla_transacciones")
class Transaccion {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var emailUsuario: String = "" // Vincula de forma lógica la transacción con el usuario activo
    var monto: Double = 0.0
    var descripcion: String = ""
    var tipoMovimiento: String = "" // Almacenará "Ingreso" o "Egreso" para calzar con Beeceptor
    var fechaHora: String = ""

    // 1. Constructor vacío obligatorio requerido por Room para el mapeo local
    constructor()

    // 2. Constructor completo nativo para que las Activities en Java lo invoquen sin problemas
    constructor(
        id: Int,
        emailUsuario: String,
        monto: Double,
        descripcion: String,
        tipoMovimiento: String,
        fechaHora: String
    ) {
        this.id = id
        this.emailUsuario = emailUsuario
        this.monto = monto
        this.descripcion = descripcion
        this.tipoMovimiento = tipoMovimiento
        this.fechaHora = fechaHora
    }
}