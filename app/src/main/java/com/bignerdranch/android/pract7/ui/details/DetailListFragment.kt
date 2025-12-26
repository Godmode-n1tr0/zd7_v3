package com.bignerdranch.android.pract7.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.pract7.data.db.AppDatabase
import com.bignerdranch.android.pract7.data.entities.DetailTypeEntity
import com.bignerdranch.android.pract7.databinding.DialogDetailBinding
import com.bignerdranch.android.pract7.databinding.FragmentDetailListBinding
import com.bignerdranch.android.pract7.utils.PrefsManager
import com.bignerdranch.android.pract7.utils.UserRole
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailListFragment : Fragment() {

    private var _binding: FragmentDetailListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: DetailAdapter
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())
        val role = PrefsManager(requireContext()).getRole()

        binding.recyclerDetails.layoutManager = LinearLayoutManager(requireContext())

        adapter = DetailAdapter(mutableListOf(), role,
            onEdit = { editItem(it) },
            onDelete = { deleteItem(it) }
        )
        binding.recyclerDetails.adapter = adapter

        if (role == UserRole.WORKER || role == UserRole.SUPPLIER) {
            binding.fabAddDetail.visibility = View.VISIBLE
            binding.fabAddDetail.setOnClickListener { addItem() }
        } else {
            binding.fabAddDetail.visibility = View.GONE
        }

        loadDetails()
    }

    private fun loadDetails() {
        CoroutineScope(Dispatchers.IO).launch {
            val details = db.detailDao().getAllDetailTypes()
            withContext(Dispatchers.Main) {
                adapter.update(details)
            }
        }
    }

    private fun addItem() {
        showDialog(null)
    }

    private fun editItem(detail: DetailTypeEntity) {
        showDialog(detail)
    }

    private fun deleteItem(detail: DetailTypeEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            db.detailDao().delete(detail)
            loadDetails()
        }
    }

    private fun showDialog(detail: DetailTypeEntity?) {
        val dialogBinding = DialogDetailBinding.inflate(layoutInflater)

        detail?.let {
            dialogBinding.etName.setText(it.name)
            dialogBinding.etCategory.setText(it.category)
            dialogBinding.etSpecs.setText(it.specifications)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (detail == null) "Добавить деталь" else "Редактировать")
            .setView(dialogBinding.root)
            .setPositiveButton("Сохранить") { _, _ ->
                val name = dialogBinding.etName.text.toString()
                val category = dialogBinding.etCategory.text.toString()
                val specs = dialogBinding.etSpecs.text.toString()

                if (name.isBlank() || category.isBlank() || specs.isBlank()) {
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                CoroutineScope(Dispatchers.IO).launch {
                    if (detail == null) {
                        db.detailDao().insert(DetailTypeEntity(name = name, category = category, specifications = specs))
                    } else {
                        db.detailDao().update(detail.copy(name = name, category = category, specifications = specs))
                    }
                    loadDetails()
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
