/**
 * Datenklasse `ChatRoom`, die die Datenstruktur eines Chatrooms in der Anwendung definiert.
 * Diese Klasse enthält Informationen über den Chatroom wie die Teilnehmer, die letzte Nachricht und die gesendeten Nachrichten.
 */
package com.example.shadowwisper.ui.theme.data.model

import com.google.firebase.Timestamp

/**
 * Repräsentiert einen Chatroom in der Anwendung.
 * Die Klasse speichert Informationen über den Chatroom, wie die Teilnehmer, den Zeitstempel der letzten Aktivität,
 * die gesendeten Nachrichten und Details über die letzte Nachricht.
 * @param lastActivityTimestamp Der Zeitpunkt der letzten Aktivität im Chatroom (z. B. letzte Nachricht).
 * @param participants Eine Liste der IDs der Teilnehmer im Chatroom.
 * @param recipientId Die ID des Empfängers im Chatroom.
 * @param chatRoomId Die eindeutige ID des Chatrooms.
 * @param chatRoomName Der Name des Chatrooms.
 * @param lastMessage Der Inhalt der zuletzt gesendeten Nachricht.
 * @param lastMessageSenderId Die ID des Absenders der zuletzt gesendeten Nachricht.
 * @param messages Eine Liste der Nachrichten, die in diesem Chatroom ausgetauscht wurden. Standardmäßig eine leere Liste.
 * @param userId Die ID des Benutzers, der den Chatroom erstellt oder beteiligt ist.
 * @param senderId Die ID des Charakters, der die Nachrichten sendet.
 * @param characterId Die ID des Charakters, der im Chatroom beteiligt ist.
 */
data class ChatRoom(
    val lastActivityTimestamp: Timestamp, // Zeitstempel der letzten Aktivität im Chatroom
    val participants: List<String>, // Liste der Teilnehmer-IDs im Chatroom
    val recipientId: String, // ID des Empfängers im Chatroom
    val chatRoomId: String, // Eindeutige ID des Chatrooms
    val chatRoomName: String, // Name des Chatrooms
    var lastMessage: String, // Inhalt der zuletzt gesendeten Nachricht
    var lastMessageSenderId: String, // ID des Absenders der zuletzt gesendeten Nachricht
    val messages: MutableList<ChatMessage> = mutableListOf(), // Liste der Nachrichten im Chatroom, standardmäßig eine leere Liste
    val userId: String, // ID des Benutzers, der am Chatroom beteiligt ist
    val senderId: String, // ID des Absenders der Nachrichten im Chatroom
    val characterId: String // ID des Charakters, der im Chatroom beteiligt ist
)