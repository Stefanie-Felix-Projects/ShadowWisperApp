package com.example.shadowwisper.ui.theme.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shadowwisper.ui.theme.data.model.Wallet

@Dao
interface WalletDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallet: Wallet)

    @Query("SELECT * FROM wallet_table ORDER BY id DESC LIMIT 1")
    fun getLatestWallet(): LiveData<Wallet>
}