package com.example.gridviewer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ImageActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val img: ImageView = findViewById<ImageView>(R.id.GridImg)
        var bundle = intent.extras
        var url = ""
        if (bundle != null) {
            url = bundle.getString("url").toString()

            var t = findViewById<TextView>(R.id.GridInfo) as TextView
            t.text = bundle.getString("info").toString()
            var t2 = findViewById<TextView>(R.id.GridInfo2) as TextView
            t2.text = bundle.getString("info2").toString()
        }


        DownloadImgTask(img, url).execute()
    }

    class DownloadImgTask(private var img: ImageView, private var imgURL: String) :
        AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg p0: String?): Bitmap {

            val separate2 = imgURL.split("/")
            var j: String = separate2[3].toString()
            var b: String = separate2[4].toString()
            val s = "https://ipsumimage.appspot.com/$j,$b"
            val url: String = imgURL.toString()
            var bitmap: Bitmap
            var connection = URL(s).openConnection() as HttpURLConnection
            try {
                connection.connect()
                val inputStream: InputStream = connection.inputStream
                bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                return bitmap
            } catch (malformedUrlException: MalformedURLException) {
                // Handle error
            } catch (ioException: IOException) {
                // Handle error
            }

            bitmap = Bitmap.createBitmap(5, 5, Bitmap.Config.ARGB_8888)
            return bitmap
        }

        override fun onPostExecute(result: Bitmap) {
            img.setImageBitmap(result)
        }
    }
}