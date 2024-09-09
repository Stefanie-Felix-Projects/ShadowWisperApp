package com.example.shadowwisper.ui.theme.data.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shadowwisper.ui.theme.data.model.ActiveCharacter
import com.example.shadowwisper.ui.theme.data.repository.ChatOverviewRepository
import com.google.firebase.auth.FirebaseAuth

class ChatOverviewViewModel : ViewModel() {

    private val repository = ChatOverviewRepository()

    private val _availableCharacters = MutableLiveData<List<ActiveCharacter>>()
    val availableCharacters: LiveData<List<ActiveCharacter>> = _availableCharacters

    private val _currentCharacterId = MutableLiveData<String>()
    val currentCharacterId: LiveData<String> = _currentCharacterId

    val currentUserId: String by lazy {
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    }

    fun loadActiveCharacters() {
        repository.getActiveCharacters(currentUserId, { characters ->
            _availableCharacters.value = characters
        }, { exception ->
        })
    }

    fun loadCurrentCharacterId() {
        Log.d("ChatOverviewViewModel", "Aktueller Benutzer: $currentUserId")

        repository.getCurrentCharacterForUser(currentUserId, { character ->
            if (character.characterId.isNotEmpty()) {
                _currentCharacterId.value = character.characterId
                Log.d("ChatOverviewViewModel", "Character-ID erfolgreich geladen: ${character.characterId}")
            } else {
                Log.e("ChatOverviewViewModel", "Character-ID ist leer.")
            }
        }, { exception ->
            Log.e("ChatOverviewViewModel", "Fehler beim Laden der Character-ID: $exception")
        })
    }
}