package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.math.pow
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {
    lateinit var heartRateVal: MutableLiveData<String>
    private var heartMeasure:String=""
    private val maxRecordsToRead = 1280
    private var accel_val:Float=0.0f
    private val filePicker: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                var acc_val: TextView =findViewById(R.id.acc_val)
                val data: Intent? = result.data
                data?.data?.let { uri ->
                    val inputStream: InputStream? = contentResolver.openInputStream(uri)
                    accel_val = readColumnData(
                        inputStream,
                        0,
                        maxRecordsToRead
                    ) // 0 represents the first column
                    Log.d("Col Data", accel_val.toString())
                    acc_val.text=accel_val.toString();

                }
            }
        }
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        Log.d("URI", uri.toString())
        GlobalScope.launch {
            val result = processVideoFrames(uri)

            Log.d("final",result);
        }
    }

    private suspend fun processVideoFrames(videoUri: Uri?): String {

        val retriever = MediaMetadataRetriever()
        var frameList = ArrayList<Bitmap>()
        try {
            retriever.setDataSource(this, videoUri)

            // Calculate the total duration of the video in microseconds.
            val duration =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
                    ?: 0
            var aduration = duration!!.toInt()
            var i = 10
            while (i < 20) {
                val bitmap = retriever.getFrameAtIndex(i)
                frameList.add(bitmap!!)
                i += 5
            }
        } catch (m_e: Exception) {
        } finally {
            retriever?.release()
            var redBucket: Long = 0
            var pixelCount: Long = 0
            val a = mutableListOf<Long>()
            for (i in frameList) {
                redBucket = 0
                for (y in 0 until 100) {
                    for (x in 0 until 100) {
                        val c: Int = i.getPixel(x, y)
                        pixelCount++
                        redBucket += Color.red(c) + Color.blue(c) + Color.green(c)
                    }
                }
                a.add(redBucket)
            }
            val b = mutableListOf<Long>()
            for (i in 0 until a.lastIndex - 5) {
                var temp =
                    (a.elementAt(i) + a.elementAt(i + 1) + a.elementAt(i + 2) + a.elementAt(
                        i + 3
                    ) + a.elementAt(
                        i + 4
                    )) / 4
                b.add(temp)
            }
            var x = b.elementAt(0)
            var count = 0
            for (i in 1 until b.lastIndex) {
                var p = b.elementAt(i.toInt())
                val dif = p - x;
                Log.d("Diff", dif.toString())
                if ((p - x) > 1000) {
                    count = count + 1
                }
                x = b.elementAt(i.toInt())
            }
            var rate = ((count.toFloat() / 45) * 60).toInt()
            val temp = (rate / 2).toString();
            Log.d("Answer:", temp)
            var tvBloodRate = findViewById<TextView>(R.id.textView)
            tvBloodRate.text = "Your heartbeat is = $temp"
            //heartRateVal.postValue(result);
            return temp.toString()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        heartRateVal = MutableLiveData("empty");
        heartRateVal.observe(this) { result ->

            heartMeasure = result
            Log.d("shek", "heartbeeat")

        }

        var showSymptomsBtn: Button = findViewById(R.id.symptoms)
        // adding on click listener for our button on below line.
        showSymptomsBtn.setOnClickListener {
            val intent = Intent(this,SecondActivity::class.java)
            startActivity(intent)
    }

        val heart = findViewById<Button>(R.id.heartRate)
        heart.setOnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "video/*"
//            startActivityForResult(intent, PICK_VIDEO_REQUEST)
//            Log.d("check:", "str")
            getContent.launch("video/*")
        }
        val acc = findViewById<Button>(R.id.respiratoryRate)
        acc.setOnClickListener {
            openFilePicker()
        }

}
    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*" // Specify the MIME type for CSV files
        filePicker.launch(intent)
    }

    private fun readColumnData(
        inputStream: InputStream?,
        columnIndex: Int,
        maxRecords: Int
    ): Float {
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String? =""
        val columnData = StringBuilder()
        var recordCount = 0
        var ret=0.00

        try {
            var previousValue = 0f
            var currentValue = 0f
            previousValue = 10f
            var k=0
            while (recordCount < maxRecords && reader.readLine().also { line = it } != null) {
                val columns = line ?.split(",") // Assuming columns are separated by commas
                if (columns != null && columns.size > columnIndex) {
                    columnData.append(columns[columnIndex]).append("\n")
                    currentValue = sqrt(
                        columns[columnIndex].toDouble().pow(2.0).toDouble()).toFloat()
                    if (abs(previousValue - currentValue) > 0.15) {
                        k++
                    }
                    previousValue=currentValue

                }
                recordCount++
            }
            ret= (k/45.00)
        }
        catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return ret.toFloat()
    }

}