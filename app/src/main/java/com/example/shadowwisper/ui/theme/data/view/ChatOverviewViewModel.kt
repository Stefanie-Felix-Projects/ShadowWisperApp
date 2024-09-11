/**
 * ViewModel-Klasse `ChatOverviewViewModel`, die für die Verwaltung und Bereitstellung von Chatroom-Übersichten und aktiven Charakteren zuständig ist.
 * Diese Klasse verwendet das `ChatOverviewRepository`, um Daten zu aktiven Charakteren abzurufen und die aktuelle Charakter-ID des Benutzers zu laden.
 * Sie ist zuständig für die Logik, die die UI nicht blockiert, und wird im Android-Lebenszyklus der Anwendung verwaltet.
 */
package com.example.shadowwisper.ui.theme.data.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shadowwisper.ui.theme.data.model.ActiveCharacter
import com.example.shadowwisper.ui.theme.data.repository.ChatOverviewRepository
import com.google.firebase.auth.FirebaseAuth

/**
 * ViewModel zur Verwaltung von Chatroom-Übersichten und aktiven Charakteren.
 * Diese Klasse greift auf das `ChatOverviewRepository` zu, um die Liste der verfügbaren Charaktere und die aktuelle Charakter-ID des Benutzers zu laden.
 */
class ChatOverviewViewModel : ViewModel() {

    // Initialisiert das Repository für den Zugriff auf die Daten zu aktiven Charakteren.
    private val repository = ChatOverviewRepository()

    // Privates MutableLiveData für die Liste der aktiven Charaktere, die in der UI angezeigt werden.
    private val _availableCharacters = MutableLiveData<List<ActiveCharacter>>()

    /**
     * Öffentliches LiveData-Objekt, das von der UI beobachtet werden kann.
     * Es enthält die Liste der aktiven Charaktere, die für den aktuellen Benutzer verfügbar sind.
     */
    val availableCharacters: LiveData<List<ActiveCharacter>> = _availableCharacters

    // Privates MutableLiveData zur Speicherung der aktuellen Charakter-ID des Benutzers.
    private val _currentCharacterId = MutableLiveData<String>()

    /**
     * Öffentliches LiveData-Objekt, das von der UI beobachtet werden kann.
     * Es enthält die aktuelle Charakter-ID des Benutzers.
     */
    val currentCharacterId: LiveData<String> = _currentCharacterId

    /**
     * Die Benutzer-ID des aktuell angemeldeten Benutzers.
     * Sie wird beim ersten Zugriff auf dieses Feld geladen und zwischengespeichert.
     */
    val currentUserId: String by lazy {
        FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    /**
     * Lädt die Liste der aktiven Charaktere, die nicht dem aktuellen Benutzer gehören, aus dem Repository.
     * Die geladenen Charaktere werden in das MutableLiveData `_availableCharacters` geschrieben, um die UI zu aktualisieren.
     */
    fun loadActiveCharacters() {
        repository.getActiveCharacters(currentUserId, { characters ->
            _availableCharacters.value = characters // Aktualisiert die Liste der verfügbaren Charaktere
        }, { exception ->
            // Fehlerfall, falls die Charaktere nicht geladen werden können (derzeit ohne Fehlerbehandlung)
        })
    }

    /**
     * Lädt die aktuelle Charakter-ID des Benutzers aus dem Repository.
     * Wenn die Charakter-ID erfolgreich geladen wird, wird sie im MutableLiveData `_currentCharacterId` gespeichert, um die UI zu aktualisieren.
     */
    fun loadCurrentCharacterId() {
        Log.d("ChatOverviewViewModel", "Aktueller Benutzer: $currentUserId")

        repository.getCurrentCharacterForUser(currentUserId, { character ->
            if (character.characterId.isNotEmpty()) {
                _currentCharacterId.value = character.characterId // Setzt die geladene Charakter-ID
                Log.d("ChatOverviewViewModel", "Character-ID erfolgreich geladen: ${character.characterId}")
            } else {
                Log.e("ChatOverviewViewModel", "Character-ID ist leer.") // Fehlerprotokollierung
            }
        }, { exception ->
            Log.e("ChatOverviewViewModel", "Fehler beim Laden der Character-ID: $exception") // Fehlerprotokollierung
        })
    }
}