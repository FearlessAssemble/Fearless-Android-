package net.fearlessplus.utils

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import io.sbox.library.extensions.getColorFromRes
import net.fearlessplus.extensions.load
import net.fearlessplus.R
import net.fearlessplus.extensions.globalContext


object Bindings {


    @JvmStatic
    @BindingAdapter("url")
    fun loadImage(@NonNull imageView: ImageView, url: String?) {
        url?.apply { imageView.load(this) }
    }

    @JvmStatic
    @BindingAdapter("visible")
    fun visible(@NonNull view: View, value: Boolean) {
        view.visibility = if (value) View.VISIBLE else View.GONE
    }

    @BindingAdapter("android:drawableLeft")
    fun setDrawableLeft(textView: TextView, resourceId: Int) {
        var drawable: Drawable? = ContextCompat.getDrawable(textView.context, resourceId)
        drawable?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
            textView.setCompoundDrawables(drawable, null, null, null)
        }
    }


    @JvmStatic
    @BindingAdapter("android:htmltext")
    fun setHtmlText(textView: TextView, html: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            textView.text = Html.fromHtml(html)
        }
    }

    private fun getColor(color: String?): Int {
        color?.let {
            if (it.startsWith("#"))
                return Color.parseColor(it)
            return  Color.parseColor("#$it")
        }
        return globalContext?.getColorFromRes(R.color.primaryDark)?:0
    }

    @JvmStatic
    @BindingAdapter("backgroundColor")
    fun setBackgroundColor(view: View, color: String?) {
        view.setBackgroundColor(getColor(color))
    }

    @JvmStatic
    @BindingAdapter("cardBackgroundColor")
    fun setCardBackgroundColor(view: CardView, color: String?) {
        view.setCardBackgroundColor(getColor(color))
    }


    @JvmStatic
    @BindingAdapter("drawableTintCompat")
    fun setDrawableTintCompat(textView: TextView, color: Int) {
        val drawables =
            textView.compoundDrawables
        if (drawables.isEmpty()) {
            return
        }
        var i = 0
        val size = drawables.size
        while (i < size) {
            var drawable = drawables[i]
            if (drawable == null) {
                i++
                continue
            }
            drawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(drawable, color)
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
            drawables[i] = drawable
            i++
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(
            drawables[0],
            drawables[1],
            drawables[2],
            drawables[3]
        )
    }

}