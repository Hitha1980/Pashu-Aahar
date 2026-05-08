package com.example.pashuaahar

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val milkChart = findViewById<LineChart>(R.id.milkChart)
        val costChart = findViewById<LineChart>(R.id.costChart)
        val profitChart = findViewById<LineChart>(R.id.profitChart)

        val historyData = getHistoryData()

        setupChart(milkChart, historyData.milkEntries, getString(R.string.milk_yield_litres), R.color.green_action)
        setupChart(costChart, historyData.costEntries, getString(R.string.feed_cost_rs), R.color.orange_action)
        setupChart(profitChart, historyData.profitEntries, getString(R.string.daily_profit_rs), R.color.primary_dark)

        displayReport(historyData.totalCost, historyData.totalMarket)
    }

    private data class HistoryEntries(
        val milkEntries: List<Entry>,
        val costEntries: List<Entry>,
        val profitEntries: List<Entry>,
        val totalCost: Float,
        val totalMarket: Float
    )

    private fun getHistoryData(): HistoryEntries {
        val historyPrefs = getSharedPreferences("history", Context.MODE_PRIVATE)
        val raw = historyPrefs.getString("data", "") ?: ""
        val lines = raw.split("\n").filter { it.isNotEmpty() }

        val milkEntries = mutableListOf<Entry>()
        val costEntries = mutableListOf<Entry>()
        val profitEntries = mutableListOf<Entry>()
        
        var totalCost = 0f
        var totalMarket = 0f

        lines.forEachIndexed { index, line ->
            val parts = line.split(",")
            if (parts.size == 2) {
                val milkVal = parts[0].toFloatOrNull() ?: 0f
                val costVal = parts[1].toFloatOrNull() ?: 0f
                val marketVal = milkVal * 120f
                val profitVal = marketVal - costVal

                totalCost += costVal
                totalMarket += marketVal

                val x = (index + 1).toFloat()
                milkEntries.add(Entry(x, milkVal))
                costEntries.add(Entry(x, costVal))
                profitEntries.add(Entry(x, profitVal))
            }
        }

        if (milkEntries.isEmpty()) {
            return HistoryEntries(
                listOf(Entry(1f, 8f), Entry(2f, 10f), Entry(3f, 9f), Entry(4f, 11f), Entry(5f, 12f)),
                listOf(Entry(1f, 500f), Entry(2f, 450f), Entry(3f, 400f), Entry(4f, 380f), Entry(5f, 350f)),
                listOf(Entry(1f, 300f), Entry(2f, 500f), Entry(3f, 600f), Entry(4f, 700f), Entry(5f, 850f)),
                2080f,
                3000f
            )
        }

        return HistoryEntries(milkEntries, costEntries, profitEntries, totalCost, totalMarket)
    }

    private fun setupChart(chart: LineChart, entries: List<Entry>, label: String, colorRes: Int) {
        val dataSet = LineDataSet(entries, label)
        dataSet.color = ContextCompat.getColor(this, colorRes)
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.primary_dark))
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setDrawValues(true)

        chart.data = LineData(dataSet)
        chart.description.isEnabled = false
        chart.animateX(1000)
        chart.invalidate()
    }

    private fun displayReport(totalCost: Float, totalMarket: Float) {
        val savingsVal = totalMarket - totalCost
        
        val reportDetails = """
            ${getString(R.string.total_cost)}: ₹${totalCost.toInt()}
            ${getString(R.string.market_cost)}: ₹${totalMarket.toInt()}
            ${getString(R.string.savings)}: ₹${savingsVal.toInt()}
        """.trimIndent()

        findViewById<TextView>(R.id.reportTitleText).text = getString(R.string.monthly_report)
        findViewById<TextView>(R.id.reportDetailsText).text = reportDetails
    }
}
