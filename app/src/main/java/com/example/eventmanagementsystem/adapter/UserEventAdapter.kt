package com.example.eventmanagementsystem.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.data.Event
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserEventAdapter(
    private var eventList: List<Event>,
    private val buttonText: String,
    private val onButtonClick: (Event) -> Unit
): RecyclerView.Adapter<UserEventAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val btnJoin: Button = itemView.findViewById(R.id.btnJoin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_event, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = eventList[position]

        holder.tvTitle.text = event.title
        holder.tvDate.text = "${event.date} - ${event.time}"
        holder.tvLocation.text = event.location

        try {

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm a", Locale.getDefault())

            val eventDateTime = sdf.parse("${event.date} ${event.time}")
            val currentDateTime = Date()

            val today = SimpleDateFormat("dd/M/yyyy", Locale.getDefault()).format(Date())

            if (event.date == today) {

                holder.tvStatus.text = "Status: Closed"
                holder.btnJoin.text = "Closed"
                holder.btnJoin.isEnabled = false

            }
            else if (eventDateTime != null && eventDateTime.before(currentDateTime)) {

                holder.tvStatus.text = "Status: Expired"
                holder.btnJoin.text = "Expired"
                holder.btnJoin.isEnabled = false

            }
            else {

                holder.tvStatus.text = "Status: Upcoming"
                holder.btnJoin.text = buttonText
                holder.btnJoin.isEnabled = true

                holder.btnJoin.setOnClickListener {
                    onButtonClick(event)
                }
            }

        } catch (e: Exception) {

            holder.tvStatus.text = "Status: Upcoming"
            holder.btnJoin.text = buttonText
            holder.btnJoin.isEnabled = true

            holder.btnJoin.setOnClickListener {
                onButtonClick(event)
            }
        }

    }

    override fun getItemCount(): Int = eventList.size

    fun updateList(newList: List<Event>) {
        eventList = newList
        notifyDataSetChanged()
    }
}