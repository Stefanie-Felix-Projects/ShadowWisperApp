package com.example.shadowwisper.ui.theme.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shadowwisper.ui.theme.data.adapter.ChatOverviewAdapter
import com.example.shadowwisper.ui.theme.data.model.ChatDetail
import com.syntax_institut.whatssyntax.R
import com.syntax_institut.whatssyntax.databinding.FragmentChatoverviewBinding

class ChatoverviewFragment : Fragment() {

    private lateinit var binding: FragmentChatoverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatoverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Beispielhafte Liste von ChatDetails
        val chatList = listOf(
            ChatDetail("1", "Character 1", "Message from Character 1", R.drawable.hex17jpg, 1627848492000L),
            ChatDetail("2", "Character 2", "Message from Character 2", R.drawable.hex17jpg, 1627848492000L),
            ChatDetail("3", "Character 3", "Message from Character 3", R.drawable.hex17jpg, 1627848492000L)
        )

        val adapter = ChatOverviewAdapter(chatList) { chatDetail ->
            val action = ChatoverviewFragmentDirections.actionChatoverviewFragmentToChatdetailFragment(chatDetail.name)
            findNavController().navigate(action)
        }

        binding.rvChatoverview.layoutManager = LinearLayoutManager(context)
        binding.rvChatoverview.adapter = adapter
    }
}