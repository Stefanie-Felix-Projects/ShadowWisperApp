/**
 * Fragment-Klasse `OrdercompletionFragment`, die für die Darstellung der Abschlussseite eines Auftrags zuständig ist.
 * Der Benutzer kann von dieser Seite aus zur Wallet-Ansicht navigieren, um weitere Details zu sehen.
 */
package com.example.shadowwisper.ui.theme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shadowwisper.R
import com.example.shadowwisper.databinding.FragmentOrdercompletionBinding

/**
 * Fragment zur Darstellung der Seite nach Abschluss eines Auftrags.
 * Diese Seite bietet dem Benutzer die Möglichkeit, nach Abschluss eines Auftrags zur Wallet-Ansicht zu navigieren.
 */
class OrdercompletionFragment : Fragment() {

    // Binding-Objekt zur einfachen Referenzierung der UI-Elemente.
    private lateinit var binding: FragmentOrdercompletionBinding

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
    ): View? {
        // Bindet das Layout für das Ordercompletion-Fragment.
        binding = FragmentOrdercompletionBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Wird nach der Erstellung der View aufgerufen und initialisiert die Benutzeroberfläche.
     * Hier wird der Klicklistener für den Button gesetzt, um zur Wallet-Ansicht zu navigieren.
     * @param view Die erstellte View für dieses Fragment.
     * @param savedInstanceState Wenn nicht null, wird dieses Fragment mit einem früher gespeicherten Zustand wiederhergestellt.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Klicklistener für den Button, um nach Abschluss eines Auftrags zur Wallet-Ansicht zu navigieren.
        binding.loginButton.setOnClickListener {
            // Navigiert zur Wallet-Ansicht.
            findNavController().navigate(R.id.action_ordercompletionFragment_to_walletFragment)
        }
    }
}