package com.example.alkewallet.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_usuarios")
class Usuario {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var rut: String = ""
    var nombre: String = ""
    var apellido: String = ""
    var email: String = "" // Campo lógico de sesión para tus búsquedas
    var contraseña: String = ""
    var reingresarContraseña: String = ""
    var saldo: Double = 0.0

    // 1. Constructor vacío obligatorio requerido por Room para poder compilar
    constructor()

    constructor(
        id: Int,
        rut: String,
        nombre: String,
        apellido: String,
        email: String,
        contraseña: String,
        reingresarContraseña: String,
        saldo: Double
    ) {
        this.id = id
        this.rut = rut
        this.nombre = nombre
        this.apellido = apellido
        this.email = email
        this.contraseña = contraseña
        this.reingresarContraseña = reingresarContraseña
        this.saldo = saldo
    }
}