package com.example.eventmanagementsystem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.data.EventHistory
import com.example.eventmanagementsystem.utils.DateUtils

class HistoryAdapter(
    private var historyList: List<EventHistory>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = historyList[position]
        holder.tvTitle.text = history.title
        val displayDate = DateUtils.formatDateForDisplay(history.date)
        holder.tvDate.text = "$displayDate | ${history.time}"
        holder.tvLocation.text = history.location
    }

    override fun getItemCount(): Int = historyList.size

    fun updateList(newList: List<EventHistory>) {
        historyList = newList
        notifyDataSetChanged()
    }
}
