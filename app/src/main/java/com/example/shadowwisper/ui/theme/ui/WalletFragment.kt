/**
 * Fragment-Klasse `WalletFragment`, die für die Verwaltung und Anzeige der Wallet-Daten zuständig ist.
 * Das Fragment zeigt dem Benutzer eine Übersicht über Einnahmen, Ausgaben, Karma und Gesamtsumme an.
 * Außerdem bietet es die Möglichkeit, diese Daten zu bearbeiten und in einer Balkendiagramm-Ansicht darzustellen.
 */
package com.example.shadowwisper.ui.theme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shadowwisper.R
import com.example.shadowwisper.databinding.FragmentWalletBinding
import com.example.shadowwisper.ui.theme.data.database.WalletDatabase
import com.example.shadowwisper.ui.theme.data.model.Wallet
import com.example.shadowwisper.ui.theme.data.repository.WalletRepository
import com.example.shadowwisper.ui.theme.data.view.WalletViewModel
import com.example.shadowwisper.ui.theme.data.view.WalletViewModelFactory
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

/**
 * Fragment zur Anzeige und Bearbeitung der Wallet-Daten.
 * Der Benutzer kann Einnahmen, Ausgaben und Karma einsehen und bearbeiten.
 * Die Gesamtsumme und eine grafische Darstellung der Einnahmen und Ausgaben werden ebenfalls angezeigt.
 */
class WalletFragment : Fragment() {

    // Binding-Objekt zur einfachen Referenzierung der UI-Elemente.
    private lateinit var binding: FragmentWalletBinding
    // ViewModel zur Verwaltung der Wallet-Daten.
    private lateinit var viewModel: WalletViewModel
    // Variable zur Überwachung, ob sich das Fragment im Bearbeitungsmodus befindet.
    private var isEditing = false

    /**
     * Erstellt die View für das Fragment und bindet das Layout an das Fragment.
     * @param inflater Das LayoutInflater-Objekt zum Aufblasen des Layouts für dieses Fragment.
     * @param container Das übergeordnete View-Element, in das die Fragment-UI eingebunden wird.
     * @param savedInstanceState Wenn nicht null, wird dieses Fragment mit einem früher gespeicherten Zustand wiederhergestellt.
     * @return Die View für das Fragment-UI, oder null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Bindet das Layout für das Wallet-Fragment.
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Wird nach der Erstellung der View aufgerufen und initialisiert die Benutzeroberfläche.
     * Hier wird das Wallet-ViewModel eingerichtet, und die Wallet-Daten werden geladen und angezeigt.
     * Außerdem wird ein Bearbeitungsmodus implementiert, um die Wallet-Daten zu ändern.
     * @param view Die erstellte View für dieses Fragment.
     * @param savedInstanceState Wenn nicht null, wird dieses Fragment mit einem früher gespeicherten Zustand wiederhergestellt.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisiert die Datenbank und das Repository für das Wallet.
        val database = WalletDatabase.getDatabase(requireContext())
        val repository = WalletRepository(database.walletDao())

        // Initialisiert das ViewModel mithilfe einer ViewModelFactory.
        val factory = WalletViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(WalletViewModel::class.java)

        // Beobachtet die Wallet-Daten und aktualisiert die Benutzeroberfläche entsprechend.
        viewModel.latestWallet.observe(viewLifecycleOwner) { wallet ->
            wallet?.let {
                updateUI(it)
            }
        }

        // Klicklistener für den Bearbeiten-Button, um den Bearbeitungsmodus zu aktivieren/deaktivieren.
        binding.editButton.setOnClickListener {
            isEditing = !isEditing
            enableEditing(isEditing)
            binding.editButton.text = if (isEditing) "Speichern" else "Bearbeiten"

            // Wenn der Bearbeitungsmodus deaktiviert wird, werden die geänderten Daten gespeichert.
            if (!isEditing) {
                saveData()
            }
        }
    }

    /**
     * Aktiviert oder deaktiviert die Bearbeitung der Eingabefelder.
     * @param enable Gibt an, ob die Bearbeitung aktiviert oder deaktiviert werden soll.
     */
    private fun enableEditing(enable: Boolean) {
        binding.einnahmenText.isEnabled = enable
        binding.ausgabenText.isEnabled = enable
        binding.karmaText.isEnabled = enable
    }

    /**
     * Speichert die eingegebenen Daten in der Datenbank.
     * Die geänderten Einnahmen, Ausgaben und Karma-Werte werden aus den Eingabefeldern gelesen und an das ViewModel übergeben.
     */
    private fun saveData() {
        val einnahmen = binding.einnahmenText.text.toString().toDoubleOrNull() ?: 0.0
        val ausgaben = binding.ausgabenText.text.toString().toDoubleOrNull() ?: 0.0
        val karma = binding.karmaText.text.toString().toDoubleOrNull() ?: 0.0
        viewModel.updateWallet(einnahmen, ausgaben, karma)
    }

    /**
     * Aktualisiert die Benutzeroberfläche mit den aktuellen Wallet-Daten.
     * Die Einnahmen, Ausgaben und Karma-Werte werden in die entsprechenden Felder eingetragen,
     * und die Gesamtsumme wird berechnet und angezeigt. Zusätzlich wird ein Balkendiagramm zur Visualisierung erstellt.
     * @param wallet Die aktuellen Wallet-Daten.
     */
    private fun updateUI(wallet: Wallet) {
        binding.einnahmenText.setText(wallet.einnahmen.toString())
        binding.ausgabenText.setText(wallet.ausgaben.toString())
        binding.karmaText.setText(wallet.karma.toString())
        binding.gesamtsummeText.text = wallet.gesamtsumme.toString()

        // Erstellt die Einträge für das Balkendiagramm.
        val entries = listOf(
            BarEntry(1f, wallet.einnahmen.toFloat()),
            BarEntry(2f, wallet.ausgaben.toFloat())
        )
        val dataSet = BarDataSet(entries, "Einnahmen und Ausgaben")

        // Setzt die Farben für das Balkendiagramm.
        dataSet.colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.md_theme_primaryContainer_mediumContrast),
            ContextCompat.getColor(requireContext(), R.color.md_theme_primaryContainer)
        )

        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.colorOnCustomColor1)

        // Verknüpft die Daten mit dem Diagramm.
        val data = BarData(dataSet)
        binding.chartEinnahmenAusgaben.data = data

        // Konfiguriert die Darstellung des Balkendiagramms.
        binding.chartEinnahmenAusgaben.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.md_theme_onPrimaryContainer))
        binding.chartEinnahmenAusgaben.legend.textColor = ContextCompat.getColor(requireContext(), R.color.colorOnCustomColor1)
        binding.chartEinnahmenAusgaben.description.isEnabled = false

        // Konfiguriert die Achsen des Diagramms.
        binding.chartEinnahmenAusgaben.axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.colorOnCustomColor1)
        binding.chartEinnahmenAusgaben.axisLeft.setDrawLabels(true)
        binding.chartEinnahmenAusgaben.axisRight.setDrawLabels(false)
        binding.chartEinnahmenAusgaben.xAxis.setDrawLabels(false)

        // Entfernt die Gitternetzlinien und Achsenlinien für eine saubere Darstellung.
        binding.chartEinnahmenAusgaben.xAxis.setDrawGridLines(false)
        binding.chartEinnahmenAusgaben.axisLeft.setDrawGridLines(false)
        binding.chartEinnahmenAusgaben.axisRight.setDrawGridLines(false)

        binding.chartEinnahmenAusgaben.axisLeft.setDrawAxisLine(false)
        binding.chartEinnahmenAusgaben.axisRight.setDrawAxisLine(false)
        binding.chartEinnahmenAusgaben.xAxis.setDrawAxisLine(false)

        // Aktualisiert das Diagramm mit den neuen Daten.
        binding.chartEinnahmenAusgaben.invalidate()
    }
}