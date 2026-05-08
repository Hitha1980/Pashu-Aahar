package com.example.pashuaahar

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.*
import android.widget.Button
import com.example.pashuaahar.utils.NotificationHelper

class MainActivity : BaseActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        findViewById<Button>(R.id.startBtn).setOnClickListener {
            startActivity(Intent(this, CowListActivity::class.java))
        }

        findViewById<Button>(R.id.langBtn).setOnClickListener {
            val currentLang = resources.configuration.locales[0].language
            val newLang = if (currentLang == "en") "kn" else "en"
            saveLang(newLang)
            
            // Re-apply locale and restart to see changes
            applyLocale()
            recreate()
        }

        findViewById<Button>(R.id.historyBtn).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        findViewById<Button>(R.id.dashboardBtnMain).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        findViewById<Button>(R.id.remindersBtn).setOnClickListener {
            startActivity(Intent(this, RemindersActivity::class.java))
        }

        createChannel()

        Handler(Looper.getMainLooper()).postDelayed({
            NotificationHelper.show(this)
        }, 5000)
    }

    private fun saveLang(lang: String) {
        prefs.edit().putString("lang", lang).apply()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "feed_channel",
                "Feed Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )
            getSystemService(NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }
}
