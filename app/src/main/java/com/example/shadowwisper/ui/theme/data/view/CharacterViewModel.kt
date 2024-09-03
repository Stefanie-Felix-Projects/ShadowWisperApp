package com.example.shadowwisper.ui.theme.data.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.example.shadowwisper.ui.theme.data.repository.CharacterRepository

class CharacterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CharacterRepository()

    val userCharacters: LiveData<List<CharacterDetail>> = repository.getAllCharacters()

    fun getCharacterById(id: String): LiveData<CharacterDetail> {
        return repository.getCharacterById(id)
    }

    fun insert(characterDetail: CharacterDetail) {
        repository.insert(characterDetail)
    }

    fun update(characterDetail: CharacterDetail) {
        repository.update(characterDetail)
    }

    fun delete(characterDetail: CharacterDetail) {
        repository.delete(characterDetail)
    }

    fun setActiveCharacter(characterDetail: CharacterDetail) {
        repository.setActiveCharacter(characterDetail)
    }

    fun getActiveCharacter(): LiveData<CharacterDetail?> {
        return repository.getActiveCharacter()
    }
}
