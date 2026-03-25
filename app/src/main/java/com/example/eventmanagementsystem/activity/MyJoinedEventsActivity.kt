package com.example.eventmanagementsystem.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.adapter.UserEventAdapter
import com.example.eventmanagementsystem.data.Event
import com.example.eventmanagementsystem.database.EventDatabase
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.eventmanagementsystem.data.EventHistory

class MyJoinedEventsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_joined_events)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Joined Events"

        val userId = intent.getIntExtra("userId", -1)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerJoined)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = EventDatabase.getDatabase(this)
        val eventJoinDao = db.eventJoinDao()
        val historyDao = db.eventHistoryDao()

        val adapter = UserEventAdapter(emptyList(), "Cancel") { event ->

            lifecycleScope.launch {
                eventJoinDao.leaveEvent(event.id, userId)
                Toast.makeText(this@MyJoinedEventsActivity, "Event Cancelled", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        recyclerView.adapter = adapter

        eventJoinDao.getJoinedEvents(userId).observe(this) { events ->

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val today = sdf.parse(sdf.format(Date()))

            val activeEvents = mutableListOf<Event>()

            lifecycleScope.launch {

                for (event in events) {

                    val eventDate = sdf.parse(event.date)

                    if (eventDate != null && !eventDate.after(today)) {

                        // history me add
                        historyDao.insertHistory(
                            EventHistory(
                                eventId = event.id,
                                userId = userId,
                                title = event.title,
                                date = event.date,
                                time = event.time,
                                location = event.location
                            )
                        )

                        // joined se remove
                        eventJoinDao.leaveEvent(event.id, userId)

                    } else {

                        activeEvents.add(event)

                    }
                }

                adapter.updateList(activeEvents)
            }
        }
    }
}