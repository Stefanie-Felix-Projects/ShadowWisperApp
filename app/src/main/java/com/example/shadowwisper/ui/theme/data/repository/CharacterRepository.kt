package com.example.shadowwisper.ui.theme.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CharacterRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

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

    fun insert(characterDetail: CharacterDetail) {
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("characters")
                .document(characterDetail.id)
                .set(characterDetail)
        }
    }

    fun update(characterDetail: CharacterDetail) {
        insert(characterDetail) // In Firestore ist `set` sowohl für Insert als auch Update
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

    fun setActiveCharacter(characterDetail: CharacterDetail) {
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("active_character")
                .document(characterDetail.id)
                .set(mapOf(
                    "characterId" to characterDetail.id,
                    "name" to characterDetail.name,
                    "profileImage" to characterDetail.profileImage
                ))
        }
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
        }
        return activeCharacterLiveData
    }
}