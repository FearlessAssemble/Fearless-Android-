package net.fearlessplus.ui.main.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.component_appbar.*
import kotlinx.android.synthetic.main.fragment_viewpager.*
import net.fearlessplus.R
import net.fearlessplus.extensions.toPx
import net.fearlessplus.ui.base.BaseFragment

class InformationFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_viewpager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar_title.setText(R.string.information)
        appbar.setAppbar(notification = true)

        activity?.let { activity ->
            var adapter = InformationPageAdapter(childFragmentManager)
            viewpager.adapter = adapter
            appbar_tablayout_default.setupWithViewPager(viewpager)

            for (i in 0 until appbar_tablayout_default.getTabCount()) {
                val tab = (appbar_tablayout_default.getChildAt(0) as ViewGroup).getChildAt(i)
                val p = tab.layoutParams as ViewGroup.MarginLayoutParams
                p.setMargins(0, 0, 14.toPx, 0)
                tab.requestLayout()
            }
            appbar_tablayout_default.visibility = View.VISIBLE
        }
    }


    class InformationPageAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int {
            return InformationCategory.values().size
        }

        override fun getItem(position: Int): Fragment {
            return InformationListFragment.newInstance(position)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return InformationCategory.values().getOrNull(position)?.label
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
        }

        override fun getItemPosition(obj: Any): Int {
            return PagerAdapter.POSITION_NONE
        }


    }


}