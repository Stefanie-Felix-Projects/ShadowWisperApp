package com.example.shadowwisper.ui.theme.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.syntax_institut.whatssyntax.R

class HomeAdapter(private val itemList: List<String>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val nameTextView: TextView = view.findViewById(R.id.name_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_home, parent,false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = itemList[position]
        holder.nameTextView.text = item
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}