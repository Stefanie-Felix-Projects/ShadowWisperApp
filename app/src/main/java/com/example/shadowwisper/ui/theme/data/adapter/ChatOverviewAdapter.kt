package com.example.shadowwisper.ui.theme.data.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.data.model.ActiveCharacter

class ChatOverviewAdapter(
    private val availableCharacters: List<ActiveCharacter>,
    private val onCharacterClicked: (ActiveCharacter) -> Unit
) : RecyclerView.Adapter<ChatOverviewAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_chatoverview, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = availableCharacters[position]
        holder.bind(character)
    }

    override fun getItemCount(): Int {
        return availableCharacters.size
    }

    inner class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.name_text)
        private val profileImageView: ImageView = view.findViewById(R.id.profile_image)

        fun bind(character: ActiveCharacter) {
            nameTextView.text = character.name
            Log.d("CharacterBind", "Character: ${character.name}")
            if (character.profileImage != null) {
                profileImageView.setImageURI(Uri.parse(character.profileImage))
            } else {
                profileImageView.setImageResource(R.drawable.hex17jpg)
            }

            itemView.setOnClickListener { onCharacterClicked(character) }
        }
    }
}