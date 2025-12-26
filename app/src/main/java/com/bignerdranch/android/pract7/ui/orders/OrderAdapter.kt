package com.bignerdranch.android.pract7.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.pract7.data.models.OrderWithFurniture
import com.bignerdranch.android.pract7.databinding.ItemOrderBinding

class OrderAdapter(
    private var orders: List<OrderWithFurniture>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount() = orders.size

    fun update(newOrders: List<OrderWithFurniture>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(orderWithFurniture: OrderWithFurniture) {
            binding.tvOrderId.text = "Заказ №${orderWithFurniture.order.orderNumber}"
            binding.tvClientId.text = "Клиент ID: ${orderWithFurniture.order.clientId}"
            binding.tvFurnitureList.text = orderWithFurniture.furniture.joinToString { it.name }
        }
    }
}
