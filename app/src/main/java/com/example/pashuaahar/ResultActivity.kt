package com.example.pashuaahar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.pashuaahar.utils.SmartFeedEngine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultActivity : BaseActivity() {

    private lateinit var planContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        planContainer = findViewById(R.id.planContainer)

        val name = intent.getStringExtra("name") ?: "Unnamed Cow"
        val breed = intent.getStringExtra("breed") ?: "Desi"
        val weight = intent.getIntExtra("weight", 0)
        val milk = intent.getIntExtra("milk", 0)

        val result = SmartFeedEngine.generate(breed, weight, milk)
        
        val targetProtein = (50 * milk) + (2 * weight)
        val targetEnergy = (70 * milk) + (3 * weight)

        val proteinPercent = if (targetProtein > 0) (result.protein * 100) / targetProtein else 0
        val energyPercent = if (targetEnergy > 0) (result.energy * 100) / targetEnergy else 0

        // Localized plans
        addPlan(getString(R.string.low_cost_plan), result.cost, proteinPercent, energyPercent, getString(R.string.best_value), result.maize, result.cake)
        addPlan(getString(R.string.balanced_plan), (result.cost * 1.2).toInt(), 85, 85, getString(R.string.recommended), result.maize + 1, result.cake)
        addPlan(getString(R.string.high_yield_plan), (result.cost * 1.5).toInt(), 95, 95, getString(R.string.max_profit), result.maize + 2, result.cake + 1)

        findViewById<Button>(R.id.dashboardBtn).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }

    private fun addPlan(title: String, price: Int, protein: Int, energy: Int, tagText: String, maizeKg: Int, cakeKg: Int) {
        val view = layoutInflater.inflate(R.layout.item_feed_plan, planContainer, false)

        view.findViewById<TextView>(R.id.planTitle).text = title
        view.findViewById<TextView>(R.id.planPrice).text = "₹$price/day"
        view.findViewById<TextView>(R.id.tag).text = tagText
        
        view.findViewById<TextView>(R.id.feed1).text = "🌽 ${getString(R.string.maize)}: ${maizeKg}kg"
        view.findViewById<TextView>(R.id.feed2).text = "🥜 ${getString(R.string.cottonseed)}: ${cakeKg}kg"

        view.findViewById<ProgressBar>(R.id.proteinBar).progress = protein
        view.findViewById<ProgressBar>(R.id.energyBar).progress = energy

        view.findViewById<Button>(R.id.saveBtn).setOnClickListener {
            saveToHistory(tagText, price)
            Toast.makeText(this, getString(R.string.plan_saved), Toast.LENGTH_SHORT).show()
        }

        planContainer.addView(view)
    }

    private fun saveToHistory(tag: String, cost: Int) {
        val name = intent.getStringExtra("name") ?: "Unnamed Cow"
        val breed = intent.getStringExtra("breed") ?: "Desi"
        val date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
        
        val historyPrefs = getSharedPreferences("history", Context.MODE_PRIVATE)
        val entry = "$name|$breed|$date|$tag|$cost\n"
        val existingData = historyPrefs.getString("full_history", "") ?: ""
        historyPrefs.edit().putString("full_history", entry + existingData).apply()
    }
}
