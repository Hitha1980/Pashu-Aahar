package com.example.pashuaahar.utils

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.annotation.SuppressLint

object NotificationHelper {

    @SuppressLint("MissingPermission")
    fun show(context: Context) {

        val builder = NotificationCompat.Builder(context, "feed_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Feed Reminder")
            .setContentText("Time to feed your cattle")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        NotificationManagerCompat.from(context).notify(1, builder.build())
    }
}
