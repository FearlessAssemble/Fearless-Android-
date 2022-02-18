@file:JvmName("util")

package net.fearlessplus.extensions


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.*
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import io.sbox.library.extensions.getColorFromRes
import io.sbox.library.extensions.isAvailable
import io.sbox.library.extensions.toPx
import net.fearlessplus.core.FearlessApplication
import net.fearlessplus.ui.activity.EmptyActivity
import net.fearlessplus.ui.base.BaseFragment
import kotlin.reflect.KClass


val Int.toStringFromRes: String
    get() {
        return getStringFromRes(this)
    }

fun Any?.getContext(): Context? {
    return takeIf { this is Context }?.let { it as Context } ?: FearlessApplication.instance
}


inline fun <reified T : BaseFragment> Any?.newFragment(bundle: Bundle? = null) {
    val argument = bundle ?: Bundle()
    argument.putString(EmptyActivity.FRAGMENT, T::class.java.canonicalName)
    getContext()?.startActivity(
        Intent(getContext(), EmptyActivity::class.java)
            .apply { putExtras(argument) }
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )
}

@SuppressLint("WrongConstant")
inline fun <reified T : BaseFragment> Fragment.stackFragment(bundle: Bundle? = null) {

    var fragment = if (bundle == null) {
        T::class.java.getMethod("newInstance")?.let {
            it.invoke(null) as T
        }
    } else {
        T::class.java.getMethod("newInstance", Bundle::class.java)?.let {
            it.invoke(null, bundle) as T
        }
    }

    activity?.let {
        val transaction = it.supportFragmentManager.beginTransaction()
        fragment?.let {
            transaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
            if (it.isAdded) transaction.show(it).commit()
            else transaction.add(id, it, fragment.getSimpleName).commit()
            transaction.addToBackStack(null)
        }
    }
}


fun ImageView.load(
    imageUrl: String,
    placeholder: Int? = null,
    isGif: Boolean = false
) {
    if (!context.isAvailable()) return
//    var resId: Int = placeholder ?: android.R.color.transparent
    val options =
        RequestOptions()
//            .placeholder(resId)
//            .error(resId)
//            .dontAnimate()
//            .dontTransform()
//            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .fitCenter()

    Glide.with(this).apply {
            if (isGif) asGif()
        }
        .load(imageUrl)
        .apply(options)
//        .thumbnail(0.1f)
        .into(this)
}


fun <T : Any> T.getClass(): KClass<out T> = this::class
val <T : Any> T.getSimpleName: String get() = getClass().java.simpleName
val <T : Any> T.kClass: KClass<T> get() = javaClass.kotlin

val Any?.globalContext: Context?
    get() {
        return takeIf { this is Context }?.let { it as Context } ?: FearlessApplication.instance
    }


val Int.toPx: Int get() = toPx(globalContext)


val Int.toColorFromRes: Int
    get() = globalContext.getColorFromRes(this)


fun Any?.getStringFromRes(@StringRes stringResId: Int, vararg formatArgs: Any): String {
    try {
        return globalContext?.resources?.getString(stringResId, *formatArgs) ?: ""
    } catch (e: Exception) {
    }

    return ""
}


