package com.example.shadowwisper.ui.theme.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shadowwisper.ui.theme.data.dao.CharacterDetailDao
import com.example.shadowwisper.ui.theme.data.model.CharacterDetail

@Database(entities = [CharacterDetail::class], version = 2, exportSchema = false)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDetailDao(): CharacterDetailDao

    companion object {
        @Volatile
        private var INSTANCE: CharacterDatabase? = null

        fun getDatabase(context: Context): CharacterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CharacterDatabase::class.java,
                    "character_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}