package com.example.shadowwisper.ui.theme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.shadowwisper.ui.theme.data.model.Wallet
import com.example.shadowwisper.ui.theme.data.view.WalletViewModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.syntax_institut.whatssyntax.databinding.FragmentWalletBinding

class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletBinding
    private val viewModel: WalletViewModel by activityViewModels()
    private var isEditing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.latestWallet.observe(viewLifecycleOwner) { wallet ->
            wallet?.let {
                updateUI(it)
            }
        }

        binding.editButton.setOnClickListener {
            isEditing = !isEditing
            enableEditing(isEditing)
            binding.editButton.text = if (isEditing) "Speichern" else "Bearbeiten"

            if (!isEditing) {
                saveData()
            }
        }
    }

    private fun enableEditing(enable: Boolean) {
        binding.einnahmenText.isEnabled = enable
        binding.ausgabenText.isEnabled = enable
        binding.karmaText.isEnabled = enable
    }

    private fun saveData() {
        val einnahmen = binding.einnahmenText.text.toString().toDoubleOrNull() ?: 0.0
        val ausgaben = binding.ausgabenText.text.toString().toDoubleOrNull() ?: 0.0
        val karma = binding.karmaText.text.toString().toDoubleOrNull() ?: 0.0
        viewModel.updateWallet(einnahmen, ausgaben, karma)
    }

    private fun updateUI(wallet: Wallet) {
        binding.einnahmenText.setText(wallet.einnahmen.toString())
        binding.ausgabenText.setText(wallet.ausgaben.toString())
        binding.karmaText.setText(wallet.karma.toString())
        binding.gesamtsummeText.text = wallet.gesamtsumme.toString()

        val entries = listOf(
            BarEntry(1f, wallet.einnahmen.toFloat()),
            BarEntry(2f, wallet.ausgaben.toFloat())
        )
        val dataSet = BarDataSet(entries, "Einnahmen und Ausgaben")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        binding.chartEinnahmenAusgaben.data = BarData(dataSet)
        binding.chartEinnahmenAusgaben.invalidate() // Refresh
    }
}