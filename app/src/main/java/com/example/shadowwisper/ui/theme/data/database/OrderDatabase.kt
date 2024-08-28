package com.example.shadowwisper.ui.theme.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shadowwisper.ui.theme.data.dao.OrderDetailDao
import com.example.shadowwisper.ui.theme.data.model.OrderDetail

@Database(entities = [OrderDetail::class], version = 3, exportSchema = false)
abstract class OrderDatabase : RoomDatabase() {
    abstract fun orderDetailDao(): OrderDetailDao

    companion object {
        @Volatile
        private var INSTANCE: OrderDatabase? = null

        fun getDatabase(context: Context): OrderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OrderDatabase::class.java,
                    "order_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}