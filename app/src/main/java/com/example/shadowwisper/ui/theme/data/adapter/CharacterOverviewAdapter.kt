package com.example.shadowwisper.ui.theme.data.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail


class CharacterOverviewAdapter(
    private val characterList: List<CharacterDetail>,
    private val onItemClicked: (CharacterDetail) -> Unit
) : RecyclerView.Adapter<CharacterOverviewAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(view: View, private val onItemClicked: (CharacterDetail) -> Unit) :
        RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_text)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val switchButton: Switch = view.findViewById(R.id.switchButton)

        fun bind(characterDetail: CharacterDetail) {
            nameTextView.text = characterDetail.name

            if (characterDetail.profileImage != null) {
                imageView.setImageURI(Uri.parse(characterDetail.profileImage))
            } else {
                imageView.setImageResource(R.drawable.hex17jpg)
            }

            itemView.setOnClickListener {
                onItemClicked(characterDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_charoverview, parent, false)
        return CharacterViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characterList[position])
    }

    override fun getItemCount(): Int {
        return characterList.size
    }
}