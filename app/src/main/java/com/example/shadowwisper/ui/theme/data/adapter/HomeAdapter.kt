package com.example.shadowwisper.ui.theme.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.ui.HomeFragmentDirections


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

        holder.itemView.setOnClickListener { view ->
            val action = when (position) {
                0 -> HomeFragmentDirections.actionHomeFragmentToChatoverviewFragment()
                1 -> HomeFragmentDirections.actionHomeFragmentToOrderoverviewFragment()
                2 -> HomeFragmentDirections.actionHomeFragmentToWalletFragment()
                3 -> HomeFragmentDirections.actionHomeFragmentToCharacteroverviewFragment()
                else -> null
            }
            action?.let { view.findNavController().navigate(it) }
        }
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
}