/**
 * Fragment-Klasse `ChatoverviewFragment`, die eine Übersicht aller aktiven Charaktere anzeigt, mit denen ein Chat begonnen werden kann.
 * Diese Klasse lädt die verfügbaren Charaktere und ermöglicht es, einen Chat zwischen dem aktuellen Charakter und einem ausgewählten Charakter zu starten.
 */
package com.example.shadowwisper.ui.theme.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shadowwisper.databinding.FragmentChatoverviewBinding
import com.example.shadowwisper.ui.theme.data.adapter.ChatOverviewAdapter
import com.example.shadowwisper.ui.theme.data.view.ChatOverviewViewModel

/**
 * Fragment zur Darstellung einer Übersicht über verfügbare Charaktere für den Start eines Chats.
 * Es lädt die aktiven Charaktere, ermöglicht die Auswahl eines Charakters und navigiert zur Chat-Detailansicht.
 */
class ChatoverviewFragment : Fragment() {

    // Binding-Objekt zur einfachen Referenzierung der UI-Elemente.
    private lateinit var binding: FragmentChatoverviewBinding
    // ViewModel zur Verwaltung der Daten für die Charakterübersicht und Chats.
    private val viewModel: ChatOverviewViewModel by activityViewModels()

    /**
     * Erstellt die View für das Fragment und bindet das Layout an das Fragment.
     */
    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Bindet das Layout mit Hilfe von ViewBinding.
        binding = FragmentChatoverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Wird nach der Erstellung der View aufgerufen und initialisiert die Benutzeroberfläche.
     * Hier wird die RecyclerView für die Chat-Übersicht eingerichtet und das ViewModel zur Verwaltung der verfügbaren Charaktere verwendet.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Deaktiviert die RecyclerView zunächst, bevor die Daten geladen werden.
        binding.rvChatoverview.isEnabled = false

        // Lädt die aktiven Charaktere, die nicht dem aktuellen Benutzer gehören.
        viewModel.loadActiveCharacters()

        // Lädt die aktuelle Charakter-ID des Benutzers.
        viewModel.loadCurrentCharacterId()

        // Beobachtet die Liste der verfügbaren Charaktere, wenn sie aus dem ViewModel geladen wird.
        viewModel.availableCharacters.observe(viewLifecycleOwner) { availableCharacters ->
            Log.d("Fragment", "Anzahl der verfügbaren Charaktere: ${availableCharacters.size}")

            // Beobachtet die aktuelle Charakter-ID des Benutzers.
            viewModel.currentCharacterId.observe(viewLifecycleOwner) { currentCharacterId ->
                if (!currentCharacterId.isNullOrEmpty()) {

                    // Aktiviert die RecyclerView, wenn die Charakter-ID vorhanden ist.
                    binding.rvChatoverview.isEnabled = true

                    // Adapter für die RecyclerView, um die Liste der Charaktere anzuzeigen.
                    val adapter = ChatOverviewAdapter(
                        availableCharacters = availableCharacters,
                        onCharacterClicked = { character ->
                            // Startet einen neuen Chat, wenn ein Charakter ausgewählt wurde.
                            val action = ChatoverviewFragmentDirections
                                .actionChatoverviewFragmentToChatdetailFragment(
                                    senderCharacterId = currentCharacterId,
                                    recipientCharacterId = character.characterId
                                )
                            findNavController().navigate(action)
                        }
                    )

                    // Setzt das Layout und den Adapter für die RecyclerView.
                    binding.rvChatoverview.layoutManager = LinearLayoutManager(context)
                    binding.rvChatoverview.adapter = adapter
                } else {
                    Log.e("ChatoverviewFragment", "Aktuelle Character-ID ist leer. Navigation nicht möglich.")
                }
            }
        }
    }
}