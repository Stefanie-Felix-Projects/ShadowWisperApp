package com.example.shadowwisper.ui.theme.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shadowwisper.databinding.FragmentChatdetailBinding
import com.example.shadowwisper.ui.theme.data.adapter.ChatDetailAdapter
import com.example.shadowwisper.ui.theme.data.model.ChatMessage
import com.example.shadowwisper.ui.theme.data.model.ChatRoom
import com.example.shadowwisper.ui.theme.data.view.ChatDetailViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

class ChatdetailFragment : Fragment() {

    private lateinit var binding: FragmentChatdetailBinding
    private val viewModel: ChatDetailViewModel by activityViewModels()
    private val args: ChatdetailFragmentArgs by navArgs()

    private lateinit var adapter: ChatDetailAdapter
    private lateinit var chatRoom: ChatRoom
    private lateinit var chatRoomId: String
    private val currentUserId: String by lazy {
        FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Überprüfen, ob die Character-IDs gültig sind
        if (args.senderCharacterId.isNullOrEmpty() || args.recipientCharacterId.isNullOrEmpty()) {
            throw IllegalArgumentException("Invalid sender or recipient character ID. They cannot be empty.")
        }

        // Erstelle oder lade die ChatRoom-ID
        chatRoomId = if (args.senderCharacterId < args.recipientCharacterId) {
            "${args.senderCharacterId}${args.recipientCharacterId}"
        } else {
            "${args.recipientCharacterId}${args.senderCharacterId}"
        }

        // Fortfahren mit der Initialisierung, nachdem die chatRoomId gültig ist
        initializeChatRoomAndMessages()
    }

    private fun initializeChatRoomAndMessages() {
        // Initialisiere ChatRoom oder lade es, falls es bereits existiert
        chatRoom = ChatRoom(
            lastActivityTimestamp = Timestamp.now(),
            participants = listOf(args.senderCharacterId, args.recipientCharacterId),  // Character-IDs von Sender und Empfänger
            chatRoomId = chatRoomId,
            chatRoomName = "Chat between ${args.senderCharacterId} and ${args.recipientCharacterId}",
            lastMessage = "",
            lastMessageSenderId = "",
            messages = mutableListOf(),
            userId = currentUserId,  // userId bleibt unverändert
            characterId = args.senderCharacterId  // Die Character-ID des aktuellen Charakters (Sender)
        )

        // Initialisiere den Adapter und die RecyclerView
        adapter = ChatDetailAdapter(chatRoom, currentUserId)
        binding.rvMessages.layoutManager = LinearLayoutManager(context)
        binding.rvMessages.adapter = adapter

        // Nachrichten beobachten und aktualisieren
        viewModel.chatMessages.observe(viewLifecycleOwner) { messages ->
            chatRoom.messages.clear()
            chatRoom.messages.addAll(messages)
            chatRoom.lastMessage = messages.lastOrNull()?.message ?: ""
            chatRoom.lastMessageSenderId = messages.lastOrNull()?.senderId ?: ""

            adapter.notifyDataSetChanged()
            binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
        }

        // Nachricht senden
        binding.btSend.setOnClickListener {
            val messageText = binding.tietMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val newMessage = ChatMessage(
                    senderId = args.senderCharacterId,
                    message = messageText,
                    timestamp = Timestamp.now(),
                    chatRoomId = chatRoomId,
                    messageId = UUID.randomUUID().toString(),
                    messageStatus = "sent"
                )
                viewModel.sendMessage(chatRoomId, newMessage)
                binding.tietMessage.text?.clear()
            }
        }

        // Nachrichten laden
        viewModel.loadMessagesForChatRoom(chatRoomId)
    }
}