package com.bignerdranch.android.pract7.ui.suppliers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.pract7.data.entities.SupplierEntity
import com.bignerdranch.android.pract7.databinding.ItemSupplierBinding
import com.bignerdranch.android.pract7.utils.UserRole

class SupplierAdapter(
    private var suppliers: List<SupplierEntity>,
    private val role: UserRole,
    private val onEdit: (SupplierEntity) -> Unit,
    private val onDelete: (SupplierEntity) -> Unit
) : RecyclerView.Adapter<SupplierAdapter.SupplierViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            SupplierViewHolder {
        val binding = ItemSupplierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SupplierViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SupplierViewHolder, position: Int) {
        holder.bind(suppliers[position])
    }

    override fun getItemCount() = suppliers.size

    fun update(newSuppliers: List<SupplierEntity>) {
        suppliers = newSuppliers
        notifyDataSetChanged()
    }

    inner class SupplierViewHolder(private val binding: ItemSupplierBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(supplier: SupplierEntity) {
            binding.tvName.text = supplier.name
            binding.tvEmail.text = supplier.email

            if (role == UserRole.WORKER) {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnEdit.setOnClickListener { onEdit(supplier) }
                binding.btnDelete.setOnClickListener { onDelete(supplier) }
            } else {
                binding.btnEdit.visibility = View.GONE
                binding.btnDelete.visibility = View.GONE
            }
        }
    }
}
