package com.example.eventmanagementsystem.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.data.Event
import com.example.eventmanagementsystem.utils.DateUtils

class UserEventAdapter(
    private var eventList: List<Event>,
    private val buttonText: String,
    private val onButtonClick: (Event) -> Unit
) : RecyclerView.Adapter<UserEventAdapter.ViewHolder>() {

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = eventList[position]

        holder.tvTitle.text = event.title
        val displayDate = DateUtils.formatDateForDisplay(event.date)
        holder.tvDate.text = "$displayDate | ${event.time}"
        holder.tvLocation.text = event.location

        val isExpired = DateUtils.isEventExpired(event.date, event.time)

        if (isExpired) {
            holder.tvStatus.text = "Status: Expired"
            holder.tvStatus.setTextColor(Color.parseColor("#EF4444"))
            holder.btnJoin.text = "Expired"
            holder.btnJoin.isEnabled = false
            holder.btnJoin.setOnClickListener(null)
        } else {
            holder.tvStatus.text = "Status: Upcoming"
            holder.tvStatus.setTextColor(Color.parseColor("#10B981"))
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
