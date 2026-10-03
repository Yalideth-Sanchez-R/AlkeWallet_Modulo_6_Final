package com.example.alkewallet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.alkewallet.dao.UsuarioDao
import com.example.alkewallet.dao.TransaccionDao
import com.example.alkewallet.model.Usuario
import com.example.alkewallet.model.Transaccion

@Database(entities = [Usuario::class, Transaccion::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun transaccionDao(): TransaccionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "alke_wallet_db"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration() // Limpia los residuos de la versión anterior
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
