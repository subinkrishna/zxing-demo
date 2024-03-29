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

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import timber.log.Timber

/**
 * Barcode generator
 */
class BarcodeGenerator {

  companion object {
    /**
     * Generates barcode [Bitmap]
     *
     * @param content - content
     * @param width - bitmap width in pixels
     * @param height - bitmap height in pixels
     * @param format - barcode format
     * @param foregroundColor - bitmap foreground color
     * @param backgroundColor - bitmap background color
     * @return Returns [Bitmap] if the content is valid, else null
     */
    @JvmStatic
    @Suppress("unused")
    fun generateBitmap(
      content: String,
      @Px width: Int,
      @Px height: Int,
      format: BarcodeFormat,
      @ColorInt foregroundColor: Int = Color.BLACK,
      @ColorInt backgroundColor: Int = Color.TRANSPARENT
    ): Bitmap? {
      Timber.d("Generate barcode: $content, $format ($width x $height)")
      return generateBitMatrix(content, width, height, format)
        ?.asBitmap(foregroundColor, backgroundColor)
    }

    /**
     * Generates the [BitMatrix]
     *
     * @param content - content
     * @param width - bitmap width in pixels
     * @param height - bitmap height in pixels
     * @param format - barcode format
     * @return returns [BitMatrix] if the content is valid, else null
     */
    @JvmStatic
    @Suppress("unused")
    fun generateBitMatrix(
      content: String,
      @Px width: Int,
      @Px height: Int,
      format: BarcodeFormat
    ): BitMatrix? {
      if (content.isBlank()) return null
      return try {
        MultiFormatWriter().encode(content, format, width, height)
      } catch (t: Throwable) {
        Timber.e(t, "Unable to generate bit matrix!")
        null
      }
    }
  }
}

/**
 * Converts [BitMatrix] into [Bitmap]
 *
 * @param foreground - foreground color
 * @param background - background color
 * @return bitmap
 */
fun BitMatrix.asBitmap(
  @ColorInt foreground: Int,
  @ColorInt background: Int
): Bitmap {
  val pixels = IntArray(width * height)
  // Convert the BitMatrix into pixels array
  for (y in 0 until height) {
    val offset = y * width
    for (x in 0 until width) {
      pixels[offset + x] = if (get(x, y)) foreground else background
    }
  }
  // Create Bitmap from the pixels
  val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
  bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
  return bitmap
}
