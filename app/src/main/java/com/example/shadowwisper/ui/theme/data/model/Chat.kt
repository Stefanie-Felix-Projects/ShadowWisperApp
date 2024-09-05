package com.example.shadowwisper.ui.theme.data.model

import com.google.firebase.Timestamp

data class Chat (

    val messages: List<String>,
    val timestamp: Timestamp,
    val participant1: String,
    val participant2: String
)

