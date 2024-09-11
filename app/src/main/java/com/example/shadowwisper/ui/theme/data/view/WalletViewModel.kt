/**
 * ViewModel-Klasse `WalletViewModel`, die für die Verwaltung und Aktualisierung der Wallet-Daten in der Benutzeroberfläche zuständig ist.
 * Diese Klasse verwendet das `WalletRepository`, um auf die Wallet-Daten zuzugreifen und Änderungen zu speichern.
 * Die Daten werden über den Lebenszyklus der Anwendung hinweg konsistent gehalten.
 */
package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shadowwisper.ui.theme.data.model.Wallet
import com.example.shadowwisper.ui.theme.data.repository.WalletRepository
import kotlinx.coroutines.launch

/**
 * ViewModel zur Verwaltung der Wallet-Daten.
 * Diese Klasse verwendet das `WalletRepository`, um die neuesten Wallet-Daten abzurufen und Aktualisierungen vorzunehmen.
 * @param repository Das Repository, das für den Datenzugriff und die Datenverwaltung verwendet wird.
 */
class WalletViewModel(
    private val repository: WalletRepository
) : ViewModel() {

    /**
     * Ein `LiveData`-Objekt, das die neuesten Wallet-Daten enthält.
     * Die Daten werden automatisch aktualisiert, wenn sich die Wallet-Daten im Repository ändern.
     */
    val latestWallet: LiveData<Wallet> = repository.latestWallet

    /**
     * Aktualisiert die Wallet-Daten mit neuen Einnahmen, Ausgaben und Karma-Werten.
     * Die Methode berechnet die Gesamtsumme basierend auf den Einnahmen und Ausgaben und erstellt ein neues `Wallet`-Objekt.
     * Dieses Wallet wird dann in die Datenbank eingefügt.
     * Die Operation wird im `viewModelScope` ausgeführt, um asynchron zu arbeiten und den Hauptthread nicht zu blockieren.
     * @param einnahmen Die neuen Einnahmen für die Wallet.
     * @param ausgaben Die neuen Ausgaben für die Wallet.
     * @param karma Der neue Karma-Wert für die Wallet.
     */
    fun updateWallet(einnahmen: Double, ausgaben: Double, karma: Double) {
        val gesamtsumme = einnahmen - ausgaben // Berechnet die Gesamtsumme (Einnahmen - Ausgaben)
        val wallet = Wallet(einnahmen = einnahmen, ausgaben = ausgaben, karma = karma, gesamtsumme = gesamtsumme)

        // Führen die Einfügeoperation in einem separaten Koroutinen-Thread aus
        viewModelScope.launch {
            repository.insert(wallet)
        }
    }
}