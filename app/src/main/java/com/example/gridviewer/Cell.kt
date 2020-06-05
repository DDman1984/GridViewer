package com.example.gridviewer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class Cell(var id: String, var desc: String, var image: String, var thumbnailImage: String) {
    var imgUrl: String = image
    var thumbnailUrl: String = thumbnailImage
    public fun GetBitMap(c: Context, view: ImageView) {
        if (view == null) {
            return
        }

        DownloadImageTask(view, thumbnailUrl).execute()
    }
}

class DownloadImageTask(private var img: ImageView, private var imgURL: String) :
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