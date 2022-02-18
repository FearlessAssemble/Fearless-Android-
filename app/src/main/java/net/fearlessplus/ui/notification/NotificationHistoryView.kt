package net.fearlessplus.ui.notification

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import io.sbox.library.extensions.getLayoutFromResource
import io.sbox.library.extensions.visible
import kotlinx.android.synthetic.main.view_push_history_cell.view.*
import kotlinx.android.synthetic.main.view_push_history_header.view.*
import net.fearlessplus.R
import net.fearlessplus.databinding.ViewPushHistoryCellBinding
import net.fearlessplus.model.PushHistoryModel
import net.fearlessplus.network.Api
import net.fearlessplus.network.send


class NotificationHistoryView : ConstraintLayout {


    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs)
    }

    val activity: Activity?

    init {
        activity = if (context is Activity) context as Activity else null
    }

    private var scrollLayout: LinearLayout? = null
    private fun initialize(context: Context, attrs: AttributeSet?) {
        scrollLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        var scrollView = NestedScrollView(context).apply {
            setBackgroundResource(R.color.base_d1)
            addView(scrollLayout)
        }
        addView(scrollView)

        Api.service.getPushHistoryList().send(this::setContent)
//        val v = context.getLayoutFromResource(R.layout.view_notification_history)
//        scrollLayout?.addView(v)
    }

    private fun setContent(model: PushHistoryModel) {

        scrollLayout?.apply {
            model.data.list.forEach {

                if(it.visibleHeader) {
                    val header = context.getLayoutFromResource(R.layout.view_push_history_header)
                    header.push_history_header.text = it.date
                    header.push_history_line.visible = it.visibleLine
                    addView(header)
                }

                val binding = DataBindingUtil.bind<ViewPushHistoryCellBinding>(context.getLayoutFromResource(R.layout.view_push_history_cell))
                binding?.data = it
                when {
                    it.segment.equals("Instagram") -> {
                        binding?.root?.push_history_segment_icon?.setBackgroundResource(R.drawable.ico_instagram)
                    }
                    it.segment.equals("Twitter") -> {
                        binding?.root?.push_history_segment_icon?.setBackgroundResource(R.drawable.ico_twitter)
                    }
                    else -> {
                        binding?.root?.push_history_segment_icon?.setBackgroundResource(R.mipmap.ic_notification)
                    }
                }

                addView(binding?.root)
            }
        }
    }

}

