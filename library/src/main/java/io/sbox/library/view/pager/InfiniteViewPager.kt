package io.sbox.library.view.pager


import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class InfiniteViewPager : ViewPager {

    private val offsetAmount: Int
        get() {
            if (adapter is InfinitePagerAdapter) {
                val infAdapter = adapter as InfinitePagerAdapter?
                return infAdapter!!.realCount * 1
            } else {
                return 0
            }
        }

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
        var item = item
        if (adapter!!.count > 0) {
            item = offsetAmount + item % adapter!!.count
            super.setCurrentItem(item, smoothScroll)
        }
    }

    fun setCurrentItemIndex(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, smoothScroll)
    }

    override fun getCurrentItem(): Int {
        return super.getCurrentItem()
        /*
        int position = super.getCurrentItem();
        if (getAdapter() instanceof InfinitePagerAdapter) {
            InfinitePagerAdapter infAdapter = (InfinitePagerAdapter) getAdapter();
            return (position % infAdapter.getRealCount());
        } else {
            return super.getCurrentItem();
        }
        */
    }
}
