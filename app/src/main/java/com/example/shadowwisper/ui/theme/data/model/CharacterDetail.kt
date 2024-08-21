package com.example.shadowwisper.ui.theme.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_details")
data class CharacterDetail(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val profileImage: String?,
    val name: String,
    val backgroundStory: String,
    val race: String,
    val skills: String,
    val equipment: String
)
