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

class CharacteroverviewFragment : Fragment() {

    private lateinit var binding: FragmentCharacteroverviewBinding
    private val viewModel: CharacterOverviewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacteroverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setze den Klicklistener für den "Add Character"-Button
        binding.btnAddCharacter.setOnClickListener {
            val action = CharacteroverviewFragmentDirections
                .actionChatoverviewFragmentToChatdetailFragment("","")
            findNavController().navigate(action)
        }

        // Beobachte die Liste der Charaktere
        viewModel.userCharacters.observe(viewLifecycleOwner) { characterList ->
            if (characterList.isEmpty()) {
                // Wenn keine Charaktere vorhanden sind, verstecke den RecyclerView
                binding.rvChar.visibility = View.GONE
            } else {
                // Wenn Charaktere vorhanden sind, zeige den RecyclerView
                binding.rvChar.visibility = View.VISIBLE

                // Adapter initialisieren und binden
                val adapter = CharacterOverviewAdapter(characterList, { selectedCharacter ->
                    val action = CharacteroverviewFragmentDirections
                        .actionChatoverviewFragmentToChatdetailFragment(selectedCharacter.characerId, "")
                    findNavController().navigate(action)
                }, { character, isChecked ->
                    viewModel.setActiveCharacter(character)
                })

                binding.rvChar.layoutManager = LinearLayoutManager(context)
                binding.rvChar.adapter = adapter
            }
        }
    }
}