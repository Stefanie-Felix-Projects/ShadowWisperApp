package com.example.shadowwisper.ui.theme.data.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CharacterOverviewAdapter(
    private val characterList: List<CharacterDetail>,
    private val onItemClicked: (CharacterDetail) -> Unit,
    private val onSwitchToggled: (CharacterDetail, Boolean) -> Unit
) : RecyclerView.Adapter<CharacterOverviewAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(view: View, private val onItemClicked: (CharacterDetail) -> Unit) :
        RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_text)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val switchButton: Switch = view.findViewById(R.id.switchButton)

        fun bind(characterDetail: CharacterDetail, onSwitchToggled: (CharacterDetail, Boolean) -> Unit) {
            nameTextView.text = characterDetail.name

            if (characterDetail.profileImage != null) {
                imageView.setImageURI(Uri.parse(characterDetail.profileImage))
            } else {
                imageView.setImageResource(R.drawable.hex17jpg)
            }

            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val firestore = FirebaseFirestore.getInstance()
            val activeCharacterRef = firestore.collection("users")
                .document(userId)
                .collection("active_character")
                .document(characterDetail.characerId)

            // Überprüfe, ob der Charakter als aktiv markiert ist und setze den Switch entsprechend
            activeCharacterRef.get()
                .addOnSuccessListener { document ->
                    switchButton.isChecked = document.exists() && document.getBoolean("isActive") == true
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error fetching character status", e)
                }

            switchButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Zuerst alle anderen Charaktere deaktivieren
                    firestore.collection("users")
                        .document(userId)
                        .collection("active_character")
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                if (document.id != characterDetail.characerId) {
                                    document.reference.update("isActive", false)
                                }
                            }

                            // Setze den aktuellen Charakter als aktiv
                            activeCharacterRef.set(
                                mapOf(
                                    "characterID" to characterDetail.characerId,
                                    "name" to characterDetail.name,
                                    "profileImage" to characterDetail.profileImage,
                                    "isActive" to true
                                )
                            )

                            // Aktualisiere den globalen Status des Charakters
                            firestore.collection("all_active_characters")
                                .document(characterDetail.characerId)
                                .set(
                                    mapOf(
                                        "characterID" to characterDetail.characerId,
                                        "name" to characterDetail.name,
                                        "profileImage" to characterDetail.profileImage,
                                        "userId" to userId,
                                        "isActive" to true
                                    )
                                )
                                .addOnSuccessListener {
                                    Log.d("Firestore", "Character successfully marked active: ${characterDetail.name}")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Firestore", "Error adding character", e)
                                }

                            onSwitchToggled(characterDetail, true)
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error retrieving active characters", e)
                        }
                } else {
                    // Entferne den aktuellen Charakter aus der aktiven Sammlung
                    activeCharacterRef.delete()

                    // Markiere den Charakter in der globalen Sammlung als inaktiv
                    firestore.collection("all_active_characters")
                        .document(characterDetail.characerId)
                        .update("isActive", false)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Character successfully marked inactive: ${characterDetail.name}")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error marking character inactive", e)
                        }

                    onSwitchToggled(characterDetail, false)
                }
            }

            itemView.setOnClickListener {
                onItemClicked(characterDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_charoverview, parent, false)
        return CharacterViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characterList[position], onSwitchToggled)
    }

    override fun getItemCount(): Int {
        return characterList.size
    }
}