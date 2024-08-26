package com.example.shadowwisper.ui.theme.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.shadowwisper.databinding.FragmentChatdetailBinding
import com.google.firebase.firestore.FirebaseFirestore


class ChatdetailFragment : Fragment() {

    private lateinit var binding: FragmentChatdetailBinding
    private val args: ChatdetailFragmentArgs by navArgs()

    private lateinit var firestore: FirebaseFirestore

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

        loadMessages()

        binding.btSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun loadMessages() {
        firestore.collection("chats")
            .document(args.characterName)
            .collection("messages")
            .orderBy("timeStamp")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val message = document.getString("message")
                }
            }
            .addOnFailureListener { exception ->
            }
    }

    private fun sendMessage() {
        val messageText = binding.tietMessage.text.toString()

        if (messageText.isNotEmpty()) {
            val messageData = mapOf(
                "message" to messageText,
                "timeStamp" to System.currentTimeMillis()
            )

            firestore.collection("chats")
                .document(args.characterName)
                .collection("messages")
                .add(messageData)
                .addOnSuccessListener {
                    binding.tietMessage.text?.clear()
                    loadMessages()
                }
                .addOnFailureListener { exception ->
                }
        }
    }
}