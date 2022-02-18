package io.sbox.library.view.image

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView

class RoundImageView(context: Context, attrs: AttributeSet? = null) : MaterialCardView(context, attrs) {


    var isFixWidth = true

    private var isCustomSize = false
    private var rate = 0f

    var imageView: ImageView = ImageView(context)
    init {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        addView(imageView)
    }

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