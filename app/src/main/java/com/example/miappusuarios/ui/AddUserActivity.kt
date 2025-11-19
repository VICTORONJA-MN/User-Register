/**
 * CLASS: AddUserActivity
 *
 * PROPÓSITO: Formulario para creación de nuevos usuarios
 * PATRÓN: Form Activity - Captura y validación de datos
 *
 * RESPONSABILIDADES:
 * - Renderizar formulario de entrada
 * - Validar datos del usuario
 * - Persistir nuevo registro
 * - Proveer feedback al usuario
 */
package com.example.miappusuarios.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.miappusuarios.data.User
import com.example.miappusuarios.data.UserDatabaseHelper
import com.example.miappusuarios.databinding.ActivityAddUserBinding

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = UserDatabaseHelper(this)

        binding.btnSaveUser.setOnClickListener {
            saveUser()
        }
    }
    /**
     * método: saveUser()
     * PROPÓSITO: Guarda un nuevo usuario en la base de datos
     * PATRÓN: Modelo-Vista-Controlador (MVC)
     */
    private fun saveUser() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User(name = name, email = email, phone = phone)
        val id = dbHelper.addUser(user)

        if (id != -1L) {
            Toast.makeText(this, "Usuario guardado exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar el usuario", Toast.LENGTH_SHORT).show()
        }
    }
}