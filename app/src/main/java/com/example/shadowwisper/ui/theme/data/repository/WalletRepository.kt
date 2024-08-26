package com.example.shadowwisper.ui.theme.data.repository

import androidx.lifecycle.LiveData
import com.example.shadowwisper.ui.theme.data.dao.WalletDao
import com.example.shadowwisper.ui.theme.data.model.Wallet


class WalletRepository(private val walletDao: WalletDao) {

    val latestWallet: LiveData<Wallet> = walletDao.getLatestWallet()

    suspend fun insert(wallet: Wallet) {
        walletDao.insert(wallet)
    }
}