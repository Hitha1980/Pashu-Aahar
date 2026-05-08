package com.example.pashuaahar.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build

object ReminderHelper {

    fun schedule(context: Context, timeMillis: Long, title: String, msg: String) {

        val intent = Intent(context, ReminderReceiver::class.java)
        intent.putExtra("title", title)
        intent.putExtra("msg", msg)

        val pendingIntent = PendingIntent.getBroadcast(
            context, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent)
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent)
        }
    }
}
