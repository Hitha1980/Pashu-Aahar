package com.example.pashuaahar.ai

import android.os.Handler
import android.os.Looper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object GenAIHelper {

    private const val API_KEY = "AIzaSyAeko_kqwTmJGVrpGiZNORbfV4L_rlj1TY"

    fun askVet(question: String, cowDetails: String? = null, callback: (String) -> Unit) {

        val client = OkHttpClient()
        val contextInfo = if (!cowDetails.isNullOrEmpty()) "Animal Details: $cowDetails." else ""

        val finalQuestion = """
            You are 'Pashu-Maitra', an expert veterinary assistant for rural farmers in Karnataka.
            This app helps farmers optimize cattle feed and track performance using parameters: Breed, Weight, Age, and Milk Yield.

            Visual understanding of graphs in the app:
            - IN THE LINE GRAPHS (Milk Yield, Feed Cost, Daily Profit): 
                * The X-Axis represents the Timeline (Date or Days).
                * The Y-Axis represents the Value (Litres for milk, Rupees ₹ for cost and profit).
            - IN THE ANALYTICS BAR CHART (Cost vs Profit):
                * The X-Axis shows Categories (Side-by-side 'Cost' and 'Profit').
                * The Y-Axis shows the Amount in Rupees (₹).

            Instructions for your response:
            1. If referring to trends, always use the phrase "In the graph..."
            2. Language Rule: If the user asks in Kannada, you MUST reply in Kannada using simple rural terms. If they ask in English, reply in English.
            3. Mention the specific parameters (X-axis time or Y-axis yield/profit) if it helps explain the answer.
            4. Provide short, practical, and helpful veterinary or financial advice considering the animal's breed and weight.

            $contextInfo

            Question: $question
        """.trimIndent()

        val json = JSONObject()
        val partsArray = org.json.JSONArray().put(JSONObject().put("text", finalQuestion))
        val contentsArray = org.json.JSONArray().put(JSONObject().put("parts", partsArray))
        json.put("contents", contentsArray)

        val body = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=$API_KEY")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val lowerQ = question.lowercase()
                val response = when {
                    lowerQ.contains("milk") || lowerQ.contains("ಹಾಲು") -> 
                        "ಹಾಲಿನ ಇಳುವರಿ ಹೆಚ್ಚಿಸಲು ಪ್ರೋಟೀನ್ ಆಹಾರ ಮತ್ತು ಶುದ್ಧ ನೀರನ್ನು ನೀಡಿ."
                    lowerQ.contains("fever") || lowerQ.contains("ಜ್ವರ") -> 
                        "ಹಸುವಿಗೆ ಜ್ವರವಿದ್ದರೆ ಕೂಡಲೇ ಪಶುವೈದ್ಯರನ್ನು ಸಂಪರ್ಕಿಸಿ."
                    else -> "ಹಸುವಿಗೆ ಸಮತೋಲಿತ ಆಹಾರ ಮತ್ತು ಸ್ವಚ್ಛತೆಯನ್ನು ಕಾಪಾಡಿ."
                }
                Handler(Looper.getMainLooper()).post { callback("OFFLINE: $response") }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val text = JSONObject(response.body?.string() ?: "")
                        .getJSONArray("candidates").getJSONObject(0)
                        .getJSONObject("content").getJSONArray("parts").getJSONObject(0)
                        .getString("text")
                    Handler(Looper.getMainLooper()).post { callback(text) }
                } catch (e: Exception) {
                    onFailure(call, IOException("API Error"))
                }
            }
        })
    }
}
