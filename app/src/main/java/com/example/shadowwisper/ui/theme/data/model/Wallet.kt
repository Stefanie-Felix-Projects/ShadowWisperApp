package com.example.shadowwisper.ui.theme.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet_table")
data class Wallet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val einnahmen: Double,
    val ausgaben: Double,
    val karma: Double,
    val gesamtsumme: Double,
)
