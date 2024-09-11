/**
 * Datenklasse `ActiveCharacter`, die die Datenstruktur eines aktiven Charakters definiert.
 * Diese Klasse wird für die Speicherung und Verwaltung von Charakterdaten verwendet, die aus Firebase Firestore geladen werden.
 */
package com.example.shadowwisper.ui.theme.data.model

import com.google.firebase.firestore.PropertyName

/**
 * Repräsentiert einen aktiven Charakter in der Anwendung.
 * Die Klasse verwendet Firebase Firestore-Anmerkungen, um die benutzerdefinierten Feldnamen zu handhaben.
 * @param characterId Die eindeutige ID des Charakters, die in Firebase als `characterID` gespeichert wird.
 * @param name Der Name des Charakters.
 * @param profileImage Ein optionaler String, der die URL des Profilbildes des Charakters enthält.
 * @param userId Die ID des Benutzers, der den Charakter besitzt.
 * @param isActive Ein Boolean-Wert, der angibt, ob der Charakter aktiv ist.
 */
data class ActiveCharacter(
    @PropertyName("characterID") val characterId: String = "", // Eindeutige ID des Charakters, wird in Firebase als "characterID" gespeichert
    val name: String = "", // Der Name des Charakters
    val profileImage: String? = null, // Optionale URL zum Profilbild des Charakters
    val userId: String = "", // Die ID des Benutzers, dem der Charakter gehört
    var isActive: Boolean = true // Gibt an, ob der Charakter aktiv ist
)