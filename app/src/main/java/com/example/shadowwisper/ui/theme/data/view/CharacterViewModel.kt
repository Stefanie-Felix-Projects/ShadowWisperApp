package com.example.shadowwisper.ui.theme.data.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.shadowwisper.ui.theme.data.database.CharacterDatabase
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.example.shadowwisper.ui.theme.data.repository.CharacterRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class CharacterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CharacterRepository
    val allCharacters: LiveData<List<CharacterDetail>>

    init {
        val characterDao = CharacterDatabase.getDatabase(application).characterDetailDao()
        repository = CharacterRepository(characterDao)
        allCharacters = repository.allCharacters
    }

    fun insert(characterDetail: CharacterDetail) = viewModelScope.launch {
        repository.insert(characterDetail)
    }

    fun update(characterDetail: CharacterDetail) = viewModelScope.launch {
        repository.update(characterDetail)
    }

    fun delete(characterDetail: CharacterDetail) = viewModelScope.launch {
        repository.delete(characterDetail)
    }

    fun getCharacterById(id: String): LiveData<CharacterDetail> {
        return repository.getCharacterById(id)
    }

    fun toggleCharacterInChat(characterDetail: CharacterDetail, isInChat: Boolean) {
        val chatCollection = FirebaseFirestore.getInstance().collection("chats")
        if (isInChat) {
            chatCollection.document(characterDetail.id)
                .set(
                    mapOf(
                        "senderId" to characterDetail.id,  // Verwende dieselbe ID
                        "name" to characterDetail.name,
                        "profileImage" to characterDetail.profileImage
                    )
                )
        } else {
            chatCollection.document(characterDetail.id).delete()
        }
    }
}