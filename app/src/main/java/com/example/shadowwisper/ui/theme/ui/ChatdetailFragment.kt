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
import com.example.shadowwisper.ui.theme.data.repository.CharacterDetailRepository
import com.google.firebase.Timestamp
import java.util.UUID

class ChatdetailFragment : Fragment() {

    private lateinit var binding: FragmentChatdetailBinding
    private val viewModel: ChatDetailViewModel by activityViewModels()
    private val args: ChatdetailFragmentArgs by navArgs()

    private lateinit var adapter: ChatDetailAdapter
    private lateinit var chatRoom: ChatRoom
    private lateinit var chatRoomId: String
    private lateinit var senderId: String
    private lateinit var recipientId: String

    private val characterRepository = CharacterDetailRepository()

    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        senderId = args.senderCharacterId
        recipientId = args.recipientCharacterId

        chatRoomId = if (senderId < recipientId) {
            "$senderId$recipientId"
        } else {
            "$recipientId$senderId"
        }

        initializeChatRoomAndMessages()

        updateChatTitle(senderId, recipientId)
    }

    private fun updateChatTitle(senderId: String, recipientId: String) {
        characterRepository.getCharacterById(senderId)
            .observe(viewLifecycleOwner) { senderCharacter ->
                val senderName = senderCharacter?.name ?: "Unbekannt"

                characterRepository.getActiveCharacterById(recipientId)
                    .observe(viewLifecycleOwner) { recipientCharacter ->
                        val recipientName = recipientCharacter?.name ?: "Unbekannt"

                        binding.chatTitle.text = "$senderName & $recipientName"
                    }
            }
    }

    private fun initializeChatRoomAndMessages() {
        if (::senderId.isInitialized && ::recipientId.isInitialized) {
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

            viewModel.loadMessagesForChatRoom(chatRoomId)

            viewModel.chatMessages.observe(viewLifecycleOwner) { messages ->
                if (messages.isEmpty()) {
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

            adapter = ChatDetailAdapter(chatRoom, senderId)
            binding.rvMessages.layoutManager = LinearLayoutManager(context)
            binding.rvMessages.adapter = adapter

            binding.btSend.setOnClickListener {
                val messageText = binding.tietMessage.text.toString().trim()
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

                    viewModel.sendMessage(chatRoomId, newMessage)
                    binding.tietMessage.text?.clear()
                }
            }
        } else {
            Log.e("ChatdetailFragment", "Sender oder Empfänger wurden nicht korrekt initialisiert")
        }
    }
}