/**
 * Adapter für eine RecyclerView, der eine Liste von Bestellungen (OrderOverview) anzeigt.
 * Jede Bestellung enthält einen Titel und ein Bild. Ein Klick auf ein Listenelement löst eine Aktion aus.
 */
package com.example.shadowwisper.ui.theme.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.data.model.OrderOverview

/**
 * OrderOverviewAdapter ist ein RecyclerView.Adapter, der eine Liste von OrderOverview-Objekten darstellt.
 * @param orderList Eine Liste von Bestellübersichten, die in der RecyclerView angezeigt werden sollen.
 * @param onItemClicked Eine Lambda-Funktion, die ausgeführt wird, wenn ein Listenelement angeklickt wird.
 */
class OrderOverviewAdapter(
    private val orderList: List<OrderOverview>, // Liste der Bestellungen (OrderOverview)
    private val onItemClicked: (OrderOverview) -> Unit // Callback für Klick auf ein Element
) : RecyclerView.Adapter<OrderOverviewAdapter.OrderViewHolder>() {

    /**
     * ViewHolder für den OrderOverviewAdapter. Diese Klasse hält die UI-Elemente, die für jedes Bestellungs-Element in der Liste verwendet werden.
     * @param view Die Ansicht (View), die das Listenelement darstellt.
     */
    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView) // ImageView für das Bild der Bestellung
        val nameTextView: TextView = view.findViewById(R.id.name_text) // TextView für den Titel der Bestellung
    }

    /**
     * Erstellt einen neuen ViewHolder, wenn die RecyclerView ihn benötigt.
     * Inflates das Layout für jedes Listenelement.
     * @param parent Die übergeordnete ViewGroup, in die der ViewHolder eingefügt wird.
     * @param viewType Der Typ des ViewHolders, der erstellt werden soll (hier immer derselbe).
     * @return Ein neuer ViewHolder für das Listenelement.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_orderoverview, parent, false) // Inflates das Layout für die Bestellübersicht
        return OrderViewHolder(view) // Gibt einen neuen ViewHolder zurück
    }

    /**
     * Bindet die Daten eines Bestellungs-Elements an den ViewHolder.
     * Diese Methode setzt den Titel und das Bild für jede Bestellung und steuert die Klickaktion.
     * @param holder Der ViewHolder, der die Daten hält.
     * @param position Die Position des aktuellen Elements in der Liste.
     */
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderItem = orderList[position] // Holt das Bestellungs-Element an der gegebenen Position
        holder.nameTextView.text = orderItem.orderTitle // Setzt den Bestelltitel in das TextView
        holder.imageView.setImageResource(orderItem.profileImage) // Setzt das Bild für die Bestellung

        // Setzt einen Klicklistener auf das Listenelement, der die onItemClicked-Funktion aufruft
        holder.itemView.setOnClickListener {
            onItemClicked(orderItem) // Führt die Aktion aus, wenn auf das Listenelement geklickt wird
        }
    }

    /**
     * Gibt die Anzahl der Bestellungen in der Liste zurück.
     * @return Die Anzahl der Bestellungen in der Liste.
     */
    override fun getItemCount(): Int {
        return orderList.size // Rückgabe der Anzahl der Bestellungs-Elemente
    }
}