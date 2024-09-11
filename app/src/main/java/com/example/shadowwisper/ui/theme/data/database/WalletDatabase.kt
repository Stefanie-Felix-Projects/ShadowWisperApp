/**
 * Definition der Room-Datenbank für die Speicherung und Verwaltung von Wallet-Daten.
 * Diese Klasse erstellt eine Singleton-Instanz der Datenbank und stellt den Zugriff auf das WalletDao bereit.
 */
package com.example.shadowwisper.ui.theme.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shadowwisper.ui.theme.data.dao.WalletDao
import com.example.shadowwisper.ui.theme.data.model.Wallet

/**
 * Deklariert die WalletDatabase-Klasse als Room-Datenbank.
 * Die Datenbank enthält eine Tabelle, die durch die `Wallet`-Entität definiert ist.
 * Die Version der Datenbank ist 3, und `exportSchema` ist auf `false` gesetzt, um kein Schema zu exportieren.
 */
@Database(entities = [Wallet::class], version = 3, exportSchema = false)
abstract class WalletDatabase : RoomDatabase() {

    /**
     * Abstrakte Methode, die das DAO (Data Access Object) für Wallet-Daten bereitstellt.
     * @return Eine Instanz von WalletDao, die für den Zugriff auf die Wallet-Tabelle verwendet wird.
     */
    abstract fun walletDao(): WalletDao

    /**
     * Companion-Object, das eine Singleton-Instanz der WalletDatabase enthält.
     * Diese sorgt dafür, dass nur eine Instanz der Datenbank zur Laufzeit existiert.
     */
    companion object {
        @Volatile
        private var INSTANCE: WalletDatabase? = null

        /**
         * Gibt die Singleton-Instanz der WalletDatabase zurück.
         * Falls keine Instanz existiert, wird sie synchron erstellt.
         * @param context Der Kontext der Anwendung, um den Datenbankzugriff zu ermöglichen.
         * @return Eine Instanz der WalletDatabase.
         */
        fun getDatabase(context: Context): WalletDatabase {
            return INSTANCE ?: synchronized(this) {
                // Erstellt eine neue Instanz der Room-Datenbank, falls noch keine existiert
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WalletDatabase::class.java,
                    "wallet_database" // Name der Datenbank
                )
                    .fallbackToDestructiveMigration() // Nutzt destruktive Migration bei einem Versionswechsel
                    .build()
                INSTANCE = instance
                instance // Gibt die neu erstellte Instanz zurück
            }
        }
    }
}