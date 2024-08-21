package com.example.shadowwisper.ui.theme.data.repository

import androidx.lifecycle.LiveData
import com.example.shadowwisper.ui.theme.data.dao.CharacterDetailDao
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail

class CharacterRepository(private val characterDetailDao: CharacterDetailDao) {

    val allCharacters: LiveData<List<CharacterDetail>> = characterDetailDao.getAllCharacters()

    suspend fun insert(characterDetail: CharacterDetail) {
        characterDetailDao.insertCharacter(characterDetail)
    }

    suspend fun update(characterDetail: CharacterDetail) {
        characterDetailDao.updateCharacter(characterDetail)
    }

    suspend fun delete(characterDetail: CharacterDetail) {
        characterDetailDao.deleteCharacter(characterDetail)
    }

    fun getCharacterById(id: Int): LiveData<CharacterDetail> {
        return characterDetailDao.getCharacterByID(id)
    }
}