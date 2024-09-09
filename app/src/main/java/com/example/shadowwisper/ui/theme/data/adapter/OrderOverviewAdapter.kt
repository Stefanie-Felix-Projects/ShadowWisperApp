package com.example.shadowwisper.ui.theme.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shadowwisper.R
import com.example.shadowwisper.ui.theme.data.model.OrderOverview


class OrderOverviewAdapter(
    private val orderList: List<OrderOverview>,
    private val onItemClicked: (OrderOverview) -> Unit
) : RecyclerView.Adapter<OrderOverviewAdapter.OrderViewHolder>() {

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
        holder.nameTextView.text = orderItem.orderTitle
        holder.imageView.setImageResource(orderItem.profileImage)

        holder.itemView.setOnClickListener {
            onItemClicked(orderItem)
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}
