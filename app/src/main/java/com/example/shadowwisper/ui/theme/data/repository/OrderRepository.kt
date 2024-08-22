package com.example.shadowwisper.ui.theme.data.repository

import androidx.lifecycle.LiveData
import com.example.shadowwisper.ui.theme.data.dao.OrderDetailDao
import com.example.shadowwisper.ui.theme.data.model.OrderDetail

class OrderRepository(private val orderDetailDao: OrderDetailDao) {

    val allOrders: LiveData<List<OrderDetail>> = orderDetailDao.getAllOrders()

    suspend fun insert(orderDetail: OrderDetail) {
        orderDetailDao.insert(orderDetail)
    }

    suspend fun update(orderDetail: OrderDetail) {
        orderDetailDao.update(orderDetail)
    }

    fun getOrderById(id: Int): LiveData<OrderDetail> {
        return orderDetailDao.getOrderById(id)
    }
}