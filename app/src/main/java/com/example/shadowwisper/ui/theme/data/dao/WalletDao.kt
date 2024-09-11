/**
 * DAO (Data Access Object) für den Zugriff auf die Tabelle `wallet_table` in der Datenbank.
 * Diese Schnittstelle enthält Methoden zum Einfügen und Abrufen von Wallet-Daten.
 * Sie dient als Vermittler zwischen der Datenbank und der Anwendung, um den Zugriff auf die Daten zu erleichtern.
 */
package com.example.shadowwisper.ui.theme.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shadowwisper.ui.theme.data.model.Wallet

/**
 * Das WalletDao-Interface bietet Methoden zur Verwaltung von Wallet-Daten in der Datenbank.
 * Es arbeitet mit Room, um SQL-Operationen zu kapseln, die das Einfügen und Abrufen von Wallet-Daten betreffen.
 */
@Dao
interface WalletDao {

    /**
     * Fügt ein Wallet-Objekt in die `wallet_table` ein.
     * Falls bereits ein Eintrag mit demselben Primärschlüssel existiert, wird dieser durch den neuen Eintrag ersetzt.
     * Diese Methode wird asynchron (suspend) ausgeführt.
     * @param wallet Das Wallet-Objekt, das in die Datenbank eingefügt werden soll.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallet: Wallet)

    /**
     * Ruft das neueste Wallet aus der `wallet_table` ab, basierend auf der höchsten ID.
     * Gibt das Ergebnis als `LiveData` zurück, sodass es in der UI beobachtet werden kann.
     * @return Ein `LiveData`-Objekt, das das zuletzt hinzugefügte Wallet enthält.
     */
    @Query("SELECT * FROM wallet_table ORDER BY id DESC LIMIT 1")
    fun getLatestWallet(): LiveData<Wallet>
}