package com.example.shadowwisper.ui.theme.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.syntax_institut.whatssyntax.R


class OrderOverviewAdapter(private val orderList: List<OrderItem>) : RecyclerView.Adapter<OrderOverviewAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val nameTextView: TextView = view.findViewById(R.id.name_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_orderoverview, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderItem = orderList[position]
        holder.nameTextView.text = orderItem.orderName
        holder.imageView.setImageResource(orderItem.imageResId)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}

data class OrderItem(val orderName: String, val imageResId: Int)