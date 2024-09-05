package com.example.shadowwisper.ui.theme.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    // Methode zum Erstellen eines neuen Chats mit Teilnehmern
    fun createChatWithParticipants(chatId: String, participants: List<String>, lastMessage: String) {
        val chatData = mapOf(
            "participants" to participants,  // Hier setzen wir die Teilnehmer
            "lastMessage" to lastMessage,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("chats")
            .document(chatId)
            .set(chatData)
            .addOnSuccessListener {
                Log.d("ChatCreation", "Chat successfully created with participants: $participants")
            }
            .addOnFailureListener { e ->
                Log.e("ChatCreation", "Error creating chat", e)
            }
    }

    // Methode zum Abrufen der aktuellen aktiven Charakter-ID des Benutzers
    fun getCurrentActiveCharacterId(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            firestore.collection("all_active_characters")
                .whereEqualTo("userId", userId)
                .whereEqualTo("isActive", true)  // Hole den aktiven Charakter
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        onFailure(Exception("No active character found"))
                    } else {
                        val characterId = documents.documents[0].getString("characterId")
                        if (characterId != null) {
                            onSuccess(characterId)
                        } else {
                            onFailure(Exception("CharacterId is null"))
                        }
                    }
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
        } else {
            onFailure(Exception("User not authenticated"))
        }
    }

    fun getAllActiveCharacters(): LiveData<List<CharacterDetail>> {
        val charactersLiveData = MutableLiveData<List<CharacterDetail>>()
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        firestore.collection("all_active_characters")
            .get()
            .addOnSuccessListener { result ->
                val characters = mutableListOf<CharacterDetail>()
                for (document in result) {
                    val character = document.toObject(CharacterDetail::class.java)
                    // Hier kannst du den isActive-Wert explizit setzen
                    character.isActive = document.getBoolean("isActive") ?: true
                    characters.add(character)
                }

                // Filtere die Charaktere des aktuellen Nutzers heraus
                val filteredCharacters = characters.filter { it.userId != currentUserId }
                charactersLiveData.value = filteredCharacters
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }

        return charactersLiveData
    }
}