/**
 * Repository-Klasse `CharacterDetailRepository`, die für die Verwaltung von Charakterdaten verantwortlich ist.
 * Diese Klasse stellt Methoden zum Einfügen, Aktualisieren, Abrufen und Hochladen von Charakterbildern bereit und arbeitet mit Firebase Firestore und Firebase Storage.
 */
package com.example.shadowwisper.ui.theme.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shadowwisper.ui.theme.data.model.ActiveCharacter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail

/**
 * Diese Klasse verwaltet Charakterdetails und interagiert mit Firebase Firestore und Firebase Storage.
 * Sie stellt Methoden zum Einfügen, Aktualisieren und Abrufen von Charakterdaten sowie zum Hochladen von Bildern bereit.
 */
class CharacterDetailRepository {

    // Initialisiert Firebase Firestore und holt die ID des aktuellen Benutzers.
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    /**
     * Fügt einen neuen Charakter in die Firebase Firestore-Datenbank ein.
     * Wenn der Benutzer angemeldet ist, wird der Charakter unter seiner Benutzer-ID gespeichert.
     * @param characterDetail Das zu speichernde Charakterdetail.
     */
    fun insertCharacter(characterDetail: CharacterDetail) {
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("characters")
                .document(characterDetail.characerId)
                .set(characterDetail)
                .addOnSuccessListener {
                    Log.d("CharacterDetailRepository", "Character successfully added!") // Erfolgreiche Speicherung wird protokolliert.
                }
                .addOnFailureListener { e ->
                    Log.e("CharacterDetailRepository", "Error adding character", e) // Fehler beim Speichern wird protokolliert.
                }
        }
    }

    /**
     * Aktualisiert einen bestehenden Charakter in der Firebase Firestore-Datenbank.
     * Diese Methode ruft intern die `insertCharacter`-Methode auf, um den Charakter zu aktualisieren.
     * @param characterDetail Die aktualisierten Charakterdaten.
     */
    fun updateCharacter(characterDetail: CharacterDetail) {
        insertCharacter(characterDetail) // Aktualisierung durch erneutes Einfügen des Charakters.
    }

    /**
     * Ruft einen Charakter basierend auf der ID aus Firebase Firestore ab und gibt ihn als `LiveData` zurück.
     * @param id Die ID des Charakters, der abgerufen werden soll.
     * @return Ein `LiveData`-Objekt, das das Charakterdetail enthält.
     */
    fun getCharacterById(id: String): LiveData<CharacterDetail> {
        val characterLiveData = MutableLiveData<CharacterDetail>()

        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("characters")
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    characterLiveData.value = document.toObject(CharacterDetail::class.java) // Charakterdaten werden zugeordnet.
                }
                .addOnFailureListener { e ->
                    Log.e("CharacterDetailRepository", "Error getting character by id", e) // Fehler beim Abrufen wird protokolliert.
                }
        }
        return characterLiveData // Gibt das LiveData-Objekt zurück, das den Charakter enthält.
    }

    /**
     * Lädt ein Bild in Firebase Storage hoch und gibt die URL des hochgeladenen Bildes zurück.
     * @param uri Die URI der Bilddatei, die hochgeladen werden soll.
     * @param onSuccess Callback-Funktion, die die URL des Bildes zurückgibt.
     */
    fun uploadImageToStorage(uri: Uri, onSuccess: (String) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().getReference("character_images/${Uri.parse(uri.lastPathSegment)}")
        storageReference.putFile(uri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString()) // Erfolgreicher Upload: URL wird zurückgegeben.
                }
            }
            .addOnFailureListener { e ->
                Log.e("CharacterDetailRepository", "Error uploading image", e) // Fehler beim Hochladen wird protokolliert.
            }
    }

    /**
     * Ruft einen aktiven Charakter basierend auf seiner ID aus Firebase Firestore ab und gibt ihn als `LiveData` zurück.
     * @param characterId Die ID des aktiven Charakters, der abgerufen werden soll.
     * @return Ein `LiveData`-Objekt, das den aktiven Charakter enthält oder `null`, falls ein Fehler auftritt.
     */
    fun getActiveCharacterById(characterId: String): LiveData<ActiveCharacter?> {
        val activeCharacterLiveData = MutableLiveData<ActiveCharacter?>()

        firestore.collection("all_active_characters")
            .document(characterId)
            .get()
            .addOnSuccessListener { document ->
                val character = document.toObject(ActiveCharacter::class.java)
                activeCharacterLiveData.value = character // Erfolgreich abgerufener Charakter wird zugeordnet.
            }
            .addOnFailureListener { e ->
                Log.e("CharacterDetailRepository", "Fehler beim Abrufen des Charakters", e) // Fehlerprotokollierung
                activeCharacterLiveData.value = null // Fehlerfall: null wird explizit gesetzt.
            }

        return activeCharacterLiveData // Gibt das LiveData-Objekt zurück, das den aktiven Charakter enthält.
    }
}