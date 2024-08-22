package com.example.shadowwisper.ui.theme.ui


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.example.shadowwisper.ui.theme.data.view.CharacterViewModel
import com.syntax_institut.whatssyntax.R
import com.syntax_institut.whatssyntax.databinding.FragmentCharacterdetailBinding
import java.io.ByteArrayOutputStream
import java.io.InputStream

class CharacterdetailFragment : Fragment() {

    private lateinit var binding: FragmentCharacterdetailBinding
    private val args: CharacterdetailFragmentArgs by navArgs()
    private val viewModel: CharacterViewModel by activityViewModels()

    private val PICK_IMAGE = 1
    private var imageUri: Uri? = null

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

        if (characterId != null) {
            viewModel.getCharacterById(characterId.toInt()).observe(viewLifecycleOwner) { character ->
                if (character != null) {
                    binding.inputName.setText(character.name)
                    character.profileImage?.let { uri ->
                        binding.icon.setImageURI(Uri.parse(uri))
                    }
                    binding.inputStory.setText(character.backgroundStory)
                    binding.inputRasse.setText(character.race)
                    binding.inputSkills.setText(character.skills)
                    binding.inputEquipment.setText(character.equipment)

                    binding.buttonSave.setOnClickListener {
                        val updatedCharacter = CharacterDetail(
                            id = character.id,
                            profileImage = imageUri?.toString() ?: character.profileImage,
                            name = binding.inputName.text.toString(),
                            backgroundStory = binding.inputStory.text.toString(),
                            race = binding.inputRasse.text.toString(),
                            skills = binding.inputSkills.text.toString(),
                            equipment = binding.inputEquipment.text.toString()
                        )
                        viewModel.update(updatedCharacter)
                        findNavController().navigateUp()
                    }
                }
            }
        } else {
            binding.inputName.text.clear()
            binding.inputStory.text.clear()
            binding.inputRasse.text.clear()
            binding.inputSkills.text.clear()
            binding.inputEquipment.text.clear()
            binding.icon.setImageResource(R.drawable.hex17jpg)

            binding.buttonSave.setOnClickListener {
                val newCharacter = CharacterDetail(
                    profileImage = imageUri?.toString(),
                    name = binding.inputName.text.toString(),
                    backgroundStory = binding.inputStory.text.toString(),
                    race = binding.inputRasse.text.toString(),
                    skills = binding.inputSkills.text.toString(),
                    equipment = binding.inputEquipment.text.toString()
                )
                viewModel.insert(newCharacter)
                findNavController().navigateUp()
            }
        }

        binding.icon.setOnClickListener {
            openGallery()
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_characterdetailFragment_to_characteroverviewFragment)
        }
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data?.data
            binding.icon.setImageURI(imageUri)
        }
    }
}