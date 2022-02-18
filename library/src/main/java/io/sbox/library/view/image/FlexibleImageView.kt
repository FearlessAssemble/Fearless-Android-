package io.sbox.library.view.image

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView


@SuppressLint("AppCompatCustomView")
class FlexibleImageView(context: Context, attrs: AttributeSet? = null) : ImageView(context, attrs) {

    var isFixWidth = true

    private var isCustomSize = false
    private var rate = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        if (isCustomSize) {
            if (isFixWidth) {
                height = (width * rate).toInt()
            } else {
                width = (height * rate).toInt()
            }
            setMeasuredDimension(width, height)
        } else if (drawable != null) {
            if (isFixWidth) {
                height =
                    Math.ceil((width.toFloat() * drawable.intrinsicHeight.toFloat() / drawable.intrinsicWidth.toFloat()).toDouble())
                        .toInt()
            } else {
                width =
                    Math.ceil((height.toFloat() * drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight.toFloat()).toDouble())
                        .toInt()
            }
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun setWidth(width: Int, rate: Float) {
        setSize((width * rate).toInt(), width)
    }

    fun setHeight(height: Int, rate: Float) {
        setSize((height * rate).toInt(), height)
    }

    fun setSize(width: Int = 0, height: Int = 0) {
        isCustomSize = true
        setMeasuredDimension(width, height)
    }

    fun setRate(rate: Float) {
        isCustomSize = true
        this.rate = rate
    }

}