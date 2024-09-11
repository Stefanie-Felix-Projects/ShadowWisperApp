/**
 * Factory-Klasse `WalletViewModelFactory`, die für die Erstellung von `WalletViewModel`-Instanzen verantwortlich ist.
 * Diese Klasse wird verwendet, um das `WalletRepository` an das `WalletViewModel` zu übergeben, wenn das ViewModel instanziiert wird.
 */
package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shadowwisper.ui.theme.data.repository.WalletRepository

/**
 * Factory zur Erstellung von `WalletViewModel`-Instanzen.
 * Diese Factory stellt sicher, dass das `WalletRepository` korrekt an das `WalletViewModel` übergeben wird.
 * @param repository Das Repository, das für den Datenzugriff verwendet wird.
 */
class WalletViewModelFactory(
    private val repository: WalletRepository
) : ViewModelProvider.Factory {

    /**
     * Erstellt eine Instanz des ViewModels, basierend auf der übergebenen `modelClass`.
     * Wenn die `modelClass` dem `WalletViewModel` entspricht, wird eine neue Instanz des ViewModels mit dem `repository` zurückgegeben.
     * Andernfalls wird eine `IllegalArgumentException` geworfen.
     * @param modelClass Die Klasse des ViewModels, das erstellt werden soll.
     * @return Eine Instanz des `WalletViewModel`, wenn die `modelClass` passt.
     * @throws IllegalArgumentException, wenn die `modelClass` nicht dem `WalletViewModel` entspricht.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WalletViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WalletViewModel(repository) as T // Erstellt eine Instanz des WalletViewModel
        }
        throw IllegalArgumentException("Unknown ViewModel class") // Wirft eine Ausnahme, wenn die Klasse nicht erkannt wird
    }
}