package com.example.eventmanagementsystem.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.eventmanagementsystem.R
import com.google.android.material.appbar.MaterialToolbar
import kotlin.jvm.java

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "User Dashboard"

        val userId = intent.getIntExtra("userId", -1)

        val btnJoinEvents = findViewById<Button>(R.id.btnJoinEvents)
        val btnMyEvents = findViewById<Button>(R.id.btnMyEvents)
        val btnHistory = findViewById<Button>(R.id.btnHistory)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnJoinEvents.setOnClickListener {
            val joinIntent = Intent(this, AllEventsActivity::class.java)
            joinIntent.putExtra("userId", userId)
            startActivity(joinIntent)
        }

        btnMyEvents.setOnClickListener {
            val myIntent = Intent(this, MyJoinedEventsActivity::class.java)
            myIntent.putExtra("userId", userId)
            startActivity(myIntent)
        }

        btnHistory.setOnClickListener {
            val historyIntent = Intent(this, HistoryActivity::class.java)
            historyIntent.putExtra("userId", userId)
            startActivity(historyIntent)
        }

        btnLogout.setOnClickListener {
            val logoutIntent = Intent(this, MainActivity::class.java)
            logoutIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logoutIntent)
        }
    }
}