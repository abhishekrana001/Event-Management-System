package com.example.eventmanagementsystem.database


import com.example.eventmanagementsystem.dao.EventJoinDao
import com.example.eventmanagementsystem.data.User
import com.example.eventmanagementsystem.dao.UserDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eventmanagementsystem.data.Event
import com.example.eventmanagementsystem.dao.EventDao
import com.example.eventmanagementsystem.dao.EventHistoryDao
import com.example.eventmanagementsystem.data.EventHistory
import com.example.eventmanagementsystem.data.EventJoin

@Database(
    entities = [
        Event::class,
        User::class,
        EventJoin::class,
        EventHistory::class
    ],
    version = 3
)
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
    abstract fun eventJoinDao(): EventJoinDao
    abstract fun eventHistoryDao(): EventHistoryDao
    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getDatabase(context: Context): EventDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    "event_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}