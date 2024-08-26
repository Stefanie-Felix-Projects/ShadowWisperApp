package com.example.shadowwisper.ui.theme.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shadowwisper.ui.theme.data.dao.WalletDao
import com.example.shadowwisper.ui.theme.data.model.Wallet

@Database(entities = [Wallet::class], version = 2, exportSchema = false)
abstract class WalletDatabase : RoomDatabase() {

    abstract fun walletDao(): WalletDao

    companion object {
        @Volatile
        private var INSTANCE: WalletDatabase? = null

        fun getDatabase(context: Context): WalletDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WalletDatabase::class.java,
                    "wallet_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}