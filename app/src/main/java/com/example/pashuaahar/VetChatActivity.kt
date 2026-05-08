package com.example.pashuaahar

import android.os.Bundle
import android.widget.*
import com.example.pashuaahar.ai.GenAIHelper

class VetChatActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val input = findViewById<EditText>(R.id.userInput)
        val output = findViewById<TextView>(R.id.chatText)
        val sendBtn = findViewById<Button>(R.id.sendBtn)

        val cowContext = intent.getStringExtra("cow_context")

        sendBtn.setOnClickListener {
            val question = input.text.toString()
            if (question.isBlank()) return@setOnClickListener

            output.text = getString(R.string.calculating)

            GenAIHelper.askVet(question, cowContext) { response ->
                runOnUiThread {
                    output.text = response
                }
            }
        }
    }
}
