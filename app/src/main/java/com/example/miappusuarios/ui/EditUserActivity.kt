/**
 * CLASS: EditUserActivity
 *
 * PROPÓSITO: Editar y eliminar usuarios existentes
 * PATRÓN: Form Activity - Edición de entidades existentes
 *
 * RESPONSABILIDADES:
 * - Cargar datos existentes del usuario
 * - Permitir modificación de campos
 * - Ejecutar UPDATE o DELETE en BD
 * - Proveer feedback y navegación
 *
 * CARACTERÍSTICAS:
 * - Pre-populado de formulario
 * - Doble acción: Actualizar o Eliminar
 * - Navegación automática al completar
 */
package com.example.miappusuarios.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.miappusuarios.data.User
import com.example.miappusuarios.data.UserDatabaseHelper
import com.example.miappusuarios.databinding.ActivityEditUserBinding

class EditUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditUserBinding
    private lateinit var dbHelper: UserDatabaseHelper
    private var userId: Long = 0
    /**
     * MÉTODO: onCreate()
     *
     * PROPÓSITO: Inicializar UI y cargar datos del usuario a editar
     * FLUJO: Inflar UI → Cargar datos del Intent → Configurar listeners
     *
     * DEPENDENCIAS:
     * - Intent extras: Datos del usuario a editar
     * - UserDatabaseHelper: Operaciones de persistencia
     *
     * COMPLEJIDAD: O(1) - Configuración y carga de datos
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = UserDatabaseHelper(this)
        getIntentData()
        setupClickListeners()
    }
    /**
     * MÉTODO: getIntentData()
     *
     * PROPÓSITO: Extraer y mostrar datos del usuario desde el Intent
     * TÉCNICA: Intent extras para comunicación inter-Activity
     *
     * DATOS EXTRAÍDOS:
     * - USER_ID: Identificador único (Long)
     * - USER_NAME: Nombre actual (String)
     * - USER_EMAIL: Email actual (String)
     * - USER_PHONE: Teléfono actual (String)
     *
     * MANEJO DE ERRORES:
     * - Valores por defecto si extras no existen
     * - userId = 0 indica error (no debería ocurrir)
     *
     * COMPLEJIDAD: O(1) - Acceso a extras del Intent
     */
    private fun getIntentData() {
        userId = intent.getLongExtra("USER_ID", 0)
        val name = intent.getStringExtra("USER_NAME") ?: ""
        val email = intent.getStringExtra("USER_EMAIL") ?: ""
        val phone = intent.getStringExtra("USER_PHONE") ?: ""

        binding.etName.setText(name)
        binding.etEmail.setText(email)
        binding.etPhone.setText(phone)
    }
    /**
     * MÉTODO: setupClickListeners()
     *
     * PROPÓSITO: Configurar comportamientos de los botones de acción
     * BOTONES:
     * - btnUpdateUser: Guardar cambios (UPDATE)
     * - btnDeleteUser: Eliminar usuario (DELETE)
     *
     * PATRÓN: Command - Cada botón ejecuta una operación específica
     *
     * COMPLEJIDAD: O(1) - Configuración de listeners
     */
    private fun setupClickListeners() {
        binding.btnUpdateUser.setOnClickListener {
            updateUser()
        }

        binding.btnDeleteUser.setOnClickListener {
            deleteUser()
        }
    }
    /**
     * MÉTODO: updateUser()
     *
     * PROPÓSITO: Validar y guardar cambios en el usuario
     * OPERACIÓN: UPDATE - Modificación de registro existente
     *
     * FLUJO:
     * 1. Validar campos del formulario
     * 2. Crear objeto User con datos actualizados
     * 3. Ejecutar UPDATE en BD
     * 4. Mostrar feedback y navegar
     *
     * VALIDACIONES:
     * - Campos no vacíos
     * - userId válido (≠ 0)
     *
     * COMPLEJIDAD: O(1) - Operación por ID único
     */
    private fun updateUser() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User(id = userId, name = name, email = email, phone = phone)
        val rowsAffected = dbHelper.updateUser(user)

        if (rowsAffected > 0) {
            Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
        }
    }
    /**
     * MÉTODO: deleteUser()
     *
     * PROPÓSITO: Eliminar usuario actual sin confirmación adicional
     * OPERACIÓN: DELETE - Eliminación directa desde pantalla de edición
     *
     * DIFERENCIA CON UsersListActivity:
     * - No requiere confirmación (botón dedicado)
     * - Eliminación inmediata
     * - Mismo feedback y navegación
     *
     * COMPLEJIDAD: O(1) - Operación por ID único
     */
    private fun deleteUser() {
        val rowsDeleted = dbHelper.deleteUser(userId)
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
        }
    }
}