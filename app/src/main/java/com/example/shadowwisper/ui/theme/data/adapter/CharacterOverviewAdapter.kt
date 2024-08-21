package com.example.shadowwisper.ui.theme.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.syntax_institut.whatssyntax.R


class CharacterOverviewAdapter(
    private val characterList: List<CharacterItem>,
    private val onItemClicked: (CharacterItem) -> Unit
) : RecyclerView.Adapter<CharacterOverviewAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(view: View, private val onItemClicked: (CharacterItem) -> Unit) :
        RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_text)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val switchButton: Switch = view.findViewById(R.id.switchButton)

        fun bind(characterItem: CharacterItem) {
            nameTextView.text = characterItem.name
            imageView.setImageResource(characterItem.imageResId)
            switchButton.isChecked = characterItem.isActive
            itemView.setOnClickListener {
                onItemClicked(characterItem)
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

data class CharacterItem(val name: String, val imageResId: Int, val isActive: Boolean)