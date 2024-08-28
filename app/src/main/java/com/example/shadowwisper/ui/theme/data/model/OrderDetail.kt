package com.example.shadowwisper.ui.theme.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "order_details")
data class OrderDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val orderName: String,
    val subText: String,
    val image: Int,
    val mapImage: Int,
    val storyTitle: String,
    val storyText: String,
    val karma: Int,
    val money: Int,
    val profileImage: ByteArray?
)
