package com.example.shadowwisper.ui.theme.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail

@Dao
interface CharacterDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(characterDetail: CharacterDetail)

    @Update
    suspend fun updateCharacter(characterDetail: CharacterDetail)

    @Query("SELECT * FROM character_details WHERE id = :characterId")
    fun getCharacterByID(characterId: String): LiveData<CharacterDetail>

    @Query("SELECT * FROM character_details")
    fun getAllCharacters(): LiveData<List<CharacterDetail>>

    @Delete
    suspend fun deleteCharacter(characterDetail: CharacterDetail)
}