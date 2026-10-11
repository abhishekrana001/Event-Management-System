package com.example.eventmanagementsystem.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.adapter.HistoryAdapter
import com.example.eventmanagementsystem.database.EventDatabase
import com.google.android.material.appbar.MaterialToolbar

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Events History"
        toolbar.setNavigationOnClickListener { finish() }

        val tvEmptyHistory = findViewById<TextView>(R.id.tvEmptyHistory)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val userId = intent.getIntExtra("userId", -1)

        val db = EventDatabase.getDatabase(this)
        val historyDao = db.eventHistoryDao()

        val adapter = HistoryAdapter(emptyList())
        recyclerView.adapter = adapter

        historyDao.getHistory(userId).observe(this) { history ->
            adapter.updateList(history)
            if (history.isEmpty()) {
                tvEmptyHistory.visibility = View.VISIBLE
            } else {
                tvEmptyHistory.visibility = View.GONE
            }
        }
    }
}
