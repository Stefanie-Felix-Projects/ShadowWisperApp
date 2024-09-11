/**
 * Datenklasse `OrderOverview`, die eine Übersicht über Bestellungen in der Anwendung definiert.
 * Diese Klasse enthält grundlegende Informationen zu einer Bestellung, wie Titel, Untertitel und Bilder.
 */
package com.example.shadowwisper.ui.theme.data.model

/**
 * Repräsentiert eine Bestellübersicht in der Anwendung.
 * Diese Klasse wird verwendet, um eine Zusammenfassung oder eine Vorschau einer Bestellung anzuzeigen.
 * @param id Die eindeutige ID der Bestellung.
 * @param profileImage Eine Ressourcen-ID, die auf das Profilbild der Bestellung verweist.
 * @param orderTitle Der Titel der Bestellung.
 * @param subTitle Der Untertitel oder eine kurze Beschreibung der Bestellung.
 * @param mapImage Eine Ressourcen-ID, die auf das Bild der Karte verweist.
 */
data class OrderOverview(
    val id: Int, // Eindeutige ID der Bestellung
    val profileImage: Int, // Referenz auf ein Profilbild der Bestellung (Ressourcen-ID)
    val orderTitle: String, // Titel der Bestellung
    val subTitle: String, // Untertitel oder kurze Beschreibung der Bestellung
    val mapImage: Int // Referenz auf das Bild der Karte (Ressourcen-ID)
)