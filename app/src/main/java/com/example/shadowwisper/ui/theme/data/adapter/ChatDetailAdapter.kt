package com.example.shadowwisper.ui.theme.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.data.database.ChatMessage


class ChatDetailAdapter(private val messageList: List<ChatMessage>) :
    RecyclerView.Adapter<ChatDetailAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.textViewMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.messageTextView.text = messageList[position].message

        Log.d("ChatDetailAdapter", "Binding message: ${messageList[position].message}")
    }

    override fun getItemCount() = messageList.size
}