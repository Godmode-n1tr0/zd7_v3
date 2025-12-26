package com.bignerdranch.android.pract7.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.pract7.R
import com.bignerdranch.android.pract7.databinding.FragmentLoginBinding
import com.bignerdranch.android.pract7.utils.PrefsManager
import com.bignerdranch.android.pract7.utils.UserRole

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = PrefsManager(requireContext())

        binding.btnLogin.setOnClickListener {
            if (validateInput()) {
                val selectedRoleId = binding.roleRadioGroup.checkedRadioButtonId
                val role = when (selectedRoleId) {
                    R.id.radioWorker -> UserRole.WORKER
                    R.id.radioClient -> UserRole.CLIENT
                    R.id.radioSupplier -> UserRole.SUPPLIER
                    else -> UserRole.CLIENT
                }

                prefs.saveUser(binding.etLogin.text.toString(), binding.etEmail.text.toString(), role)
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }
        }
    }

    private fun validateInput(): Boolean {
        binding.etLogin.error = null
        binding.etEmail.error = null
        binding.etPassword.error = null

        val login = binding.etLogin.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (login.isEmpty()) {
            binding.etLogin.error = "Поле не может быть пустым"
            return false
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "Поле не может быть пустым"
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Введите корректный email"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Поле не может быть пустым"
            return false
        }

        if (password.length < 6) {
            binding.etPassword.error = "Пароль должен быть не менее 6 символов"
            return false
        }

        if (binding.roleRadioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(context, "Выберите роль", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
