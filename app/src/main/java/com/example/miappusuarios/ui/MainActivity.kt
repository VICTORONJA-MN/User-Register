/**
 * CLASS: MainActivity
 * PATRÓN: Activity - Componente principal de Android
 * CICLO DE VIDA: onCreate() → onStart() → onResume() → ... → onDestroy()
 * RESPONSABILIDADES:
 * - Inflar y configurar la vista principal
 * - Manejar navegación entre pantallas
 * - Gestionar ciclo de vida de la UI
 */
package com.example.miappusuarios.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.miappusuarios.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // VIEW BINDING - Reemplaza findViewById() con type safety
    private lateinit var binding: ActivityMainBinding
    /**
     * MÉTODO: onCreate()
     * FASE: Initialization - Called when activity is first created
     * COMPLEJIDAD: O(1) - Operaciones constantes
     * MEMORY: ~2MB (layout inflation + binding initialization)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar los botones
        setupButtons()
    }
    /**
     * MÉTODO: setupButtons()
     * PROPÓSITO: Configurar event listeners para navegación
     * PATRÓN: Observer - Listeners para eventos de UI
     */
    private fun setupButtons() {
        // Botón para agregar usuario
        binding.btnAddUser.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

        // Botón para ver lista de usuarios
        binding.btnViewUsers.setOnClickListener {
            val intent = Intent(this, UsersListActivity::class.java)
            startActivity(intent)
        }
    }
}