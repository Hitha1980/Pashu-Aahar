package com.example.pashuaahar.utils

import android.content.Context
import com.example.pashuaahar.models.Cow
import com.example.pashuaahar.models.MilkEntry
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

object CowStorage {

    fun saveCow(context: Context, cow: Cow) {
        val prefs = context.getSharedPreferences("cows", Context.MODE_PRIVATE)
        val old = prefs.getString("data", "[]")

        val array = JSONArray(old)

        val obj = JSONObject()
        obj.put("name", cow.name)
        obj.put("breed", cow.breed)
        obj.put("weight", cow.weight)
        obj.put("age", cow.age)
        obj.put("milkYield", cow.milkYield)

        array.put(obj)

        prefs.edit().putString("data", array.toString()).apply()
    }

    fun getCows(context: Context): List<Cow> {
        val prefs = context.getSharedPreferences("cows", Context.MODE_PRIVATE)
        val data = prefs.getString("data", "[]")

        val list = mutableListOf<Cow>()
        val array = JSONArray(data)

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            list.add(
                Cow(
                    obj.getString("name"),
                    obj.getString("breed"),
                    obj.getInt("weight"),
                    obj.getInt("age"),
                    obj.optDouble("milkYield", 0.0)
                )
            )
        }

        return list
    }

    fun addMilkEntry(context: Context, cowName: String, amount: Double) {
        val prefs = context.getSharedPreferences("milk_history", Context.MODE_PRIVATE)
        val key = "history_$cowName"
        val existingData = prefs.getString(key, "[]")
        val array = JSONArray(existingData)

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        
        // Find if entry for today already exists to update it (summing morning/evening)
        var updated = false
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            if (obj.getString("date") == date) {
                val currentAmount = obj.getDouble("amount")
                obj.put("amount", currentAmount + amount)
                updated = true
                break
            }
        }

        if (!updated) {
            val newObj = JSONObject()
            newObj.put("date", date)
            newObj.put("amount", amount)
            array.put(newObj)
        }

        prefs.edit().putString(key, array.toString()).apply()
    }

    fun getMilkHistory(context: Context, cowName: String): List<MilkEntry> {
        val prefs = context.getSharedPreferences("milk_history", Context.MODE_PRIVATE)
        val key = "history_$cowName"
        val data = prefs.getString(key, "[]")

        val list = mutableListOf<MilkEntry>()
        val array = JSONArray(data)

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(MilkEntry(obj.getString("date"), obj.getDouble("amount")))
        }
        
        return list.sortedBy { it.date }
    }
}
