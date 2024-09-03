package com.example.shadowwisper.ui.theme.ui

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
import com.example.shadowwisper.ui.theme.data.database.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatdetailFragment : Fragment() {

    private lateinit var binding: FragmentChatdetailBinding
    private val args: ChatdetailFragmentArgs by navArgs()

    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ChatDetailAdapter
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

        adapter = ChatDetailAdapter(messages, currentUserId)
        binding.rvMessages.layoutManager = LinearLayoutManager(context)
        binding.rvMessages.adapter = adapter

        loadMessages()

        binding.btSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun loadMessages() {
        val chatId = args.characterId

        if (chatId.isNotBlank()) {
            firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener { result ->
                    messages.clear()
                    val newMessages = result.toObjects(ChatMessage::class.java)
                    messages.addAll(newMessages)
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.e("ChatdetailFragment", "Error loading messages", exception)
                }
        } else {
            Log.e("ChatdetailFragment", "Invalid chatId, cannot load messages")
        }
    }

    private fun sendMessage() {
        val messageText = binding.tietMessage.text.toString()

        if (messageText.isNotEmpty()) {
            val message = ChatMessage(
                senderId = currentUserId,
                message = messageText
            )

            val chatId = args.characterId
            if (chatId.isNotEmpty()) {
                // Nachricht sofort zur Liste hinzufügen und RecyclerView aktualisieren
                messages.add(message)
                adapter.notifyItemInserted(messages.size - 1)
                binding.rvMessages.scrollToPosition(messages.size - 1)

                firestore.collection("chats")
                    .document(chatId)
                    .collection("messages")
                    .add(message)
                    .addOnSuccessListener {
                        binding.tietMessage.text?.clear()
                        Log.d("ChatdetailFragment", "Message sent and added to Firestore")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("ChatdetailFragment", "Error sending message", exception)
                        // Nachricht aus der Liste entfernen, wenn das Speichern fehlschlägt
                        messages.remove(message)
                        adapter.notifyItemRemoved(messages.size)
                    }
            } else {
                Log.e("ChatdetailFragment", "Invalid chatId, cannot send message")
            }
        }
    }
}