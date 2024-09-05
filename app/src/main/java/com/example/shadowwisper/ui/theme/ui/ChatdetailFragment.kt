package com.example.shadowwisper.ui.theme.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shadowwisper.databinding.FragmentChatdetailBinding
import com.example.shadowwisper.ui.theme.data.adapter.ChatDetailAdapter
import com.example.shadowwisper.ui.theme.data.model.ChatMessage
import com.example.shadowwisper.ui.theme.data.repository.ChatRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatdetailFragment : Fragment() {

    private lateinit var binding: FragmentChatdetailBinding
    private val args: ChatdetailFragmentArgs by navArgs()

    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ChatDetailAdapter
    private lateinit var chatRepository: ChatRepository
    private val messages = mutableListOf<ChatMessage>()
    private val currentUserId: String by lazy {
        FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        chatRepository = ChatRepository()

        adapter = ChatDetailAdapter(messages, currentUserId)
        binding.rvMessages.layoutManager = LinearLayoutManager(context)
        binding.rvMessages.adapter = adapter

        listenToMessages()

        // Setzt den onClickListener für den Senden-Button
        binding.btSend.setOnClickListener {
            val messageText = binding.tietMessage.text.toString().trim()

            // Prüfen, ob der Text nicht leer ist
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                // Leere das Eingabefeld nach dem Senden
                binding.tietMessage.text?.clear()
            }
        }
    }

    // Echtzeit-Listener für Nachrichten
    private fun listenToMessages() {
        val senderCharacterId = args.senderCharacterId
        val recipientCharacterId = args.recipientCharacterId

        // Erstelle die gleiche Chat-ID
        val chatId = if (senderCharacterId < recipientCharacterId) {
            "$senderCharacterId$recipientCharacterId"
        } else {
            "$recipientCharacterId$senderCharacterId"
        }

        // Nachrichten aus dem gemeinsamen Chat-Dokument laden
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { result, error ->
                if (error != null) {
                    Log.e("ChatdetailFragment", "Error loading messages", error)
                    return@addSnapshotListener
                }

                if (result != null) {
                    messages.clear()
                    val newMessages = result.toObjects(ChatMessage::class.java)
                    messages.addAll(newMessages)
                    adapter.notifyDataSetChanged()
                    binding.rvMessages.scrollToPosition(messages.size - 1)
                }
            }
    }

    private fun sendMessage(messageText: String) {
        val senderCharacterId = args.senderCharacterId
        val recipientCharacterId = args.recipientCharacterId

        // Erstelle eine Chat-ID basierend auf den beiden Teilnehmern
        val chatId = if (senderCharacterId < recipientCharacterId) {
            "$senderCharacterId$recipientCharacterId"
        } else {
            "$recipientCharacterId$senderCharacterId"
        }

        // Erstellen des ChatMessage-Objekts mit der Nachricht aus dem Eingabefeld
        val chatMessage = ChatMessage(
            senderId = senderCharacterId, // Setze hier die CharacterId als Sender
            message = messageText,
            timestamp = Timestamp.now()
        )

        // Nachricht im gemeinsamen Chat-Dokument speichern
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Nachricht im Chat gespeichert")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Fehler beim Speichern der Nachricht im Chat", e)
            }
    }
}