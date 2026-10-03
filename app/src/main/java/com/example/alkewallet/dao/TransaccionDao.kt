package com.example.alkewallet.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alkewallet.model.Transaccion

@Dao
interface TransaccionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun registrarTransaccion(transaccion: Transaccion)

    @Query("SELECT * FROM Tabla_transacciones WHERE emailUsuario = :email ORDER BY id DESC")
    fun obtenerHistorial(email: String): List<Transaccion>
}
