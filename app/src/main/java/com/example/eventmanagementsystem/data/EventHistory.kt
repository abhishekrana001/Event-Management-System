package com.example.eventmanagementsystem.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_history")
data class EventHistory(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val eventId: Int,
    val userId: Int,
    val title: String,
    val date: String,
    val time: String,
    val location: String
)