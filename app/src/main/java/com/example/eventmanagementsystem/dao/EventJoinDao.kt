package com.example.eventmanagementsystem.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.eventmanagementsystem.data.Event
import com.example.eventmanagementsystem.data.EventJoin

@Dao
interface EventJoinDao {

    @Insert
    suspend fun joinEvent(eventJoin: EventJoin)

    @Query("""
    SELECT events.* FROM events
    INNER JOIN event_join
    ON events.id = event_join.eventId
    WHERE event_join.userId = :userId
""")
    fun getJoinedEvents(userId: Int): LiveData<List<Event>>

    @Query("SELECT * FROM event_join WHERE eventId = :eventId AND userId = :userId LIMIT 1")
    suspend fun isUserJoined(eventId: Int, userId: Int): EventJoin?

    @Query("DELETE FROM event_join WHERE eventId = :eventId AND userId = :userId")
    suspend fun leaveEvent(eventId: Int, userId: Int)

    @Query("SELECT COUNT(*) FROM event_join WHERE eventId = :eventId")
    suspend fun getJoinCount(eventId: Int): Int

    // ✅ Same time validation
    @Query("""
    SELECT event_join.* FROM event_join
    INNER JOIN events
    ON event_join.eventId = events.id
    WHERE event_join.userId = :userId
    AND events.date = :date
    AND events.time = :time
    LIMIT 1
""")
    suspend fun isUserJoinedSameTimeEvent(
        userId: Int,
        date: String,
        time: String
    ): EventJoin?

    @Query("SELECT COUNT(*) FROM event_join")
    fun getTotalParticipants(): LiveData<Int>
}