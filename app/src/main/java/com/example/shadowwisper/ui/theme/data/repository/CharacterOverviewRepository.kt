package com.example.shadowwisper.ui.theme.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class CharacterOverviewRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var listenerRegistration: ListenerRegistration? = null

    fun getAllCharacters(): LiveData<List<CharacterDetail>> {
        val charactersLiveData = MutableLiveData<List<CharacterDetail>>()
        if (userId != null) {
            // Start listening for real-time updates from Firestore
            listenerRegistration = firestore.collection("users")
                .document(userId)
                .collection("characters")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w("CharacterOverviewRepo", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshots != null) {
                        val characters = snapshots.toObjects(CharacterDetail::class.java)
                        charactersLiveData.value = characters
                    }
                }
        }
        return charactersLiveData
    }

    fun setActiveCharacter(characterDetail: CharacterDetail): LiveData<CharacterDetail?> {
        val activeCharacterLiveData = MutableLiveData<CharacterDetail?>()

        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("active_character")
                .get()
                .addOnSuccessListener { result ->
                    // Alle anderen Charaktere deaktivieren
                    for (document in result) {
                        if (document.id != characterDetail.characerId) {
                            document.reference.update("isActive", false)
                        }
                    }

                    // Den ausgewählten Charakter aktivieren
                    val profileImage = characterDetail.profileImage ?: "default_image.jpg"
                    firestore.collection("users")
                        .document(userId)
                        .collection("active_character")
                        .document(characterDetail.characerId)
                        .set(mapOf(
                            "characterId" to characterDetail.characerId,
                            "name" to characterDetail.name,
                            "profileImage" to profileImage,
                            "isActive" to true
                        ))
                        .addOnSuccessListener {
                            activeCharacterLiveData.value = characterDetail
                        }
                        .addOnFailureListener { e ->
                            activeCharacterLiveData.value = null
                        }
                }
        }

        return activeCharacterLiveData
    }

    // Make sure to remove the listener to avoid memory leaks
    fun removeListener() {
        listenerRegistration?.remove()
    }
}