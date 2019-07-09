package com.subinkrishna.zxing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import timber.log.Timber

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.plant(Timber.DebugTree())

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<BarcodeView>(R.id.code).apply {
      setContent("12345678910")
      setFormat(BarcodeFormat.UPC_A)
      setBitmapWidth(400)
      setBitmapHeight(200)
    }
  }
}
