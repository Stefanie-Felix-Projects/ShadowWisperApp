package com.example.shadowwisper.ui.theme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.shadowwisper.R
import com.example.shadowwisper.databinding.FragmentLoginBinding
import com.example.shadowwisper.ui.theme.data.view.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.loginSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        })

        loginViewModel.loginError.observe(viewLifecycleOwner, Observer { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        })

        binding.btLogin.setOnClickListener {
            val email = binding.tietEmail.text.toString()
            val password = binding.tietPass.text.toString()
            loginViewModel.login(email, password)
        }

        binding.btToRegister.setOnClickListener {
            findNavController().navigate(R.id. action_loginFragment_to_registerFragment)
        }


        binding.btToReset.setOnClickListener {

        }
    }
}