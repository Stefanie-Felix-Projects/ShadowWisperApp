/**
 * Fragment-Klasse `CharacteroverviewFragment`, die eine Übersicht über alle erstellten Charaktere anzeigt.
 * Der Benutzer kann neue Charaktere hinzufügen oder bestehende Charaktere ansehen und bearbeiten.
 * Das Fragment verwendet ein RecyclerView-Adapter, um die Liste der Charaktere anzuzeigen.
 */
package com.example.shadowwisper.ui.theme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shadowwisper.databinding.FragmentCharacteroverviewBinding
import com.example.shadowwisper.ui.theme.data.adapter.CharacterOverviewAdapter
import com.example.shadowwisper.ui.theme.data.view.CharacterOverviewViewModel

/**
 * Fragment zur Darstellung einer Charakterübersicht.
 * Die Liste der Charaktere wird in einem RecyclerView angezeigt. Der Benutzer kann einen Charakter hinzufügen, bearbeiten oder aktivieren.
 */
class CharacteroverviewFragment : Fragment() {

    // Binding-Objekt zur einfachen Referenzierung der UI-Elemente.
    private lateinit var binding: FragmentCharacteroverviewBinding
    // ViewModel für die Verwaltung der Charakterübersichtsdaten.
    private val viewModel: CharacterOverviewViewModel by activityViewModels()

    /**
     * Erstellt die View des Fragments, indem das Layout für die Charakterübersicht aufgeblasen wird.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacteroverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Wird nach der Erstellung der View aufgerufen und initialisiert die Benutzeroberfläche.
     * Hier wird der Button zum Hinzufügen von Charakteren konfiguriert und die Liste der Charaktere in einem RecyclerView angezeigt.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Klicklistener für den Button zum Hinzufügen eines neuen Charakters.
        binding.btnAddCharacter.setOnClickListener {
            val action = CharacteroverviewFragmentDirections
                .actionCharacteroverviewFragmentToCharacterdetailFragment(null)
            findNavController().navigate(action) // Navigiert zum Fragment zum Hinzufügen eines neuen Charakters.
        }

        // Beobachtet die Liste der Charaktere im ViewModel und aktualisiert die UI entsprechend.
        viewModel.userCharacters.observe(viewLifecycleOwner) { characterList ->
            if (characterList.isEmpty()) {
                binding.rvChar.visibility = View.GONE // Versteckt die RecyclerView, wenn keine Charaktere vorhanden sind.
            } else {
                binding.rvChar.visibility = View.VISIBLE // Zeigt die RecyclerView an, wenn Charaktere vorhanden sind.

                // Adapter für das RecyclerView, der die Liste der Charaktere verwaltet.
                val adapter = CharacterOverviewAdapter(characterList, { selectedCharacter ->
                    // Klicklistener für die Auswahl eines Charakters, navigiert zur Charakterdetailansicht.
                    val action = CharacteroverviewFragmentDirections
                        .actionCharacteroverviewFragmentToCharacterdetailFragment(selectedCharacter.characerId)
                    findNavController().navigate(action)
                }, { character, isChecked ->
                    // Aktiviert den ausgewählten Charakter.
                    viewModel.setActiveCharacter(character)
                })

                // Initialisiert das Layout des RecyclerView als LinearLayout und setzt den Adapter.
                binding.rvChar.layoutManager = LinearLayoutManager(context)
                binding.rvChar.adapter = adapter
            }
        }
    }
}