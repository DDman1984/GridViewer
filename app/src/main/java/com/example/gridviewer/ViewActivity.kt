package com.example.gridviewer

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ViewActivity : AppCompatActivity() {
    var screenWidth = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels

        val gridView = findViewById(R.id.grid) as GridView
        GetDataTask(
            this,
            "https://jsonplaceholder.typicode.com/photos",
            gridView,
            screenWidth
        ).execute()
    }

    //task class
    internal class GetDataTask(
        private var c: Context,
        private var jsonURL: String,
        private var gridView: GridView,
        private var widthFix: Int
    ) : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String {
            var connection = URL(jsonURL).openConnection() as HttpURLConnection
            try {
                connection.connect()
                if (connection.responseCode == 200) {
                    val `is` = BufferedInputStream(connection.inputStream)
                    val br = BufferedReader(InputStreamReader(`is`))
                    val jsonData = StringBuffer()
                    var line: String?

                    do {
                        line = br.readLine()

                        if (line == null) {
                            break
                        }

                        jsonData.append(line);

                    } while (true)

                    br.close()
                    `is`.close()
                    return jsonData.toString()
                } else {
                    return "Error " + connection.responseMessage
                }
            } finally {
                connection.disconnect()
            }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            JSONParser(c, result, gridView, widthFix).execute()
        }
    }
}