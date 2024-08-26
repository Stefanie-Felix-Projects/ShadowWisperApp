package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.LiveData
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shadowwisper.ui.theme.data.database.WalletDatabase
import com.example.shadowwisper.ui.theme.data.model.Wallet
import com.example.shadowwisper.ui.theme.data.repository.WalletRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData

class WalletViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WalletRepository
    private val _latestWallet = MutableLiveData<Wallet?>()
    val latestWallet: LiveData<Wallet?> get() = _latestWallet

    init {
        val walletDao = WalletDatabase.getDatabase(application).walletDao()
        repository = WalletRepository(walletDao)

        viewModelScope.launch {
            val wallet = repository.latestWallet.value
            if (wallet != null) {
                _latestWallet.postValue(wallet)
            } else {
                val initialWallet = Wallet(einnahmen = 0.0, ausgaben = 0.0, karma = 0.0, gesamtsumme = 0.0)
                _latestWallet.postValue(initialWallet)
            }
        }
    }

    fun updateWallet(einnahmen: Double, ausgaben: Double, karma: Double) {
        val gesamtsumme = einnahmen - ausgaben
        val wallet = Wallet(einnahmen = einnahmen, ausgaben = ausgaben, karma = karma, gesamtsumme = gesamtsumme)
        viewModelScope.launch {
            repository.insert(wallet)
            _latestWallet.postValue(wallet)
        }
    }
}