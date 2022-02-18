package net.fearlessplus.ui.main.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import io.sbox.library.extensions.dispatcherEvent
import io.sbox.library.extensions.safeClick
import io.sbox.library.extensions.trace
import io.sbox.library.extensions.visible
import kotlinx.android.synthetic.main.component_appbar.*
import kotlinx.android.synthetic.main.component_appbar.view.*
import kotlinx.android.synthetic.main.fragment_viewpager.*
import net.fearlessplus.R
import net.fearlessplus.extensions.toPx
import net.fearlessplus.extensions.toStringFromRes
import net.fearlessplus.model.GalleryFolderItem
import net.fearlessplus.model.GalleryFolderModel
import net.fearlessplus.network.Api
import net.fearlessplus.network.send
import net.fearlessplus.ui.base.BaseFragment

@Suppress("DEPRECATION")
class GalleryFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_viewpager, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar_title.setText(R.string.gallery.toStringFromRes + " (beta)")
        appbar.setAppbar(notification = true)

        appbar.appbar_gallery_type.safeClick {
            GalleryListFragment.defaultType = !GalleryListFragment.defaultType
            activity?.dispatcherEvent("GALLERY_TYPE")
        }

//        context?.showLoading()
        getGalleryFolder()
    }

    fun getGalleryFolder() {
        Api.service.getGalleryFolderList().send(this::setGalleryFolder)
    }

    fun setGalleryFolder(tags: GalleryFolderModel) {

        activity?.let { activity ->
            var adapter = GalleryPageAdapter(childFragmentManager, tags.data.menuList)
            viewpager.adapter = adapter
            appbar_tablayout.setupWithViewPager(viewpager)

            for (i in 0 until appbar_tablayout.getTabCount()) {
                val tab = (appbar_tablayout.getChildAt(0) as ViewGroup).getChildAt(i)
                val p = tab.layoutParams as MarginLayoutParams
                p.setMargins(0, 0, 14.toPx, 0)
                tab.requestLayout()
            }
            appbar_tablayout.visible = tags.data.menuList.size > 1
        }
    }

    class GalleryPageAdapter(fm: FragmentManager, val items: List<GalleryFolderItem>) :
        FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int {
            return items.size
        }

        override fun getItem(position: Int): Fragment {
            return GalleryListFragment.newInstance(items.get(position).id)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return items.getOrNull(position)?.name
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
        }

        override fun getItemPosition(obj: Any): Int {
            return PagerAdapter.POSITION_NONE
        }
    }

}