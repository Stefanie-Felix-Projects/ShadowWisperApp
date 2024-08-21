package com.example.shadowwisper.ui.theme.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shadowwisper.ui.theme.data.adapter.CharacterOverviewAdapter
import com.example.shadowwisper.ui.theme.data.view.CharacterViewModel
import com.syntax_institut.whatssyntax.databinding.FragmentCharacteroverviewBinding

class CharacteroverviewFragment : Fragment() {

    private lateinit var binding: FragmentCharacteroverviewBinding
    private val viewModel: CharacterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacteroverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.characterList.observe(viewLifecycleOwner) { characterList ->
            val adapter = CharacterOverviewAdapter(characterList) { selectedCharacter ->
                // Navigation zu den Charakterdetails
                val action = CharacteroverviewFragmentDirections
                    .actionCharacteroverviewFragmentToCharacterdetailFragment(selectedCharacter.name)
                findNavController().navigate(action)
            }
            binding.rvChar.layoutManager = LinearLayoutManager(context)
            binding.rvChar.adapter = adapter
        }
    }
}