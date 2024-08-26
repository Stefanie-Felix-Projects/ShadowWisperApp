package com.example.shadowwisper.ui.theme.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shadowwisper.R
import com.example.shadowwisper.databinding.FragmentChatoverviewBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.example.shadowwisper.ui.theme.data.adapter.ChatOverviewAdapter
import com.example.shadowwisper.ui.theme.data.model.ChatDetail


class ChatoverviewFragment : Fragment() {

    private lateinit var binding: FragmentChatoverviewBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatoverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firestore-Instanz initialisieren
        firestore = FirebaseFirestore.getInstance()

        // Chat-Übersicht laden
        loadChatOverview()
    }

    private fun loadChatOverview() {
        firestore.collection("chats")
            .get()
            .addOnSuccessListener { result ->
                val chatList = mutableListOf<ChatDetail>()
                for (document in result) {
                    val lastMessageDoc = document.reference.collection("messages")
                        .orderBy("timeStamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                        .limit(1)
                        .get()

                    lastMessageDoc.addOnSuccessListener { messages ->
                        for (msg in messages) {
                            val chatDetail = ChatDetail(
                                id = document.id,
                                name = document.id, // Der Name des Charakters als Dokument-ID
                                message = msg.getString("message") ?: "",
                                profileImage = R.drawable.hex17jpg, // Dein Profilbild hier
                                timeStamp = msg.getLong("timeStamp") ?: 0L
                            )
                            chatList.add(chatDetail)
                        }

                        // RecyclerView aktualisieren
                        val adapter = ChatOverviewAdapter(chatList) { chatDetail ->
                            val action = ChatoverviewFragmentDirections.actionChatoverviewFragmentToChatdetailFragment(chatDetail.name)
                            findNavController().navigate(action)
                        }
                        binding.rvChatoverview.layoutManager = LinearLayoutManager(context)
                        binding.rvChatoverview.adapter = adapter
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Fehlerbehandlung
            }
    }
}