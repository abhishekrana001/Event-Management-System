package com.example.eventmanagementsystem.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.util.Calendar
import androidx.lifecycle.lifecycleScope
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.data.Event
import com.example.eventmanagementsystem.database.EventDatabase
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CreateEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Add Event"

        setContentView(R.layout.activity_create_event)
        val db = EventDatabase.getDatabase(this)
        val eventDao = db.eventDao()

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etDate = findViewById<EditText>(R.id.etDate)
        val etTime = findViewById<EditText>(R.id.etTime)
        val etLocation = findViewById<EditText>(R.id.etLocation)
        val btnSave = findViewById<Button>(R.id.btnSaveEvent)

        val eventId = intent.getIntExtra("eventId", -1)

        if (eventId != -1) {
            etTitle.setText(intent.getStringExtra("title"))
            etDescription.setText(intent.getStringExtra("description"))
            etDate.setText(intent.getStringExtra("date"))
            etTime.setText(intent.getStringExtra("time"))
            etLocation.setText(intent.getStringExtra("location"))
        }

        etDate.setOnClickListener {

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    etDate.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                },
                year, month, day
            )

            datePicker.show()
        }

        etTime.setOnClickListener {

            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(this,
                { _, selectedHour, selectedMinute ->

                    val amPm: String
                    var hour12 = selectedHour

                    if (selectedHour >= 12) {
                        amPm = "PM"
                        if (selectedHour > 12) hour12 = selectedHour - 12
                    } else {
                        amPm = "AM"
                        if (selectedHour == 0) hour12 = 12
                    }

                    val formattedMinute = String.format("%02d", selectedMinute)

                    etTime.setText("$hour12:$formattedMinute $amPm")

                },
                hour, minute, false   // ✅ false = 12 hour format
            )

            timePicker.show()
        }


        btnSave.setOnClickListener {

            val title = etTitle.text.toString()
            val description = etDescription.text.toString()
            val date = etDate.text.toString()
            val time = etTime.text.toString()
            val location = etLocation.text.toString()

            // ❌ Agar event date aaj ki hai to save nahi hoga
            if (isToday(date)) {
                Toast.makeText(this, "Today's event cannot be added", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {

                if (eventId == -1) {
                    eventDao.insert(
                        Event(
                            title = title,
                            description = description,
                            date = date,
                            time = time,
                            location = location
                        )
                    )
                } else {
                    eventDao.update(
                        Event(
                            id = eventId,
                            title = title,
                            description = description,
                            date = date,
                            time = time,
                            location = location
                        )
                    )
                }

                Toast.makeText(this@CreateEventActivity, "Saved", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    fun isToday(eventDate: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val today = LocalDate.now()
        val eDate = LocalDate.parse(eventDate, formatter)

        return eDate.isEqual(today)
    }
}