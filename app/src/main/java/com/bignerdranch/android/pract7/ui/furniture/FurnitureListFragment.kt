package com.bignerdranch.android.pract7.ui.furniture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.pract7.data.db.AppDatabase
import com.bignerdranch.android.pract7.data.entities.FurnitureTypeEntity
import com.bignerdranch.android.pract7.databinding.DialogFurnitureBinding
import com.bignerdranch.android.pract7.databinding.FragmentFurnitureListBinding
import com.bignerdranch.android.pract7.utils.PrefsManager
import com.bignerdranch.android.pract7.utils.UserRole
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FurnitureListFragment : Fragment() {

    private var _binding: FragmentFurnitureListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FurnitureAdapter
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFurnitureListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())
        val role = PrefsManager(requireContext()).getRole()

        binding.recyclerFurniture.layoutManager = LinearLayoutManager(requireContext())

        adapter = FurnitureAdapter(mutableListOf(), role,
            onEdit = { editItem(it) },
            onDelete = { deleteItem(it) }
        )
        binding.recyclerFurniture.adapter = adapter

        if (role == UserRole.WORKER) {
            binding.fabAdd.visibility = View.VISIBLE
            binding.fabAdd.setOnClickListener { addItem() }
        } else {
            binding.fabAdd.visibility = View.GONE
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchData(it) }
                return true
            }
        })

        loadData()
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Data is pre-populated from DatabaseCallback, so we just load it
                val list = db.furnitureDao().getAllTypes()
                withContext(Dispatchers.Main) {
                    adapter.update(list)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error loading data: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun searchData(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = db.furnitureDao().search(query)
            withContext(Dispatchers.Main) {
                adapter.update(list)
            }
        }
    }

    private fun addItem() {
        showDialog(null)
    }

    private fun editItem(item: FurnitureTypeEntity) {
        showDialog(item)
    }

    private fun deleteItem(item: FurnitureTypeEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            db.furnitureDao().delete(item)
            loadData()
        }
    }

    private fun showDialog(item: FurnitureTypeEntity?) {
        val dialogBinding = DialogFurnitureBinding.inflate(layoutInflater)

        item?.let {
            dialogBinding.etName.setText(it.name)
            dialogBinding.etUrl.setText(it.imageUrl)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (item == null) "Добавить мебель" else "Редактировать")
            .setView(dialogBinding.root)
            .setPositiveButton("Сохранить") { _, _ ->
                val name = dialogBinding.etName.text.toString()
                val url = dialogBinding.etUrl.text.toString()

                if (name.isBlank() || url.isBlank()) {
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                CoroutineScope(Dispatchers.IO).launch {
                    if (item == null) {
                        db.furnitureDao().insert(FurnitureTypeEntity(name = name, imageUrl = url))
                    } else {
                        db.furnitureDao().update(item.copy(name = name, imageUrl = url))
                    }
                    loadData()
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
