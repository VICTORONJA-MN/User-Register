/**
 * CLASS: UsersListActivity
 *
 * PROPÓSITO: Mostrar lista completa de usuarios registrados
 * PATRÓN: List Activity - Display y gestión de colecciones
 *
 * RESPONSABILIDADES:
 * - Cargar y mostrar lista de usuarios desde BD
 * - Manejar eventos de edición y eliminación
 * - Gestionar estado vacío de la lista
 * - Coordinar navegación a pantalla de edición
 *
 * CARACTERÍSTICAS:
 * - RecyclerView con Adapter personalizado
 * - Dialogos de confirmación para eliminación
 * - Actualización automática onResume()
 * - Empty state handling
 */
package com.example.miappusuarios.ui

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miappusuarios.adapters.UserAdapter
import com.example.miappusuarios.data.User
import com.example.miappusuarios.data.UserDatabaseHelper
import com.example.miappusuarios.databinding.ActivityUsersListBinding

class UsersListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersListBinding
    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = UserDatabaseHelper(this)
        setupRecyclerView()
        loadUsers()
    }

    /**
     * MÉTODO: setupRecyclerView()
     *
     * PROPÓSITO: Configurar RecyclerView y Adapter con comportamientos
     * COMPONENTES:
     * - LinearLayoutManager: Layout vertical estándar
     * - UserAdapter: Renderizado y eventos de items
     * - Callbacks: Comunicación Activity-Adapter
     *
     * PATRÓN: Observer - Callbacks para eventos de UI
     *
     * COMPLEJIDAD: O(1) - Configuración de componentes
     */
    private fun setupRecyclerView() {
        userAdapter = UserAdapter(
            users = emptyList(),
            onItemClick = { user ->
                editUser(user)
            },
            onItemDelete = { user ->
                deleteUser(user)
            }
        )

        binding.recyclerViewUsers.apply {
            layoutManager = LinearLayoutManager(this@UsersListActivity)
            adapter = userAdapter
        }
    }

    /**
     * MÉTODO: loadUsers()
     *
     * PROPÓSITO: Cargar datos desde BD y actualizar UI
     * OPERACIÓN: READ - Consulta completa de la tabla
     *
     * FLUJO:
     * 1. Ejecutar query en BD
     * 2. Actualizar Adapter con nuevos datos
     * 3. Mostrar/ocultar empty state
     *
     * MANEJO DE ESTADOS:
     * - Lista vacía: Mostrar mensaje empty state
     * - Con datos: Mostrar RecyclerView
     *
     * COMPLEJIDAD: O(n) - n = número de registros
     * RENDIMIENTO: Operación en hilo principal (mejorable)
     */
    private fun loadUsers() {
        val users = dbHelper.getAllUsers()
        userAdapter.updateUsers(users)

        if (users.isEmpty()) {
            binding.tvEmptyMessage.visibility = android.view.View.VISIBLE
        } else {
            binding.tvEmptyMessage.visibility = android.view.View.GONE
        }
    }
    /**
     * MÉTODO: editUser()
     *
     * PROPÓSITO: Navegar a pantalla de edición con datos del usuario
     * PATRÓN: Intent with Extras - Paso de datos entre Activities
     *
     * @param user Usuario seleccionado para edición
     *
     * TÉCNICA:
     * - Intent explícito a EditUserActivity
     * - Extras para pasar datos del usuario
     * - No se usa Parcelable (mejora futura)
     *
     * COMPLEJIDAD: O(1) - Creación de Intent
     */
    private fun editUser(user: User) {
        val intent = Intent(this, EditUserActivity::class.java).apply {
            putExtra("USER_ID", user.id)
            putExtra("USER_NAME", user.name)
            putExtra("USER_EMAIL", user.email)
            putExtra("USER_PHONE", user.phone)
        }
        startActivity(intent)
    }
    /**
     * MÉTODO: deleteUser()
     *
     * PROPÓSITO: Eliminar usuario con confirmación del usuario
     * PATRÓN: Confirmation Dialog - Prevención de eliminación accidental
     *
     * @param user Usuario a eliminar
     *
     * FLUJO:
     * 1. Mostrar diálogo de confirmación
     * 2. Si confirma: Ejecutar DELETE en BD
     * 3. Actualizar lista y mostrar feedback
     *
     * SEGURIDAD:
     * - Confirmación requerida
     * - Feedback visual del resultado
     * - Rollback automático en error de BD
     *
     * COMPLEJIDAD: O(1) - Operación por ID único
     */
    private fun deleteUser(user: User) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Usuario")
            .setMessage("¿Estás seguro de que quieres eliminar a ${user.name}?")
            .setPositiveButton("Eliminar") { _, _ ->
                val rowsDeleted = dbHelper.deleteUser(user.id)
                if (rowsDeleted > 0) {
                    android.widget.Toast.makeText(this, "Usuario eliminado", android.widget.Toast.LENGTH_SHORT).show()
                    loadUsers()
                } else {
                    android.widget.Toast.makeText(this, "Error al eliminar", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    /**
     * MÉTODO: onResume()
     *
     * CICLO DE VIDA: Llamado cuando la Activity vuelve al foreground
     * PROPÓSITO: Actualizar datos después de ediciones/altas en otras pantallas
     *
     * ESCENARIOS:
     * - Regreso desde EditUserActivity (después de actualizar)
     * - Regreso desde AddUserActivity (después de agregar)
     * - Vuelta desde multitarea
     *
     * COMPLEJIDAD: O(n) - Recarga completa de datos
     */
    override fun onResume() {
        super.onResume()
        loadUsers()
    }
}