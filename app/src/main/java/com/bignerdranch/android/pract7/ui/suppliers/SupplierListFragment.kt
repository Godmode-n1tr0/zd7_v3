package com.bignerdranch.android.pract7.ui.suppliers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.pract7.data.db.AppDatabase
import com.bignerdranch.android.pract7.data.entities.SupplierEntity
import com.bignerdranch.android.pract7.databinding.DialogSupplierBinding
import com.bignerdranch.android.pract7.databinding.FragmentSupplierListBinding
import com.bignerdranch.android.pract7.utils.PrefsManager
import com.bignerdranch.android.pract7.utils.UserRole
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SupplierListFragment : Fragment() {

    private var _binding: FragmentSupplierListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SupplierAdapter
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupplierListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())
        val role = PrefsManager(requireContext()).getRole()

        binding.recyclerSuppliers.layoutManager = LinearLayoutManager(requireContext())

        adapter = SupplierAdapter(mutableListOf(), role,
            onEdit = { editItem(it) },
            onDelete = { deleteItem(it) }
        )
        binding.recyclerSuppliers.adapter = adapter

        if (role == UserRole.WORKER) {
            binding.fabAddSupplier.visibility = View.VISIBLE
            binding.fabAddSupplier.setOnClickListener { addItem() }
        } else {
            binding.fabAddSupplier.visibility = View.GONE
        }

        loadSuppliers()
    }

    private fun loadSuppliers() {
        CoroutineScope(Dispatchers.IO).launch {
            val suppliers = db.supplierDao().getAll()
            withContext(Dispatchers.Main) {
                adapter.update(suppliers)
            }
        }
    }

    private fun addItem() {
        showDialog(null)
    }

    private fun editItem(supplier: SupplierEntity) {
        showDialog(supplier)
    }

    private fun deleteItem(supplier: SupplierEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            db.supplierDao().delete(supplier)
            loadSuppliers()
        }
    }

    private fun showDialog(supplier: SupplierEntity?) {
        val dialogBinding = DialogSupplierBinding.inflate(layoutInflater)

        supplier?.let {
            dialogBinding.etName.setText(it.name)
            dialogBinding.etEmail.setText(it.email)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (supplier == null) "Добавить поставщика" else "Редактировать")
            .setView(dialogBinding.root)
            .setPositiveButton("Сохранить") { _, _ ->
                val name = dialogBinding.etName.text.toString()
                val email = dialogBinding.etEmail.text.toString()

                if (name.isBlank() || email.isBlank()) {
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                CoroutineScope(Dispatchers.IO).launch {
                    if (supplier == null) {
                        db.supplierDao().insert(SupplierEntity(name = name, email = email))
                    } else {
                        db.supplierDao().update(supplier.copy(name = name, email = email))
                    }
                    loadSuppliers()
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
