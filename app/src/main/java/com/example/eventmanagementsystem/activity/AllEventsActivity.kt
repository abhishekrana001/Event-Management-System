package com.example.eventmanagementsystem.activity

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.adapter.UserEventAdapter
import com.example.eventmanagementsystem.database.EventDatabase
import com.example.eventmanagementsystem.data.EventJoin
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.LocalDate

class AllEventsActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_events)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Join Events"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userId = intent.getIntExtra("userId", -1)

        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerAllEvents)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = EventDatabase.getDatabase(this)
        val eventDao = db.eventDao()
        val eventJoinDao = db.eventJoinDao()

        val adapter = UserEventAdapter(emptyList(), "Join") { event ->

            lifecycleScope.launch {

                // 1️⃣ Expired event check
                try {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val eventDate = LocalDate.parse(event.date, formatter)
                    val currentDate = LocalDate.now()

                    if (eventDate.isBefore(currentDate)) {
                        Toast.makeText(
                            this@AllEventsActivity,
                            "This event has expired",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@launch
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                // 2️⃣ Check duplicate event
                val alreadyJoined = eventJoinDao.isUserJoined(event.id, userId)

                if (alreadyJoined != null) {

                    Toast.makeText(
                        this@AllEventsActivity,
                        "You already joined this event",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@launch
                }

                // 3️⃣ Same time event check
                val sameTimeEvent = eventJoinDao.isUserJoinedSameTimeEvent(
                    userId,
                    event.date,
                    event.time
                )

                if (sameTimeEvent != null) {

                    Toast.makeText(
                        this@AllEventsActivity,
                        "You already joined another event at this time",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@launch
                }

                // 4️⃣ Join event
                eventJoinDao.joinEvent(
                    EventJoin(
                        eventId = event.id,
                        userId = userId
                    )
                )

                Toast.makeText(
                    this@AllEventsActivity,
                    "Event Joined Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        recyclerView.adapter = adapter

        eventDao.getAllEvents().observe(this) { events ->
            adapter.updateList(events)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
