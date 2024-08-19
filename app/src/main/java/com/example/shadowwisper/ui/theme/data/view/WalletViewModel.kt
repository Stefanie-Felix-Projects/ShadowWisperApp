package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WalletViewModel : ViewModel() {

    private val _kontostand = MutableLiveData<Double>()
    val kontostand: LiveData<Double> = _kontostand

    private val _karmapunkte = MutableLiveData<Double>()
    val karmapunkte: LiveData<Double> = _karmapunkte

    private val _edgepunkte = MutableLiveData<Double>()
    val edgepunkte: LiveData<Double> = _edgepunkte

    init {
        // Initiale Daten festlegen
        _kontostand.value = 1000.0
        _karmapunkte.value = 500.0
        _edgepunkte.value = 200.0
    }

    fun updateWallet(kontostand: Double, karmapunkte: Double, edgepunkte: Double) {
        _kontostand.value = kontostand
        _karmapunkte.value = karmapunkte
        _edgepunkte.value = edgepunkte
    }
}