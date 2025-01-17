package com.example.myapplication.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "healthApp_data")
data class UserData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nausea_rating: Float,
    val headache_rating: Float,
    val diarrhea_rating: Float,
    val sore_throat_rating: Float,
    val fever_rating: Float,
    val muscle_ache_rating: Float,
    val smell_loss_rating: Float,
    val cough_rating: Float,
    val breath_rating: Float,
    val tiredness_rating: Float,
    val heart_rate: String,
    val acclerometer: Int
) {
    override fun toString(): String {
        return "MedicalData(id=$id, nausea_rating=$nausea_rating, headache_rating=$headache_rating, diarrhea_rating=$diarrhea_rating, sore_throat_rating=$sore_throat_rating, fever_rating=$fever_rating, muscle_ache_rating=$muscle_ache_rating, smell_loss_rating=$smell_loss_rating, cough_rating=$cough_rating, breath_rating=$breath_rating, tiredness_rating=$tiredness_rating)"
    }
}