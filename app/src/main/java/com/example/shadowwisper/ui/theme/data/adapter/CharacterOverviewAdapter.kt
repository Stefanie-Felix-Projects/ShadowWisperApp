/**
 * Adapter für die RecyclerView, der die Liste der Charaktere anzeigt.
 * Verwendet Glide für die Bildverarbeitung und Firebase Firestore zur Speicherung des Status der aktiven Charaktere.
 */
package com.example.shadowwisper.ui.theme.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Diese Klasse ist der Adapter für die Anzeige von Charakteren in einer RecyclerView.
 * Der Adapter nimmt eine Liste von CharacterDetail-Objekten sowie zwei Lambda-Funktionen für Klick- und Switch-Events.
 */
class CharacterOverviewAdapter(
    private val characterList: List<CharacterDetail>, // Liste der Charaktere
    private val onItemClicked: (CharacterDetail) -> Unit, // Callback für Klick auf ein Element
    private val onSwitchToggled: (CharacterDetail, Boolean) -> Unit // Callback für das Umschalten des Switches
) : RecyclerView.Adapter<CharacterOverviewAdapter.CharacterViewHolder>() {

    /**
     * ViewHolder, der die UI-Elemente für jeden Charakter hält.
     * Diese Klasse ist für die Verbindung der Daten mit den Views in der Layout-Datei verantwortlich.
     */
    class CharacterViewHolder(view: View, private val onItemClicked: (CharacterDetail) -> Unit) :
        RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_text) // TextView für den Charakternamen
        val imageView: ImageView = view.findViewById(R.id.imageView) // ImageView für das Charakterbild
        val switchButton: Switch = view.findViewById(R.id.switchButton) // Switch zur Auswahl des aktiven Charakters

        /**
         * Verbindet die Daten eines CharacterDetail-Objekts mit den UI-Elementen im ViewHolder.
         * Lädt das Charakterbild mit Glide und holt den Status des Switches aus Firebase Firestore.
         * Handhabt die Logik, wenn der Switch geändert wird.
         */
        fun bind(
            characterDetail: CharacterDetail, // Charakterdetails, die gebunden werden sollen
            onSwitchToggled: (CharacterDetail, Boolean) -> Unit // Callback für das Umschalten des Switches
        ) {
            // Setzt den Namen des Charakters in das TextView
            nameTextView.text = characterDetail.name

            // Lädt das Profilbild des Charakters, wenn es vorhanden ist. Ansonsten wird ein Platzhalter verwendet.
            if (characterDetail.profileImage != null) {
                Glide.with(itemView.context)
                    .load(characterDetail.profileImage)
                    .placeholder(R.drawable.hex17jpg)
                    .transform(CircleCrop()) // Rundes Bild
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.hex17jpg) // Setzt das Standardbild, falls kein Bild vorhanden ist
            }

            // Holt den aktuellen Nutzer aus FirebaseAuth und greift auf die Firestore-Datenbank zu
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val firestore = FirebaseFirestore.getInstance()
            val activeCharacterRef = firestore.collection("users")
                .document(userId)
                .collection("active_character")
                .document(characterDetail.characerId)

            // Überprüft, ob der Charakter aktiv ist, indem die Daten aus Firestore abgerufen werden
            activeCharacterRef.get()
                .addOnSuccessListener { document ->
                    switchButton.isChecked =
                        document.exists() && document.getBoolean("isActive") == true // Setzt den Switch-Status
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Fehler beim Abrufen des Charakterstatus", e)
                }

            // Setzt einen Listener für den Switch, um den Status des Charakters zu aktualisieren, wenn der Switch geändert wird
            switchButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Schaltet den Charakter aktiv, indem alle anderen Charaktere als inaktiv gesetzt werden
                    firestore.collection("users")
                        .document(userId)
                        .collection("active_character")
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                if (document.id != characterDetail.characerId) {
                                    document.reference.update("isActive", false)
                                }
                            }

                            // Fügt den Charakter zu den aktiven Charakteren hinzu
                            activeCharacterRef.set(
                                mapOf(
                                    "characterID" to characterDetail.characerId,
                                    "name" to characterDetail.name,
                                    "profileImage" to characterDetail.profileImage,
                                    "isActive" to true
                                )
                            )

                            // Speichert den Charakter auch in einer globalen Sammlung von aktiven Charakteren
                            firestore.collection("all_active_characters")
                                .document(characterDetail.characerId)
                                .set(
                                    mapOf(
                                        "characterID" to characterDetail.characerId,
                                        "name" to characterDetail.name,
                                        "profileImage" to characterDetail.profileImage,
                                        "userId" to userId,
                                        "isActive" to true
                                    )
                                )
                                .addOnSuccessListener {
                                    Log.d(
                                        "Firestore",
                                        "Charakter erfolgreich als aktiv markiert: ${characterDetail.name}"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Firestore", "Fehler beim Hinzufügen des Charakters", e)
                                }

                            onSwitchToggled(characterDetail, true) // Ruft den Callback auf, wenn der Switch aktiviert wurde
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Fehler beim Abrufen der aktiven Charaktere", e)
                        }
                } else {
                    // Entfernt den Charakter aus den aktiven Charakteren
                    activeCharacterRef.delete()

                    firestore.collection("all_active_characters")
                        .document(characterDetail.characerId)
                        .update("isActive", false)
                        .addOnSuccessListener {
                            Log.d(
                                "Firestore",
                                "Charakter erfolgreich als inaktiv markiert: ${characterDetail.name}"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Fehler beim Deaktivieren des Charakters", e)
                        }

                    onSwitchToggled(characterDetail, false) // Ruft den Callback auf, wenn der Switch deaktiviert wurde
                }
            }

            // Setzt den OnClickListener, der die onItemClicked-Funktion aufruft, wenn auf das Element geklickt wird
            itemView.setOnClickListener {
                onItemClicked(characterDetail)
            }
        }
    }

    /**
     * Erstellt einen neuen ViewHolder, wenn die RecyclerView ihn benötigt.
     * Inflated das Layout für jedes Element in der Liste.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_charoverview, parent, false)
        return CharacterViewHolder(view, onItemClicked)
    }

    /**
     * Verbindet die Daten eines bestimmten Elements in der Liste mit dem ViewHolder.
     */
    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characterList[position], onSwitchToggled)
    }

    /**
     * Gibt die Anzahl der Elemente in der Liste zurück.
     */
    override fun getItemCount(): Int {
        return characterList.size
    }
}
