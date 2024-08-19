package com.example.shadowwisper.ui.theme.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shadowwisper.ui.theme.data.adapter.ChatOverviewAdapter
import com.example.shadowwisper.ui.theme.data.view.ChatViewModel
import com.syntax_institut.whatssyntax.databinding.FragmentChatoverviewBinding

class ChatoverviewFragment : Fragment() {

    private lateinit var binding: FragmentChatoverviewBinding
    private val viewModel: ChatViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatoverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val chatList = viewModel.getChatList()

        val adapter = ChatOverviewAdapter(chatList)
        binding.rvChatoverview.layoutManager = LinearLayoutManager(context)
        binding.rvChatoverview.adapter = adapter
    }
}