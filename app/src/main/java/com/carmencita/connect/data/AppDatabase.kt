package com.carmencita.connect.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.carmencita.connect.data.local.ContactoFrecuenteDao
import com.carmencita.connect.data.local.ContactoFrecuenteEntity
import com.carmencita.connect.data.local.EncomiendaDao
import com.carmencita.connect.data.local.EncomiendaEntity
import com.carmencita.connect.data.local.NotificacionDao
import com.carmencita.connect.data.local.NotificacionEntity
import com.carmencita.connect.data.local.PersonaDao
import com.carmencita.connect.data.local.PersonaEntity
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        PersonaEntity::class,
        EncomiendaEntity::class,
        ContactoFrecuenteEntity::class,
        NotificacionEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personaDao(): PersonaDao
    abstract fun encomiendaDao(): EncomiendaDao
    abstract fun contactoFrecuenteDao(): ContactoFrecuenteDao
    abstract fun notificacionDao(): NotificacionDao

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
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5
                    )
                    .addCallback(CREAR_DATOS_INICIALES)
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

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                crearTablaEncomiendas(database)
                insertarEncomiendasPrueba(database)
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                crearTablaContactosFrecuentes(database)
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                crearTablaNotificaciones(database)
            }
        }

        private val CREAR_DATOS_INICIALES = object : RoomDatabase.Callback() {
            override fun onCreate(database: SupportSQLiteDatabase) {
                super.onCreate(database)
                insertarEncomiendasPrueba(database)
            }
        }

        private fun crearTablaEncomiendas(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS encomiendas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    numeroGuia TEXT NOT NULL,
                    largo REAL NOT NULL,
                    ancho REAL NOT NULL,
                    alto REAL NOT NULL,
                    peso REAL NOT NULL,
                    origen TEXT NOT NULL,
                    destino TEXT NOT NULL,
                    tarifaDestino TEXT NOT NULL,
                    tarifaCosto REAL NOT NULL,
                    estado TEXT NOT NULL,
                    fechaRegistro TEXT,
                    fechaTransito TEXT,
                    fechaAgencia TEXT,
                    fechaEntrega TEXT
                )
                """.trimIndent()
            )
            database.execSQL(
                "CREATE UNIQUE INDEX IF NOT EXISTS index_encomiendas_numeroGuia " +
                    "ON encomiendas(numeroGuia)"
            )
        }

        private fun crearTablaContactosFrecuentes(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS contactos_frecuentes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    personaId INTEGER NOT NULL,
                    nombre TEXT NOT NULL,
                    dni TEXT NOT NULL,
                    telefono TEXT NOT NULL,
                    direccion TEXT NOT NULL
                )
                """.trimIndent()
            )
            database.execSQL(
                "CREATE UNIQUE INDEX IF NOT EXISTS index_contactos_frecuentes_personaId_dni " +
                "ON contactos_frecuentes(personaId, dni)"
            )
        }

        private fun crearTablaNotificaciones(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS notificaciones (
                    id TEXT PRIMARY KEY NOT NULL,
                    personaId INTEGER NOT NULL,
                    titulo TEXT NOT NULL,
                    mensaje TEXT NOT NULL,
                    fecha TEXT NOT NULL,
                    estado TEXT NOT NULL,
                    creadoEn INTEGER NOT NULL
                )
                """.trimIndent()
            )
            database.execSQL(
                "CREATE INDEX IF NOT EXISTS index_notificaciones_personaId_creadoEn " +
                    "ON notificaciones(personaId, creadoEn)"
            )
        }

        private fun insertarEncomiendasPrueba(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                INSERT OR IGNORE INTO encomiendas (
                    id, numeroGuia, largo, ancho, alto, peso, origen, destino,
                    tarifaDestino, tarifaCosto, estado, fechaRegistro,
                    fechaTransito, fechaAgencia, fechaEntrega
                ) VALUES (
                    1, 'C000000001', 20.0, 20.0, 20.0, 10.0, 'Trujillo', 'Angasmarca',
                    'Angasmarca', 45.0, 'EN AGENCIA', '20/05/26',
                    '20/05/26', '21/05/26', NULL
                )
                """.trimIndent()
            )
            database.execSQL(
                """
                INSERT OR IGNORE INTO encomiendas (
                    id, numeroGuia, largo, ancho, alto, peso, origen, destino,
                    tarifaDestino, tarifaCosto, estado, fechaRegistro,
                    fechaTransito, fechaAgencia, fechaEntrega
                ) VALUES (
                    2, 'C000000345', 35.0, 25.0, 18.0, 7.5, 'Lima', 'Trujillo',
                    'Trujillo', 38.0, 'EN TRÁNSITO', '18/06/26',
                    '19/06/26', NULL, NULL
                )
                """.trimIndent()
            )
            database.execSQL(
                """
                INSERT OR IGNORE INTO encomiendas (
                    id, numeroGuia, largo, ancho, alto, peso, origen, destino,
                    tarifaDestino, tarifaCosto, estado, fechaRegistro,
                    fechaTransito, fechaAgencia, fechaEntrega
                ) VALUES (
                    3, 'C000000782', 15.0, 12.0, 10.0, 2.0, 'Chiclayo', 'Lima',
                    'Lima', 24.0, 'REGISTRADO', '21/06/26',
                    NULL, NULL, NULL
                )
                """.trimIndent()
            )
        }
    }
}
