package com.bignerdranch.android.pract7.ui.furniture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.pract7.data.entities.FurnitureTypeEntity
import com.bignerdranch.android.pract7.databinding.ItemFurnitureBinding
import com.bignerdranch.android.pract7.utils.UserRole
import com.squareup.picasso.Picasso

class FurnitureAdapter(
    private var furniture: List<FurnitureTypeEntity>,
    private val role: UserRole,
    private val onEdit: (FurnitureTypeEntity) -> Unit,
    private val onDelete: (FurnitureTypeEntity) -> Unit
) : RecyclerView.Adapter<FurnitureAdapter.FurnitureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            FurnitureViewHolder {
        val binding = ItemFurnitureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FurnitureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FurnitureViewHolder, position: Int) {
        holder.bind(furniture[position])
    }

    override fun getItemCount() = furniture.size

    fun update(newFurniture: List<FurnitureTypeEntity>) {
        furniture = newFurniture
        notifyDataSetChanged()
    }

    inner class FurnitureViewHolder(private val binding: ItemFurnitureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(furniture: FurnitureTypeEntity) {
            binding.tvName.text = furniture.name
            Picasso.get().load(furniture.imageUrl).into(binding.ivImage)

            if (role == UserRole.WORKER) {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnEdit.setOnClickListener { onEdit(furniture) }
                binding.btnDelete.setOnClickListener { onDelete(furniture) }
            } else {
                binding.btnEdit.visibility = View.GONE
                binding.btnDelete.visibility = View.GONE
            }
        }
    }
}
