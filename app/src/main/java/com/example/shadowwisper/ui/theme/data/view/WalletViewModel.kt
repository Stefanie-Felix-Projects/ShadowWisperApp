package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shadowwisper.ui.theme.data.model.Wallet
import com.example.shadowwisper.ui.theme.data.repository.WalletRepository
import kotlinx.coroutines.launch

class WalletViewModel(
    private val repository: WalletRepository
) : ViewModel() {

    val latestWallet: LiveData<Wallet> = repository.latestWallet

    fun updateWallet(einnahmen: Double, ausgaben: Double, karma: Double) {
        val gesamtsumme = einnahmen - ausgaben
        val wallet = Wallet(einnahmen = einnahmen, ausgaben = ausgaben, karma = karma, gesamtsumme = gesamtsumme)
        viewModelScope.launch {
            repository.insert(wallet)
        }
    }
}