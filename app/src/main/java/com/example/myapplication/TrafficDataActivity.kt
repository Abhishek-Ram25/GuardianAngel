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
            val apiUrl =
                "https://maps.googleapis.com/maps/api/distancematrix/json?origins=${sourceLatitude.text},${sourceLongitude.text}&destinations=${targetLatitude.text},${targetLongitude.text}&key=$apiKey"
            Log.d("URL",apiUrl)
                GlobalScope.launch {
                val response = makeApiCall(apiUrl)
                // Parse and display the response (You can use a JSON parser)
                withContext(Dispatchers.Main) {
                    resultTextView.text = response
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
}