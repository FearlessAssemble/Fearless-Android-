package io.sbox.library.extensions

import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.scrollToPositionSpeed(position: Int = 0, factor: Float = 0.1f) {
    val linearSmoothScroller = object : LinearSmoothScroller(context) {
        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return super.calculateSpeedPerPixel(displayMetrics) * factor
//            return factor / displayMetrics.densityDpi
        }
    }
    linearSmoothScroller.setTargetPosition(position)
    layoutManager?.startSmoothScroll(linearSmoothScroller)
}