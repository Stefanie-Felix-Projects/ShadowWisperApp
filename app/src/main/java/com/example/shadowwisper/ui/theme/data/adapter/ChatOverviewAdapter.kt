package com.example.shadowwisper.ui.theme.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.data.model.ChatDetail


class ChatOverviewAdapter(
    private val chatList: List<ChatDetail>,
    private val onItemClicked: (ChatDetail) -> Unit
) : RecyclerView.Adapter<ChatOverviewAdapter.ChatViewHolder>() {

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
        holder.nameTextView.text = chatItem.name
        holder.messageTextView.text = chatItem.message
        holder.profileImageView.setImageResource(chatItem.profileImage)

        holder.itemView.setOnClickListener {
            onItemClicked(chatItem)
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}