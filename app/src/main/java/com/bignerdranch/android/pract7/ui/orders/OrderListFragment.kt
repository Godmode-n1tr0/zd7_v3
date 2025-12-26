package com.bignerdranch.android.pract7.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.pract7.data.db.AppDatabase
import com.bignerdranch.android.pract7.data.entities.ClientEntity
import com.bignerdranch.android.pract7.data.entities.FurnitureTypeEntity
import com.bignerdranch.android.pract7.data.entities.OrderEntity
import com.bignerdranch.android.pract7.databinding.DialogOrderBinding
import com.bignerdranch.android.pract7.databinding.FragmentOrderListBinding
import com.bignerdranch.android.pract7.utils.PrefsManager
import com.bignerdranch.android.pract7.utils.UserRole
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class OrderListFragment : Fragment() {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OrderAdapter
    private lateinit var db: AppDatabase

    private var clientList: List<ClientEntity> = emptyList()
    private var furnitureList: List<FurnitureTypeEntity> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())
        val role = PrefsManager(requireContext()).getRole()

        binding.recyclerOrders.layoutManager = LinearLayoutManager(requireContext())
        adapter = OrderAdapter(mutableListOf())
        binding.recyclerOrders.adapter = adapter

        if (role == UserRole.WORKER) {
            binding.fabAddOrder.visibility = View.VISIBLE
            binding.fabAddOrder.setOnClickListener { addItem() }
        } else {
            binding.fabAddOrder.visibility = View.GONE
        }

        loadInitialData()
    }

    private fun loadInitialData() {
        CoroutineScope(Dispatchers.IO).launch {
            clientList = db.clientDao().getAll()
            furnitureList = db.furnitureDao().getAllTypes()
            val orders = db.orderDao().getOrdersWithFurniture()

            withContext(Dispatchers.Main) {
                adapter.update(orders)
            }
        }
    }

    private fun addItem() {
        if (clientList.isEmpty() || furnitureList.isEmpty()) {
            Toast.makeText(context, "Сначала добавьте клиентов и мебель", Toast.LENGTH_SHORT).show()
            return
        }
        showDialog()
    }

    private fun showDialog() {
        val dialogBinding = DialogOrderBinding.inflate(layoutInflater)

        val clientNames = clientList.map { it.name }
        val furnitureNames = furnitureList.map { it.name }

        val clientAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, clientNames)
        val furnitureAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, furnitureNames)

        dialogBinding.spinnerClients.adapter = clientAdapter
        dialogBinding.spinnerFurniture.adapter = furnitureAdapter

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Создать заказ")
            .setView(dialogBinding.root)
            .setPositiveButton("Сохранить") { _, _ ->
                val selectedClient = clientList[dialogBinding.spinnerClients.selectedItemPosition]
                val selectedFurniture = furnitureList[dialogBinding.spinnerFurniture.selectedItemPosition]

                CoroutineScope(Dispatchers.IO).launch {
                    val orderNumber = UUID.randomUUID().toString()
                    // В реальном приложении здесь была бы логика добавления мебели к заказу через связующую таблицу
                    db.orderDao().insert(OrderEntity(orderNumber = orderNumber, clientId = selectedClient.id))
                    loadInitialData()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
