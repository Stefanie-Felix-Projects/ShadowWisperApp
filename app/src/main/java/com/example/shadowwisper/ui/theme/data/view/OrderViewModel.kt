package com.example.shadowwisper.ui.theme.data.view


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shadowwisper.ui.theme.data.adapter.OrderItem
import com.example.shadowwisper.ui.theme.data.database.OrderDatabase
import com.example.shadowwisper.ui.theme.data.model.OrderDetail
import com.example.shadowwisper.ui.theme.data.repository.OrderRepository
import com.syntax_institut.whatssyntax.R
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OrderRepository
    val allOrders: LiveData<List<OrderDetail>>

    init {
        val orderDetailDao = OrderDatabase.getDatabase(application).orderDetailDao()
        repository = OrderRepository(orderDetailDao)
        allOrders = repository.allOrders
    }

    fun insert(orderDetail: OrderDetail) = viewModelScope.launch {
        repository.insert(orderDetail)
    }

    fun update(orderDetail: OrderDetail) = viewModelScope.launch {
        repository.update(orderDetail)
    }

    fun getOrderById(id: Int): LiveData<OrderDetail> {
        return repository.getOrderById(id)
    }
}