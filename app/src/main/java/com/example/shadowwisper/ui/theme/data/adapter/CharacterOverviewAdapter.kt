package com.example.shadowwisper.ui.theme.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.syntax_institut.whatssyntax.R


class CharacterOverviewAdapter(private val characterList: List<CharacterItem>) :
    RecyclerView.Adapter<CharacterOverviewAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_text)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val switchButton: Switch = view.findViewById(R.id.switchButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_charoverview, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val characterItem = characterList[position]
        holder.nameTextView.text = characterItem.name
        holder.imageView.setImageResource(characterItem.imageResId)
        holder.switchButton.isChecked = characterItem.isActive
    }

    override fun getItemCount(): Int {
        return characterList.size
    }
}

data class CharacterItem(val name: String, val imageResId: Int, val isActive: Boolean)