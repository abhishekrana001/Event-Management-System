package com.example.eventmanagementsystem.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.data.Event
import com.example.eventmanagementsystem.database.EventDatabase
import com.example.eventmanagementsystem.utils.DateUtils
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class CreateEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val eventId = intent.getIntExtra("eventId", -1)
        supportActionBar?.title = if (eventId == -1) "Add Event" else "Edit Event"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val db = EventDatabase.getDatabase(this)
        val eventDao = db.eventDao()

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etDate = findViewById<EditText>(R.id.etDate)
        val etTime = findViewById<EditText>(R.id.etTime)
        val etLocation = findViewById<EditText>(R.id.etLocation)
        val btnSave = findViewById<Button>(R.id.btnSaveEvent)

        if (eventId != -1) {
            etTitle.setText(intent.getStringExtra("title"))
            etDescription.setText(intent.getStringExtra("description"))
            etDate.setText(intent.getStringExtra("date"))
            etTime.setText(intent.getStringExtra("time"))
            etLocation.setText(intent.getStringExtra("location"))
            btnSave.text = "Update Event"
        }

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format(
                        Locale.getDefault(),
                        "%02d/%02d/%04d",
                        selectedDay,
                        selectedMonth + 1,
                        selectedYear
                    )
                    etDate.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePicker.show()
        }

        etTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    val amPm = if (selectedHour >= 12) "PM" else "AM"
                    var hour12 = if (selectedHour > 12) selectedHour - 12 else selectedHour
                    if (hour12 == 0) hour12 = 12

                    val formattedTime = String.format(
                        Locale.getDefault(),
                        "%02d:%02d %s",
                        hour12,
                        selectedMinute,
                        amPm
                    )
                    etTime.setText(formattedTime)
                },
                hour,
                minute,
                false
            )
            timePicker.show()
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val date = etDate.text.toString().trim()
            val time = etTime.text.toString().trim()
            val location = etLocation.text.toString().trim()

            if (title.isEmpty() || description.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (DateUtils.isPastDate(date)) {
                Toast.makeText(this, "Cannot create event in the past date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val event = Event(
                    id = if (eventId != -1) eventId else 0,
                    title = title,
                    description = description,
                    date = date,
                    time = time,
                    location = location
                )

                if (eventId == -1) {
                    eventDao.insert(event)
                    Toast.makeText(this@CreateEventActivity, "Event Created Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    eventDao.update(event)
                    Toast.makeText(this@CreateEventActivity, "Event Updated Successfully", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }
    }
}
