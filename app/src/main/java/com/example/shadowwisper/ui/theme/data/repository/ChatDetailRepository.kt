package com.example.shadowwisper.ui.theme.data.repository

import android.util.Log
import com.example.shadowwisper.ui.theme.data.model.ChatMessage
import com.example.shadowwisper.ui.theme.data.model.ChatRoom
import com.google.firebase.firestore.FirebaseFirestore

class ChatDetailRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getMessagesForChatRoom(chatRoomId: String, onSuccess: (List<ChatMessage>) -> Unit, onFailure: (Exception) -> Unit) {
        Log.d("ChatDetailRepository", "Lade Nachrichten für ChatRoom: $chatRoomId")
        firestore.collection("chats")
            .document(chatRoomId)
            .collection("messages")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                val messages = result.toObjects(ChatMessage::class.java)
                Log.d("ChatDetailRepository", "Nachrichten erfolgreich geladen: ${messages.size} Nachrichten")
                onSuccess(messages)
            }
            .addOnFailureListener { e ->
                Log.e("ChatDetailRepository", "Fehler beim Laden der Nachrichten", e)
                onFailure(e)
            }
    }


    fun sendMessage(chatRoomId: String, message: ChatMessage, onComplete: () -> Unit) {
        firestore.collection("chats")
            .document(chatRoomId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener {
                firestore.collection("chats").document(chatRoomId)
                    .update(
                        "lastMessage", message.message,
                        "lastMessageSenderId", message.senderId,
                        "participants", listOf(message.senderId, message.chatRoomId)
                    )
                    .addOnSuccessListener { onComplete() }
            }
            .addOnFailureListener { e ->
            }
    }

    fun createChatRoom(chatRoom: ChatRoom, onComplete: () -> Unit) {
        firestore.collection("chats")
            .document(chatRoom.chatRoomId)
            .set(chatRoom)
            .addOnSuccessListener {
                onComplete()
            }
            .addOnFailureListener { e ->
                Log.e("ChatDetailRepository", "Fehler beim Erstellen des Chatrooms", e)
            }
    }
}