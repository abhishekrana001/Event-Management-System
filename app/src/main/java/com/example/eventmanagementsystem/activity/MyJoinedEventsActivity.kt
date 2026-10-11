package com.example.eventmanagementsystem.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.adapter.UserEventAdapter
import com.example.eventmanagementsystem.data.Event
import com.example.eventmanagementsystem.data.EventHistory
import com.example.eventmanagementsystem.database.EventDatabase
import com.example.eventmanagementsystem.utils.DateUtils
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class MyJoinedEventsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_joined_events)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Joined Events"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val tvEmptyJoined = findViewById<TextView>(R.id.tvEmptyJoined)
        val userId = intent.getIntExtra("userId", -1)

        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerJoined)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = EventDatabase.getDatabase(this)
        val eventJoinDao = db.eventJoinDao()
        val historyDao = db.eventHistoryDao()

        val adapter = UserEventAdapter(emptyList(), "Cancel Registration") { event ->
            lifecycleScope.launch {
                eventJoinDao.leaveEvent(event.id, userId)
                Toast.makeText(
                    this@MyJoinedEventsActivity,
                    "Registration Cancelled",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        recyclerView.adapter = adapter

        eventJoinDao.getJoinedEvents(userId).observe(this) { events ->
            val activeEvents = mutableListOf<Event>()
            val expiredEvents = mutableListOf<Event>()

            for (event in events) {
                if (DateUtils.isEventExpired(event.date, event.time)) {
                    expiredEvents.add(event)
                } else {
                    activeEvents.add(event)
                }
            }

            adapter.updateList(activeEvents)

            if (activeEvents.isEmpty()) {
                tvEmptyJoined.visibility = View.VISIBLE
            } else {
                tvEmptyJoined.visibility = View.GONE
            }

            if (expiredEvents.isNotEmpty()) {
                lifecycleScope.launch {
                    for (event in expiredEvents) {
                        val count = historyDao.isEventInHistory(event.id, userId)
                        if (count == 0) {
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
                        }
                        eventJoinDao.leaveEvent(event.id, userId)
                    }
                }
            }
        }
    }
}
