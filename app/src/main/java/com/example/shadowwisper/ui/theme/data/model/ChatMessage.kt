package com.example.shadowwisper.ui.theme.data.model

import com.google.firebase.Timestamp

data class ChatMessage(
    val senderId: String = "",
    val message: String = "",
    val timestamp: Timestamp = Timestamp.now()
)