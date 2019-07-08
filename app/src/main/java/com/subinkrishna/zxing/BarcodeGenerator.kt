package com.subinkrishna.zxing

import android.graphics.Bitmap
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.Px
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

/**
 * Barcode generator
 */
class BarcodeGenerator {

    companion object {

        // Colors
        private const val BLACK = 0xff000000.toInt()
        private const val TRANSPARENT = Color.TRANSPARENT

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
        fun generateBitmap(
            content: String,
            @Px width: Int,
            @Px height: Int,
            @ColorInt foregroundColor: Int = BLACK,
            @ColorInt backgroundColor: Int = TRANSPARENT,
            format: BarcodeFormat = BarcodeFormat.UPC_A
        ): Bitmap? {
            if (content.isBlank()) return null
            return try {
                val bitMatrix = MultiFormatWriter().encode(
                    content, format, width, height)
                bitMatrix.asBitmap(
                    foreground = foregroundColor,
                    background = backgroundColor)
            } catch (t: Throwable) {
                Log.e("BG", "Unable to generate bitmap!", t)
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