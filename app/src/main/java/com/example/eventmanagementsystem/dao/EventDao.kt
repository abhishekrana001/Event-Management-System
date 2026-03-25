package com.example.eventmanagementsystem.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.eventmanagementsystem.data.Event
import com.example.eventmanagementsystem.data.EventWithCount

@Dao
interface EventDao {

    @Insert
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * FROM events ORDER BY id DESC")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("""
        SELECT events.id,
               events.title,
               COUNT(event_join.eventId) as participantCount
        FROM events
        LEFT JOIN event_join
        ON events.id = event_join.eventId
        GROUP BY events.id
        ORDER BY events.id DESC
    """)
    fun getEventWithParticipantCount(): LiveData<List<EventWithCount>>


    @Query("SELECT COUNT(*) FROM events")
    fun getTotalEvents(): LiveData<Int>
}