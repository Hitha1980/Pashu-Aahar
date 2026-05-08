package com.example.pashuaahar

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.pashuaahar.models.Cow
import com.example.pashuaahar.utils.CowStorage

class AddCowActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cow)

        val name = findViewById<EditText>(R.id.nameInput)
        val breed = findViewById<EditText>(R.id.breedInput)
        val weight = findViewById<EditText>(R.id.weightInput)
        val age = findViewById<EditText>(R.id.ageInput)
        val milkYield = findViewById<EditText>(R.id.milkYieldInput)

        findViewById<Button>(R.id.saveBtn).setOnClickListener {
            val nameText = name.text.toString()
            val breedText = breed.text.toString()
            val weightText = weight.text.toString()
            val ageText = age.text.toString()
            val milkYieldText = milkYield.text.toString()

            if (nameText.isEmpty() || breedText.isEmpty() || weightText.isEmpty() || ageText.isEmpty() || milkYieldText.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cow = Cow(
                nameText,
                breedText,
                weightText.toInt(),
                ageText.toInt(),
                milkYieldText.toDouble()
            )

            CowStorage.saveCow(this, cow)

            Toast.makeText(this, getString(R.string.cow_saved), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
