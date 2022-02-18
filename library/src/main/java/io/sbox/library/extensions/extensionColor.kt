package io.sbox.library.extensions

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat


@ColorInt
fun Context?.getColorFromRes(@ColorRes resId: Int): Int {
    try {
        return this?.let { ContextCompat.getColor(it, resId) } ?: 0
    } catch (e: Exception) {
    }

    return 0
}

val Int.toColorFromRes: Int
    get() = getColorFromRes(this)


fun Int.toColorFromRes(context: Context?): Int {
    try {
        return context?.let { ContextCompat.getColor(it, this) } ?: 0
    } catch (e: Exception) {
    }
    return 0
}

