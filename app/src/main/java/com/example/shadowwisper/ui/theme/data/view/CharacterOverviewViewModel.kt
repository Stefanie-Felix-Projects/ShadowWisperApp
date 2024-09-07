package com.example.shadowwisper.ui.theme.data.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.example.shadowwisper.ui.theme.data.repository.CharacterOverviewRepository

class CharacterOverviewViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CharacterOverviewRepository()

    val userCharacters: LiveData<List<CharacterDetail>> = repository.getAllCharacters()

    fun setActiveCharacter(characterDetail: CharacterDetail) {
        repository.setActiveCharacter(characterDetail)
    }

    override fun onCleared() {
        super.onCleared()
        repository.removeListener()
    }
}