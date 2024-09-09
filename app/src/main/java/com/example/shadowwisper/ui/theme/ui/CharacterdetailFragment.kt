package com.example.shadowwisper.ui.theme.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shadowwisper.R
import com.example.shadowwisper.databinding.FragmentCharacterdetailBinding
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.example.shadowwisper.ui.theme.data.view.CharacterDetailViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

class CharacterdetailFragment : Fragment() {

    private lateinit var binding: FragmentCharacterdetailBinding
    private val args: CharacterdetailFragmentArgs by navArgs()
    private val viewModel: CharacterDetailViewModel by activityViewModels()

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val characterId = args.characterId
        Log.d("CharacterdetailFragment", "Received characterId: $characterId")

        if (characterId != null && characterId.isNotEmpty()) {
            Log.d("CharacterdetailFragment", "Calling getCharacterById with id: $characterId")

            viewModel.getCharacterById(characterId).observe(viewLifecycleOwner) { character ->
                if (character != null) {
                    Log.d("CharacterdetailFragment", "Character loaded: $character")
                    loadCharacterData(character)
                } else {
                    Log.d("CharacterdetailFragment", "Character is null")
                }
            }
        } else {
            setupNewCharacterForm()
        }

        binding.icon.setOnClickListener {
            openGallery()
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadCharacterData(character: CharacterDetail) {
        binding.inputName.setText(character.name)
        binding.inputStory.setText(character.backgroundStory)
        binding.inputRasse.setText(character.race)
        binding.inputSkills.setText(character.skills)
        binding.inputEquipment.setText(character.equipment)

        character.profileImage?.let { uri ->
            binding.icon.setImageURI(Uri.parse(uri))
        } ?: binding.icon.setImageResource(R.drawable.hex17jpg)

        binding.buttonSave.setOnClickListener {
            saveCharacter(character.characerId, isNewCharacter = false)
        }
    }

    private fun setupNewCharacterForm() {
        binding.inputName.text.clear()
        binding.inputStory.text.clear()
        binding.inputRasse.text.clear()
        binding.inputSkills.text.clear()
        binding.inputEquipment.text.clear()
        binding.icon.setImageResource(R.drawable.hex17jpg)

        binding.buttonSave.setOnClickListener {
            saveCharacter(id = null, isNewCharacter = true)
        }
    }

    private fun saveCharacter(id: String?, isNewCharacter: Boolean) {
        val generatedId = id ?: UUID.randomUUID().toString()

        val character = CharacterDetail(
            characerId = generatedId,
            name = binding.inputName.text.toString(),
            backgroundStory = binding.inputStory.text.toString(),
            race = binding.inputRasse.text.toString(),
            skills = binding.inputSkills.text.toString(),
            equipment = binding.inputEquipment.text.toString(),
            userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        )

        if (imageUri != null) {
            viewModel.saveCharacter(character, imageUri, isNewCharacter)
        } else {
            viewModel.saveCharacter(character, null, isNewCharacter)
        }

        findNavController().navigateUp()
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            imageUri = data?.data
            binding.icon.setImageURI(imageUri)
        }
    }
}