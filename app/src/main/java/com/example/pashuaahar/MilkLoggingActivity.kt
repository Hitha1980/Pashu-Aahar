package com.example.pashuaahar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.pashuaahar.utils.CowStorage

class MilkLoggingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milk_logging)

        val cowName = intent.getStringExtra("name") ?: getString(R.string.unnamed_cow)
        findViewById<TextView>(R.id.cowNameTxt).text = cowName

        val morningEdit = findViewById<EditText>(R.id.morningMilkEdit)
        val eveningEdit = findViewById<EditText>(R.id.eveningMilkEdit)

        findViewById<Button>(R.id.saveMorningBtn).setOnClickListener {
            val amount = morningEdit.text.toString().toDoubleOrNull() ?: 0.0
            if (amount > 0) {
                CowStorage.addMilkEntry(this, cowName, amount)
                val msg = getString(R.string.milk_logged_msg, "Morning", amount.toString())
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                morningEdit.text.clear()
            }
        }

        findViewById<Button>(R.id.saveEveningBtn).setOnClickListener {
            val amount = eveningEdit.text.toString().toDoubleOrNull() ?: 0.0
            if (amount > 0) {
                CowStorage.addMilkEntry(this, cowName, amount)
                val msg = getString(R.string.milk_logged_msg, "Evening", amount.toString())
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                eveningEdit.text.clear()
            }
        }

        findViewById<Button>(R.id.viewAnalyticsBtn).setOnClickListener {
            val intent = Intent(this, AnalyticsActivity::class.java)
            intent.putExtra("name", cowName)
            startActivity(intent)
        }
    }
}
