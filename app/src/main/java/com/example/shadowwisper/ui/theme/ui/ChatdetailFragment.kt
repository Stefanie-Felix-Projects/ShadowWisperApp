/**
 * Fragment-Klasse `ChatdetailFragment`, die für die Anzeige und Verwaltung eines Chats zwischen zwei Charakteren zuständig ist.
 * Dieses Fragment lädt die Chat-Nachrichten, erstellt bei Bedarf ein neues ChatRoom-Objekt und ermöglicht das Senden von Nachrichten.
 */
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

/**
 * Fragment zur Anzeige eines Chatverlaufs zwischen zwei Charakteren.
 * Es ermöglicht das Senden von Nachrichten und das Erstellen eines neuen Chatrooms, falls noch keiner existiert.
 */
class ChatdetailFragment : Fragment() {

    // Binding-Objekt zur einfachen Referenzierung der UI-Elemente.
    private lateinit var binding: FragmentChatdetailBinding
    // ViewModel zur Verwaltung der Chat-Daten.
    private val viewModel: ChatDetailViewModel by activityViewModels()
    // Argumente, die von einem anderen Fragment übergeben wurden (z. B. die IDs der Chat-Teilnehmer).
    private val args: ChatdetailFragmentArgs by navArgs()

    // Adapter für die Anzeige der Chat-Nachrichten.
    private lateinit var adapter: ChatDetailAdapter
    // ChatRoom-Objekt für den aktuellen Chat.
    private lateinit var chatRoom: ChatRoom
    // IDs des Senders und Empfängers.
    private lateinit var chatRoomId: String
    private lateinit var senderId: String
    private lateinit var recipientId: String

    // Repository für den Zugriff auf Charakterdetails.
    private val characterRepository = CharacterDetailRepository()

    /**
     * Erstellt die View für das Fragment und bindet das Layout an das Fragment.
     */
    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Wird nach der Erstellung der View aufgerufen, um den Chatroom zu initialisieren und die Chat-Nachrichten anzuzeigen.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sender- und Empfänger-ID aus den übergebenen Argumenten.
        senderId = args.senderCharacterId
        recipientId = args.recipientCharacterId

        // Erzeugt eine eindeutige ChatRoom-ID basierend auf den IDs der Teilnehmer.
        chatRoomId = if (senderId < recipientId) {
            "$senderId$recipientId"
        } else {
            "$recipientId$senderId"
        }

        // Initialisiert den Chatroom und lädt die Nachrichten.
        initializeChatRoomAndMessages()

        // Aktualisiert den Chat-Titel basierend auf den Teilnehmernamen.
        updateChatTitle(senderId, recipientId)
    }

    /**
     * Aktualisiert den Titel des Chats mit den Namen der beiden Teilnehmer.
     * Ruft die Namen der Charaktere aus dem Repository ab.
     * @param senderId Die ID des Senders.
     * @param recipientId Die ID des Empfängers.
     */
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

    /**
     * Initialisiert den ChatRoom und lädt die Nachrichten.
     * Wenn der ChatRoom noch nicht existiert, wird ein neuer erstellt.
     */
    private fun initializeChatRoomAndMessages() {
        if (::senderId.isInitialized && ::recipientId.isInitialized) {
            // Erzeugt ein neues ChatRoom-Objekt.
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

            // Lädt die Nachrichten für den Chatroom.
            viewModel.loadMessagesForChatRoom(chatRoomId)

            // Beobachtet die Nachrichten und aktualisiert die UI.
            viewModel.chatMessages.observe(viewLifecycleOwner) { messages ->
                if (messages.isEmpty()) {
                    // Erstellt einen neuen Chatroom, wenn keine Nachrichten vorhanden sind.
                    viewModel.createChatRoom(chatRoom) {
                        Log.d("ChatdetailFragment", "Chatroom erfolgreich erstellt")
                    }
                } else {
                    // Aktualisiert den Chatroom mit den Nachrichten und zeigt diese an.
                    chatRoom.messages.clear()
                    chatRoom.messages.addAll(messages)
                    chatRoom.lastMessage = messages.lastOrNull()?.message ?: ""
                    chatRoom.lastMessageSenderId = messages.lastOrNull()?.senderId ?: ""

                    adapter.notifyDataSetChanged()
                    binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
                }
            }

            // Initialisiert den Adapter und das Layout für die Nachrichtenliste (RecyclerView).
            adapter = ChatDetailAdapter(chatRoom, senderId)
            binding.rvMessages.layoutManager = LinearLayoutManager(context)
            binding.rvMessages.adapter = adapter

            // Klicklistener für den Button zum Senden einer Nachricht.
            binding.btSend.setOnClickListener {
                val messageText = binding.tietMessage.text.toString().trim()
                if (messageText.isNotEmpty()) {
                    // Erstellt eine neue Nachricht und sendet sie.
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