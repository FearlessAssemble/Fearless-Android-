package net.fearlessplus.ui.main.bgtube

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.component_appbar.*
import kotlinx.android.synthetic.main.fragment_viewpager.*
import net.fearlessplus.R
import net.fearlessplus.extensions.toPx
import net.fearlessplus.model.VideoTagItem
import net.fearlessplus.model.VideoTagModel
import net.fearlessplus.network.Api
import net.fearlessplus.network.send
import net.fearlessplus.ui.base.BaseFragment
import io.sbox.library.extensions.showLoading

class BgTubeFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_viewpager, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar_title.setText(R.string.bgtube)

        getVideoTag()
    }

    fun getVideoTag() {
        Api.service.getVideoTagsList().send(this::setVideoTag)
    }

    fun setVideoTag(tags: VideoTagModel) {

        activity?.let { activity ->
            var adapter = BGTubePageAdapter(childFragmentManager, tags.data)
            viewpager.adapter = adapter
            appbar_tablayout.setupWithViewPager(viewpager)

            for (i in 0 until appbar_tablayout.getTabCount()) {
                val tab = (appbar_tablayout.getChildAt(0) as ViewGroup).getChildAt(i)
                val p = tab.layoutParams as MarginLayoutParams
                p.setMargins(0, 0, 14.toPx, 0)
                tab.requestLayout()
            }
            appbar_tablayout.visibility = View.VISIBLE
        }
    }

    class BGTubePageAdapter(fm: FragmentManager, val items: List<VideoTagItem>) :
        FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int {
            return items.size
        }

        override fun getItem(position: Int): Fragment {
            return BgTubeListFragment.newInstance(items.get(position).id)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return items.getOrNull(position)?.title
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
        }

        override fun getItemPosition(obj: Any): Int {
            return PagerAdapter.POSITION_NONE
        }


    }



}