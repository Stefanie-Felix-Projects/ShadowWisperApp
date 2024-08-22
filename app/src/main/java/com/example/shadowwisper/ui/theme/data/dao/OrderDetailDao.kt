package com.example.shadowwisper.ui.theme.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shadowwisper.ui.theme.data.model.OrderDetail

@Dao
interface OrderDetailDao {

    @Insert
    suspend fun insert(orderDetail: OrderDetail)

    @Update
    suspend fun update(orderDetail: OrderDetail)

    @Query("SELECT * FROM order_details WHERE id = :id")
    fun getOrderById(id: Int): LiveData<OrderDetail>

    @Query("SELECT * FROM order_details")
    fun getAllOrders(): LiveData<List<OrderDetail>>
}