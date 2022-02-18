package io.sbox.library.extensions

import android.app.AlertDialog
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.*
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager


val View.screenX get() = IntArray(2).apply { getLocationOnScreen(this) }[0]
val View.screenY get() = IntArray(2).apply { getLocationOnScreen(this) }[1]

fun ViewGroup.getChildFromTag(tag: String): View? {
    for (i in 0 until childCount) {
        try {
            if (getChildAt(i).tag.equals(tag)) {
                return getChildAt(i)
            }
        } catch (e: Exception) {
        }
    }
    return null
}

fun ViewGroup.getViewsByTag(tag: String): ArrayList<View> {
    val views = ArrayList<View>()
    val childCount = childCount
    for (i in 0..childCount - 1) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            views.addAll(child.getViewsByTag(tag))
        }

        val tagObj = child.tag
        if (tagObj != null && tagObj == tag) {
            views.add(child)
        }

    }
    return views
}

fun Context.getLayoutFromResource(@LayoutRes resId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View {
    val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    if(parent.isNotNull) {
        return layoutInflater.inflate(resId, parent, attachToRoot)
    }
    return layoutInflater.inflate(resId, null)
//    return LayoutInflater.from(this).inflate(resId, parent, attachToRoot)
}


fun ViewPager.pageChangeListener(onPageStateChanged: ((Int) -> Unit)? = null, onPageSelected: ((Int) -> Unit)? = null) {
    addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
        var isStartSelected = true
        override fun onPageScrolled(position: Int, po: Float, pop: Int) {
            super.onPageScrolled(position, po, pop)
            if (isStartSelected && position == 0) {
                isStartSelected = false
                onPageSelected?.invoke(position)
            }
        }
        override fun onPageSelected(position: Int) {
            onPageSelected?.invoke(position)
        }
        override fun onPageScrollStateChanged(state: Int) {
            onPageStateChanged?.invoke(state)
        }
    })
}



fun ViewGroup.removeViewsByTag(tag: String) {
    for (i in 0..childCount - 1) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            child.removeViewsByTag(tag)
        }

        if (child.tag == tag) {
            removeView(child)
        }
    }
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun View.doOnLayout(onLayout: (View) -> Boolean) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View, left: Int, top: Int, right: Int, bottom: Int,
            oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
        ) {
            if (onLayout(view)) {
                view.removeOnLayoutChangeListener(this)
            }
        }
    })
}



fun View.setPaddingLeft(value: Int) = setPadding(value, paddingTop, paddingRight, paddingBottom)
fun View.setPaddingRight(value: Int) = setPadding(paddingLeft, paddingTop, value, paddingBottom)


fun View.resizeWidth(value: Int): View {
    layoutParams?.let {
        layoutParams.width = value
    }
    if (layoutParams == null) {
        afterMeasured { resizeWidth(value) }
    }
    return this
}

fun View.resizeHeight(value: Int): View {
    layoutParams?.let {
        layoutParams.height = value
    }
    if (layoutParams == null) {
        afterMeasured { resizeHeight(value) }
    }
    return this
}

fun View.resize(width: Int, height: Int): View {
    layoutParams?.let {
        layoutParams.width = width
        layoutParams.height = height
    }
    if (layoutParams == null) {
        afterMeasured { resize(width, height) }
    }
    return this
}


var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun View.setColor(@ColorRes id: Int) {
    var color = ContextCompat.getColor(context, id)
    var filter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)

    when (this) {
        is ImageView -> drawable?.setColorFilter(filter)
        is TextView -> {
            setTextColor(color)
            for (draw in this.compoundDrawables) {
                draw?.apply {
                    setColorFilter(filter)
                }
            }
        }
        else -> {
        }
    }
    invalidate()
}


fun View.tween(x: Number? = null, y: Number? = null,
               alpha: Float? = null,
               scaleX: Number? = null, scaleY: Number? = null,
               pivotX: Float? = null, pivotY: Float? = null,
               duration: Long = 100): ViewPropertyAnimator {
    var interpolator = AnimationUtils.loadInterpolator(
        this.context,
        android.R.anim.accelerate_interpolator
    )
    return animate().apply {
        x?.let {translationX(it.toFloat())}
        y?.let {translationY(it.toFloat())}
        alpha?.let {alpha(it)}
        scaleX?.let {scaleX(it.toFloat())}
        scaleY?.let {scaleY(it.toFloat())}
        pivotX?.let {setPivotX(it)}
        pivotY?.let {setPivotY(it)}
        setDuration(duration)
        setInterpolator(interpolator)

    }
}


//https://pluu.github.io/blog/rxjava/2017/02/04/android-alertdialog/
//http://dailyddubby.blogspot.com/2018/04/106-custom-alert-dialog.html
fun Context.openDialog(
    title: String = "",
    message: String = "",
    positive: Pair<String, ()->Unit>? = null,
    negative: Pair<String, ()->Unit>? = null,
    neutral: Pair<String, ()->Unit>? = null,
    themeResId: Int = 0
) {
    AlertDialog.Builder(this, themeResId).apply {
        title.isNotEmptyFunc { setTitle(it) }
        message.isNotEmptyFunc { setMessage(it) }
        positive?.apply {
            first.isNotEmptyFunc {
                setPositiveButton(it) { dialog, which ->
                    second?.let { it() }
                }
            }
        }
        negative?.apply {
            first.isNotEmptyFunc {
                setNegativeButton(it) { dialog, which ->
                    second?.let { it() }
                }
            }
        }
        neutral?.apply {
            first.isNotEmptyFunc {
                setNeutralButton(it) { _, _ ->
                    second?.let { it() }
                }
            }
        }

        create().show()
    }
}


fun EditText.setEditTextFocus() {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
}






/*

package io.skizo.library.extensions

import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AnimationUtils
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes


fun View.setBackgroundDrawable(
    @ColorRes color: Int = android.R.color.transparent,
    @ColorRes strokeColor: Int = android.R.color.transparent,
    strokeSize: Int = 0,
    cornerRadius: Float = 0f,
    @ColorInt colors: IntArray? = null,
    cornerRadii: FloatArray? = null
) {
    val gd = GradientDrawable()
    gd.mutate()
    colors.notNullAction({
        gd.setColor(context.getColorFromRes(color))
    }) {
        gd.setColors(it.map {
            context.getColorFromRes(it)
        }.toIntArray())
    }

    var corner: FloatArray = cornerRadii?.let {
        when (it.size) {
            8 -> it
            4 -> floatArrayOf(
                it.getOrNull(0) ?: 0f,
                it.getOrNull(0) ?: 0f,
                it.getOrNull(1) ?: 0f,
                it.getOrNull(1) ?: 0f,
                it.getOrNull(2) ?: 0f,
                it.getOrNull(2) ?: 0f,
                it.getOrNull(3) ?: 0f,
                it.getOrNull(3) ?: 0f
            )
            else -> null
        }
    } ?: floatArrayOf(
        cornerRadius,
        cornerRadius,
        cornerRadius,
        cornerRadius,
        cornerRadius,
        cornerRadius,
        cornerRadius,
        cornerRadius
    )

    gd.cornerRadii = corner.map {
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, it, resources.displayMetrics)
    }.toFloatArray()

    gd.setStroke(
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            strokeSize.toFloat(),
            resources.displayMetrics
        ).toInt(), context.getColorFromRes(strokeColor)
    )


//    setBackgroundDrawable(gd)
    background = gd
}


fun View.tween(
    x: Number? = null, y: Number? = null,
    alpha: Float? = null,
    scaleX: Number? = null, scaleY: Number? = null,
    pivotX: Float? = null, pivotY: Float? = null,
    rotation: Float? = null,
    duration: Long = 100
): ViewPropertyAnimator {
    var interpolator = AnimationUtils.loadInterpolator(
        this.context,
        android.R.anim.accelerate_interpolator
    )
    return animate().apply {
        x?.let { translationX(it.toFloat()) }
        y?.let { translationY(it.toFloat()) }
        alpha?.let { alpha(it) }
        scaleX?.let { scaleX(it.toFloat()) }
        scaleY?.let { scaleY(it.toFloat()) }
        pivotX?.let { setPivotX(it) }
        pivotY?.let { setPivotY(it) }
        rotation?.let { rotation(it) }
        setDuration(duration)
        setInterpolator(interpolator)

    }
}


 */