package com.bignerdranch.android.pract7.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.pract7.R
import com.bignerdranch.android.pract7.databinding.FragmentRoleSelectBinding
import com.bignerdranch.android.pract7.utils.PrefsManager
import com.bignerdranch.android.pract7.utils.UserRole

class RoleSelectFragment : Fragment() {

    private var _binding: FragmentRoleSelectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoleSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = PrefsManager(requireContext())
        val role = prefs.getRole()

        binding.btnClients.visibility = View.GONE
        binding.btnFurniture.visibility = View.GONE
        binding.btnSuppliers.visibility = View.GONE
        binding.btnDetails.visibility = View.GONE
        binding.btnOrders.visibility = View.GONE

        when (role) {
            UserRole.WORKER -> {
                binding.btnClients.visibility = View.VISIBLE
                binding.btnFurniture.visibility = View.VISIBLE
                binding.btnSuppliers.visibility = View.VISIBLE
                binding.btnDetails.visibility = View.VISIBLE
                binding.btnOrders.visibility = View.VISIBLE
            }
            UserRole.CLIENT -> {
                binding.btnFurniture.visibility = View.VISIBLE
                binding.btnOrders.visibility = View.VISIBLE
            }
            UserRole.SUPPLIER -> {
                binding.btnDetails.visibility = View.VISIBLE
            }
            else -> {}
        }

        binding.btnFurniture.setOnClickListener {
            findNavController().navigate(R.id.action_roleSelectFragment_to_furnitureListFragment)
        }

        binding.btnClients.setOnClickListener {
            findNavController().navigate(R.id.action_roleSelectFragment_to_clientListFragment)
        }

        binding.btnSuppliers.setOnClickListener {
            findNavController().navigate(R.id.action_roleSelectFragment_to_supplierListFragment)
        }

        binding.btnDetails.setOnClickListener {
            findNavController().navigate(R.id.action_roleSelectFragment_to_detailListFragment)
        }

        binding.btnOrders.setOnClickListener {
            findNavController().navigate(R.id.action_roleSelectFragment_to_orderListFragment)
        }

        binding.btnLogout.setOnClickListener {
            prefs.clear()
            findNavController().navigate(R.id.action_roleSelectFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
