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
        })
    }

    fun sendMessage(chatRoomId: String, message: ChatMessage) {
        repository.sendMessage(chatRoomId, message) {
            loadMessagesForChatRoom(chatRoomId)
        }
    }

    fun createChatRoom(chatRoom: ChatRoom, onComplete: () -> Unit) {
        repository.createChatRoom(chatRoom) {
            onComplete()
        }
    }
}