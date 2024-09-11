/**
 * ViewModel-Klasse `ChatDetailViewModel`, die für die Verwaltung der Chat-Daten und Nachrichten in der Benutzeroberfläche zuständig ist.
 * Diese Klasse verwendet das `ChatDetailRepository`, um Nachrichten zu laden, zu senden und Chatrooms zu erstellen.
 * Sie verwaltet UI-bezogene Daten so, dass sie den Lebenszyklus der Anwendung überstehen.
 */
package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shadowwisper.ui.theme.data.model.ChatMessage
import com.example.shadowwisper.ui.theme.data.model.ChatRoom
import com.example.shadowwisper.ui.theme.data.repository.ChatDetailRepository

/**
 * ViewModel zur Verwaltung von Chat-Details und Nachrichten.
 * Es greift auf das `ChatDetailRepository` zu, um Chat-Daten zu laden und neue Nachrichten zu senden.
 */
class ChatDetailViewModel : ViewModel() {

    // Initialisiert das Repository, das für den Zugriff auf die Chat-Daten zuständig ist.
    private val repository = ChatDetailRepository()

    // Privates MutableLiveData, um die Nachrichtenliste zu halten.
    private val _chatMessages = MutableLiveData<List<ChatMessage>>()

    /**
     * Öffentliches LiveData-Objekt, das von der UI beobachtet werden kann.
     * Es enthält die Nachrichtenliste des aktuellen Chatrooms.
     */
    val chatMessages: LiveData<List<ChatMessage>> = _chatMessages

    /**
     * Lädt die Nachrichten für einen bestimmten Chatroom aus dem Repository.
     * Die abgerufenen Nachrichten werden in das `_chatMessages`-MutableLiveData geschrieben, das dann die UI aktualisiert.
     * @param chatRoomId Die ID des Chatrooms, dessen Nachrichten geladen werden sollen.
     */
    fun loadMessagesForChatRoom(chatRoomId: String) {
        repository.getMessagesForChatRoom(chatRoomId, { messages ->
            _chatMessages.value = messages // Aktualisiert die Nachrichtenliste im LiveData
        }, { exception ->
            // Fehlerfall, falls die Nachrichten nicht geladen werden können (derzeit ohne Fehlerbehandlung)
        })
    }

    /**
     * Sendet eine neue Nachricht in den angegebenen Chatroom und lädt die Nachrichtenliste nach dem Senden neu.
     * @param chatRoomId Die ID des Chatrooms, in den die Nachricht gesendet werden soll.
     * @param message Das `ChatMessage`-Objekt, das die zu sendende Nachricht enthält.
     */
    fun sendMessage(chatRoomId: String, message: ChatMessage) {
        repository.sendMessage(chatRoomId, message) {
            loadMessagesForChatRoom(chatRoomId) // Lädt die Nachrichtenliste neu, nachdem die Nachricht gesendet wurde
        }
    }

    /**
     * Erstellt einen neuen Chatroom und ruft die `onComplete`-Callback-Funktion auf, nachdem der Chatroom erfolgreich erstellt wurde.
     * @param chatRoom Das `ChatRoom`-Objekt, das den neuen Chatroom beschreibt.
     * @param onComplete Callback-Funktion, die aufgerufen wird, wenn der Chatroom erfolgreich erstellt wurde.
     */
    fun createChatRoom(chatRoom: ChatRoom, onComplete: () -> Unit) {
        repository.createChatRoom(chatRoom) {
            onComplete() // Ruft die Callback-Funktion auf, um zu signalisieren, dass der Chatroom erstellt wurde
        }
    }
}