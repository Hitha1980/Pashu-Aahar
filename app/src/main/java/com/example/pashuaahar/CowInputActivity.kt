package com.example.pashuaahar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.*
import java.util.Locale

class CowInputActivity : BaseActivity() {

    private val SPEECH_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val breedSpinner = findViewById<Spinner>(R.id.breedSpinner)
        val weight = findViewById<EditText>(R.id.weightInput)
        val age = findViewById<EditText>(R.id.ageInput)
        val milk = findViewById<EditText>(R.id.milkInput)

        val mic = findViewById<ImageButton>(R.id.micBtn)
        val calc = findViewById<Button>(R.id.calcBtn)
        val logMilkBtn = findViewById<Button>(R.id.logMilkBtn)
        val chat = findViewById<Button>(R.id.chatBtn)
        val tips = findViewById<Button>(R.id.tipsBtn)

        val breeds = arrayOf(getString(R.string.breed_jersey), getString(R.string.breed_desi), "Holstein", "Sahiwal")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, breeds)
        breedSpinner.adapter = adapter

        // Autofill from CowListActivity
        val namePassed = intent.getStringExtra("name")
        val breedPassed = intent.getStringExtra("breed")
        val weightVal = intent.getIntExtra("weight", 0)
        val ageVal = intent.getIntExtra("age", 0)
        val milkVal = intent.getDoubleExtra("milk", 0.0)

        if (namePassed != null) {
            nameInput.setText(namePassed)
            weight.setText(weightVal.toString())
            age.setText(ageVal.toString())
            milk.setText(milkVal.toString())
            val spinnerPosition = adapter.getPosition(breedPassed)
            if (spinnerPosition >= 0) breedSpinner.setSelection(spinnerPosition)
        }

        mic.setOnClickListener {
            val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
            val lang = prefs.getString("lang", "en") ?: "en"
            
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, if(lang == "kn") "kn-IN" else "en-US")
            startActivityForResult(intent, SPEECH_CODE)
        }

        calc.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("name", nameInput.text.toString().ifEmpty { "Unnamed Cow" })
            intent.putExtra("breed", breedSpinner.selectedItem.toString())
            intent.putExtra("weight", weight.text.toString().toIntOrNull() ?: 0)
            intent.putExtra("milk", milk.text.toString().toIntOrNull() ?: 0)
            startActivity(intent)
        }

        logMilkBtn.setOnClickListener {
            val intent = Intent(this, MilkLoggingActivity::class.java)
            intent.putExtra("name", nameInput.text.toString().ifEmpty { "Unnamed Cow" })
            startActivity(intent)
        }

        chat.setOnClickListener {
            val intent = Intent(this, VetChatActivity::class.java)
            val contextInfo = "Cow: ${nameInput.text}, Breed: ${breedSpinner.selectedItem}, Weight: ${weight.text}kg, Milk Yield: ${milk.text}L/day"
            intent.putExtra("cow_context", contextInfo)
            startActivity(intent)
        }

        tips.setOnClickListener {
            startActivity(Intent(this, VetTipsActivity::class.java))
        }
    }

    override fun onActivityResult(req: Int, res: Int, data: Intent?) {
        super.onActivityResult(req, res, data)
        if (req == SPEECH_CODE && res == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            findViewById<EditText>(R.id.weightInput).setText(result?.get(0))
        }
    }
}
