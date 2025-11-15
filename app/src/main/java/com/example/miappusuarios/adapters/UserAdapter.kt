package com.example.miappusuarios.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.miappusuarios.R
import com.example.miappusuarios.data.User

class UserAdapter(
    private var users: List<User>,
    private val onItemClick: (User) -> Unit,
    private val onItemDelete: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvEmail: TextView = itemView.findViewById(R.id.tvUserEmail)
        private val tvPhone: TextView = itemView.findViewById(R.id.tvUserPhone) // NUEVO

        fun bind(user: User) {
            tvName.text = user.name
            tvEmail.text = user.email
            tvPhone.text = user.phone // NUEVO

            itemView.setOnClickListener {
                onItemClick(user)
            }

            itemView.setOnLongClickListener {
                onItemDelete(user)
                true
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun updateUsers(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }
}