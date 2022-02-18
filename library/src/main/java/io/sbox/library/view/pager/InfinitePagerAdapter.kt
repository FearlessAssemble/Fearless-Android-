package io.sbox.library.view.pager

import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class InfinitePagerAdapter(private var adapter: PagerAdapter?) : PagerAdapter() {

    val realCount: Int
        get() = adapter!!.count

    override fun getCount(): Int {
        return adapter!!.count * 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var virtualPosition = 1
        if (realCount > 0)
            virtualPosition = position % realCount
        return adapter!!.instantiateItem(container, virtualPosition)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        var virtualPosition = 1
        if (realCount > 0)
            virtualPosition = position % realCount
        adapter!!.destroyItem(container, virtualPosition, `object`)
    }

    override fun finishUpdate(container: ViewGroup) {
        adapter!!.finishUpdate(container)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return adapter!!.isViewFromObject(view, `object`)
    }

    override fun restoreState(bundle: Parcelable?, classLoader: ClassLoader?) {
        adapter!!.restoreState(bundle, classLoader)
    }

    override fun saveState(): Parcelable? {
        return adapter!!.saveState()
    }

    override fun startUpdate(container: ViewGroup) {
        adapter!!.startUpdate(container)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var virtualPosition = 1
        if (realCount > 0)
            virtualPosition = position % realCount
        return adapter!!.getPageTitle(virtualPosition)
    }

    fun destory() {
        adapter = null
    }

}
