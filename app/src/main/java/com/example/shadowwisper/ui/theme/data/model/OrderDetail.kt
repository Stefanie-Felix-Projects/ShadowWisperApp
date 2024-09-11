/**
 * Datenklasse `OrderDetail`, die die Struktur einer Bestellungsdetail-Tabelle in der Datenbank definiert.
 * Diese Klasse wird als Entity in Room verwendet und repräsentiert die Tabelle `order_details` in der Datenbank.
 */
package com.example.shadowwisper.ui.theme.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Repräsentiert die Entity `OrderDetail` in der Room-Datenbank.
 * Diese Klasse definiert die Spalten der `order_details`-Tabelle und enthält Informationen zu Bestellungen wie Name, Text, Bilder und Ressourcen.
 * @param id Die eindeutige, automatisch generierte ID der Bestellung.
 * @param orderName Der Name der Bestellung.
 * @param subText Ein kurzer Beschreibungstext zur Bestellung.
 * @param image Eine Referenz auf das Bild der Bestellung (als Ressourcen-ID gespeichert).
 * @param mapImage Eine Referenz auf das Bild der Karte (als Ressourcen-ID gespeichert).
 * @param storyTitle Der Titel der zugehörigen Geschichte.
 * @param storyText Der vollständige Text der Geschichte, die mit der Bestellung verbunden ist.
 * @param karma Ein Wert, der das Karma (Punkte oder Status) der Bestellung darstellt.
 * @param money Der Geldbetrag, der mit der Bestellung verbunden ist.
 * @param profileImage Ein optionales ByteArray, das das Profilbild der Bestellung speichert (z. B. als Binärdaten).
 */
@Entity(tableName = "order_details") // Definiert die Tabelle in der Room-Datenbank
data class OrderDetail(
    @PrimaryKey(autoGenerate = true) // Definiert die Primärschlüsselspalte mit automatischer Generierung der ID
    val id: Int = 0, // Eindeutige ID der Bestellung, die automatisch generiert wird
    val orderName: String, // Name der Bestellung
    val subText: String, // Untertitel oder Beschreibung der Bestellung
    val image: Int, // Referenz auf ein Bild der Bestellung (Ressourcen-ID)
    val mapImage: Int, // Referenz auf ein Bild der Karte (Ressourcen-ID)
    val storyTitle: String, // Titel der Geschichte, die mit der Bestellung verbunden ist
    val storyText: String, // Text der Geschichte, der die Bestellung beschreibt
    val karma: Int, // Karma-Punkte oder Status, die mit der Bestellung verbunden sind
    val money: Int, // Geldbetrag, der mit der Bestellung verbunden ist
    val profileImage: ByteArray? // Optionales Profilbild der Bestellung als Byte-Array
)