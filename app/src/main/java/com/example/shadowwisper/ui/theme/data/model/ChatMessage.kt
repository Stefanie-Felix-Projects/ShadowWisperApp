package com.example.shadowwisper.ui.theme.data.model

import com.google.firebase.Timestamp
import java.util.UUID

data class ChatMessage(
    val senderId: String = "",
    val recipientId: String = "",
    val characterId: String = "",
    val message: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val chatRoomId: String = "",
    val messageId: String = UUID.randomUUID().toString(),
    val messageStatus: String = ""
)