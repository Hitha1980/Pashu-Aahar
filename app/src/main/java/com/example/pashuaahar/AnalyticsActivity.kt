package com.example.pashuaahar

import android.graphics.Color
import android.os.Bundle
import com.example.pashuaahar.utils.CowStorage
import com.example.pashuaahar.utils.SmartFeedEngine
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter

class AnalyticsActivity : BaseActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var barChart: BarChart
    private var cowName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        cowName = intent.getStringExtra("name") ?: ""
        lineChart = findViewById(R.id.lineChart)
        barChart = findViewById(R.id.barChart)

        setupMilkTrendChart()
        setupCostProfitChart()
    }

    private fun setupMilkTrendChart() {
        val history = CowStorage.getMilkHistory(this, cowName).takeLast(7)
        
        if (history.isEmpty()) {
            lineChart.setNoDataText(getString(R.string.no_milk_data))
            return
        }

        val entries = history.mapIndexed { index, milkEntry ->
            Entry(index.toFloat(), milkEntry.amount.toFloat())
        }

        val dataSet = LineDataSet(entries, getString(R.string.milk_yield_litres))
        dataSet.color = Color.BLUE
        dataSet.valueTextSize = 12f
        dataSet.setCircleColor(Color.RED)
        dataSet.lineWidth = 2f
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.BLUE
        dataSet.fillAlpha = 50

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val idx = value.toInt()
                return if (idx >= 0 && idx < history.size) {
                    history[idx].date.substring(5) 
                } else ""
            }
        }
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM

        lineChart.description.text = getString(R.string.milk_trend_description)
        lineChart.animateX(1000)
        lineChart.invalidate()
    }

    private fun setupCostProfitChart() {
        val history = CowStorage.getMilkHistory(this, cowName).takeLast(7)
        if (history.isEmpty()) {
            barChart.setNoDataText(getString(R.string.no_data_available))
            return
        }

        val avgMilk = history.map { it.amount }.average()
        
        val weight = 400 
        val feedPlan = SmartFeedEngine.generate("Desi", weight, avgMilk.toInt())
        
        val dailyCost = feedPlan.cost.toFloat()
        val dailyIncome = (avgMilk * 40).toFloat() 
        val profit = dailyIncome - dailyCost

        val entries = listOf(
            BarEntry(1f, dailyCost),
            BarEntry(2f, profit)
        )

        val dataSet = BarDataSet(entries, getString(R.string.cost_profit_label))
        dataSet.colors = listOf(Color.RED, Color.GREEN)
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        barChart.data = barData

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value == 1f) getString(R.string.cost) else if (value == 2f) getString(R.string.profit) else ""
            }
        }
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f

        barChart.description.text = getString(R.string.daily_avg_rs)
        barChart.animateY(1000)
        barChart.invalidate()
    }
}
