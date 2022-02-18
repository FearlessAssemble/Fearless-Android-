package net.fearlessplus.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import io.sbox.library.extensions.forEach

class ScalableTabLayout : TabLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var tempWidth = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val tabLayout = getChildAt(0) as ViewGroup
        val childCount = tabLayout.childCount
        if (childCount > 0 && measuredWidth > 0) {
            val widthPixels = MeasureSpec.getSize(widthMeasureSpec)

            if (measuredWidth <= widthPixels
                && tabMode == MODE_AUTO) {
//                tabMode = MODE_FIXED

                val tabMinWidth = widthPixels / childCount
                var remainderPixels = widthPixels % childCount
                tabLayout.childCount.forEach {
                    var v = tabLayout.getChildAt(it)
                    if (remainderPixels > 0) {
                        v.minimumWidth = tabMinWidth + 1
                        remainderPixels--
                    } else {
                        v.minimumWidth = tabMinWidth
                    }
                }


            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


}