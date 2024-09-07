package com.example.shadowwisper.ui.theme.data.model

import com.google.firebase.Timestamp

data class ChatRoom (
    val lastActivityTimestamp: Timestamp,
    val participants: List<String>,    // Liste der Teilnehmer-IDs (z.B. Absender und Empfänger)
    val chatRoomId: String,            // Eindeutige ID des Chatrooms
    val chatRoomName: String,          // Name des Chatrooms (z.B. basierend auf den Teilnehmern)
    var lastMessage: String,           // Die letzte gesendete Nachricht
    var lastMessageSenderId: String,   // ID des Absenders der letzten Nachricht
    val messages: MutableList<ChatMessage> = mutableListOf(), // Liste der Nachrichten im Chatroom
    val userId: String,                // Der Benutzer, der den Chatraum sieht
    val characterId: String            // Charakter-ID des Benutzers
)