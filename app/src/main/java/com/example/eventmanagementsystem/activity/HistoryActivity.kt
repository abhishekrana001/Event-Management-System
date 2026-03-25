
package com.example.eventmanagementsystem.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.database.EventDatabase
import com.example.eventmanagementsystem.adapter.HistoryAdapter
import com.google.android.material.appbar.MaterialToolbar

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Events History"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val userId = intent.getIntExtra("userId", -1)

        val db = EventDatabase.getDatabase(this)
        val historyDao = db.eventHistoryDao()

        val adapter = HistoryAdapter(emptyList())
        recyclerView.adapter = adapter

        historyDao.getHistory(userId).observe(this) { history ->
            adapter.updateList(history)
        }
    }
}