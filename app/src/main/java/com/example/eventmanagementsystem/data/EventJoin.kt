package com.example.eventmanagementsystem.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_join")
data class EventJoin(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val eventId: Int,
    val userId: Int
)