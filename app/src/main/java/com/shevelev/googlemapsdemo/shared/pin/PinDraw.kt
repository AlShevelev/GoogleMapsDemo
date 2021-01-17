package com.shevelev.googlemapsdemo.shared.pin

import android.content.Context
import android.graphics.*
import android.net.Uri
import androidx.annotation.ColorInt
import com.shevelev.googlemapsdemo.R

class PinDraw
constructor(private val context: Context) {

    private data class MeasuredText(
        val text: String,
        val width: Int,
        val height: Int,
        val baselineOffset: Int
    )

    private val scaleFactor = 1f

    // Sizes [px]
    private val height = 410.scale()
    private val widthMax = 990.scale()

    private val pinWidth = 330.scale()
    private val pinImageSize = 300.scale()
    private val pinSpearheadWidth = 40.scale()

    private val textMargin = 25.scale()
    private val textTagHeight = 80.scale()

    fun draw(backgroundColor: Int, textColor: Int, image: Uri?, text: String?): PinDrawResult =
        draw(backgroundColor, textColor, cropUriImageToCircle(image), text)

    private fun draw(backgroundColor: Int, textColor: Int, image: Bitmap?, text: String?): PinDrawResult {
        return if(text.isNullOrBlank()) {
            drawWithoutText(image, backgroundColor)
        } else {
            drawWithText(image, text, backgroundColor, textColor)
        }
    }

    private fun drawWithoutText(image: Bitmap?, @ColorInt color: Int) : PinDrawResult {
        val output = Bitmap.createBitmap(pinWidth, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()

        drawPin(canvas, paint, color, pinWidth, height)
        drawImage(canvas, paint, image, pinWidth)

        return PinDrawResult(output, 0.5f)
    }

    private fun drawWithText(image: Bitmap?, text: String, @ColorInt backgroundColor: Int, @ColorInt textColor: Int) : PinDrawResult {
        val paint = Paint()
        paint.textSize = context.resources.getDimension(R.dimen.textSizeSmall)

        val maxTextWidth = widthMax.scale() - pinWidth - textMargin * 2

        val measuredText = measureText(text, paint, maxTextWidth.toFloat())

        val width = pinWidth + textMargin*2 + measuredText.width + textTagHeight/2

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        drawPin(
            canvas,
            paint,
            backgroundColor,
            pinWidth,
            height)

        drawTag(
            canvas,
            paint,
            backgroundColor,
            textColor,
            pinWidth,
            textMargin,
            measuredText)

        drawImage(
            canvas,
            paint,
            image,
            pinWidth)

        return PinDrawResult(output, (pinWidth / width).toFloat())
    }

    /**
     * Calculates text length to inscribe in into the painted area
     * @return painted area len and inscribed
     */
    private fun measureText(text: String, paint: Paint, maxTextWidth: Float): MeasuredText {
        val textBounds = Rect()

        paint.getTextBounds(text, 0, text.length, textBounds)

        val textWidth = textBounds.width()

        if(textWidth <= maxTextWidth) {
            return MeasuredText(text, textBounds.width(), textBounds.height(), textBounds.bottom)
        }

        val oneCharWidth = textWidth / text.length
        val maxChars = (maxTextWidth / oneCharWidth).toInt()

        return MeasuredText(text.substring(0, maxChars-3)+"...", maxChars * oneCharWidth, textBounds.height(), textBounds.bottom)
    }

    private fun drawPin(canvas: Canvas, paint: Paint, @ColorInt color: Int, pinWidth: Int, pinHeight: Int) {
        paint.color = color
        paint.style = Paint.Style.FILL

        // The image background
        canvas.drawCircle(pinWidth/2f, pinWidth/2f, pinWidth/2f, paint)

        // The pin spearhead
        val spearheadPath = Path()
        spearheadPath.moveTo((pinWidth-pinSpearheadWidth)/2f, pinWidth*0.95f)
        spearheadPath.lineTo((pinWidth+pinSpearheadWidth)/2f, pinWidth*0.95f)
        spearheadPath.lineTo(pinWidth/2f, pinHeight.toFloat())
        spearheadPath.lineTo((pinWidth-pinSpearheadWidth)/2f, pinWidth*0.95f)
        canvas.drawPath(spearheadPath, paint)
    }

    private fun drawImage(canvas: Canvas, paint: Paint, image: Bitmap?, pinWidth: Int) {
        image?.let {
            val destOffset = (pinWidth - pinImageSize) / 2

            val imageDestRect = Rect(destOffset, destOffset, destOffset + pinImageSize, destOffset + pinImageSize)
            canvas.drawBitmap(it, Rect(0, 0, it.width, it.height), imageDestRect, paint)
        }
    }

    private fun drawTag(
        canvas: Canvas,
        paint: Paint,
        @ColorInt backgroundColor: Int,
        @ColorInt textColor: Int,
        pinWidth: Int,
        textMargin: Int,
        measuredText: MeasuredText
    ) {

        val path = Path()
        paint.color = backgroundColor
        paint.style = Paint.Style.FILL

        val left = pinWidth*0.95f
        val leftOffset = pinWidth - left
        val right = left +leftOffset + textMargin + measuredText.width

        val top = (pinWidth - textTagHeight) / 2f
        val bottom = (pinWidth + textTagHeight) / 2f

        // Tag
        path.moveTo(left, top)
        path.lineTo(right, top)
        path.arcTo(right - textTagHeight/2f, top, right + textTagHeight/2f, bottom, 270f, 180f, false)
        path.lineTo(left, bottom)
        path.lineTo(left, top)
        canvas.drawPath(path, paint)

        paint.color = textColor
        canvas.drawText(
            measuredText.text,
            (pinWidth + textMargin).toFloat(),
            ((pinWidth + measuredText.height) / 2f) - measuredText.baselineOffset,
            paint)
    }

    private fun cropUriImageToCircle(imageUri: Uri?): Bitmap? =
        imageUri?.let {
            val bitmap = context.contentResolver.openInputStream(it).use { stream ->
                BitmapFactory.decodeStream(stream)
            }

            cropBitmapToCircle(bitmap)
        }

    private fun cropBitmapToCircle(bitmap: Bitmap): Bitmap {
        val outputSize = bitmap.width.coerceAtMost(bitmap.height)
        val output = Bitmap.createBitmap(outputSize, outputSize, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(output)
        val paint = Paint()
        paint.isAntiAlias = true

        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(outputSize/2f, outputSize/2f, outputSize/2f, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        val bitmapRect = if(bitmap.width > bitmap.height) {
            val left = (bitmap.width-outputSize)/2
            Rect(left, 0, bitmap.width - left, outputSize)
        } else {
            val top = (bitmap.height - outputSize) / 2
            Rect(0, top, outputSize, bitmap.height - top)
        }
        canvas.drawBitmap(bitmap, bitmapRect, Rect(0, 0, outputSize, outputSize), paint)

        return output
    }

    private fun Int.scale() = (this*scaleFactor).toInt() + 1
}