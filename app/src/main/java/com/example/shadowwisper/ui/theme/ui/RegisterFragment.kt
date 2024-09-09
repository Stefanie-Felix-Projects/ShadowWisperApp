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
import com.example.shadowwisper.databinding.FragmentRegisterBinding
import com.example.shadowwisper.ui.theme.data.view.RegisterViewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigiere nach erfolgreicher Registrierung zum HomeFragment
        registerViewModel.registrationSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(requireContext(), "Registrierung erfolgreich!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
            }
        })

        // Zeige einen Fehler an, falls die Registrierung fehlschlägt
        registerViewModel.registrationError.observe(viewLifecycleOwner, Observer { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        })

        // Setze den Klicklistener für den Zurück-Button (navigiert zurück zum Login)
        binding.btBackToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        // Setze den Klicklistener für den Registrieren-Button
        binding.btRegister.setOnClickListener {
            val email = binding.tietEmail.text.toString()
            val password = binding.tietPass.text.toString()
            registerViewModel.register(email, password)
        }
    }
}