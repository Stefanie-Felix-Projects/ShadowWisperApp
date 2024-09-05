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
                .document(characterDetail.id)

            // Check if the character is in the active collection
            activeCharacterRef.get()
                .addOnSuccessListener { document ->
                    switchButton.isChecked = document.exists()
                }

            switchButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Add to active_character collection for the user
                    activeCharacterRef.set(
                        mapOf(
                            "characterID" to characterDetail.id,
                            "name" to characterDetail.name,
                            "profileImage" to characterDetail.profileImage,
                            "isActive" to true
                        )
                    )

                    // Update or add in all_active_characters
                    firestore.collection("all_active_characters")
                        .document(characterDetail.id)
                        .set(
                            mapOf(
                                "characterID" to characterDetail.id,
                                "name" to characterDetail.name,
                                "profileImage" to characterDetail.profileImage,
                                "userId" to userId,
                                "isActive" to true // Mark it as active in global collection
                            )
                        )
                        .addOnSuccessListener {
                            Log.d("Firestore", "Character successfully marked active: ${characterDetail.name}")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error adding character", e)
                        }

                    onSwitchToggled(characterDetail, true)
                } else {
                    // Remove from active_character collection for the user
                    activeCharacterRef.delete()

                    // Update in all_active_characters (set as inactive)
                    firestore.collection("all_active_characters")
                        .document(characterDetail.id)
                        .update("isActive", false) // Mark as inactive instead of deleting
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