package com.example.shadowwisper.ui.theme.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "character_details")
data class CharacterDetail(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val profileImage: String? = null,
    val name: String = "",
    val backgroundStory: String = "",
    val race: String = "",
    val skills: String = "",
    val equipment: String = "",
    val userId: String = "",
    val isActive: Boolean = true
) {
    constructor() : this(UUID.randomUUID().toString(), null, "", "", "", "", "")
}