/**
 * Fragment-Klasse `CharacterdetailFragment`, die für die Anzeige und Bearbeitung der Details eines Charakters zuständig ist.
 * Diese Klasse ermöglicht es dem Benutzer, Charakterdaten wie Name, Geschichte, Rasse, Fähigkeiten und Ausrüstung anzuzeigen oder zu bearbeiten.
 * Außerdem kann der Benutzer ein Bild für den Charakter aus der Galerie hochladen und die Daten in Firebase speichern.
 */
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
import com.bumptech.glide.Glide
import com.example.shadowwisper.R
import com.example.shadowwisper.databinding.FragmentCharacterdetailBinding
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.example.shadowwisper.ui.theme.data.view.CharacterDetailViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

/**
 * Fragment zur Verwaltung der Detailansicht eines Charakters.
 * Hier können Benutzer Charakterdetails wie Name, Hintergrundgeschichte, Rasse und Ausrüstung anzeigen und bearbeiten.
 * Außerdem können sie ein Profilbild hochladen und die Daten in Firebase speichern.
 */
class CharacterdetailFragment : Fragment() {

    // ViewBinding-Objekt zur einfachen Referenzierung von UI-Elementen.
    private lateinit var binding: FragmentCharacterdetailBinding
    // Argumente, die von einem anderen Fragment übergeben wurden (z. B. die Charakter-ID).
    private val args: CharacterdetailFragmentArgs by navArgs()
    // ViewModel zur Verwaltung der Charakterdaten.
    private val viewModel: CharacterDetailViewModel by activityViewModels()
    // Variable zur Speicherung der Bild-URI, falls der Benutzer ein neues Bild auswählt.
    private var imageUri: Uri? = null
    // Referenz zu Firebase Storage für das Hochladen von Bildern.
    private val storageReference = FirebaseStorage.getInstance().reference

    /**
     * Erstellt die View für das Fragment und bindet das Layout an das Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Wird aufgerufen, nachdem die View erstellt wurde.
     * Diese Methode lädt die Charakterdaten, wenn eine Charakter-ID vorhanden ist, oder setzt ein neues Charakterformular auf.
     * Außerdem werden Klicklistener für die Bildauswahl und den Speichern-Button eingerichtet.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val characterId = args.characterId
        if (characterId != null && characterId.isNotEmpty()) {
            viewModel.getCharacterById(characterId).observe(viewLifecycleOwner) { character ->
                if (character != null) {
                    loadCharacterData(character)
                }
            }
        } else {
            setupNewCharacterForm()
        }

        // Klicklistener zum Öffnen der Galerie für die Bildauswahl.
        binding.icon.setOnClickListener {
            openGallery()
        }

        // Klicklistener zum Navigieren zurück.
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    /**
     * Lädt die Daten eines bestehenden Charakters und zeigt diese in den Eingabefeldern an.
     * Wenn ein Profilbild vorhanden ist, wird es aus Firebase Storage geladen und angezeigt.
     * @param character Das `CharacterDetail`-Objekt mit den Daten des Charakters.
     */
    private fun loadCharacterData(character: CharacterDetail) {
        binding.inputName.setText(character.name)
        binding.inputStory.setText(character.backgroundStory)
        binding.inputRasse.setText(character.race)
        binding.inputSkills.setText(character.skills)
        binding.inputEquipment.setText(character.equipment)

        // Lädt das Profilbild aus Firebase Storage.
        if (character.profileImage != null) {
            val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(character.profileImage!!)
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(this).load(uri).into(binding.icon)
            }.addOnFailureListener { exception ->
                Log.e("Firebase", "Fehler beim Herunterladen des Bildes", exception)
            }
        } else {
            binding.icon.setImageResource(R.drawable.hex17jpg)
        }

        // Klicklistener zum Speichern der aktualisierten Daten.
        binding.buttonSave.setOnClickListener {
            saveCharacter(character.characerId, isNewCharacter = false)
        }
    }

    /**
     * Bereitet das Formular für die Eingabe eines neuen Charakters vor.
     * Alle Felder werden zurückgesetzt und der Save-Button wird entsprechend konfiguriert.
     */
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

    /**
     * Speichert den Charakter in Firebase und lädt das Profilbild hoch, wenn vorhanden.
     * Die Daten werden entweder für einen neuen oder einen bestehenden Charakter gespeichert.
     * @param id Die ID des Charakters (kann null sein für neue Charaktere).
     * @param isNewCharacter Boolean-Wert, der angibt, ob es sich um einen neuen Charakter handelt.
     */
    private fun saveCharacter(id: String?, isNewCharacter: Boolean) {
        val generatedId = id ?: UUID.randomUUID().toString()

        viewModel.getCharacterById(generatedId).observe(viewLifecycleOwner) { existingCharacter ->
            val character = CharacterDetail(
                characerId = generatedId,
                name = binding.inputName.text.toString(),
                backgroundStory = binding.inputStory.text.toString(),
                race = binding.inputRasse.text.toString(),
                skills = binding.inputSkills.text.toString(),
                equipment = binding.inputEquipment.text.toString(),
                profileImage = existingCharacter?.profileImage ?: viewModel.getCharacterById(generatedId).value?.profileImage,
                userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            )

            if (imageUri != null) {
                uploadImageToFirebase(imageUri) { imageUrl ->
                    val updatedCharacter = character.copy(profileImage = imageUrl)
                    viewModel.saveCharacter(updatedCharacter, null, isNewCharacter)
                }
            } else {
                viewModel.saveCharacter(character, null, isNewCharacter)
            }

            findNavController().navigateUp()
        }
    }

    /**
     * Öffnet die Galerie, um dem Benutzer die Auswahl eines Profilbildes zu ermöglichen.
     */
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, 1)
    }

    /**
     * Wird aufgerufen, wenn der Benutzer ein Bild aus der Galerie ausgewählt hat.
     * Die URI des ausgewählten Bildes wird gespeichert und das Bild wird in der UI angezeigt.
     * @param requestCode Der Code für die angeforderte Aktion.
     * @param resultCode Das Ergebnis der Aktion.
     * @param data Die Daten, die das ausgewählte Bild enthalten.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            imageUri = data?.data
            binding.icon.setImageURI(imageUri)
        }
    }

    /**
     * Lädt das ausgewählte Bild in Firebase Storage hoch und gibt die URL des hochgeladenen Bildes zurück.
     * @param imageUri Die URI des Bildes, das hochgeladen werden soll.
     * @param onSuccess Callback-Funktion, die die URL des Bildes zurückgibt, wenn der Upload erfolgreich war.
     */
    private fun uploadImageToFirebase(imageUri: Uri?, onSuccess: (String) -> Unit) {
        if (imageUri != null) {
            val fileName = "character_images/${UUID.randomUUID()}.jpg"
            val imageRef = storageReference.child(fileName)

            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val downloadUrl = downloadUri.toString()
                        onSuccess(downloadUrl)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firebase", "Fehler beim Hochladen des Bildes", exception)
                }
        }
    }
}