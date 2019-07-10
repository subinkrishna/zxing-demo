/**
 * Copyright (C) 2019 Subinkrishna Gopi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.subinkrishna.zxing

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import android.util.AttributeSet
import com.google.zxing.BarcodeFormat
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

/**
 * Custom AppCompatImageView implementation that asynchronously
 * generates and renders a barcode
 *
 * @see BarcodeGenerator
 */
open class BarcodeView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

  /** Barcode configuration */
  data class Config(
    val content: String,
    val format: BarcodeFormat,
    val width: Int,
    val height: Int
  )

  private var config = Config("", BarcodeFormat.UPC_A, 0, 0)
  private val configStream = BehaviorSubject.create<Config>()
  private val disposables = CompositeDisposable()

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    disposables.add(
      configStream
        .filter { it.width > 0 && it.height > 0 }
        .distinctUntilChanged()
        .switchMap { config ->
          Observable.just(
            BarcodeGenerator.generateBitmap(
              content = config.content,
              format = config.format,
              width = config.width,
              height = config.height
            )
          )
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
          { bitmap -> setImageBitmap(bitmap) },
          { error -> Timber.e(error, "Unable to create bitmap!") }
        )
    )
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    disposables.dispose()
  }

  fun setContent(content: String) {
    config = config.copy(content = content)
    configStream.onNext(config)
  }

  fun setFormat(format: BarcodeFormat) {
    config = config.copy(format = format)
    configStream.onNext(config)
  }

  fun setBitmapWidth(width: Int) {
    config = config.copy(width = width)
    configStream.onNext(config)
  }

  fun setBitmapHeight(height: Int) {
    config = config.copy(height = height)
    configStream.onNext(config)
  }
}
