package com.example.pashuaahar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pashuaahar.R
import com.example.pashuaahar.models.Cow

class CowAdapter(
    private val list: List<Cow>,
    private val onClick: (Cow) -> Unit
) : RecyclerView.Adapter<CowAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.tvCowName)
        val breed: TextView = v.findViewById(R.id.tvBreed)
        val milk: TextView = v.findViewById(R.id.tvMilk)
        val age: TextView = v.findViewById(R.id.tvAge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cow, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cow = list[position]
        holder.name.text = cow.name
        holder.breed.text = cow.breed
        holder.milk.text = "${cow.milkYield}L/day"
        holder.age.text = "${cow.age} yrs"

        holder.itemView.setOnClickListener {
            onClick(cow)
        }
    }
}