package com.subinkrishna.zxing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val image = findViewById<ImageView>(R.id.code)

        val t1 = System.currentTimeMillis()
        val matrix = BarcodeGenerator.generateBitMatrix(
            content = "00000000002",
            format = BarcodeFormat.UPC_A,
            width = 500,
            height = 100
        )
        val t2 = System.currentTimeMillis()
        val bitmap = matrix?.asBitmap(background = Color.YELLOW, foreground = Color.BLACK)
        val t3 = System.currentTimeMillis()

        Log.d("BIV", "Matrix: ${t2 - t1}ms, bitmap: ${t3 - t2}ms")

        image.setImageBitmap(bitmap)
        image.postInvalidate()
    }
}

class BarcodeImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    val TAG = "BIV"

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "onMeasure: $measuredWidth x $measuredHeight")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d(TAG, "Size changed: $w x $h")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(TAG, "Attached")
        post { Log.d(TAG, "--- do this") }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        Log.d(TAG, "setBitmap")
    }
}
