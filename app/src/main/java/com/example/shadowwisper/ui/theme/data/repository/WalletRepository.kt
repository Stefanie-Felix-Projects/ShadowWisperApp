/**
 * Repository-Klasse `WalletRepository`, die für den Zugriff auf die Wallet-Daten in der Datenbank verantwortlich ist.
 * Diese Klasse verwendet das `WalletDao`, um auf die Wallet-Tabelle in der Room-Datenbank zuzugreifen und Operationen wie
 * Einfügen und Abrufen der neuesten Wallet-Daten durchzuführen.
 */
package com.example.shadowwisper.ui.theme.data.repository

import androidx.lifecycle.LiveData
import com.example.shadowwisper.ui.theme.data.dao.WalletDao
import com.example.shadowwisper.ui.theme.data.model.Wallet

/**
 * Das `WalletRepository` stellt Methoden zum Einfügen und Abrufen von Wallet-Daten bereit.
 * Es verwendet das `WalletDao`, um SQL-Operationen in der Room-Datenbank durchzuführen.
 * @param walletDao Das DAO-Objekt, das für den Datenbankzugriff auf die Wallet-Daten verwendet wird.
 */
class WalletRepository(private val walletDao: WalletDao) {

    /**
     * Ein LiveData-Objekt, das die neuesten Wallet-Daten aus der Datenbank enthält.
     * Dieses LiveData-Objekt wird automatisch aktualisiert, wenn sich die Daten in der Datenbank ändern.
     */
    val latestWallet: LiveData<Wallet> = walletDao.getLatestWallet()

    /**
     * Fügt eine neue Wallet in die Datenbank ein.
     * Diese Methode wird asynchron ausgeführt, um den Hauptthread nicht zu blockieren.
     * @param wallet Das `Wallet`-Objekt, das in die Datenbank eingefügt werden soll.
     */
    suspend fun insert(wallet: Wallet) {
        walletDao.insert(wallet)
    }
}