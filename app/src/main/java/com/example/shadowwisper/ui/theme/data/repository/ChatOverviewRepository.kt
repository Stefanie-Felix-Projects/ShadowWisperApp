/**
 * Repository-Klasse `ChatOverviewRepository`, die für das Abrufen von aktiven Charakteren und dem aktuellen Charakter eines Benutzers
 * aus Firebase Firestore zuständig ist. Diese Klasse bietet Methoden, um aktive Charaktere anderer Benutzer und den aktuellen Charakter
 * des angemeldeten Benutzers zu verwalten.
 */
package com.example.shadowwisper.ui.theme.data.repository

import android.util.Log
import com.example.shadowwisper.ui.theme.data.model.ActiveCharacter
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Diese Klasse interagiert mit Firebase Firestore, um aktive Charaktere anderer Benutzer und den aktuellen Charakter des angemeldeten Benutzers abzurufen.
 */
class ChatOverviewRepository {

    // Initialisiert Firebase Firestore.
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Ruft alle aktiven Charaktere ab, die nicht zum aktuellen Benutzer gehören.
     * Charaktere werden basierend auf dem Status "isActive" und dem Ausschluss des aktuellen Benutzers gefiltert.
     * Bei Erfolg werden die aktiven Charaktere an die `onSuccess`-Callback-Funktion übergeben.
     * Bei einem Fehler wird die `onFailure`-Callback-Funktion aufgerufen.
     * @param userId Die ID des aktuellen Benutzers, um dessen Charaktere aus der Abfrage auszuschließen.
     * @param onSuccess Callback-Funktion, die eine Liste von aktiven `ActiveCharacter`-Objekten zurückgibt.
     * @param onFailure Callback-Funktion, die aufgerufen wird, wenn ein Fehler auftritt.
     */
    fun getActiveCharacters(userId: String, onSuccess: (List<ActiveCharacter>) -> Unit, onFailure: (Exception) -> Unit) {
        // Abfrage nach aktiven Charakteren, die nicht dem aktuellen Benutzer gehören.
        firestore.collection("all_active_characters")
            .whereNotEqualTo("userId", userId) // Schließt den aktuellen Benutzer aus
            .whereEqualTo("isActive", true) // Filtert nur aktive Charaktere
            .get()
            .addOnSuccessListener { result ->
                val characters = result.toObjects(ActiveCharacter::class.java) // Konvertiert die Firestore-Ergebnisse in ActiveCharacter-Objekte
                Log.d("FirebaseRequest", "Aktive Charaktere gefunden: ${characters.size}")
                onSuccess(characters) // Erfolgreicher Abruf: Übergibt die Charakterliste an die Callback-Funktion
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRequest", "Fehler bei der Abfrage der aktiven Charaktere: $e") // Fehlerprotokollierung
                onFailure(e) // Fehlerfall: Callback-Funktion mit Ausnahme aufrufen
            }
    }

    /**
     * Ruft den aktuellen Charakter für den angegebenen Benutzer ab.
     * Der erste Charakter, der zur Benutzer-ID passt, wird zurückgegeben.
     * Bei Erfolg wird der aktuelle Charakter an die `onSuccess`-Callback-Funktion übergeben.
     * Bei einem Fehler oder wenn kein Charakter gefunden wird, wird die `onFailure`-Callback-Funktion aufgerufen.
     * @param userId Die ID des Benutzers, dessen Charakter abgerufen werden soll.
     * @param onSuccess Callback-Funktion, die den gefundenen `ActiveCharacter` zurückgibt.
     * @param onFailure Callback-Funktion, die aufgerufen wird, wenn ein Fehler auftritt oder kein Charakter gefunden wird.
     */
    fun getCurrentCharacterForUser(userId: String, onSuccess: (ActiveCharacter) -> Unit, onFailure: (Exception) -> Unit) {
        // Abfrage nach dem aktuellen Charakter des Benutzers.
        firestore.collection("all_active_characters")
            .whereEqualTo("userId", userId) // Filtert nach der Benutzer-ID
            .limit(1) // Gibt nur das erste Ergebnis zurück
            .get()
            .addOnSuccessListener { result ->
                Log.d("FirebaseRequest", "Firestore-Ergebnis: ${result.documents.size} Dokumente gefunden")
                if (!result.isEmpty) {
                    val character = result.documents[0].toObject(ActiveCharacter::class.java) // Konvertiert das Ergebnis in ein ActiveCharacter-Objekt
                    Log.d("FirebaseRequest", "Gefundener Charakter: $character")
                    if (character != null) {
                        onSuccess(character) // Erfolgreicher Abruf: Übergibt den gefundenen Charakter an die Callback-Funktion
                    } else {
                        onFailure(Exception("Character not found")) // Fehlerfall: Kein Charakter gefunden
                    }
                } else {
                    Log.e("FirebaseRequest", "Kein Charakter für userId: $userId gefunden")
                    onFailure(Exception("No characters found for userId: $userId")) // Fehlerfall: Kein Charakter gefunden
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRequest", "Fehler beim Abrufen der Character-ID: $e") // Fehlerprotokollierung
                onFailure(e) // Fehlerfall: Callback-Funktion mit Ausnahme aufrufen
            }
    }
}