/**
 * ViewModel-Klasse `CharacterOverviewViewModel`, die für die Verwaltung und Bereitstellung von Charakterübersichten in der Benutzeroberfläche zuständig ist.
 * Diese Klasse verwendet das `CharacterOverviewRepository`, um Daten zu laden und den aktiven Charakter zu setzen.
 * Sie wird im Android-Lebenszyklus verwaltet, um die Daten über UI-Änderungen hinweg konsistent zu halten.
 */
package com.example.shadowwisper.ui.theme.data.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.example.shadowwisper.ui.theme.data.repository.CharacterOverviewRepository

/**
 * ViewModel zur Verwaltung von Charakterübersichten.
 * Es verwendet das `CharacterOverviewRepository`, um alle Charaktere des Benutzers zu laden und den aktiven Charakter zu setzen.
 * @param application Der Anwendungskontext, der für die Lebenszyklusverwaltung des ViewModels verwendet wird.
 */
class CharacterOverviewViewModel(application: Application) : AndroidViewModel(application) {

    // Initialisiert das Repository für den Zugriff auf die Charakterübersicht.
    private val repository = CharacterOverviewRepository()

    /**
     * Ein `LiveData`-Objekt, das eine Liste aller Charaktere des Benutzers enthält.
     * Diese Liste wird automatisch aktualisiert, wenn sich die Charakterdaten im Repository ändern.
     */
    val userCharacters: LiveData<List<CharacterDetail>> = repository.getAllCharacters()

    /**
     * Setzt den angegebenen Charakter als aktiven Charakter.
     * Die Methode ruft die `setActiveCharacter`-Funktion des Repositories auf, um den Charakter in der Datenbank als aktiv zu markieren.
     * @param characterDetail Das `CharacterDetail`-Objekt des Charakters, der als aktiv markiert werden soll.
     */
    fun setActiveCharacter(characterDetail: CharacterDetail) {
        repository.setActiveCharacter(characterDetail)
    }

    /**
     * Wird aufgerufen, wenn das ViewModel endgültig zerstört wird.
     * Diese Methode entfernt den Listener im Repository, um unnötige Firebase-Abfragen zu vermeiden.
     */
    override fun onCleared() {
        super.onCleared()
        repository.removeListener()
    }
}