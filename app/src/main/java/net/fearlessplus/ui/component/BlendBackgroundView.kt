package net.fearlessplus.ui.component

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import io.sbox.library.extensions.getColorFromRes
import net.fearlessplus.R


class BlendBackgroundView: View {

    interface IBlendListener {
        fun onBitmapMaskUpdated(bmp: Bitmap)
    }

    private val rect = Rect()
    private val bgColor = context.getColorFromRes(R.color.primary)
    val paint = Paint()
    private lateinit var bitmap: Bitmap
    private lateinit var invertedBitmap: Bitmap

    var progress = 0.5
        set(value) {
            field = Math.max(0.0, Math.min(1.0, value))
            updateRect()
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val bitmapConfig = Bitmap.Config.ARGB_8888
        bitmap = Bitmap.createBitmap(w, h, bitmapConfig)
        invertedBitmap = Bitmap.createBitmap(w, h, bitmapConfig)

        updateRect()
    }

    fun getMaskBitmap(): Bitmap? {
        if(this::invertedBitmap.isInitialized) {
            return invertedBitmap
        } else {
            return null
        }
    }
/*

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawBitmap(bitmap, Matrix(), paint)
    }
*/

    private fun updateRect() {
        rect.set(0, 0, (progress * width).toInt(), height)

        // -- Draw the background --
        val canvas = Canvas(bitmap)
        paint.color = context.getColorFromRes(R.color.primary)
        paint.style = Paint.Style.FILL
        canvas.drawColor(bgColor)
        canvas.drawRect(rect, paint)

        // -- Generate the inverted bitmap --
        val invertedCanvas = Canvas(invertedBitmap)
        invertedCanvas.drawColor(bgColor)
        invertedCanvas.drawRect(Rect((progress * width).toInt(), 0, width, height), paint)

        if(context is IBlendListener) {
//            (context as IBlendListener).onBitmapMaskUpdated(invertedBitmap)
        }

        postInvalidate()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val color1 = Color.rgb(137, 195, 246) // outside background's color
        val color2 = Color.rgb(246, 187, 137) // inside background's color
        val color3 = Color.WHITE // outside text's color
        val color4 = Color.BLACK // inside text's color
        canvas.drawColor(color1) // fill outside background

        val paint = Paint()
        paint.color = color2
        canvas.drawArc(
            width * 0.36f, 20f,
            (width * 3 / 4).toFloat(), (height - 20).toFloat(), 0f, 360f, false, paint
        )

        val bitmap = Bitmap.createBitmap(
            width,
            height, Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas = Canvas(bitmap) // new canvas

        /* draw text on bitmapCanvas */
        paint.color = color3
        paint.setTextSize(50f)
        bitmapCanvas.drawText("Hi I am some text", 60f, 60f, paint)

        val xfermode: Xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        paint.xfermode = xfermode
        paint.color = color4
        bitmapCanvas.drawArc(
            width * 0.36f, 20f,
            (width * 3 / 4).toFloat(), (height - 20).toFloat(), 0f, 360f, false, paint
        )

        /* draw this bitmap to original canvas */
        paint.xfermode = null
        canvas.drawBitmap(bitmap, Matrix(), paint)
//        canvas.drawBitmap(bitmap, 0, 0, paint)
    }
}