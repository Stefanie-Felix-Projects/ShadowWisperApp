package com.example.shadowwisper.ui.theme.data.view


import androidx.lifecycle.ViewModel
import com.example.shadowwisper.ui.theme.data.adapter.OrderItem
import com.syntax_institut.whatssyntax.R

class OrderViewModel : ViewModel() {

    fun getOrderList(): List<OrderItem> {
        return listOf(
            OrderItem("Order 1", R.drawable.order),
            OrderItem("Order 2", R.drawable.order),
            OrderItem("Order 3", R.drawable.order)
        )
        //ToDo an Datenbank anbinden
    }
}