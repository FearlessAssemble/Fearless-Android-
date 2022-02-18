package net.fearlessplus.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.sbox.library.extensions.visible
import kotlinx.android.synthetic.main.component_empty.view.*
import net.fearlessplus.R
import net.fearlessplus.extensions.toStringFromRes

class EmptyView(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.component_empty, this)
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
    }


    fun set(@DrawableRes icon: Int, @StringRes caption: Int, @StringRes description: Int) {
        empty_icon.setImageResource(icon)
        empty_caption.text = caption.toStringFromRes
        visible = true
    }



    fun setMaintenance(@DrawableRes icon: Int, caption: String, description: String, endAt: String?) {
        empty_icon.setImageResource(icon)
        empty_caption.text = caption
        visible = true
    }

}