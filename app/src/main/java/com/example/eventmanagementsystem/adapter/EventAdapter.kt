package com.example.eventmanagementsystem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.data.Event
import com.example.eventmanagementsystem.utils.DateUtils

class EventAdapter(
    private var eventList: List<Event>,
    private val onDeleteClick: (Event) -> Unit,
    private val onEditClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]

        holder.tvTitle.text = event.title
        val displayDate = DateUtils.formatDateForDisplay(event.date)
        holder.tvDate.text = "$displayDate | ${event.time}"
        holder.tvLocation.text = event.location

        holder.btnDelete.setOnClickListener {
            onDeleteClick(event)
        }
        holder.btnEdit.setOnClickListener {
            onEditClick(event)
        }
    }

    override fun getItemCount(): Int = eventList.size

    fun updateList(newList: List<Event>) {
        eventList = newList
        notifyDataSetChanged()
    }
}
