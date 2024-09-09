package com.example.shadowwisper.ui.theme.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.data.model.ChatRoom

class ChatDetailAdapter(
    private val chatRoom: ChatRoom,
    private val currentCharacterId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2

    override fun getItemViewType(position: Int): Int {
        val message = chatRoom.messages[position]

        // Prüfe, ob der aktuelle Charakter der Sender der Nachricht ist
        return if (message.senderId == currentCharacterId) {
            VIEW_TYPE_SENT  // Wenn der aktuelle Charakter der Absender ist, Nachricht rechts
        } else {
            VIEW_TYPE_RECEIVED  // Wenn der aktuelle Charakter der Empfänger ist, Nachricht links
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_message_outgoing, parent, false)
            OutgoingMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_message_incoming, parent, false)
            IncomingMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatRoom.messages[position]
        Log.d("ChatDetailAdapter", "Nachricht von: ${message.senderId} an: ${message.recipientId} - Text: ${message.message}")
        if (holder is IncomingMessageViewHolder) {
            holder.messageTextViewIncoming.text = message.message
        } else if (holder is OutgoingMessageViewHolder) {
            holder.messageTextView.text = message.message
        }
    }

    override fun getItemCount() = chatRoom.messages.size

    class IncomingMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextViewIncoming: TextView = view.findViewById(R.id.textViewMessageIncoming)
    }

    class OutgoingMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.textViewMessageOutgoing)
    }
}