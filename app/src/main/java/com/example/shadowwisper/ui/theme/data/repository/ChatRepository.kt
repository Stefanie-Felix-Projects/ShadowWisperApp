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
                .collection("characters")
                .get()
                .addOnSuccessListener { result ->
                    val characters = result.toObjects(CharacterDetail::class.java)
                    charactersLiveData.value = characters
                }
        }
        return charactersLiveData
    }
}