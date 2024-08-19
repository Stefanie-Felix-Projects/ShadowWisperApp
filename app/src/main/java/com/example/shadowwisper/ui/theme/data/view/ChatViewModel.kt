package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.ViewModel
import com.example.shadowwisper.ui.theme.data.adapter.ChatItem

class ChatViewModel : ViewModel() {

    fun getChatList(): List<ChatItem> {
        return listOf(
            ChatItem("Character 1", "Character 1 Information"),
            ChatItem("Character 2", "Character 2 Information"),
            ChatItem("Character 3", "Character 3 Information")
        )
    }

    //ToDo Datenbank anbinden
}