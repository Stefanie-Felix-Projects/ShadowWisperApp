package com.example.shadowwisper.ui.theme.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail

class CharacterDetailRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    fun insertCharacter(characterDetail: CharacterDetail) {
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("characters")
                .document(characterDetail.characerId)
                .set(characterDetail)
                .addOnSuccessListener {
                    Log.d("CharacterDetailRepository", "Character successfully added!")
                }
                .addOnFailureListener { e ->
                    Log.e("CharacterDetailRepository", "Error adding character", e)
                }
        }
    }

    fun updateCharacter(characterDetail: CharacterDetail) {
        insertCharacter(characterDetail)
    }


    fun deleteCharacter(characterDetail: CharacterDetail) {
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .collection("characters")
                .document(characterDetail.characerId)
                .delete()
                .addOnSuccessListener {
                    Log.d("CharacterDetailRepository", "Character successfully deleted!")
                }
                .addOnFailureListener { e ->
                    Log.e("CharacterDetailRepository", "Error deleting character", e)
                }
        }
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
                .addOnFailureListener { e ->
                    Log.e("CharacterDetailRepository", "Error getting character by id", e)
                }
        }
        return characterLiveData
    }

    fun uploadImageToStorage(uri: Uri, onSuccess: (String) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().getReference("character_images/${Uri.parse(uri.lastPathSegment)}")
        storageReference.putFile(uri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString())
                }
            }
            .addOnFailureListener { e ->
                Log.e("CharacterDetailRepository", "Error uploading image", e)
            }
    }
}