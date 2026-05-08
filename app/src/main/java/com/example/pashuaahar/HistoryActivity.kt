package com.example.pashuaahar

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pashuaahar.adapters.HistoryAdapter
import com.example.pashuaahar.models.FeedHistory

class HistoryActivity : BaseActivity() {

    private lateinit var adapter: HistoryAdapter
    private var allHistory: List<FeedHistory> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val recycler = findViewById<RecyclerView>(R.id.historyRecycler)
        val search = findViewById<EditText>(R.id.searchInput)

        allHistory = loadHistory()

        adapter = HistoryAdapter(allHistory)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        search.addTextChangedListener {
            val query = it.toString().lowercase()
            val filtered = allHistory.filter { item ->
                item.name.lowercase().contains(query) ||
                item.date.lowercase().contains(query) ||
                item.breed.lowercase().contains(query) ||
                item.tag.lowercase().contains(query)
            }
            adapter.updateList(filtered)
        }
    }

    private fun loadHistory(): List<FeedHistory> {
        val prefs = getSharedPreferences("history", Context.MODE_PRIVATE)
        val rawData = prefs.getString("full_history", "") ?: ""
        
        if (rawData.isEmpty()) {
            return listOf(
                FeedHistory("Gauri", "Jersey", "Oct 24, 2023", "BALANCED", 450),
                FeedHistory("Lakshmi", "Gir", "Oct 22, 2023", "PROTEIN RICH", 520),
                FeedHistory("Nandini", "HF", "Oct 15, 2023", "HIGH YIELD", 610)
            )
        }

        return rawData.split("\n")
            .filter { it.isNotEmpty() }
            .map { line ->
                val parts = line.split("|")
                if (parts.size == 5) {
                    FeedHistory(
                        name = parts[0],
                        breed = parts[1],
                        date = parts[2],
                        tag = parts[3],
                        cost = parts[4].toIntOrNull() ?: 0
                    )
                } else {
                    null
                }
            }.filterNotNull()
    }
}
