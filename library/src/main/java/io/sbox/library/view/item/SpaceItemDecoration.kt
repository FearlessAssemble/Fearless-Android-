package io.sbox.library.view.item

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


class SpaceItemDecoration(
    var spacingIn: Int = 0,
    var spacingTop: Int = 0,
    var numberOfColumns: Int = 1
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.setEmpty()

        val position = parent.getChildAdapterPosition(view).takeIf {
            it != RecyclerView.NO_POSITION
        } ?: run {
            return
        }

        outRect.top = if (position < numberOfColumns) spacingTop else outRect.top
        outRect.bottom = spacingIn

        if(numberOfColumns > 1) {
            var spanIndex = 0
            if (view.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
            } else if(view.layoutParams is GridLayoutManager.LayoutParams) {
                spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
            }

            outRect.left = spanIndex * spacingIn / numberOfColumns
            outRect.right = spacingIn - (spanIndex + 1) * spacingIn / numberOfColumns

            /*
            val num = numberOfColumns - 1
            var space = (parent.width - (spacingIn * num)) % numberOfColumns
            val spacingInValue = spacingIn / 2
            if(spanIndex == num) {
                outRect.right = space
            }
             */

        }

    }
}
