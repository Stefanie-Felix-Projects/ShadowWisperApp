package com.example.shadowwisper.ui.theme.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CharacterRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    fun insert(characterDetail: CharacterDetail) {
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("characters")
                .document(characterDetail.id)
                .set(characterDetail)
                .addOnSuccessListener {
                    Log.d("CharacterRepository", "Character successfully added to Firestore")
                }
                .addOnFailureListener { e ->
                    Log.w("CharacterRepository", "Error adding character", e)
                }
        }
    }

    fun getAllCharacters(): LiveData<List<CharacterDetail>> {
        val charactersLiveData = MutableLiveData<List<CharacterDetail>>()
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("characters")
                .get()
                .addOnSuccessListener { result ->
                    val characters = result.toObjects(CharacterDetail::class.java)
                    charactersLiveData.value = characters
                    Log.d("CharacterRepository", "Characters loaded: ${characters.size}")
                }
                .addOnFailureListener { e ->
                    Log.w("CharacterRepository", "Error loading characters", e)
                }
        }
        return charactersLiveData
    }

    fun getCharacterById(id: String): LiveData<CharacterDetail> {
        val characterLiveData = MutableLiveData<CharacterDetail>()
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("characters")
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    characterLiveData.value = document.toObject(CharacterDetail::class.java)
                }
        }
        return characterLiveData
    }

    fun update(characterDetail: CharacterDetail) {
        insert(characterDetail)
    }

    fun delete(characterDetail: CharacterDetail) {
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("characters")
                .document(characterDetail.id)
                .delete()
        }
    }

    fun setActiveCharacter(characterDetail: CharacterDetail): LiveData<CharacterDetail?> {
        val activeCharacterLiveData = MutableLiveData<CharacterDetail?>()

        if (userId != null) {
            val profileImage = characterDetail.profileImage ?: "default_image.jpg"

            firestore.collection("users")
                .document(userId)
                .collection("active_character")
                .document(characterDetail.id)
                .set(mapOf(
                    "characterId" to characterDetail.id,
                    "name" to characterDetail.name,
                    "profileImage" to profileImage,
                    "isActive" to true
                ))
                .addOnSuccessListener {
                    Log.d("CharacterRepository", "Active character set in user collection")
                }
                .addOnFailureListener { e ->
                    Log.e("CharacterRepository", "Error setting active character in user collection", e)
                }

            firestore.collection("all_active_characters")
                .document(characterDetail.id)
                .set(
                    mapOf(
                        "characterId" to characterDetail.id,
                        "name" to characterDetail.name,
                        "profileImage" to profileImage,
                        "userId" to userId,
                        "isActive" to true
                    )
                )
                .addOnSuccessListener {
                    Log.d("Firestore", "Character successfully added: ${characterDetail.name}")
                    activeCharacterLiveData.value = characterDetail
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error adding character", e)
                    activeCharacterLiveData.value = null
                }
        }

        return activeCharacterLiveData
    }

    fun getActiveCharacter(): LiveData<CharacterDetail?> {
        val activeCharacterLiveData = MutableLiveData<CharacterDetail?>()
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("active_character")
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val document = result.documents[0]
                        activeCharacterLiveData.value = document.toObject(CharacterDetail::class.java)
                    } else {
                        activeCharacterLiveData.value = null
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("CharacterRepository", "Error getting active character", e)
                    activeCharacterLiveData.value = null
                }
        }
        return activeCharacterLiveData
    }
}