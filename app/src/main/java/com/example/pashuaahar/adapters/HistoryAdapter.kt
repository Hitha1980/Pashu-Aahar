package com.example.pashuaahar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pashuaahar.R
import com.example.pashuaahar.models.FeedHistory

class HistoryAdapter(private var list: List<FeedHistory>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.cowName)
        val date: TextView = view.findViewById(R.id.dateText)
        val tag: TextView = view.findViewById(R.id.tag)
        val price: TextView = view.findViewById(R.id.price)
        val refresh: ImageButton = view.findViewById(R.id.refreshBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.name.text = "${item.name} - ${item.breed}"
        holder.date.text = item.date
        holder.tag.text = item.tag
        holder.price.text = "₹${item.cost}"

        holder.refresh.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Recalculating for ${item.name}...", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateList(newList: List<FeedHistory>) {
        list = newList
        notifyDataSetChanged()
    }
}
