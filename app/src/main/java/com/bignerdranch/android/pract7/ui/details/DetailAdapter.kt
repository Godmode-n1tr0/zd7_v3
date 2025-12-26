package com.bignerdranch.android.pract7.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.pract7.data.entities.DetailTypeEntity
import com.bignerdranch.android.pract7.databinding.ItemDetailBinding
import com.bignerdranch.android.pract7.utils.UserRole

class DetailAdapter(
    private var details: List<DetailTypeEntity>,
    private val role: UserRole,
    private val onEdit: (DetailTypeEntity) -> Unit,
    private val onDelete: (DetailTypeEntity) -> Unit
) : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            DetailViewHolder {
        val binding = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(details[position])
    }

    override fun getItemCount() = details.size

    fun update(newDetails: List<DetailTypeEntity>) {
        details = newDetails
        notifyDataSetChanged()
    }

    inner class DetailViewHolder(private val binding: ItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(detail: DetailTypeEntity) {
            binding.tvName.text = "Название: ${detail.name}"
            binding.tvCategory.text = "Категория: ${detail.category}"
            binding.tvSpecs.text = "Характеристики: ${detail.specifications}"

            if (role == UserRole.WORKER || role == UserRole.SUPPLIER) {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnEdit.setOnClickListener { onEdit(detail) }
                binding.btnDelete.setOnClickListener { onDelete(detail) }
            } else {
                binding.btnEdit.visibility = View.GONE
                binding.btnDelete.visibility = View.GONE
            }
        }
    }
}
