package com.example.shadowwisper.ui.theme.data.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail

class ChatOverviewAdapter(
    private val chatList: List<CharacterDetail>,
    private val onItemClicked: (CharacterDetail, String) -> Unit  // Übergibt die recipientCharacterId
) : RecyclerView.Adapter<ChatOverviewAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_text)
        val profileImageView: ImageView = view.findViewById(R.id.profile_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_chatoverview, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val character = chatList[position]
        holder.nameTextView.text = character.name

        if (character.profileImage != null) {
            holder.profileImageView.setImageURI(Uri.parse(character.profileImage))
        } else {
            holder.profileImageView.setImageResource(R.drawable.hex17jpg)
        }

        if (!character.isActive) {
            holder.nameTextView.alpha = 0.5f
            holder.profileImageView.alpha = 0.5f
        } else {
            holder.nameTextView.alpha = 1.0f
            holder.profileImageView.alpha = 1.0f

            holder.itemView.setOnClickListener {
                val recipientCharacterId = character.id  // Übergib die CharacterId des Empfängers
                onItemClicked(character, recipientCharacterId)
            }
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}