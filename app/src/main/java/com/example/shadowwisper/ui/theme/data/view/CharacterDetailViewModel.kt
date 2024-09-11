/**
 * ViewModel-Klasse `CharacterDetailViewModel`, die zur Verwaltung der UI-bezogenen Daten für Charakterdetails verwendet wird.
 * Diese Klasse verwendet das `CharacterDetailRepository`, um auf die Datenquelle zuzugreifen und Operationen wie Abrufen, Einfügen und Aktualisieren von Charakterdaten auszuführen.
 * Sie ist zuständig für die Logik, die die UI nicht blockiert, und wird im Android-Lebenszyklus der Anwendung verwaltet.
 */
package com.example.shadowwisper.ui.theme.data.view

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.example.shadowwisper.ui.theme.data.repository.CharacterDetailRepository

/**
 * ViewModel zur Verwaltung von Charakterdetails.
 * Diese Klasse greift auf das `CharacterDetailRepository` zu, um die Daten der Charaktere zu laden und zu manipulieren.
 * Sie verwaltet die UI-bezogenen Daten so, dass sie den Lebenszyklus der Anwendung überstehen.
 * @param application Die Anwendung, die den Kontext für das ViewModel bereitstellt.
 */
class CharacterDetailViewModel(application: Application) : AndroidViewModel(application) {

    // Initialisiert das Repository für den Zugriff auf die Charakterdaten.
    private val repository = CharacterDetailRepository()

    /**
     * Holt die Charakterdetails basierend auf der ID.
     * Diese Methode gibt ein `LiveData`-Objekt zurück, das von der UI beobachtet werden kann, um Änderungen zu überwachen.
     * @param id Die eindeutige ID des Charakters, der abgerufen werden soll.
     * @return Ein `LiveData`-Objekt mit den Details des Charakters.
     */
    fun getCharacterById(id: String): LiveData<CharacterDetail> {
        return repository.getCharacterById(id)
    }

    /**
     * Fügt einen neuen Charakter hinzu, indem die Daten an das Repository weitergeleitet werden.
     * @param characterDetail Das `CharacterDetail`-Objekt, das hinzugefügt werden soll.
     */
    fun addCharacter(characterDetail: CharacterDetail) {
        repository.insertCharacter(characterDetail)
    }

    /**
     * Aktualisiert die Daten eines bestehenden Charakters, indem sie an das Repository übergeben werden.
     * @param characterDetail Das aktualisierte `CharacterDetail`-Objekt.
     */
    fun updateCharacter(characterDetail: CharacterDetail) {
        repository.updateCharacter(characterDetail)
    }

    /**
     * Speichert den Charakter und lädt optional ein Profilbild hoch, wenn eine Bild-URI vorhanden ist.
     * Wenn ein neues Bild hochgeladen wird, wird die URL des Bildes in den Charakterdetails aktualisiert.
     * Abhängig davon, ob der Charakter neu ist, wird entweder ein neuer Charakter hinzugefügt oder ein vorhandener aktualisiert.
     * @param characterDetail Das `CharacterDetail`-Objekt, das gespeichert werden soll.
     * @param imageUri Die URI des Bildes, das hochgeladen werden soll, falls vorhanden.
     * @param isNew Boolean, das angibt, ob der Charakter neu ist oder aktualisiert wird.
     */
    fun saveCharacter(characterDetail: CharacterDetail, imageUri: Uri?, isNew: Boolean) {
        if (imageUri != null) {
            repository.uploadImageToStorage(imageUri) { imageUrl ->
                val updatedCharacter = characterDetail.copy(profileImage = imageUrl) // Aktualisiert das Profilbild
                if (isNew) {
                    addCharacter(updatedCharacter) // Fügt den neuen Charakter hinzu
                } else {
                    updateCharacter(updatedCharacter) // Aktualisiert den vorhandenen Charakter
                }
            }
        } else {
            if (isNew) {
                addCharacter(characterDetail) // Fügt den neuen Charakter hinzu, falls er neu ist
            } else {
                updateCharacter(characterDetail) // Aktualisiert den Charakter, falls er existiert
            }
        }
    }
}