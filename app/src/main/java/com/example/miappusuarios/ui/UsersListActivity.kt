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

    private fun loadUsers() {
        val users = dbHelper.getAllUsers()
        userAdapter.updateUsers(users)

        if (users.isEmpty()) {
            binding.tvEmptyMessage.visibility = android.view.View.VISIBLE
        } else {
            binding.tvEmptyMessage.visibility = android.view.View.GONE
        }
    }

    private fun editUser(user: User) {
        val intent = Intent(this, EditUserActivity::class.java).apply {
            putExtra("USER_ID", user.id)
            putExtra("USER_NAME", user.name)
            putExtra("USER_EMAIL", user.email)
            putExtra("USER_PHONE", user.phone)
        }
        startActivity(intent)
    }

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

    override fun onResume() {
        super.onResume()
        loadUsers()
    }
}