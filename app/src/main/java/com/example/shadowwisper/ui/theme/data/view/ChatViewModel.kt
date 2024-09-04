package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.example.shadowwisper.ui.theme.data.repository.ChatRepository

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()
    val chatList: LiveData<List<CharacterDetail>> = repository.getAllActiveCharacters()
}