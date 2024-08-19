package com.example.shadowwisper.ui.theme.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.syntax_institut.whatssyntax.R


class ChatOverviewAdapter(private val chatList: List<ChatItem>) : RecyclerView.Adapter<ChatOverviewAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileImageView: ImageView = view.findViewById(R.id.profile_image)
        val nameTextView: TextView = view.findViewById(R.id.name_text)
        val messageTextView: TextView = view.findViewById(R.id.message_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_chatoverview, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatItem = chatList[position]
        holder.nameTextView.text = chatItem.characterName
        holder.messageTextView.text = chatItem.characterInfo

        holder.profileImageView.setImageResource(R.drawable.hex17jpg)
        //ToDo Bilder aus Datenbank
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}

data class ChatItem(val characterName: String, val characterInfo: String)