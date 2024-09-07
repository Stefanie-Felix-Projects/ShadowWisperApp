package com.example.shadowwisper.ui.theme.data.repository

import com.example.shadowwisper.ui.theme.data.model.ChatMessage
import com.example.shadowwisper.ui.theme.data.model.ChatRoom
import com.google.firebase.firestore.FirebaseFirestore

class ChatDetailRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Holt alle Nachrichten für den Chatroom
    fun getMessagesForChatRoom(chatRoomId: String, onSuccess: (List<ChatMessage>) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("chats")
            .document(chatRoomId)
            .collection("messages")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                val messages = result.toObjects(ChatMessage::class.java)
                onSuccess(messages)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    // Methode zum Senden von Nachrichten in einem Chatroom
    fun sendMessage(chatRoomId: String, message: ChatMessage, onComplete: () -> Unit) {
        firestore.collection("chats")
            .document(chatRoomId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener {
                // Aktualisiere die letzte Nachricht und die Teilnehmer im Chatroom-Dokument
                firestore.collection("chats").document(chatRoomId)
                    .update(
                        "lastMessage", message.message,
                        "lastMessageSenderId", message.senderId,
                        "participants", listOf(message.senderId, message.chatRoomId) // Character-IDs der Teilnehmer
                    )
                    .addOnSuccessListener { onComplete() }
            }
            .addOnFailureListener { e ->
                // Fehlerbehandlung bei Nachrichtensendung
            }
    }

    // Methode zum Erstellen eines neuen Chatrooms, falls dieser noch nicht existiert
    fun createChatRoom(chatRoom: ChatRoom, onComplete: () -> Unit) {
        firestore.collection("chats")
            .document(chatRoom.chatRoomId)
            .set(chatRoom)
            .addOnSuccessListener {
                onComplete()
            }
            .addOnFailureListener { e ->
                // Fehlerbehandlung
            }
    }
}