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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = UserDatabaseHelper(this)
        getIntentData()
        setupClickListeners()
    }

    private fun getIntentData() {
        userId = intent.getLongExtra("USER_ID", 0)
        val name = intent.getStringExtra("USER_NAME") ?: ""
        val email = intent.getStringExtra("USER_EMAIL") ?: ""
        val phone = intent.getStringExtra("USER_PHONE") ?: ""

        binding.etName.setText(name)
        binding.etEmail.setText(email)
        binding.etPhone.setText(phone)
    }

    private fun setupClickListeners() {
        binding.btnUpdateUser.setOnClickListener {
            updateUser()
        }

        binding.btnDeleteUser.setOnClickListener {
            deleteUser()
        }
    }

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