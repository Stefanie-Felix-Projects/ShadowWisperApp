/**
 * Datenklasse `Wallet`, die die Struktur einer Wallet-Tabelle in der Datenbank definiert.
 * Diese Klasse wird als Entity in Room verwendet und repräsentiert die Tabelle `wallet_table` in der Datenbank.
 * Sie enthält Informationen über Einnahmen, Ausgaben, Karma-Punkte und die Gesamtsumme des Wallets.
 */
package com.example.shadowwisper.ui.theme.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Repräsentiert die Entität `Wallet` in der Room-Datenbank.
 * Diese Klasse definiert die Spalten der Tabelle `wallet_table` und speichert Informationen zur Wallet-Bilanz.
 * @param id Die eindeutige, automatisch generierte ID des Wallet-Eintrags.
 * @param einnahmen Der Betrag, der als Einnahmen im Wallet verbucht ist.
 * @param ausgaben Der Betrag, der als Ausgaben im Wallet verbucht ist.
 * @param karma Der Karma-Wert, der mit dem Wallet verknüpft ist.
 * @param gesamtsumme Der berechnete Gesamtbetrag (Einnahmen - Ausgaben + Karma) im Wallet.
 */
@Entity(tableName = "wallet_table") // Definiert die Tabelle in der Room-Datenbank
data class Wallet(
    @PrimaryKey(autoGenerate = true) // Automatisch generierte Primärschlüsselspalte für die Wallet-ID
    val id: Int = 0, // Eindeutige ID des Wallet-Eintrags
    val einnahmen: Double, // Betrag der Einnahmen im Wallet
    val ausgaben: Double, // Betrag der Ausgaben im Wallet
    val karma: Double, // Karma-Wert, der mit dem Wallet verknüpft ist
    val gesamtsumme: Double // Gesamtsumme (Einnahmen - Ausgaben + Karma) im Wallet
)