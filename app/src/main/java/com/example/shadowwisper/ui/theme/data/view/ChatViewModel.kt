package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.ViewModel
import com.example.shadowwisper.ui.theme.data.model.ChatOverview

class ChatViewModel : ViewModel() {

    fun getChatList(): List<ChatOverview> {
        return listOf(
            ChatOverview("Character 1"),
            ChatOverview("Character 2"),
            ChatOverview("Character 3")
        )
    }

    //ToDo Datenbank anbinden
}