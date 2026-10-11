package com.example.eventmanagementsystem.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.adapter.EventAdapter
import com.example.eventmanagementsystem.database.EventDatabase
import com.example.eventmanagementsystem.utils.DateUtils
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class ViewEventsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_events)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "All Events"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val tvEmpty = findViewById<TextView>(R.id.tvEmpty)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = EventDatabase.getDatabase(this)
        val eventDao = db.eventDao()

        val adapter = EventAdapter(
            emptyList(),
            onDeleteClick = { event ->
                lifecycleScope.launch {
                    eventDao.delete(event)
                }
            },
            onEditClick = { event ->
                val intent = Intent(this, CreateEventActivity::class.java)
                intent.putExtra("eventId", event.id)
                intent.putExtra("title", event.title)
                intent.putExtra("description", event.description)
                intent.putExtra("date", event.date)
                intent.putExtra("time", event.time)
                intent.putExtra("location", event.location)
                startActivity(intent)
            }
        )

        recyclerView.adapter = adapter

        eventDao.getAllEvents().observe(this) { events ->
            val activeEvents = events.filter { event ->
                !DateUtils.isEventExpired(event.date, event.time)
            }

            adapter.updateList(activeEvents)

            if (activeEvents.isEmpty()) {
                tvEmpty.visibility = View.VISIBLE
            } else {
                tvEmpty.visibility = View.GONE
            }
        }
    }
}
