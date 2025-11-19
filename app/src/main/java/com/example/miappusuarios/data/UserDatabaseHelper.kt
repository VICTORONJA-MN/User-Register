/**
 * CLASS: UserDatabaseHelper
 * PATRÓN: Database Helper - Implementación del patrón Data Access Object (DAO)
 * HERENCIA: SQLiteOpenHelper (Android Framework)
 *
 * RESPONSABILIDADES:
 * - Creación y actualización del esquema de base de datos
 * - Ejecución de operaciones CRUD (Create, Read, Update, Delete)
 * - Manejo de conexiones y recursos de BD
 * - Conversión entre modelos Kotlin y registros SQLite
 */
//paquete donde se encuentra la clase
package com.example.miappusuarios.data

//importaciones necesarias
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // CONSTANTES DE CONFIGURACIÓN
    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PHONE = "phone"
    }

    /**
     * MÉTODO: onCreate()
     * INVOCACIÓN: Automática al crear BD por primera vez
     * COMPLEJIDAD: O(1) - Operación única
     * TRANSACCIÓN: Implícita con SQLite
     */
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL,
                $COLUMN_PHONE TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    /**
     * MÉTODO: addUser()
     * OPERACIÓN: CREATE - Insertar nuevo registro
     * @param user Instancia de User a persistir
     * @return Long ID generado o -1 en caso de error
     * COMPLEJIDAD: O(1) - Inserción directa
     */
    fun addUser(user: User): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, user.name)
            put(COLUMN_EMAIL, user.email)
            put(COLUMN_PHONE, user.phone)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    /**
     * MÉTODO: getAllUsers()
     * OPERACIÓN: READ - Consultar todos los registros
     * @return List<User> Lista de usuarios ordenada por ID
     * COMPLEJIDAD: O(n) - n = número de registros
     */
    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            users.add(User(id, name, email, phone))
        }
        cursor.close()
        db.close()
        return users
    }

    /**
     * MÉTODO: updateUser()
     * OPERACIÓN: UPDATE - Actualizar un registro existente
     * - Consultar todos los registros
     * @param user Instancia de User a actualizar
     * @return Int Número de registros actualizados (1 o 0)
     * COMPLEJIDAD: O(1) - Actualización directa
     */

    fun updateUser(user: User): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, user.name)
            put(COLUMN_EMAIL, user.email)
            put(COLUMN_PHONE, user.phone)
        }
        val rowsAffected = db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(user.id.toString()))
        db.close()
        return rowsAffected
    }

    /**
     * MÉTODO: delteUser()
     * OPERACIÓN: DELETE - Eliminar un registro
     * @param id ID del registro a eliminar
     * @return Int Número de registros eliminados (1 o 0)
     * COMPLEJIDAD: O(1) - Eliminación directa
     */
    fun deleteUser(id: Long): Int {
        val db = writableDatabase
        val rowsAffected = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return rowsAffected
    }
}