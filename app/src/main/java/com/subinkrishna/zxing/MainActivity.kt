package com.subinkrishna.zxing

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val image = findViewById<ImageView>(R.id.code)
        val time = measureTimeMillis {
            image.setImageBitmap(
                BarcodeGenerator.generateBitmap(
                    content = "12345678910",
                    format = BarcodeFormat.QR_CODE,
                    backgroundColor = Color.YELLOW,
                    width = 600,
                    height = 600)
            )
        }
        Log.d("MA", "Took ${time}ms")
    }
}
