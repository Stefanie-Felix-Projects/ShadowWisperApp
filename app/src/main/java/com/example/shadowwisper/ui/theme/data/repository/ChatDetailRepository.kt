/**
 * Repository-Klasse `ChatDetailRepository`, die für die Verwaltung von Nachrichten und Chatrooms in Firebase Firestore zuständig ist.
 * Diese Klasse stellt Methoden zum Abrufen, Senden von Nachrichten und Erstellen von Chatrooms bereit.
 */
package com.example.shadowwisper.ui.theme.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shadowwisper.ui.theme.data.model.ActiveCharacter
import com.example.shadowwisper.ui.theme.data.model.ChatMessage
import com.example.shadowwisper.ui.theme.data.model.ChatRoom
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Diese Klasse verwaltet Nachrichten in Chatrooms und interagiert mit Firebase Firestore.
 * Sie enthält Funktionen zum Abrufen von Nachrichten, Senden neuer Nachrichten und Erstellen von Chatrooms.
 */
class ChatDetailRepository {

    // Initialisiert Firebase Firestore.
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Ruft alle Nachrichten für einen bestimmten Chatroom aus Firebase Firestore ab.
     * Die Nachrichten werden nach ihrem Zeitstempel sortiert und an die Callback-Funktion `onSuccess` übergeben.
     * Bei einem Fehler wird die `onFailure`-Callback-Funktion aufgerufen.
     * @param chatRoomId Die ID des Chatrooms, dessen Nachrichten abgerufen werden sollen.
     * @param onSuccess Callback-Funktion, die die Liste der abgerufenen `ChatMessage`-Objekte zurückgibt.
     * @param onFailure Callback-Funktion, die aufgerufen wird, wenn ein Fehler auftritt.
     */
    fun getMessagesForChatRoom(chatRoomId: String, onSuccess: (List<ChatMessage>) -> Unit, onFailure: (Exception) -> Unit) {
        Log.d("ChatDetailRepository", "Lade Nachrichten für ChatRoom: $chatRoomId")

        // Abruf der Nachrichten aus Firestore, sortiert nach dem Zeitstempel.
        firestore.collection("chats")
            .document(chatRoomId)
            .collection("messages")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                val messages = result.toObjects(ChatMessage::class.java) // Konvertiert die Ergebnisse in ChatMessage-Objekte.
                Log.d("ChatDetailRepository", "Nachrichten erfolgreich geladen: ${messages.size} Nachrichten")
                onSuccess(messages) // Erfolgreicher Abruf: Nachrichten werden an die Callback-Funktion übergeben.
            }
            .addOnFailureListener { e ->
                Log.e("ChatDetailRepository", "Fehler beim Laden der Nachrichten", e) // Fehlerprotokollierung.
                onFailure(e) // Fehlerfall: Callback-Funktion mit Ausnahme aufrufen.
            }
    }

    /**
     * Sendet eine neue Nachricht in einen Chatroom und aktualisiert die Chatroom-Daten in Firebase Firestore.
     * Nach erfolgreichem Senden der Nachricht wird die `onComplete`-Callback-Funktion aufgerufen.
     * @param chatRoomId Die ID des Chatrooms, in den die Nachricht gesendet werden soll.
     * @param message Das `ChatMessage`-Objekt, das gesendet wird.
     * @param onComplete Callback-Funktion, die nach erfolgreichem Senden der Nachricht aufgerufen wird.
     */
    fun sendMessage(chatRoomId: String, message: ChatMessage, onComplete: () -> Unit) {
        firestore.collection("chats")
            .document(chatRoomId)
            .collection("messages")
            .add(message) // Fügt die Nachricht zur Nachrichtenliste im Chatroom hinzu.
            .addOnSuccessListener {
                // Aktualisiert die letzten Nachrichteninformationen im Chatroom.
                firestore.collection("chats").document(chatRoomId)
                    .update(
                        "lastMessage", message.message,
                        "lastMessageSenderId", message.senderId,
                        "participants", listOf(message.senderId, message.chatRoomId)
                    )
                    .addOnSuccessListener { onComplete() } // Erfolgreicher Abschluss: Callback-Funktion aufrufen.
            }
            .addOnFailureListener { e ->
                Log.e("ChatDetailRepository", "Fehler beim Senden der Nachricht", e) // Fehlerprotokollierung.
            }
    }

    /**
     * Erstellt einen neuen Chatroom in Firebase Firestore.
     * Nach erfolgreichem Erstellen des Chatrooms wird die `onComplete`-Callback-Funktion aufgerufen.
     * @param chatRoom Das `ChatRoom`-Objekt, das erstellt werden soll.
     * @param onComplete Callback-Funktion, die nach erfolgreichem Erstellen des Chatrooms aufgerufen wird.
     */
    fun createChatRoom(chatRoom: ChatRoom, onComplete: () -> Unit) {
        firestore.collection("chats")
            .document(chatRoom.chatRoomId)
            .set(chatRoom) // Fügt den neuen Chatroom zu Firestore hinzu.
            .addOnSuccessListener {
                onComplete() // Erfolgreicher Abschluss: Callback-Funktion aufrufen.
            }
            .addOnFailureListener { e ->
                Log.e("ChatDetailRepository", "Fehler beim Erstellen des Chatrooms", e) // Fehlerprotokollierung.
            }
    }
}