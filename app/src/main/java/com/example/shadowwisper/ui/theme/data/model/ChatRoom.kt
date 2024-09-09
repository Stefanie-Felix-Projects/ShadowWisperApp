package com.example.shadowwisper.ui.theme.data.model

import com.google.firebase.Timestamp

data class ChatRoom (
    val lastActivityTimestamp: Timestamp,
    val participants: List<String>,
    val recipientId: String,
    val chatRoomId: String,
    val chatRoomName: String,
    var lastMessage: String,
    var lastMessageSenderId: String,
    val messages: MutableList<ChatMessage> = mutableListOf(),
    val userId: String,
    val senderId: String,
    val characterId: String
)