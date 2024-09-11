/**
 * Adapter für eine RecyclerView, der die Nachrichten eines ChatRooms anzeigt.
 * Der Adapter entscheidet, ob die Nachricht gesendet oder empfangen wurde,
 * und zeigt sie entsprechend an. Er nutzt verschiedene ViewHolders für ausgehende
 * und eingehende Nachrichten.
 */
package com.example.shadowwisper.ui.theme.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.data.model.ChatRoom

/**
 * ChatDetailAdapter ist ein RecyclerView-Adapter, der Nachrichten eines ChatRooms darstellt.
 * @param chatRoom Ein ChatRoom-Objekt, das die Nachrichten und ihre Details enthält.
 * @param currentCharacterId Die ID des aktuellen Charakters, um zwischen gesendeten und empfangenen Nachrichten zu unterscheiden.
 */
class ChatDetailAdapter(
    private val chatRoom: ChatRoom, // ChatRoom mit allen Nachrichten
    private val currentCharacterId: String // Die ID des aktuellen Charakters
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SENT = 1 // Typ für gesendete Nachrichten
    private val VIEW_TYPE_RECEIVED = 2 // Typ für empfangene Nachrichten

    /**
     * Bestimmt den View-Typ für eine Nachricht an der gegebenen Position.
     * Wenn der Absender der Nachricht der aktuelle Charakter ist, wird der Typ für gesendete Nachrichten zurückgegeben.
     * Ansonsten wird der Typ für empfangene Nachrichten zurückgegeben.
     * @param position Die Position der Nachricht in der Liste.
     * @return Der View-Typ der Nachricht.
     */
    override fun getItemViewType(position: Int): Int {
        val message = chatRoom.messages[position] // Holt die Nachricht an der gegebenen Position

        return if (message.senderId == currentCharacterId) {
            VIEW_TYPE_SENT // Rückgabe des Typs für gesendete Nachrichten
        } else {
            VIEW_TYPE_RECEIVED // Rückgabe des Typs für empfangene Nachrichten
        }
    }

    /**
     * Erstellt einen neuen ViewHolder für die Nachricht.
     * Abhängig vom View-Typ wird entweder ein Layout für gesendete oder empfangene Nachrichten inflated.
     * @param parent Die übergeordnete ViewGroup, in die der ViewHolder eingefügt wird.
     * @param viewType Der Typ des zu erstellenden ViewHolders (gesendet oder empfangen).
     * @return Ein neuer RecyclerView.ViewHolder für die Nachricht.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_message_outgoing, parent, false) // Layout für gesendete Nachrichten
            OutgoingMessageViewHolder(view) // Rückgabe eines ViewHolders für gesendete Nachrichten
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_message_incoming, parent, false) // Layout für empfangene Nachrichten
            IncomingMessageViewHolder(view) // Rückgabe eines ViewHolders für empfangene Nachrichten
        }
    }

    /**
     * Bindet die Daten einer Nachricht an den ViewHolder.
     * Je nachdem, ob es sich um eine gesendete oder empfangene Nachricht handelt, wird der passende ViewHolder befüllt.
     * @param holder Der ViewHolder, der die Daten hält.
     * @param position Die Position der Nachricht in der Liste.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatRoom.messages[position] // Holt die Nachricht an der gegebenen Position
        Log.d(
            "ChatDetailAdapter",
            "Nachricht von: ${message.senderId} an: ${message.recipientId} - Text: ${message.message}"
        ) // Loggt die Nachricht für Debugging-Zwecke

        // Befüllt den ViewHolder je nachdem, ob die Nachricht empfangen oder gesendet wurde
        if (holder is IncomingMessageViewHolder) {
            holder.messageTextViewIncoming.text = message.message // Setzt den Text für eine empfangene Nachricht
        } else if (holder is OutgoingMessageViewHolder) {
            holder.messageTextView.text = message.message // Setzt den Text für eine gesendete Nachricht
        }
    }

    /**
     * Gibt die Anzahl der Nachrichten im ChatRoom zurück.
     * @return Die Anzahl der Nachrichten.
     */
    override fun getItemCount() = chatRoom.messages.size // Größe der Nachrichtenliste

    /**
     * ViewHolder für empfangene Nachrichten.
     * Enthält ein TextView, das die Nachricht anzeigt.
     */
    class IncomingMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextViewIncoming: TextView = view.findViewById(R.id.textViewMessageIncoming) // TextView für die eingehende Nachricht
    }

    /**
     * ViewHolder für gesendete Nachrichten.
     * Enthält ein TextView, das die Nachricht anzeigt.
     */
    class OutgoingMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.textViewMessageOutgoing) // TextView für die ausgehende Nachricht
    }
}