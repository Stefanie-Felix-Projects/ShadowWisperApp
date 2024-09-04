package com.example.shadowwisper.ui.theme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shadowwisper.databinding.FragmentChatoverviewBinding
import com.example.shadowwisper.ui.theme.data.adapter.ChatOverviewAdapter
import com.example.shadowwisper.ui.theme.data.view.ChatViewModel

class ChatoverviewFragment : Fragment() {

    private lateinit var binding: FragmentChatoverviewBinding
    private val viewModel: ChatViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatoverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chatList.observe(viewLifecycleOwner) { chatList ->
            val adapter = ChatOverviewAdapter(chatList) { selectedCharacter ->
                val action = ChatoverviewFragmentDirections
                    .actionChatoverviewFragmentToChatdetailFragment(selectedCharacter.id)
                findNavController().navigate(action)
            }
            binding.rvChatoverview.layoutManager = LinearLayoutManager(context)
            binding.rvChatoverview.adapter = adapter
        }
    }
}