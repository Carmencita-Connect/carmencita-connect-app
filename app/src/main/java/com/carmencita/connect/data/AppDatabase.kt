package com.carmencita.connect.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.carmencita.connect.data.local.CredencialDao
import com.carmencita.connect.data.local.CredencialEntity
import com.carmencita.connect.data.local.PersonaDao
import com.carmencita.connect.data.local.PersonaEntity
import com.carmencita.connect.data.local.SesionDao
import com.carmencita.connect.data.local.SesionEntity

@Database(
    entities = [
        PersonaEntity::class,
        CredencialEntity::class,
        SesionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personaDao(): PersonaDao
    abstract fun credencialDao(): CredencialDao
    abstract fun sesionDao(): SesionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtener(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "carmencita_local.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
