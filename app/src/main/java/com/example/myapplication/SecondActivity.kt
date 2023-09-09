package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.SharedPreferences
import android.widget.*
import androidx.lifecycle.ViewModelProvider

class SecondActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var autocompleteTV: AutoCompleteTextView
    private lateinit var ratingBar: RatingBar
    private lateinit var medicalDataViewModal: MedicalDataViewModal
    private var items = mutableListOf<SymptomEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        var uploadSymptomsBtn: Button = findViewById(R.id.button1)

        uploadSymptomsBtn.setOnClickListener {
            // on below line we are displaying the toast message.
            val selectedSymptom = autocompleteTV.text.toString()
            // Store the selected rating in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putFloat(selectedSymptom, 0.0f)
            editor.apply()

            val newEntry = MedicalData(0, items[0].rating, items[1].rating,items[2].rating,items[3].rating,items[4].rating,
                items[5].rating,items[6].rating,items[7].rating,items[8].rating,items[9].rating,"0",0)
            medicalDataViewModal.insert(newEntry)

        }

        var goBackBtn: Button = findViewById(R.id.goBackButton)

        goBackBtn.setOnClickListener{
//
            val intent= Intent(this,MainActivity::class.java)
//
            startActivity(intent)
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SymptomRatings", MODE_PRIVATE)

        val symptoms = resources.getStringArray(R.array.Symptoms_list)
        for (symptom in symptoms) {
            items.add(SymptomEntity(symptom, 0.0f))
        }
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, symptoms)
        autocompleteTV = findViewById(R.id.autoCompleteTextView)
        ratingBar = findViewById(R.id.newRatingBar)

        autocompleteTV.setAdapter(arrayAdapter)

        // Add an OnItemClickListener to AutoCompleteTextView
        autocompleteTV.setOnItemClickListener { _, _, position, _ ->
            val selectedSymptom = arrayAdapter.getItem(position).toString()
            // Retrieve and set the rating for the selected symptom (if any)
            val storedRating = sharedPreferences.getFloat(selectedSymptom, 0.0f)
            ratingBar.rating = storedRating
            ratingBar.visibility = RatingBar.VISIBLE // Show the RatingBar
        }

        // Add an OnRatingBarChangeListener to RatingBar
        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                val selectedSymptom = autocompleteTV.text.toString()
                // Store the selected rating in SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putFloat(selectedSymptom, rating)
                //items.add(SymptomEntity(selectedSymptom,rating))
                var itemToUpdate = items.find { it.symptom == selectedSymptom }

                // Update the rating for the selected symptom
                itemToUpdate?.rating = rating
                editor.apply()
            }
        medicalDataViewModal = ViewModelProvider(this).get(MedicalDataViewModal::class.java)

        //val selectedSymptom = autocompleteTV.text.toString()
        //val selectedRating = ratingBar.rating
        //val symptomEntity = SymptomEntity(symptom = selectedSymptom, rating = selectedRating)


    }
}
