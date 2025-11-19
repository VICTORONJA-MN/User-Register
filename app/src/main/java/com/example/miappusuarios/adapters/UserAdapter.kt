/**
 * CLASS: UserAdapter
 * PATRÓN: Adapter - Implementación de RecyclerView.Adapter
 * RESPONSABILIDADES:
 * - Binding de datos a vistas de items
 * - Manejo de eventos de click y long-click
 * - Actualización eficiente de la lista
 * - Patrón ViewHolder para reciclaje de vistas
 */
//paqute de adaptadores
package com.example.miappusuarios.adapters

//importaciones necesarias
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
    /**
     * INNER CLASS: UserViewHolder
     * PATRÓN: ViewHolder - Cache de vistas para reciclaje
     * OPTIMIZACIÓN: Reduce findViewById calls
     */
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvEmail: TextView = itemView.findViewById(R.id.tvUserEmail)
        private val tvPhone: TextView = itemView.findViewById(R.id.tvUserPhone) // NUEVO
        /**
         * MÉTODO: bind()
         * PROPÓSITO: Asignar datos a vistas y configurar eventos
         * @param user Datos a mostrar en el item
         */
        fun bind(user: User) {
            tvName.text = user.name
            tvEmail.text = user.email
            tvPhone.text = user.phone // NUEVO
            // CONFIGURACIÓN DE EVENTOS
            itemView.setOnClickListener {
                onItemClick(user)
            }

            itemView.setOnLongClickListener {
                onItemDelete(user)
                true
            }
        }
    }

    /**
     * MÉTODO: onCreateViewHolder()
     * PROPÓSITO: Crear una nueva instancia de ViewHolder
     * @param parent Grupo de vistas al que pertenece el nuevo ViewHolder
     * @param viewType Tipo de vista (no utilizado en este caso)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }
    // devuelve el número de elementos en la lista
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }
    // devuelve el número de elementos en la lista
    override fun getItemCount(): Int = users.size
    /**
     * MÉTODO: updateUsers()
     * PROPÓSITO: Actualizar datos y notificar cambios
     * OPTIMIZACIÓN: notifyDataSetChanged() - Simple pero ineficiente para grandes datasets
     * MEJORA FUTURA: Implementar DiffUtil para updates eficientes
     */
    fun updateUsers(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }
}