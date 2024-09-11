/**
 * Datenklasse `CharacterDetail`, die die Datenstruktur eines Charakters in der Anwendung definiert.
 * Diese Klasse enthält Informationen wie Charakter-ID, Profilbild, Name, Hintergrundgeschichte, Rasse, Fähigkeiten und Ausrüstung.
 * Sie wird verwendet, um die Charakterdetails in der App zu speichern und zu verwalten.
 */
package com.example.shadowwisper.ui.theme.data.model

import java.util.UUID

/**
 * Repräsentiert die Details eines Charakters in der Anwendung.
 * Die Klasse erstellt automatisch eine eindeutige ID für jeden Charakter und ermöglicht die Speicherung von Attributen wie Name, Rasse und Fähigkeiten.
 * @param characerId Die eindeutige ID des Charakters, die automatisch generiert wird, wenn kein Wert übergeben wird.
 * @param profileImage Eine optionale String-URL zum Profilbild des Charakters. Sie kann `null` sein, falls kein Bild vorhanden ist.
 * @param name Der Name des Charakters.
 * @param backgroundStory Die Hintergrundgeschichte des Charakters als String.
 * @param race Die Rasse des Charakters (z. B. Mensch, Elf).
 * @param skills Eine String-Beschreibung der Fähigkeiten des Charakters.
 * @param equipment Eine String-Beschreibung der Ausrüstung des Charakters.
 * @param userId Die ID des Benutzers, der den Charakter erstellt hat.
 * @param isActive Ein Boolean-Wert, der angibt, ob der Charakter aktiv ist. Standardmäßig ist der Charakter aktiv (`true`).
 */
data class CharacterDetail(

    val characerId: String = UUID.randomUUID().toString(), // Eindeutige ID des Charakters, automatisch generiert durch UUID
    var profileImage: String? = null, // Optionale URL zum Profilbild des Charakters, kann null sein
    val name: String = "", // Name des Charakters
    val backgroundStory: String = "", // Hintergrundgeschichte des Charakters
    val race: String = "", // Rasse des Charakters (z.B. Mensch, Elf)
    val skills: String = "", // Fähigkeiten des Charakters als String
    val equipment: String = "", // Ausrüstung des Charakters als String
    val userId: String = "", // ID des Benutzers, der den Charakter besitzt
    var isActive: Boolean = true // Gibt an, ob der Charakter aktiv ist (standardmäßig true)
)