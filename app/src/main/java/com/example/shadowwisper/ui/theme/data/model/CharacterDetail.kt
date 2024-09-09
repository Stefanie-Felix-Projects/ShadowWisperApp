package com.example.shadowwisper.ui.theme.data.model

import java.util.UUID

data class CharacterDetail(

    val characerId: String = UUID.randomUUID().toString(),
    var profileImage: String? = null,
    val name: String = "",
    val backgroundStory: String = "",
    val race: String = "",
    val skills: String = "",
    val equipment: String = "",
    val userId: String = "",
    var isActive: Boolean = true
) {

}