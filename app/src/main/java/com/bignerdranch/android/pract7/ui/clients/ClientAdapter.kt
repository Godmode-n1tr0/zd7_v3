package com.bignerdranch.android.pract7.ui.clients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.pract7.data.entities.ClientEntity
import com.bignerdranch.android.pract7.databinding.ItemClientBinding
import com.bignerdranch.android.pract7.utils.UserRole

class ClientAdapter(
    private var clients: List<ClientEntity>,
    private val role: UserRole,
    private val onEdit: (ClientEntity) -> Unit,
    private val onDelete: (ClientEntity) -> Unit
) : RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ClientViewHolder {
        val binding = ItemClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bind(clients[position])
    }

    override fun getItemCount() = clients.size

    fun update(newClients: List<ClientEntity>) {
        clients = newClients
        notifyDataSetChanged()
    }

    inner class ClientViewHolder(private val binding: ItemClientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(client: ClientEntity) {
            binding.tvName.text = client.name
            binding.tvEmail.text = client.email
            binding.tvDiscount.text = "Скидка: ${client.discount}%"

            if (role == UserRole.WORKER) {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnEdit.setOnClickListener { onEdit(client) }
                binding.btnDelete.setOnClickListener { onDelete(client) }
            } else {
                binding.btnEdit.visibility = View.GONE
                binding.btnDelete.visibility = View.GONE
            }
        }
    }
}
