package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shadowwisper.ui.theme.data.model.ChatMessage
import com.example.shadowwisper.ui.theme.data.model.ChatRoom
import com.example.shadowwisper.ui.theme.data.repository.ChatDetailRepository

class ChatDetailViewModel : ViewModel() {

    private val repository = ChatDetailRepository()

    private val _chatMessages = MutableLiveData<List<ChatMessage>>()
    val chatMessages: LiveData<List<ChatMessage>> = _chatMessages

    fun loadMessagesForChatRoom(chatRoomId: String) {
        repository.getMessagesForChatRoom(chatRoomId, { messages ->
            _chatMessages.value = messages
        }, { exception ->
            // Fehlerbehandlung
        })
    }

    fun sendMessage(chatRoomId: String, message: ChatMessage) {
        // Zuerst den ChatRoom erstellen oder überprüfen, falls er noch nicht existiert
        repository.getMessagesForChatRoom(chatRoomId, { messages ->
            if (messages.isEmpty()) {
                // Wenn noch keine Nachrichten vorhanden sind, erstelle den ChatRoom
                val chatRoom = ChatRoom(
                    lastActivityTimestamp = message.timestamp,
                    participants = listOf(message.senderId, message.chatRoomId), // Beispiel
                    chatRoomId = chatRoomId,
                    chatRoomName = "Chatroom",
                    lastMessage = message.message,
                    lastMessageSenderId = message.senderId,
                    messages = mutableListOf(message),
                    userId = message.senderId,
                    characterId = message.senderId
                )
                repository.createChatRoom(chatRoom) {
                    // Nachdem der ChatRoom erstellt wurde, sende die Nachricht
                    repository.sendMessage(chatRoomId, message) {
                        loadMessagesForChatRoom(chatRoomId)
                    }
                }
            } else {
                // Wenn der ChatRoom bereits existiert, nur die Nachricht senden
                repository.sendMessage(chatRoomId, message) {
                    loadMessagesForChatRoom(chatRoomId)
                }
            }
        }, { exception ->
            // Fehlerbehandlung
        })
    }
}