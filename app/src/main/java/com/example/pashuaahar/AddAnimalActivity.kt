package com.example.pashuaahar

import android.os.Bundle
import android.widget.*
import com.example.pashuaahar.models.Cow
import com.example.pashuaahar.utils.CowStorage

class AddAnimalActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_animal)

        val nameInput = findViewById<EditText>(R.id.etCowName)
        val breedSpinner = findViewById<Spinner>(R.id.spinnerBreed)
        val weightSeekBar = findViewById<SeekBar>(R.id.sbWeight)
        val weightValue = findViewById<TextView>(R.id.tvWeightValue)
        val ageSpinner = findViewById<Spinner>(R.id.spinnerYears)
        val milkInput = findViewById<EditText>(R.id.etMilkYield)
        val saveBtn = findViewById<Button>(R.id.btnSaveCow)
        val cancelBtn = findViewById<Button>(R.id.btnCancel)
        val backBtn = findViewById<ImageView>(R.id.btnBack)

        // Setup Breed Spinner with localized strings
        val breeds = arrayOf(
            getString(R.string.breed_desi),
            getString(R.string.breed_jersey),
            "Holstein",
            "Sahiwal"
        )
        breedSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, breeds)

        // Setup Age Spinner
        val ages = (0..20).map { "$it" }.toTypedArray() 
        ageSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ages)

        // Weight SeekBar logic
        weightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                weightValue.text = "$progress kg"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        saveBtn.setOnClickListener {
            val name = nameInput.text.toString()
            val breed = breedSpinner.selectedItem.toString()
            val weight = weightSeekBar.progress
            val age = ageSpinner.selectedItemPosition
            val milk = milkInput.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isEmpty()) {
                Toast.makeText(this, getString(R.string.please_enter_name), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cow = Cow(
                name = name, 
                breed = breed, 
                weight = weight, 
                age = age, 
                milkYield = milk,
                imageUri = null
            )

            CowStorage.saveCow(this, cow)

            Toast.makeText(this, getString(R.string.cow_added_success), Toast.LENGTH_SHORT).show()
            finish()
        }

        cancelBtn.setOnClickListener { finish() }
        backBtn.setOnClickListener { finish() }
    }
}
