package com.example.shadowwisper.ui.theme.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.shadowwisper.ui.theme.data.view.CharacterViewModel
import com.syntax_institut.whatssyntax.databinding.FragmentCharacterdetailBinding

class CharacterdetailFragment : Fragment() {

    private lateinit var binding: FragmentCharacterdetailBinding
    private val args: CharacterdetailFragmentArgs by navArgs()
    private val viewModel: CharacterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val characterId = args.characterId

        // Lade die Details anhand der ID
        val character = viewModel.characterList.value?.find { it.name == characterId }

        if (character != null) {
            binding.inputName.setText(character.name)
            binding.icon.setImageResource(character.imageResId)
            // Platzhalter für die restlichen Felder
            binding.inputStory.setText("Placeholder Story")
            binding.inputRasse.setText("Placeholder Race")
            binding.inputSkills.setText("Placeholder Skills")
            binding.inputEquipment.setText("Placeholder Equipment")
        }
    }
}