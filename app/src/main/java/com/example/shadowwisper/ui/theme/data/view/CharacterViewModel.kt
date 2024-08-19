package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.shadowwisper.ui.theme.data.adapter.CharacterItem
import com.syntax_institut.whatssyntax.R

class CharacterViewModel : ViewModel() {

    private val _characterList = MutableLiveData<List<CharacterItem>>()
    val characterList: LiveData<List<CharacterItem>> = _characterList

    init {
        _characterList.value = listOf(
            CharacterItem("Character 1", R.drawable.rahmen, true),
            CharacterItem("Character 2", R.drawable.rahmen, false),
            CharacterItem("Character 3", R.drawable.rahmen, true)
        )
        //ToDo Datenbank anbinden
    }
}