package com.example.myapplication
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.abs


class TrafficDataActivity : AppCompatActivity() {
    private lateinit var sourceLatitude: EditText
    private lateinit var sourceLongitude: EditText
    private lateinit var targetLatitude: EditText
    private lateinit var targetLongitude: EditText
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic_data) // Set the layout for this activity

        sourceLatitude = findViewById(R.id.sourceLatitude)
        sourceLongitude = findViewById(R.id.sourceLongitude)
        targetLatitude = findViewById(R.id.targetLatitude)
        targetLongitude = findViewById(R.id.targetLongitude)
        resultTextView = findViewById(R.id.resultTextView)

        val apiKey = "AIzaSyDjn-lkhaukMFgpC9A87cbZnx2zTc1utlY" // Replace with your Google API key

        val submitButton = findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener {
            val apiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=${sourceLatitude.text},${sourceLongitude.text}&destinations=${targetLatitude.text},${targetLongitude.text}&departure_time=now&key=$apiKey"

            Log.d("URL",apiUrl)
            GlobalScope.launch {
                val response = makeApiCall(apiUrl)
                // Parse and display the response (You can use a JSON parser)
                withContext(Dispatchers.Main) {
                    val (distance, duration, durationInTraffic) = parseApiResponse(response)
//                    val distanceNumeric = parseDistance(distance)
//                    val durationNumeric = parseDuration(duration)
//                    val durationInTrafficNumeric = parseDuration(durationInTraffic)

                    val s1 = calculateSpeed(distance, duration)
                    val s2 = calculateSpeed(distance, durationInTraffic)
                    val speedDifference = abs(s1 - s2)
                   // resultTextView.text = "Distance: $distance\nDuration: $duration\nDuration in Traffic: $durationInTraffic"
                    resultTextView.text = "Distance: $distance m\nDuration: $duration s\nDuration in Traffic: $durationInTraffic s\nSpeed s1: $s1 m/s\nSpeed s2: $s2 m/s\nDifference in speeds : $speedDifference m/s"
                }
            }
        }
    }

    private suspend fun makeApiCall(url: String): String {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                return responseBody ?: ""
            } else {
                // Handle the case when the API call was not successful
                return "API request was not successful: ${response.code}"
            }
        } catch (e: Exception) {
            // Handle exceptions
            return "API request failed: ${e.message}"
        }
    }

    private fun parseApiResponse(apiResponse: String): Triple<Double, Double, Double> {
        try {
            val jsonResponse = JSONObject(apiResponse)
            val status = jsonResponse.optString("status")

            if (status == "OK") {
                val rows = jsonResponse.getJSONArray("rows")
                if (rows.length() > 0) {
                    val elements = rows.getJSONObject(0).getJSONArray("elements")
                    if (elements.length() > 0) {
                        val distance = elements.getJSONObject(0).getJSONObject("distance").getDouble("value")
                        val duration = elements.getJSONObject(0).getJSONObject("duration").getDouble("value")
                        val durationInTraffic = elements.getJSONObject(0).getJSONObject("duration_in_traffic").getDouble("value")
                        return Triple(distance, duration, durationInTraffic)
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return Triple(0.0, 0.0, 0.0)
    }
    private fun parseDistance(distanceText: String): Double {
        // Assuming the distance text is in "meters" (e.g., "1234 m")
        return distanceText.split(" ")[0].toDouble()
    }

    private fun parseDuration(durationText: String): Double {
        // Assuming the duration text is in "seconds" (e.g., "567 s")
        return durationText.split(" ")[0].toDouble()/60
    }

    private fun calculateSpeed(distance: Double, duration: Double): Double {
        // Calculate speed in m/s
        return distance / duration
    }



}

