package com.example.shadowwisper.ui.theme.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.shadowwisper.ui.theme.data.view.WalletViewModel
import com.syntax_institut.whatssyntax.databinding.FragmentWalletBinding

class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletBinding
    private val viewModel: WalletViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.kontostand.observe(viewLifecycleOwner) { value ->
            binding.kontostandText.setText(value.toString())
        }

        viewModel.karmapunkte.observe(viewLifecycleOwner) { value ->
            binding.karmapunkteText.setText(value.toString())
        }

        viewModel.edgepunkte.observe(viewLifecycleOwner) { value ->
            binding.edgepunkteText.setText(value.toString())
        }

        binding.editButton.setOnClickListener {
            enableEditing(true)
        }
    }

    private fun enableEditing(enable: Boolean) {
        binding.kontostandText.isEnabled = enable
        binding.karmapunkteText.isEnabled = enable
        binding.edgepunkteText.isEnabled = enable

        if (!enable) {
            val kontostand = binding.kontostandText.text.toString().toDouble()
            val karmapunkte = binding.karmapunkteText.text.toString().toDouble()
            val edgepunkte = binding.edgepunkteText.text.toString().toDouble()

            viewModel.updateWallet(kontostand, karmapunkte, edgepunkte)
        }
    }
}