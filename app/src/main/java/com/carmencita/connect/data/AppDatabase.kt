package com.carmencita.connect.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.carmencita.connect.data.local.PersonaDao
import com.carmencita.connect.data.local.PersonaEntity
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [PersonaEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personaDao(): PersonaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtener(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "carmencita_local.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS personas_nueva (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        nombre TEXT NOT NULL,
                        dni TEXT NOT NULL,
                        telefono TEXT NOT NULL,
                        direccion TEXT NOT NULL,
                        correo TEXT NOT NULL,
                        passwordHash TEXT NOT NULL,
                        salt TEXT NOT NULL,
                        correoVerificado INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                database.execSQL(
                    """
                    INSERT INTO personas_nueva (
                        id, nombre, dni, telefono, direccion,
                        correo, passwordHash, salt, correoVerificado
                    )
                    SELECT
                        p.id, p.nombre, p.dni, p.telefono, p.direccion,
                        c.correo, c.passwordHash, c.salt, 1
                    FROM personas p
                    INNER JOIN credenciales c ON c.personaId = p.id
                    """.trimIndent()
                )
                database.execSQL("DROP TABLE IF EXISTS sesiones")
                database.execSQL("DROP TABLE IF EXISTS credenciales")
                database.execSQL("DROP TABLE personas")
                database.execSQL("ALTER TABLE personas_nueva RENAME TO personas")
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_personas_dni ON personas(dni)")
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_personas_correo ON personas(correo)")
            }
        }
    }
}
