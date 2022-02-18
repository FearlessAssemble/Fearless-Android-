@file:Suppress("DEPRECATION")

package io.sbox.library.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.view.*
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import io.sbox.library.R
import kotlinx.android.synthetic.main.item_bottom_sheet.view.*
import kotlinx.android.synthetic.main.item_snackbar.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private var tempToast: Toast? = null
fun Context.showToast(
    message: Any,
    duration: Int = Toast.LENGTH_LONG,
    gravity: Int = Gravity.CENTER
) {
    when (message) {
        is @StringRes Int -> getString(message)
        is String -> message
        else -> return
    }?.let {

        val context = this
        val layout = RelativeLayout(this)
        layout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val card = CardView(this).apply {
            radius = 8.toPx(context).toFloat()
//                alpha = 0.8f
            setCardBackgroundColor(android.R.color.black.toColorFromRes(context))
            val param = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            param.setMargins(100)
            layout.addView(this, param)
        }
        TextView(context).apply {
            text = it
            this.gravity = Gravity.CENTER
            setTextColor(R.color.white.toColorFromRes(context))
            val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(14.toPx(context))
            card.addView(this, param)
        }

        mainThread {
            tempToast?.apply {
                cancel()
                tempToast = null
            }
            tempToast = Toast(context).apply {
                this.duration = duration
                setGravity(gravity, 0, -100.toPx(context))
                view = layout
                show()
            }
        }
    }
}


private var snackbar: Snackbar? = null
fun Activity.showSnackBar(
    message: String,
    positive: Pair<String, (() -> Unit)?>? = null,
    nagative: Pair<String, (() -> Unit)?>? = null
) = window.decorView.findViewById<View>(android.R.id.content)
    .showSnackBar(message, positive, nagative)

@SuppressLint("ResourceAsColor")
fun View.showSnackBar(
    message: String,
    positive: Pair<String, (() -> Unit)?>? = null,
    nagative: Pair<String, (() -> Unit)?>? = null,
    duration: Int = 5000
) {
    snackbar?.let {
        it.dismiss()
    }

    snackbar = Snackbar.make(this, R.string.app_name, duration)

    snackbar?.view?.apply {
        findViewById<TextView>(com.google.android.material.R.id.snackbar_text).visibility =
            View.INVISIBLE
        setPadding(0, 0, 0, 0)

        var customView =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.item_snackbar,
                null
            )
        val objLayoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        objLayoutParams.gravity = Gravity.BOTTOM

        customView.snackbar_text.text = message

        customView.snackbar_positive.visibility = View.GONE
        customView.snackbar_nagative.visibility = View.GONE

        positive?.let { btn ->
            customView.snackbar_positive.visibility = View.VISIBLE
            customView.snackbar_positive.text = btn.first
            customView.snackbar_positive.safeClick {
                btn.second?.invoke()
                snackbar?.dismiss()
            }
        }

        nagative?.let { btn ->
            customView.snackbar_nagative.visibility = View.VISIBLE
            customView.snackbar_nagative.text = btn.first
            customView.snackbar_nagative.safeClick {
                btn.second?.invoke()
                snackbar?.dismiss()
            }
        }

        (this as Snackbar.SnackbarLayout)?.apply {
            removeAllViews()
            addView(customView, objLayoutParams)
        }

        val animation = AnimationUtils.loadAnimation(this.context, R.anim.snackbar_in)
        animation.interpolator = LinearInterpolator()
        this.animation = animation

        snackbar?.show()
    }
}


var loadingDialog: Dialog? = null
var countDownTimer: CountDownTimer? = null
fun Context.showLoading(timer: Long = 3000, enableAutoHide: Boolean = true) {
    hideLoading()

    if(enableAutoHide) {
        countDownTimer = object : CountDownTimer(timer, timer) {
            override fun onFinish() { hideLoading() }
            override fun onTick(p0: Long) {}
        }.start()
    }

    loadingDialog = Dialog(this)
    loadingDialog?.setCanceledOnTouchOutside(false)
    loadingDialog?.setCancelable(false)
    loadingDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    loadingDialog?.setContentView(ProgressBar(this))

    if (this is Activity) {
        try {
            loadingDialog?.setOwnerActivity(this)
        } catch (e: Exception) {
        }
    }
    delayed {
        loadingDialog?.show()
    }
}
fun Any?.hideLoading() {
    countDownTimer?.cancel()
    countDownTimer = null
    loadingDialog?.let {
        if(it.isShowing) {
            it.ownerActivity?.apply {
                GlobalScope.launch(Dispatchers.Main) {
                    loadingDialog?.hide()
                    loadingDialog?.dismiss()
                    loadingDialog = null
                }
            }
        }
    }
}


var bottomDialog: BottomSheetDialog? = null
fun Context.showBottomSheetDialog(view: View) {
    bottomDialog?.hide()
    bottomDialog?.dismiss()
    bottomDialog = null
//    val layout = ConstraintLayout(this)
//    val param: CoordinatorLayout.LayoutParams = view.layoutParams as CoordinatorLayout.LayoutParams
//    param.behavior = AppBarLayout.ScrollingViewBehavior()
//    layout.layoutParams = param
    val layout = getLayoutFromResource(R.layout.item_bottom_sheet)
    layout.item_bottom_sheet_root.addView(view)
    bottomDialog = BottomSheetDialog(this).apply {
        setContentView(layout)
        show()
    }
}



fun Context.showDialog(
    title: String = "",
    message: String = "",
    positive: Pair<String, (() -> Unit)?>? = null,
    negative: Pair<String, (() -> Unit)?>? = null,
    neutral: Pair<String, (() -> Unit)?>? = null,
    icon: Drawable? = null,
    themeResId: Int = R.style.SDialogTheme,
    enableCancel: Boolean = true,
    callbackListener: (() -> Unit)? = null
) {
    AlertDialog.Builder(this, themeResId)
        .setTitle(title)
        .setMessage(message)
        .apply {
            positive?.let {
                this.setPositiveButton(it.first, { dialog, which ->
                    it.second?.let { it.invoke() }
                })
            }
            negative?.let {
                this.setNegativeButton(it.first, { dialog, which ->
                    it.second?.let { it.invoke() }
                })
            }
            neutral?.let {
                this.setNeutralButton(it.first, { dialog, which ->
                    it.second?.let { it.invoke() }
                })
            }
            icon?.let { setIcon(it) }
        }
        .setCancelable(enableCancel)
        .setOnDismissListener {
            callbackListener?.let { it.invoke() }
        }.create().show()
}

/*
showDialog("title", arrayOf("a", "b", "c") to { value -> })
 */
fun Context.showDialog(
    title: String = "",
    items: Pair<Array<String>, (String) -> Unit>,
    icon: Drawable? = null,
    themeResId: Int = R.style.SDialogTheme,
    enableCancel: Boolean = true,
    callbackListener: (() -> Unit)? = null
) {

    AlertDialog.Builder(this, themeResId)
        .setTitle(title)
        .setItems(items.first, { dialog, which ->
            items.second.invoke(items.first.get(which))
        })
        .apply {
            icon?.let { setIcon(it) }
        }
        .setCancelable(enableCancel)
        .setOnDismissListener {
            callbackListener?.let { it.invoke() }
        }.show()

}

/*
showDialog("title", arrayOf("a", "b", "c") to { value -> }, "select" to { value -> })
 */
fun Context.showDialog(
    title: String = "",
    items: Pair<Array<String>, ((String) -> Unit)?>,
    positive: Pair<String, (String) -> Unit>,
    icon: Drawable? = null,
    themeResId: Int = R.style.SDialogTheme,
    enableCancel: Boolean = true,
    callbackListener: (() -> Unit)? = null
) {
    var nSelectItem = -1
    AlertDialog.Builder(this, themeResId)
        .setTitle(title)
        .setSingleChoiceItems(items.first, -1, { dialog, which ->
            nSelectItem = which
            items.second?.invoke(items.first.get(which))
        })
        .setPositiveButton(positive.first, { dialog, which ->
            positive.second?.let { it.invoke(items.first.getOrNull(nSelectItem) ?: "") }
        })
        .apply {
            icon?.let { setIcon(it) }
        }
        .setCancelable(enableCancel)
        .setOnDismissListener {
            callbackListener?.let { it.invoke() }
        }.show()
}


fun Context.showDialog(
    title: String = "",
    items: Array<String>,
    positive: Pair<String, (ArrayList<String>) -> Unit>,
    icon: Drawable? = null,
    themeResId: Int = R.style.SDialogTheme,
    enableCancel: Boolean = true,
    callbackListener: (() -> Unit)? = null
) {
    val selectedItems = ArrayList<String>()
    AlertDialog.Builder(this, themeResId)
        .setTitle(title)
        .setMultiChoiceItems(items, null, { dialog, which, isChecked ->
            if (isChecked)
                selectedItems.add(items[which])
            else
                selectedItems.remove(items[which])
        })
        .setPositiveButton(positive.first, { dialog, which ->
            positive.second?.let { it.invoke(selectedItems) }
        })
        .apply {
            icon?.let { setIcon(it) }
        }
        .setCancelable(enableCancel)
        .setOnDismissListener {
            callbackListener?.let { it.invoke() }
        }.show()
}


/*
AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                                 android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

String strHtml =
      "<b><font color='#ff0000'>HTML 컨텐츠 팝업</font></b> 입니다.<br/>HTML이 제대로 표현되나요?";
Spanned oHtml;

if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
    // noinspection deprecation
    oHtml = Html.fromHtml(strHtml);
}
else
{
    oHtml = Html.fromHtml(strHtml, Html.FROM_HTML_MODE_LEGACY);
}

oDialog.setTitle("색상을 선택하세요")
       .setMessage(oHtml)
       .setPositiveButton("ok", null)
       .setCancelable(false)
       .show();




ProgressDialog oDialog = new ProgressDialog(this,
                                        android.R.style.Theme_DeviceDefault_Light_Dialog);
oDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
oDialog.setMessage("잠시만 기다려 주세요.");

oDialog.show();




final ProgressDialog oDialog = new ProgressDialog(this,
                                        android.R.style.Theme_DeviceDefault_Light_Dialog);
oDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
oDialog.setMessage("로딩중..");
oDialog.setMax(200);

oDialog.show();

// Progress 증가 시키기.
oDialog.setProgress(111);

 */