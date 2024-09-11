/**
 * Datenklasse `ChatMessage`, die die Datenstruktur einer Chat-Nachricht in der Anwendung definiert.
 * Diese Klasse wird verwendet, um Nachrichten zwischen Charakteren in einem Chatroom zu speichern und zu verwalten.
 */
package com.example.shadowwisper.ui.theme.data.model

import com.google.firebase.Timestamp
import java.util.UUID

/**
 * Repräsentiert eine Chat-Nachricht in der Anwendung.
 * Die Klasse speichert Informationen wie Absender, Empfänger, den Inhalt der Nachricht und den Zeitstempel.
 * @param senderId Die ID des Absenders der Nachricht.
 * @param recipientId Die ID des Empfängers der Nachricht.
 * @param characterId Die ID des Charakters, der die Nachricht sendet oder empfängt.
 * @param message Der Inhalt der Nachricht als String.
 * @param timestamp Der Zeitpunkt, zu dem die Nachricht gesendet wurde. Standardmäßig wird der aktuelle Zeitpunkt verwendet.
 * @param chatRoomId Die ID des Chatrooms, in dem die Nachricht gesendet wurde.
 * @param messageId Die eindeutige ID der Nachricht, die automatisch mit einer UUID generiert wird.
 * @param messageStatus Der Status der Nachricht (z. B. "gesendet", "zugestellt", "gelesen").
 */
data class ChatMessage(
    val senderId: String = "", // Die ID des Absenders der Nachricht
    val recipientId: String = "", // Die ID des Empfängers der Nachricht
    val characterId: String = "", // Die ID des Charakters, der die Nachricht sendet oder empfängt
    val message: String = "", // Der Inhalt der Nachricht als Text
    val timestamp: Timestamp = Timestamp.now(), // Der Zeitpunkt, zu dem die Nachricht gesendet wurde (standardmäßig jetzt)
    val chatRoomId: String = "", // Die ID des Chatrooms, in dem die Nachricht gesendet wurde
    val messageId: String = UUID.randomUUID().toString(), // Eindeutige ID der Nachricht, automatisch generiert durch UUID
    val messageStatus: String = "" // Status der Nachricht, z.B. "gesendet", "zugestellt", "gelesen"
)