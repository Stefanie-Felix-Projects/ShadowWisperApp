/**
 * Repository-Klasse `CharacterOverviewRepository`, die für die Verwaltung und den Zugriff auf Charakterübersichten verantwortlich ist.
 * Diese Klasse interagiert mit Firebase Firestore, um Charakterdaten in Echtzeit abzurufen und den aktiven Charakter festzulegen.
 */
package com.example.shadowwisper.ui.theme.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shadowwisper.ui.theme.data.model.ActiveCharacter
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

/**
 * Diese Klasse verwaltet die Abfrage und das Festlegen von Charakteren in der Firebase Firestore-Datenbank.
 * Sie ermöglicht es, alle Charaktere des aktuellen Benutzers in Echtzeit zu beobachten und einen aktiven Charakter zu setzen.
 */
class CharacterOverviewRepository {

    // Initialisiert Firebase Firestore und erhält die ID des aktuell angemeldeten Benutzers.
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var listenerRegistration: ListenerRegistration? = null

    /**
     * Ruft alle Charaktere des aktuell angemeldeten Benutzers aus Firebase Firestore ab.
     * Die Methode verwendet einen Echtzeit-Listener, um auf Änderungen an den Charakterdaten zu reagieren.
     * @return Ein `LiveData`-Objekt, das eine Liste von `CharacterDetail`-Objekten enthält.
     */
    fun getAllCharacters(): LiveData<List<CharacterDetail>> {
        val charactersLiveData = MutableLiveData<List<CharacterDetail>>()

        if (userId != null) {
            listenerRegistration = firestore.collection("users")
                .document(userId)
                .collection("characters")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w("CharacterOverviewRepo", "Listen failed.", e) // Fehlerprotokollierung
                        return@addSnapshotListener
                    }

                    if (snapshots != null) {
                        val characters = snapshots.toObjects(CharacterDetail::class.java) // Konvertiert die Snapshots in CharacterDetail-Objekte
                        charactersLiveData.value = characters // Aktualisiert die LiveData mit der neuen Liste von Charakteren
                    }
                }
        }
        return charactersLiveData
    }

    /**
     * Setzt einen bestimmten Charakter als aktiven Charakter für den aktuellen Benutzer.
     * Der Charakter wird in Firestore als aktiv markiert und vorherige aktive Charaktere werden deaktiviert.
     * @param characterDetail Das `CharacterDetail`-Objekt des Charakters, der als aktiv markiert werden soll.
     * @return Ein `LiveData`-Objekt, das das aktive `CharacterDetail` oder `null` im Fehlerfall enthält.
     */
    fun setActiveCharacter(characterDetail: CharacterDetail): LiveData<CharacterDetail?> {
        val activeCharacterLiveData = MutableLiveData<CharacterDetail?>()

        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("active_character")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id != characterDetail.characerId) {
                            document.reference.update("isActive", false) // Deaktiviert andere aktive Charaktere
                        }
                    }

                    val profileImage = characterDetail.profileImage ?: "default_image.jpg" // Standardbild, falls kein Bild vorhanden ist
                    firestore.collection("users")
                        .document(userId)
                        .collection("active_character")
                        .document(characterDetail.characerId)
                        .set(mapOf(
                            "characterId" to characterDetail.characerId,
                            "name" to characterDetail.name,
                            "profileImage" to profileImage,
                            "isActive" to true // Markiert den Charakter als aktiv
                        ))
                        .addOnSuccessListener {
                            activeCharacterLiveData.value = characterDetail // Setzt das LiveData auf den aktiven Charakter
                        }
                        .addOnFailureListener { e ->
                            activeCharacterLiveData.value = null // Setzt null, falls ein Fehler auftritt
                        }
                }
        }

        return activeCharacterLiveData
    }

    /**
     * Entfernt den Echtzeit-Listener, um unnötige Datenbankabfragen zu verhindern.
     * Diese Methode sollte aufgerufen werden, wenn der Listener nicht mehr benötigt wird, z. B. beim Schließen der Ansicht.
     */
    fun removeListener() {
        listenerRegistration?.remove() // Entfernt den Firestore Listener, falls er registriert ist
    }
}