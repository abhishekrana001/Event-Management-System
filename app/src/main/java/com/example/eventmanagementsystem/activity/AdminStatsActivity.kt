package com.example.eventmanagementsystem.activity

import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AppCompatActivity
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.adapter.AdminEventAdapter
import com.example.eventmanagementsystem.database.EventDatabase
import com.google.android.material.appbar.MaterialToolbar

class AdminStatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_stats)


        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarStats)
        setSupportActionBar(toolbar)

        supportActionBar?.title = "Event Statistics"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val tvTotalEvents = findViewById<TextView>(R.id.tvTotalEvents)
        val tvTotalParticipants = findViewById<TextView>(R.id.tvTotalParticipants)

        val database = EventDatabase.getDatabase(this)

        // 🔹 Total Events
        database.eventDao().getTotalEvents().observe(this) { totalEvents ->
            tvTotalEvents.text = "Total Events: $totalEvents"
        }

        // 🔹 Total Participants
        database.eventJoinDao().getTotalParticipants().observe(this) { total ->
            tvTotalParticipants.text =
                getString(R.string.total_participants, total)
        }

        // 🔹 RecyclerView Setup
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAdmin)
        recyclerView.layoutManager = LinearLayoutManager(this)

        database.eventDao().getEventWithParticipantCount().observe(this) { list ->
            recyclerView.adapter = AdminEventAdapter(list)
        }
    }
}