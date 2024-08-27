package com.example.shadowwisper.ui.theme.data.database

data class ChatMessage(
    val senderId: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)