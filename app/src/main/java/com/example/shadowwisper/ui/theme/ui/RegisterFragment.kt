/**
 * Fragment-Klasse `RegisterFragment`, die für die Benutzerregistrierung zuständig ist.
 * Diese Klasse ermöglicht es dem Benutzer, sich mit einer E-Mail und einem Passwort zu registrieren.
 * Bei erfolgreicher Registrierung wird der Benutzer zur Startseite weitergeleitet.
 */
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

/**
 * Fragment zur Verwaltung des Registrierungsprozesses für neue Benutzer.
 * Der Benutzer kann sich mit einer E-Mail und einem Passwort registrieren und nach erfolgreicher Registrierung zur Startseite navigieren.
 */
class RegisterFragment : Fragment() {

    // Binding-Objekt zur einfachen Referenzierung der UI-Elemente.
    private lateinit var binding: FragmentRegisterBinding
    // ViewModel für die Verwaltung des Registrierungsprozesses.
    private val registerViewModel: RegisterViewModel by viewModels()

    /**
     * Erstellt die View für das Fragment und bindet das Layout an das Fragment.
     * @param inflater Das LayoutInflater-Objekt zum Aufblasen des Layouts für dieses Fragment.
     * @param container Das übergeordnete View-Element, in das die Fragment-UI eingebunden wird.
     * @param savedInstanceState Wenn nicht null, wird dieses Fragment mit einem früher gespeicherten Zustand wiederhergestellt.
     * @return Die View für das Fragment-UI, oder null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Bindet das Layout für den Registrierungsbildschirm.
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Wird nach der Erstellung der View aufgerufen und initialisiert die Benutzeroberfläche.
     * Hier wird der Registrierungsprozess überwacht und Klicklistener für die Buttons eingerichtet.
     * @param view Die erstellte View für dieses Fragment.
     * @param savedInstanceState Wenn nicht null, wird dieses Fragment mit einem früher gespeicherten Zustand wiederhergestellt.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Beobachtet das LiveData für den Registrierungserfolg. Bei erfolgreicher Registrierung wird der Benutzer zur Startseite geleitet.
        registerViewModel.registrationSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(requireContext(), "Registrierung erfolgreich!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
            }
        })

        // Beobachtet das LiveData für Registrierungsfehler. Bei einem Fehler wird eine Toast-Nachricht angezeigt.
        registerViewModel.registrationError.observe(viewLifecycleOwner, Observer { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        })

        // Klicklistener für den Button, um zur Login-Seite zurückzukehren.
        binding.btBackToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        // Klicklistener für den Registrierungsbutton. Liest die E-Mail und das Passwort aus und startet den Registrierungsprozess.
        binding.btRegister.setOnClickListener {
            val email = binding.tietEmail.text.toString()
            val password = binding.tietPass.text.toString()
            registerViewModel.register(email, password)
        }
    }
}