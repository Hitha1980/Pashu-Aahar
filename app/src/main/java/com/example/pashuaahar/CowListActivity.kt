package com.example.pashuaahar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pashuaahar.adapters.CowAdapter
import com.example.pashuaahar.utils.CowStorage

class CowListActivity : BaseActivity() {

    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cow_list)

        recycler = findViewById(R.id.cowRecycler)
        recycler.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.addCowBtn).setOnClickListener {
            startActivity(Intent(this, AddAnimalActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        val cows = CowStorage.getCows(this)
        recycler.adapter = CowAdapter(cows) { selectedCow ->
            val intent = Intent(this, CowInputActivity::class.java)
            intent.putExtra("name", selectedCow.name)
            intent.putExtra("breed", selectedCow.breed)
            intent.putExtra("weight", selectedCow.weight)
            intent.putExtra("age", selectedCow.age)
            intent.putExtra("milk", selectedCow.milkYield)
            startActivity(intent)
        }
    }
}
