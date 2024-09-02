package com.example.shadowwisper.ui.theme.data.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shadowwisper.ui.theme.data.database.CharacterDatabase
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.example.shadowwisper.ui.theme.data.repository.CharacterRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class CharacterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CharacterRepository
    private val _userCharacters = MutableLiveData<List<CharacterDetail>>()
    val userCharacters: LiveData<List<CharacterDetail>> get() = _userCharacters

    init {
        val characterDao = CharacterDatabase.getDatabase(application).characterDetailDao()
        repository = CharacterRepository(characterDao)
        loadUserCharacters()
    }

    private fun loadUserCharacters() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .collection("characters")
            .get()
            .addOnSuccessListener { result ->
                val characters = mutableListOf<CharacterDetail>()
                for (document in result) {
                    val character = document.toObject(CharacterDetail::class.java)
                    characters.add(character)
                }
                _userCharacters.value = characters
            }
            .addOnFailureListener { e ->
            }
    }

    fun insert(characterDetail: CharacterDetail) = viewModelScope.launch {
        repository.insert(characterDetail)
        saveCharacterToFirestore(characterDetail)
        loadUserCharacters()
    }


    private fun saveCharacterToFirestore(characterDetail: CharacterDetail) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .collection("characters")
            .document(characterDetail.id)
            .set(characterDetail)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->

            }
    }

    fun update(characterDetail: CharacterDetail) = viewModelScope.launch {
        repository.update(characterDetail)
        loadUserCharacters()
    }

    fun delete(characterDetail: CharacterDetail) = viewModelScope.launch {
        repository.delete(characterDetail)
        loadUserCharacters()
    }

    fun getCharacterById(id: String): LiveData<CharacterDetail> {
        return repository.getCharacterById(id)
    }

    fun setActiveCharacter(characterDetail: CharacterDetail) = viewModelScope.launch {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
        val activeCharacterRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .collection("active_character")
            .document(characterDetail.id)

        val activeCharacterData = mapOf(
            "characterId" to characterDetail.id,
            "name" to characterDetail.name,
            "profileImage" to characterDetail.profileImage
        )

        activeCharacterRef.set(activeCharacterData)
    }

    fun getActiveCharacter(): LiveData<CharacterDetail?> {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return MutableLiveData(null)
        val activeCharacterRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .collection("active_character")
            .document("activeCharacter")

        val activeCharacterLiveData = MutableLiveData<CharacterDetail?>()

        activeCharacterRef.addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                val activeCharacter = snapshot.toObject(CharacterDetail::class.java)
                activeCharacterLiveData.value = activeCharacter
            } else {
                activeCharacterLiveData.value = null
            }
        }

        return activeCharacterLiveData
    }
}
