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
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class CharacterdetailFragment : Fragment() {

    private lateinit var binding: FragmentCharacterdetailBinding
    private val args: CharacterdetailFragmentArgs by navArgs()
    private val viewModel: CharacterDetailViewModel by activityViewModels()

    private val PICK_IMAGE = 1
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

        if (characterId != null) {
            Log.d("CharacterdetailFragment", "Calling getCharacterById with id: $characterId")

            viewModel.getCharacterById(characterId).observe(viewLifecycleOwner) { character ->
                Log.d("CharacterdetailFragment", "Character loaded: $character")

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
                        saveCharacter(
                            id = character.characerId,
                            isNewCharacter = false
                        )
                    }
                } else {
                    Log.d("CharacterdetailFragment", "Character is null")
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
                saveCharacter(
                    id = null,
                    isNewCharacter = true
                )
            }
        }

        binding.icon.setOnClickListener {
            openGallery()
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_characterdetailFragment_to_characteroverviewFragment)
        }
    }

    private fun saveCharacter(id: String?, isNewCharacter: Boolean) {
        val generatedId = id ?: UUID.randomUUID().toString()

        // Erstelle den CharacterDetail
        val character = CharacterDetail(
            characerId = generatedId,
            name = binding.inputName.text.toString(),
            backgroundStory = binding.inputStory.text.toString(),
            race = binding.inputRasse.text.toString(),
            skills = binding.inputSkills.text.toString(),
            equipment = binding.inputEquipment.text.toString(),
            userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""  // Benutzer-ID hinzufügen
        )

        // Prüfe, ob ein Bild hochgeladen werden soll
        if (imageUri != null) {
            // Lade das Bild zu Firebase Storage hoch
            uploadImageToStorage(imageUri!!) { imageUrl ->
                // Setze das Bild-URL in den CharacterDetail und speichere ihn
                val updatedCharacter = character.copy(profileImage = imageUrl)
                if (isNewCharacter) {
                    viewModel.insert(updatedCharacter)
                } else {
                    viewModel.update(updatedCharacter)
                }
                findNavController().navigateUp()  // Navigiere zurück zur Charakterübersicht
            }
        } else {
            // Speichere den Charakter ohne Bild
            if (isNewCharacter) {
                viewModel.insert(character)
            } else {
                viewModel.update(character)
            }
            findNavController().navigateUp()  // Navigiere zurück zur Charakterübersicht
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

    private fun uploadImageToStorage(uri: Uri, onSuccess: (String) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().getReference("character_images/${UUID.randomUUID()}")
        storageReference.putFile(uri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString())
                }
            }
            .addOnFailureListener {
                Log.e("CharacterDetail", "Error uploading image", it)
            }
    }
}