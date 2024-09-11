/**
 * Adapter für eine RecyclerView, die eine Liste von aktiven Charakteren anzeigt.
 * Jeder Charakter wird mit einem Profilbild und Namen dargestellt, und auf einen Klick auf den Charakter wird reagiert.
 */
package com.example.shadowwisper.ui.theme.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.data.model.ActiveCharacter

/**
 * ChatOverviewAdapter ist ein RecyclerView.Adapter, der eine Liste von ActiveCharacter-Objekten anzeigt.
 * @param availableCharacters Liste von Charakteren, die in der RecyclerView angezeigt werden sollen.
 * @param onCharacterClicked Lambda-Funktion, die aufgerufen wird, wenn ein Charakter angeklickt wird.
 */
class ChatOverviewAdapter(
    private val availableCharacters: List<ActiveCharacter>, // Liste der verfügbaren Charaktere
    private val onCharacterClicked: (ActiveCharacter) -> Unit // Callback für Klick-Events auf einen Charakter
) : RecyclerView.Adapter<ChatOverviewAdapter.CharacterViewHolder>() {

    /**
     * Erstellt den ViewHolder für die Charakterelemente.
     * Hier wird das Layout für ein Charakterelement inflated.
     * @param parent Die übergeordnete ViewGroup, in die der ViewHolder eingefügt wird.
     * @param viewType Der Typ des zu erstellenden ViewHolders (hier immer der gleiche).
     * @return Ein neuer ViewHolder für einen Charakter.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_chatoverview, parent, false) // Inflates das Layout für die Charakterübersicht
        return CharacterViewHolder(view) // Gibt einen neuen ViewHolder zurück
    }

    /**
     * Bindet die Daten eines Charakters an den ViewHolder.
     * Diese Methode ruft die bind-Methode des ViewHolders auf, um den aktuellen Charakter anzuzeigen.
     * @param holder Der ViewHolder, der die Daten anzeigt.
     * @param position Die Position des Charakters in der Liste.
     */
    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = availableCharacters[position] // Holt den Charakter an der gegebenen Position
        holder.bind(character) // Bindet den Charakter an den ViewHolder
    }

    /**
     * Gibt die Anzahl der Charaktere in der Liste zurück.
     * @return Die Anzahl der Charaktere in der Liste.
     */
    override fun getItemCount(): Int {
        return availableCharacters.size // Rückgabe der Größe der Charakterliste
    }

    /**
     * ViewHolder für ein Charakterelement.
     * Hält die UI-Elemente für die Anzeige eines Charakternamens und eines Profilbilds.
     */
    inner class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.name_text) // TextView für den Namen des Charakters
        private val profileImageView: ImageView = view.findViewById(R.id.profile_image) // ImageView für das Profilbild des Charakters

        /**
         * Bindet die Daten eines ActiveCharacter an die UI-Elemente des ViewHolders.
         * Setzt den Namen des Charakters und lädt das Profilbild mit Glide.
         * Wenn kein Bild vorhanden ist, wird ein Platzhalterbild verwendet.
         * @param character Der Charakter, dessen Daten gebunden werden sollen.
         */
        fun bind(character: ActiveCharacter) {
            nameTextView.text = character.name // Setzt den Namen des Charakters in das TextView

            // Lädt das Profilbild des Charakters mit Glide. Wenn kein Bild vorhanden ist, wird ein Platzhalter gesetzt.
            if (character.profileImage != null) {
                Glide.with(itemView.context)
                    .load(character.profileImage) // Lädt das Profilbild
                    .placeholder(R.drawable.hex17jpg) // Setzt ein Platzhalterbild, falls kein Bild vorhanden
                    .transform(CircleCrop()) // Formt das Bild zu einem runden Bild
                    .into(profileImageView) // Zeigt das Bild im ImageView an
            } else {
                profileImageView.setImageResource(R.drawable.hex17jpg) // Setzt das Standardbild, falls kein Bild vorhanden ist
            }

            // Setzt einen OnClickListener, der die onCharacterClicked-Funktion aufruft, wenn der Charakter angeklickt wird
            itemView.setOnClickListener { onCharacterClicked(character) }
        }
    }
}