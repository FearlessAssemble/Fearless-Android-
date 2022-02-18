@file:Suppress("DEPRECATION")

package io.sbox.library.extensions

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import java.util.regex.Pattern
import kotlin.math.roundToInt


/**
 * SpannableStringBuilder Text Util
 */
enum class TextType {
    NONE, BOLD, UNDERLINE, ITALIC, BOLD_UNDERLINE, STRIKE_THRU
}
private fun SpannableStringBuilder.setSpan(view: TextView, start: Int, end: Int,
                                           type: TextType? = null, @ColorRes color: Int? = null, size: Int? = null, block: (() -> Unit)? = null) {
    val isUnderline = type == TextType.UNDERLINE || type == TextType.BOLD_UNDERLINE

    color?.let {
        setSpan(ForegroundColorSpan(ContextCompat.getColor(view.context, color)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    size?.let {
        setSpan(AbsoluteSizeSpan(size, true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    block?.let {
        setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                block()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = isUnderline
            }
        }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.setMovementMethod(LinkMovementMethod.getInstance())
    }

    if(type == TextType.STRIKE_THRU)
        setSpan(StrikethroughSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    if(isUnderline)
        setSpan(UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    if(type == TextType.BOLD || type == TextType.BOLD_UNDERLINE)
        setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    if(type == TextType.ITALIC)
        setSpan(StyleSpan(Typeface.ITALIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    if(type == TextType.NONE)
        setSpan(null, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

fun TextView.set(str: String,
                 type: TextType? = null, @ColorRes color: Int? = null, size: Int? = null, block: (() -> Unit)? = null) {
    text = str
    text?.let { text ->
        SpannableStringBuilder(text)?.let {
            it.setSpan(this, 0, it.length, type, color, size, block)
            setText(it, TextView.BufferType.SPANNABLE)
}

    }
}
fun TextView.add(str: String,
                 type: TextType? = null, @ColorRes color: Int? = null, size: Int? = null, block: (() -> Unit)? = null) {
    text?.let { text ->
    SpannableStringBuilder(text)?.let {
        val beforeLength = it.length
        it.append(str)
        it.setSpan(this, beforeLength, it.length, type, color, size, block)
        setText(it, TextView.BufferType.SPANNABLE)
    }
}

}
fun TextView.replace(str: String,
                     type: TextType? = null, @ColorRes color: Int? = null, size: Int? = null, block: (() -> Unit)? = null) {
    text?.let { text ->
    SpannableStringBuilder(text)?.let {
        val startIndex = text.indexOf(str)
        val endIndex = if(startIndex >= 0) startIndex + str.length else -1
        if(startIndex >= 0) {
            it.setSpan(this, startIndex, endIndex, type, color, size, block)
            setText(it, TextView.BufferType.SPANNABLE)
        }
    }
}
}
/**
 * end
 */


/*

val Any?.globalContext: Context?
    get() {
        return takeIf { this is Context }?.let { it as Context } ?: Builder.context
    }

*/

fun String.extractionRegex(regex: String): String {
    val ptn = Pattern.compile(regex)
    val matcher = ptn.matcher(this)
    val regexPath = StringBuilder()
    while (matcher.find()) {
        regexPath.append(matcher.group())
    }
    return regexPath.toString()
}

fun TextView.setTextStyle(@StyleRes resId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setTextAppearance(resId)
    } else {
        setTextAppearance(this.context, resId);
    }
}


val Int.toStringFromRes: String
    get() {
        return getStringFromRes(this)
    }

fun String?.isNotEmptyFunc(func: (String)->Unit) {
    if(!this.isNullOrEmpty()) func(this)
}

fun TextView.none() {
    paint.isFakeBoldText = false
    paint.isAntiAlias = true
}
fun TextView.bold() {
    paint.isFakeBoldText = true
    paint.isAntiAlias = true
}
fun TextView.deleteLine() {
    paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
    paint.isAntiAlias = true
}
fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}
fun TextView.setColorOfSubstring(substring: String, @ColorRes color: Int) {
    try {
        val spannable = android.text.SpannableString(text)
        val start = text.indexOf(substring)
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, color)), start, start + substring.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = spannable
    } catch (e: Exception) {
        e.printStackTrace()
        Log.println(Log.VERBOSE, "extensionsText", "exception in setColorOfSubstring, text=$text, substring=$substring")
    }
}

fun TextView.text(value: Int) {
    text = value.toString()
}



fun TextView.lineHeight(lineHeight: Float, factor: Float = 1.48f) {
    val lineSpacingExtra = lineHeight.toInt().toPx.toFloat() - factor * this.textSize

    val padding = lineSpacingExtra
        .takeIf { it != 0.0f }
        ?.div(2)
        ?.roundToInt()
        ?: 0

    setLineSpacing(lineSpacingExtra, 1f)
    updatePadding(
        top = padding,
        bottom = padding
    )
}



/*
fun TextView.syncLineHeight(figmaLineHeight: Float, factor: Float = 1.48f) {
    val lineSpacingExtra =
        figmaLineHeight.toDp(requireContext()) - factor * this.textSize

    val padding = lineSpacingExtra
        .takeIf { it != 0.0f }
        ?.div(2)
        ?.roundToInt()
        ?: 0

    setLineSpacing(lineSpacingExtra, 1f)
    updatePadding(
        top = padding,
        bottom = padding
    )
}
 */
fun String.isPhone(): Boolean {
    val p = "^1([34578])\\d{9}\$".toRegex()
    return matches(p)
}

fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}

fun Int.twoDigitTime() = if (this < 10) "0" + toString() else toString()

fun String.equalsIgnoreCase(other: String) = this.toLowerCase().contentEquals(other.toLowerCase())


private fun bytes2Hex(bts: ByteArray): String {
    var des = ""
    var tmp: String
    for (i in bts.indices) {
        tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}
