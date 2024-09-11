/**
 * Adapter für eine RecyclerView, der eine Liste von String-Elementen anzeigt und
 * bei einem Klick auf ein Element die Navigation zu verschiedenen Fragmenten steuert.
 * Dieser Adapter unterstützt auch das Ausloggen eines Benutzers.
 */
package com.example.shadowwisper.ui.theme.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.ui.HomeFragmentDirections
import com.google.firebase.auth.FirebaseAuth

/**
 * HomeAdapter ist ein RecyclerView.Adapter, der eine Liste von Strings anzeigt.
 * @param itemList Eine Liste von Strings, die in der RecyclerView angezeigt werden sollen.
 */
class HomeAdapter(private val itemList: List<String>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    /**
     * ViewHolder für den HomeAdapter. Diese Klasse hält die UI-Elemente, die für jedes Element in der Liste verwendet werden.
     * @param view Die Ansicht (View), die das Listenelement darstellt.
     */
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_text) // TextView für den Namen des Elements
    }

    /**
     * Erstellt einen neuen ViewHolder, wenn die RecyclerView ihn benötigt.
     * Inflates das Layout für jedes Listenelement.
     * @param parent Die übergeordnete ViewGroup, in die der ViewHolder eingefügt wird.
     * @param viewType Der Typ des ViewHolders, der erstellt werden soll.
     * @return Ein neuer ViewHolder für das Listenelement.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_home, parent, false) // Inflates das Layout für das Listenelement
        return HomeViewHolder(view) // Gibt einen neuen ViewHolder zurück
    }

    /**
     * Bindet die Daten eines Elements an den ViewHolder.
     * Setzt den Text für das Listenelement und steuert die Navigation, wenn auf das Element geklickt wird.
     * @param holder Der ViewHolder, der die Daten hält.
     * @param position Die Position des aktuellen Elements in der Liste.
     */
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = itemList[position] // Holt das Element an der gegebenen Position
        holder.nameTextView.text = item // Setzt den Text des Elements in das TextView

        // Setzt einen Klicklistener, der die Navigation steuert
        holder.itemView.setOnClickListener { view ->
            val action = when (position) {
                0 -> HomeFragmentDirections.actionHomeFragmentToChatoverviewFragment() // Navigiert zur Chat-Übersicht
                1 -> HomeFragmentDirections.actionHomeFragmentToOrderoverviewFragment() // Navigiert zur Bestellübersicht
                2 -> HomeFragmentDirections.actionHomeFragmentToWalletFragment() // Navigiert zur Wallet-Ansicht
                3 -> HomeFragmentDirections.actionHomeFragmentToCharacteroverviewFragment() // Navigiert zur Charakterübersicht
                4 -> {
                    FirebaseAuth.getInstance().signOut() // Meldet den Benutzer ab
                    HomeFragmentDirections.actionHomeFragmentToLoginFragment() // Navigiert zur Login-Ansicht nach dem Ausloggen
                }

                else -> null // Keine Aktion, falls die Position nicht definiert ist
            }
            action?.let { view.findNavController().navigate(it) } // Führt die Navigation durch, falls eine Aktion definiert ist
        }
    }

    /**
     * Gibt die Anzahl der Elemente in der Liste zurück.
     * @return Die Anzahl der Elemente in der Liste.
     */
    override fun getItemCount(): Int {
        return itemList.size // Rückgabe der Anzahl der Listenelemente
    }
}