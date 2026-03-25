package com.example.eventmanagementsystem.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.eventmanagementsystem.R
import com.google.android.material.appbar.MaterialToolbar

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Admin Dashboard"

        val btnAddEvent = findViewById<Button>(R.id.btnAddEvent)
        val btnViewEvents = findViewById<Button>(R.id.btnViewEvents)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnViewStats = findViewById<Button>(R.id.btnViewStats)

        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // 📋 Normal Events List
        btnViewEvents.setOnClickListener {
            startActivity(Intent(this, ViewEventsActivity::class.java))
        }

        // 📊 Stats Screen
        btnViewStats.setOnClickListener {
            startActivity(Intent(this, AdminStatsActivity::class.java))
        }

        // ➕ Add Event
        btnAddEvent.setOnClickListener {
            startActivity(Intent(this, CreateEventActivity::class.java))
        }
    }
}