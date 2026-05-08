package com.example.pashuaahar

import android.os.Bundle
import android.widget.*
import com.example.pashuaahar.utils.ReminderHelper
import java.util.*

class RemindersActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)

        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val typeGroup = findViewById<RadioGroup>(R.id.reminderTypeGroup)
        val setBtn = findViewById<Button>(R.id.setReminderBtn)

        setBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth, 
                         timePicker.hour, timePicker.minute, 0)

            val selectedId = typeGroup.checkedRadioButtonId
            val title = if (selectedId == R.id.radioVaccination) getString(R.string.vaccination) else getString(R.string.deworming)
            val msg = if (selectedId == R.id.radioVaccination) getString(R.string.msg_vaccination) else getString(R.string.msg_deworming)

            ReminderHelper.schedule(this, calendar.timeInMillis, title, msg)
            
            Toast.makeText(this, getString(R.string.reminder_set), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
