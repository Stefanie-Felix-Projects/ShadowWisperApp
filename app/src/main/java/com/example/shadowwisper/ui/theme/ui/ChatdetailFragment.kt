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
import com.google.firebase.firestore.FirebaseFirestore

class ChatdetailFragment : Fragment() {

    private lateinit var binding: FragmentChatdetailBinding
    private val args: ChatdetailFragmentArgs by navArgs()

    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ChatDetailAdapter
    private val messages = mutableListOf<ChatMessage>()

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

        adapter = ChatDetailAdapter(messages)
        binding.rvMessages.layoutManager = LinearLayoutManager(context)
        binding.rvMessages.adapter = adapter

        loadMessages()

        binding.btSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun loadMessages() {
        firestore.collection("chats")
            .document(args.characterName)
            .collection("messages")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                messages.clear()
                val newMessages = result.toObjects(ChatMessage::class.java)
                messages.addAll(newMessages)
                adapter.notifyDataSetChanged()

                newMessages.forEach {
                    Log.d("ChatdetailFragment", "Loaded message: ${it.message}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ChatdetailFragment", "Error loading messages", exception)
            }
    }

    private fun sendMessage() {
        val messageText = binding.tietMessage.text.toString()

        if (messageText.isNotEmpty()) {
            val message = ChatMessage(
                senderId = args.characterName,
                message = messageText
            )

            firestore.collection("chats")
                .document(args.characterName)
                .collection("messages")
                .add(message)
                .addOnSuccessListener {
                    binding.tietMessage.text?.clear()
                    loadMessages()
                }
                .addOnFailureListener { exception ->
                }
        }
    }
}