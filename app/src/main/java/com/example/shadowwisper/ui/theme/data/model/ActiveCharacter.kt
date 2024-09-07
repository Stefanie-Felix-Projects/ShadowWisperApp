package com.example.shadowwisper.ui.theme.data.model

import com.google.firebase.firestore.PropertyName

data class ActiveCharacter(
    @PropertyName("characterID") val characterId: String = "",
    val name: String = "",
    val profileImage: String? = null,
    val userId: String = "",
    var isActive: Boolean = true
)