package com.example.gridviewer

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var cellList = ArrayList<Cell>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun Start(view: View) {
        val intent = Intent(this, ViewActivity::class.java)
        startActivity(intent)
    }
}

