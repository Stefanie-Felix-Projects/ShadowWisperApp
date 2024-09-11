/**
 * Fragment-Klasse `HomeFragment`, die für die Anzeige des Home-Bildschirms der Anwendung zuständig ist.
 * Dieses Fragment zeigt eine Liste von Optionen wie "Chat", "Aufträge", "Wallet", "Character" und "Logout" in einem RecyclerView an.
 */
package com.example.shadowwisper.ui.theme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.data.adapter.HomeAdapter

/**
 * Fragment zur Darstellung der Startseite der App, auf der verschiedene Optionen wie Chat, Aufträge und Wallet angezeigt werden.
 * Das Fragment verwendet ein RecyclerView, um die Liste der Optionen in einem vertikalen Layout anzuzeigen.
 */
class HomeFragment : Fragment() {

    // RecyclerView zur Anzeige der Liste von Optionen auf der Startseite.
    private lateinit var recyclerView: RecyclerView
    // Adapter für das RecyclerView, das die Daten für die Optionen anzeigt.
    private lateinit var homeAdapter: HomeAdapter

    /**
     * Erstellt die View des Home-Fragment und bindet das Layout an das Fragment.
     * Das RecyclerView wird initialisiert und mit einem Adapter bestückt, der die Liste der Optionen anzeigt.
     * @param inflater Das LayoutInflater-Objekt zum Aufblasen des Layouts für dieses Fragment.
     * @param container Das übergeordnete View-Element, in das die Fragment-UI eingebunden wird.
     * @param savedInstanceState Wenn nicht null, wird dieses Fragment mit einem früher gespeicherten Zustand wiederhergestellt.
     * @return Die View für das Fragment-UI, oder null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Bläst das Layout für das Fragment auf.
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialisiert das RecyclerView und setzt das Layout auf eine vertikale Liste (LinearLayout).
        recyclerView = view.findViewById(R.id.rv_home)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Definiert die Daten, die im RecyclerView angezeigt werden (Liste von Optionen).
        val data = listOf("Chat", "Aufträge", "Wallet", "Character", "Logout")

        // Initialisiert den Adapter mit den Daten und setzt den Adapter für das RecyclerView.
        homeAdapter = HomeAdapter(data)
        recyclerView.adapter = homeAdapter

        // Gibt die erstellte View zurück.
        return view
    }

}