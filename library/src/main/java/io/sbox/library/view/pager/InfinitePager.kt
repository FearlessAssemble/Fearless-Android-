package com.smackjeeves.ui.component.pager


import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class InfinitePager : ViewPager {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        currentItem = 0
    }

    override fun setCurrentItem(item: Int) {
        setCurrentItem(item, false)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        adapter?.let {
            if (it.count > 0 && it is InfinitePagerAdapter) {
                super.setCurrentItem((it.realCount + item) % it.count, smoothScroll)
            }
        }
    }

    fun setCurrentItemIndex(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, smoothScroll)
    }

    override fun getCurrentItem(): Int {
        adapter?.let {
            if (it is InfinitePagerAdapter) {
                return (super.getCurrentItem() % it.realCount)
            }
        }
        return super.getCurrentItem()
    }
}


class InfinitePagerAdapter(private var adapter: PagerAdapter?) : PagerAdapter() {

    val realCount: Int
        get() = adapter?.count ?: 0

    override fun getCount(): Int {
        return adapter?.count ?: 0 * 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        adapter?.let {
            return it.instantiateItem(container, position * realCount)
        }
        return super.instantiateItem(container, position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        adapter?.let {
            it.destroyItem(container, position * realCount, obj)
        }
    }

    override fun finishUpdate(container: ViewGroup) {
        adapter?.finishUpdate(container)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return adapter?.isViewFromObject(view, obj) ?: false
    }

    override fun restoreState(bundle: Parcelable?, classLoader: ClassLoader?) {
        adapter?.restoreState(bundle, classLoader)
    }

    override fun saveState(): Parcelable? {
        return adapter?.saveState()
    }

    override fun startUpdate(container: ViewGroup) {
        adapter?.startUpdate(container)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        adapter?.let {
            return it.getPageTitle(position * realCount)
        }
        return null
    }

    fun destory() {
        adapter = null
    }

}
