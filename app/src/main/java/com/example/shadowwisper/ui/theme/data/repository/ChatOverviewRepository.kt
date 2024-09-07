package com.example.shadowwisper.ui.theme.data.repository

import android.util.Log
import com.example.shadowwisper.ui.theme.data.model.ActiveCharacter
import com.google.firebase.firestore.FirebaseFirestore

class ChatOverviewRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Holt die aktiven Charaktere aller Benutzer außer dem aktuellen Benutzer
    fun getActiveCharacters(userId: String, onSuccess: (List<ActiveCharacter>) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("all_active_characters")
            .whereNotEqualTo("userId", userId)
            .whereEqualTo("isActive", true)
            .get()
            .addOnSuccessListener { result ->
                val characters = result.toObjects(ActiveCharacter::class.java)
                Log.d("FirebaseRequest", "Aktive Charaktere gefunden: ${characters.size}")
                onSuccess(characters)
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRequest", "Fehler bei der Abfrage der aktiven Charaktere: $e")
                onFailure(e)
            }
    }

    // Holt die Character-ID für den aktuellen Benutzer (basierend auf userId)
    fun getCurrentCharacterForUser(userId: String, onSuccess: (ActiveCharacter) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("all_active_characters")
            .whereEqualTo("userId", userId)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                Log.d("FirebaseRequest", "Firestore-Ergebnis: ${result.documents.size} Dokumente gefunden")
                if (!result.isEmpty) {
                    val character = result.documents[0].toObject(ActiveCharacter::class.java)
                    Log.d("FirebaseRequest", "Gefundener Charakter: $character")
                    if (character != null) {
                        onSuccess(character)
                    } else {
                        onFailure(Exception("Character not found"))
                    }
                } else {
                    Log.e("FirebaseRequest", "Kein Charakter für userId: $userId gefunden")
                    onFailure(Exception("No characters found for userId: $userId"))
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRequest", "Fehler beim Abrufen der Character-ID: $e")
                onFailure(e)
            }
    }
}