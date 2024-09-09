package com.example.shadowwisper.ui.theme.ui

import android.os.Bundle
import android.util.Log
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
import java.util.UUID

class ChatdetailFragment : Fragment() {

    private lateinit var binding: FragmentChatdetailBinding
    private val viewModel: ChatDetailViewModel by activityViewModels()
    private val args: ChatdetailFragmentArgs by navArgs()  // Empfange Argumente

    private lateinit var adapter: ChatDetailAdapter
    private lateinit var chatRoom: ChatRoom
    private lateinit var chatRoomId: String
    private lateinit var senderId: String  // Sende-ID Variable
    private lateinit var recipientId: String  // Empfänger-ID Variable

    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("ChatdetailFragment", "onViewCreated wurde aufgerufen")

        // Argumente aus NavArgs empfangen
        senderId = args.senderCharacterId
        recipientId = args.recipientCharacterId

        Log.d("ChatdetailFragment", "Sender ID: $senderId, Empfänger ID: $recipientId")

        // Erstelle oder lade die ChatRoom-ID
        chatRoomId = if (senderId < recipientId) {
            "$senderId$recipientId"
        } else {
            "$recipientId$senderId"
        }
        Log.d("ChatdetailFragment", "ChatRoom ID: $chatRoomId")

        // Initialisiere den ChatRoom und Nachrichten
        initializeChatRoomAndMessages()
    }

    private fun initializeChatRoomAndMessages() {
        Log.d("ChatdetailFragment", "initializeChatRoomAndMessages wird ausgeführt")

        if (::senderId.isInitialized && ::recipientId.isInitialized) {
            // Initialisiere den ChatRoom
            chatRoom = ChatRoom(
                lastActivityTimestamp = Timestamp.now(),
                participants = listOf(senderId, recipientId),
                chatRoomId = chatRoomId,
                chatRoomName = "Chat between $senderId and $recipientId",
                lastMessage = "",
                lastMessageSenderId = "",
                messages = mutableListOf(),
                userId = senderId,
                senderId = senderId,
                recipientId = recipientId,
                characterId = senderId
            )

            // Lade vorhandene Nachrichten
            viewModel.loadMessagesForChatRoom(chatRoomId)

            // Nachrichtenänderungen beobachten
            viewModel.chatMessages.observe(viewLifecycleOwner) { messages ->
                Log.d("ChatdetailFragment", "Nachrichten empfangen: ${messages.size} Nachrichten")

                if (messages.isEmpty()) {
                    Log.d("ChatdetailFragment", "Chatroom existiert nicht, erstelle neuen Chatroom.")
                    viewModel.createChatRoom(chatRoom) {
                        Log.d("ChatdetailFragment", "Chatroom erfolgreich erstellt")
                    }
                } else {
                    chatRoom.messages.clear()
                    chatRoom.messages.addAll(messages)
                    chatRoom.lastMessage = messages.lastOrNull()?.message ?: ""
                    chatRoom.lastMessageSenderId = messages.lastOrNull()?.senderId ?: ""

                    adapter.notifyDataSetChanged()
                    binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
                }
            }

            // Adapter setzen
            adapter = ChatDetailAdapter(chatRoom, senderId)  // Verwende senderId
            binding.rvMessages.layoutManager = LinearLayoutManager(context)
            binding.rvMessages.adapter = adapter

            // Nachricht senden
            binding.btSend.setOnClickListener {
                val messageText = binding.tietMessage.text.toString().trim()
                Log.d("ChatdetailFragment", "Sende Button geklickt - Nachricht: $messageText")
                if (messageText.isNotEmpty()) {
                    val newMessage = ChatMessage(
                        senderId = senderId,
                        recipientId = recipientId,
                        message = messageText,
                        timestamp = Timestamp.now(),
                        chatRoomId = chatRoomId,
                        messageId = UUID.randomUUID().toString(),
                        messageStatus = "sent"
                    )

                    Log.d("ChatdetailFragment", "Neue Nachricht erstellt: $newMessage")
                    viewModel.sendMessage(chatRoomId, newMessage)
                    binding.tietMessage.text?.clear()
                }
            }
        } else {
            Log.e("ChatdetailFragment", "Sender oder Empfänger wurden nicht korrekt initialisiert")
        }
    }
}