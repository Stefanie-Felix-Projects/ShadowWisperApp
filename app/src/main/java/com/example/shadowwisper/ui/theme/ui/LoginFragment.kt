/**
 * Fragment-Klasse `LoginFragment`, die für die Verwaltung des Anmeldevorgangs verantwortlich ist.
 * Diese Klasse verwendet das `LoginViewModel`, um den Benutzer über Firebase Authentication anzumelden.
 * Sie ermöglicht es dem Benutzer, sich einzuloggen, und zeigt Fehlermeldungen bei fehlerhaften Anmeldungen an.
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
import com.example.shadowwisper.databinding.FragmentLoginBinding
import com.example.shadowwisper.ui.theme.data.view.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * Fragment zur Verwaltung des Login-Bildschirms.
 * Hier kann sich der Benutzer mit E-Mail und Passwort anmelden oder zur Registrierungsseite navigieren.
 * Bei erfolgreicher Anmeldung wird der Benutzer zur Startseite weitergeleitet.
 */
class LoginFragment : Fragment() {

    // ViewBinding zur einfachen Referenzierung der UI-Elemente.
    private lateinit var binding: FragmentLoginBinding
    // ViewModel für die Verwaltung des Login-Prozesses.
    private val loginViewModel: LoginViewModel by viewModels()

    /**
     * Erstellt die View für das Fragment und bindet das Layout an das Fragment.
     * Wenn der Benutzer bereits angemeldet ist, wird er direkt zur Startseite weitergeleitet.
     * @param inflater Das LayoutInflater-Objekt zum Aufblasen des Layouts für dieses Fragment.
     * @param container Das übergeordnete View-Element, in das die Fragment-UI eingebunden wird.
     * @param savedInstanceState Wenn nicht null, wird dieses Fragment mit einem früher gespeicherten Zustand wiederhergestellt.
     * @return Die View für das Fragment-UI, oder null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Überprüft, ob der Benutzer bereits angemeldet ist. Wenn ja, navigiert er zur Startseite.
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        // Bindet das Layout für den Login-Bildschirm.
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Wird nach der Erstellung der View aufgerufen und initialisiert die Benutzeroberfläche.
     * Hier wird der Login-Prozess beobachtet, und es werden Klicklistener für die Anmelde- und Registrierungsbuttons gesetzt.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Beobachtet das LiveData für den Anmeldeerfolg. Bei erfolgreicher Anmeldung wird zur Startseite navigiert.
        loginViewModel.loginSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        })

        // Beobachtet das LiveData für Anmeldefehler. Bei einem Fehler wird eine Toast-Nachricht angezeigt.
        loginViewModel.loginError.observe(viewLifecycleOwner, Observer { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        })

        // Klicklistener für den Login-Button. Liest die E-Mail und das Passwort aus und startet den Login-Prozess.
        binding.btLogin.setOnClickListener {
            val email = binding.tietEmail.text.toString()
            val password = binding.tietPass.text.toString()
            loginViewModel.login(email, password)
        }

        // Klicklistener für den Button zur Registrierung. Navigiert zur Registrierungsseite.
        binding.btToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}