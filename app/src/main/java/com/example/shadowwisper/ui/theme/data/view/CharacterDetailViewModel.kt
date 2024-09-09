package com.example.shadowwisper.ui.theme.data.view

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.example.shadowwisper.ui.theme.data.repository.CharacterDetailRepository

class CharacterDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CharacterDetailRepository()

    fun getCharacterById(id: String): LiveData<CharacterDetail> {
        return repository.getCharacterById(id)
    }

    fun addCharacter(characterDetail: CharacterDetail) {
        repository.insertCharacter(characterDetail)
    }

    fun updateCharacter(characterDetail: CharacterDetail) {
        repository.updateCharacter(characterDetail)
    }

    fun deleteCharacter(characterDetail: CharacterDetail) {
        repository.deleteCharacter(characterDetail)
    }

    fun saveCharacter(characterDetail: CharacterDetail, imageUri: Uri?, isNew: Boolean) {
        if (imageUri != null) {
            repository.uploadImageToStorage(imageUri) { imageUrl ->
                val updatedCharacter = characterDetail.copy(profileImage = imageUrl)
                if (isNew) {
                    addCharacter(updatedCharacter)
                } else {
                    updateCharacter(updatedCharacter)
                }
            }
        } else {
            if (isNew) {
                addCharacter(characterDetail)
            } else {
                updateCharacter(characterDetail)
            }
        }
    }
}