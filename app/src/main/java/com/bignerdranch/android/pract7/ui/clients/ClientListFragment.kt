package com.bignerdranch.android.pract7.ui.clients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.pract7.data.db.AppDatabase
import com.bignerdranch.android.pract7.data.entities.ClientEntity
import com.bignerdranch.android.pract7.databinding.DialogClientBinding
import com.bignerdranch.android.pract7.databinding.FragmentClientListBinding
import com.bignerdranch.android.pract7.utils.PrefsManager
import com.bignerdranch.android.pract7.utils.UserRole
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClientListFragment : Fragment() {

    private var _binding: FragmentClientListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ClientAdapter
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())
        val role = PrefsManager(requireContext()).getRole()

        binding.recyclerClients.layoutManager = LinearLayoutManager(requireContext())

        adapter = ClientAdapter(mutableListOf(), role,
            onEdit = { editItem(it) },
            onDelete = { deleteItem(it) }
        )
        binding.recyclerClients.adapter = adapter

        if (role == UserRole.WORKER) {
            binding.fabAddClient.visibility = View.VISIBLE
            binding.fabAddClient.setOnClickListener { addItem() }
        } else {
            binding.fabAddClient.visibility = View.GONE
        }

        loadClients()
    }

    private fun loadClients() {
        CoroutineScope(Dispatchers.IO).launch {
            val clients = db.clientDao().getAll()
            withContext(Dispatchers.Main) {
                adapter.update(clients)
            }
        }
    }

    private fun addItem() {
        showDialog(null)
    }

    private fun editItem(client: ClientEntity) {
        showDialog(client)
    }

    private fun deleteItem(client: ClientEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            db.clientDao().delete(client)
            loadClients()
        }
    }

    private fun showDialog(client: ClientEntity?) {
        val dialogBinding = DialogClientBinding.inflate(layoutInflater)

        client?.let {
            dialogBinding.etName.setText(it.name)
            dialogBinding.etEmail.setText(it.email)
            dialogBinding.etDiscount.setText(it.discount.toString())
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (client == null) "Добавить клиента" else "Редактировать")
            .setView(dialogBinding.root)
            .setPositiveButton("Сохранить") { _, _ ->
                val name = dialogBinding.etName.text.toString()
                val email = dialogBinding.etEmail.text.toString()
                val discount = dialogBinding.etDiscount.text.toString().toIntOrNull() ?: 0

                if (name.isBlank() || email.isBlank()) {
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                CoroutineScope(Dispatchers.IO).launch {
                    if (client == null) {
                        db.clientDao().insert(ClientEntity(name = name, email = email, discount = discount))
                    } else {
                        db.clientDao().update(client.copy(name = name, email = email, discount = discount))
                    }
                    loadClients()
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
