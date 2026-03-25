package com.example.eventmanagementsystem.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.eventmanagementsystem.data.EventHistory

@Dao
interface EventHistoryDao {

    @Insert
    suspend fun insertHistory(eventHistory: EventHistory)

    @Query("SELECT * FROM event_history WHERE userId = :userId ORDER BY eventId DESC")
    fun getHistory(userId: Int): LiveData<List<EventHistory>>

    @Query("SELECT COUNT(*) FROM event_history WHERE eventId = :eventId AND userId = :userId")
    suspend fun isEventInHistory(eventId: Int, userId: Int): Int
}