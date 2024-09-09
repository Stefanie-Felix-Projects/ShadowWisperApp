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

class ChatoverviewFragment : Fragment() {

    private lateinit var binding: FragmentChatoverviewBinding
    private val viewModel: ChatOverviewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatoverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChatoverview.isEnabled = false

        viewModel.loadActiveCharacters()

        viewModel.loadCurrentCharacterId()

        viewModel.availableCharacters.observe(viewLifecycleOwner) { availableCharacters ->
            Log.d("Fragment", "Anzahl der verfügbaren Charaktere: ${availableCharacters.size}")

            viewModel.currentCharacterId.observe(viewLifecycleOwner) { currentCharacterId ->
                if (!currentCharacterId.isNullOrEmpty()) {

                    binding.rvChatoverview.isEnabled = true

                    val adapter = ChatOverviewAdapter(
                        availableCharacters = availableCharacters,
                        onCharacterClicked = { character ->
                            val action = ChatoverviewFragmentDirections
                                .actionChatoverviewFragmentToChatdetailFragment(
                                    senderCharacterId = currentCharacterId,
                                    recipientCharacterId = character.characterId
                                )
                            findNavController().navigate(action)
                        }
                    )

                    binding.rvChatoverview.layoutManager = LinearLayoutManager(context)
                    binding.rvChatoverview.adapter = adapter
                } else {
                    Log.e("ChatoverviewFragment", "Aktuelle Character-ID ist leer. Navigation nicht möglich.")
                }
            }
        }
    }
}