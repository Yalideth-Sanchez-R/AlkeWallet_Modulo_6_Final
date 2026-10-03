package com.example.alkewallet.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.alkewallet.model.Usuario

@Dao
interface UsuarioDao {

    // 1. CREATE: Registrar un nuevo usuario
    @Insert
    fun registrarUsuario(usuario: Usuario)

    // 2. READ: Validación del inicio de sesión
    @Query("SELECT * FROM tabla_usuarios WHERE email = :email AND contraseña = :contraseňa LIMIT 1")
    fun iniciarSesion(email: String, contraseňa: String): Usuario

    // 3. UPDATE: Actualizar datos del usuario
    @Update
    fun actualizarUsuario(usuario: Usuario)

    // 4. READ: Permite a las pantallas en Java leer los saldos de la tabla
    @Query("SELECT * FROM tabla_usuarios")
    fun obtenerTodos(): List<Usuario>

    // 5. READ: Buscar un usuario único por su correo de sesión
    @Query("SELECT * FROM tabla_usuarios WHERE email = :email LIMIT 1")
    fun obtenerPorEmail(email: String): Usuario?

    // 6. DELETE
    @Delete
    fun eliminarUsuario(usuario: Usuario)
}
