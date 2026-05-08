package com.example.pashuaahar.utils

import android.content.Context
import com.example.pashuaahar.R
import java.util.*

object SeasonalFeedHelper {

    fun getSeason(): String {
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1

        return when (month) {
            in 3..6 -> "Summer"
            in 7..10 -> "Monsoon"
            else -> "Winter"
        }
    }

    fun getSuggestion(context: Context): String {
        return when (getSeason()) {
            "Summer" -> context.getString(R.string.suggestion_summer)
            "Monsoon" -> context.getString(R.string.suggestion_monsoon)
            else -> context.getString(R.string.suggestion_winter)
        }
    }
}
