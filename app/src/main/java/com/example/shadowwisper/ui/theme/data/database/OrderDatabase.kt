/**
 * Definition der Room-Datenbank für die Speicherung und Verwaltung von OrderDetail-Daten.
 * Diese Klasse erstellt eine Singleton-Instanz der Datenbank und stellt den Zugriff auf das OrderDetailDao bereit.
 */
package com.example.shadowwisper.ui.theme.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shadowwisper.ui.theme.data.dao.OrderDetailDao
import com.example.shadowwisper.ui.theme.data.model.OrderDetail

/**
 * Deklariert die OrderDatabase-Klasse als Room-Datenbank.
 * Die Datenbank enthält eine Tabelle, die durch die `OrderDetail`-Entität definiert ist.
 * Die Version der Datenbank ist 3, und `exportSchema` ist auf `false` gesetzt, um kein Schema zu exportieren.
 */
@Database(entities = [OrderDetail::class], version = 3, exportSchema = false)
abstract class OrderDatabase : RoomDatabase() {

    /**
     * Abstrakte Methode, die das DAO (Data Access Object) für OrderDetail bereitstellt.
     * @return Eine Instanz von OrderDetailDao, die für den Zugriff auf die OrderDetail-Tabelle verwendet wird.
     */
    abstract fun orderDetailDao(): OrderDetailDao

    /**
     * Companion-Object, das eine Singleton-Instanz der OrderDatabase enthält.
     * Diese sorgt dafür, dass nur eine Instanz der Datenbank zur Laufzeit existiert.
     */
    companion object {
        @Volatile
        private var INSTANCE: OrderDatabase? = null

        /**
         * Gibt die Singleton-Instanz der OrderDatabase zurück.
         * Falls keine Instanz existiert, wird sie synchron erstellt.
         * @param context Der Kontext der Anwendung, um den Datenbankzugriff zu ermöglichen.
         * @return Eine Instanz der OrderDatabase.
         */
        fun getDatabase(context: Context): OrderDatabase {
            return INSTANCE ?: synchronized(this) {
                // Erstellt eine neue Instanz der Room-Datenbank, falls noch keine existiert
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OrderDatabase::class.java,
                    "order_database" // Name der Datenbank
                )
                    .fallbackToDestructiveMigration() // Nutzt destruktive Migration bei einem Versionswechsel
                    .build()
                INSTANCE = instance
                instance // Gibt die neu erstellte Instanz zurück
            }
        }
    }
}