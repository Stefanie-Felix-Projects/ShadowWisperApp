package com.example.shadowwisper.ui.theme.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    fun getAllChatDetails(): LiveData<List<CharacterDetail>> {
        val charactersLiveData = MutableLiveData<List<CharacterDetail>>()
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("active_character")
                .get()
                .addOnSuccessListener { result ->
                    val characters = result.toObjects(CharacterDetail::class.java)
                    charactersLiveData.value = characters
                }
        }
        return charactersLiveData
    }

    fun addOrUpdateActiveCharacter(character: CharacterDetail) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val characterData = hashMapOf(
            "characterId" to character.id,
            "name" to character.name,
            "profileImage" to character.profileImage,
            "userId" to currentUserId,
            "isActive" to true
        )

        firestore.collection("all_active_characters")
            .document(character.id)
            .set(characterData)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun getAllActiveCharacters(): LiveData<List<CharacterDetail>> {
        val charactersLiveData = MutableLiveData<List<CharacterDetail>>()
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        firestore.collection("all_active_characters")
            .whereEqualTo("isActive", true)
            .get()
            .addOnSuccessListener { result ->
                val characters = result.toObjects(CharacterDetail::class.java)

                val filteredCharacters = characters.filter { it.userId != currentUserId }
                charactersLiveData.value = filteredCharacters
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }

        return charactersLiveData
    }
}