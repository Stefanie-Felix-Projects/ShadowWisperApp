package com.example.shadowwisper.ui.theme.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class CharacterDetailRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    fun uploadImageToStorage(uri: Uri, onSuccess: (String) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().getReference("character_images/${UUID.randomUUID()}")
        storageReference.putFile(uri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString())
                }
            }
            .addOnFailureListener {
                Log.e("CharacterDetail", "Error uploading image", it)
            }
    }

    fun insert(characterDetail: CharacterDetail) {
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("characters")
                .document(characterDetail.characerId)
                .set(characterDetail)
        }
    }

    fun update(characterDetail: CharacterDetail) {
        insert(characterDetail)
    }

    fun delete(characterDetail: CharacterDetail) {
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("characters")
                .document(characterDetail.characerId)
                .delete()
        }
    }

    // Hier fügst du die Methode `getCharacterById()` hinzu
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
                .addOnFailureListener { e ->
                    Log.e("CharacterDetailRepository", "Error getting character by id", e)
                }
        }
        return characterLiveData
    }
}