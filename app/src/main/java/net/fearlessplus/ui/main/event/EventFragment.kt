package net.fearlessplus.ui.main.event

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import io.sbox.library.extensions.observeNotNull
import io.sbox.library.extensions.visible
import io.sbox.library.view.adapter.RecyclerAdapter
import net.fearlessplus.ui.base.BaseFragment
import kotlinx.android.synthetic.main.component_appbar.*
import kotlinx.android.synthetic.main.fragment_recyclerview_refresh.*
import kotlinx.android.synthetic.main.layout_recyclerview.*
import net.fearlessplus.BR
import net.fearlessplus.R
import net.fearlessplus.databinding.CellEventListBinding
import net.fearlessplus.model.EventModel
import net.fearlessplus.model.item.EventItem

class EventFragment : BaseFragment() {

    private lateinit var eventViewModel: EventViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)
        return inflater.inflate(R.layout.fragment_recyclerview_refresh, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar_title.setText(R.string.event)

        try {
            swipe_refresh_recyclerview.javaClass.getDeclaredField("mCircleView")?.let { f ->
                f.setAccessible(true)
                val img: ImageView = f.get(swipe_refresh_recyclerview) as ImageView
                img.setImageResource(R.drawable.ico_bg_logo)
            }
        } catch (e: Exception) {

        }

        swipe_refresh_recyclerview.setOnRefreshListener {
            eventViewModel.getContent()
        }

        eventViewModel.content.observeNotNull(this, {
            setContent(it)
        })

        eventViewModel.getContent()
    }


    fun setContent(items: EventModel) {
        swipe_refresh_recyclerview.isRefreshing = false

        recyclerview?.let {

            if(items.data.list.size == 0) {

                recyclerview?.adapter?.let { adapter ->
                    if(adapter is RecyclerAdapter<*, *>) {
                        adapter.items?.clear()
                    }
                }
            } else {
                emptyview?.visible = false

                context?.let {
                    var adapter = RecyclerAdapter<CellEventListBinding, EventItem>(
                        it,
                        R.layout.cell_event_list,
                        BR.data
                    )
                    recyclerview.setAdapter(adapter)
                    adapter.addItem(items.data.list)
                }
            }


        }

    }
}