package io.sbox.library.view.recycler

/*
recyclerview.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true))

//        var snapHelper = PagerSnapHelper()
//        snapHelper.attachToRecyclerView(recyclerview)

        val linearSnapHelper = LinearSnapHelper()
        linearSnapHelper.attachToRecyclerView(recyclerview)
 */


/*

class GravitySnapHelper @JvmOverloads constructor(
    gravity: Int,
    enableSnapLastItem: Boolean = false,
    snapListener: SnapListener? = null
) :
    LinearSnapHelper() {
    private var mVerticalHelper: OrientationHelper? = null
    private var mHorizontalHelper: OrientationHelper? = null
    private val mGravity: Int
    private var mIsRtlHorizontal = false
    private var mSnapLastItemEnabled: Boolean
    var mSnapListener: SnapListener?
    var mSnapping = false
    private val mScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                mSnapping = false
            }
            if (newState == RecyclerView.SCROLL_STATE_IDLE && mSnapping && mSnapListener != null) {
                val position = getSnappedPosition(recyclerView)
                if (position != RecyclerView.NO_POSITION) {
                    mSnapListener!!.onSnap(position)
                }
                mSnapping = false
            }
        }
    }

    @Throws(IllegalStateException::class)
    override fun attachToRecyclerView(@Nullable recyclerView: RecyclerView?) {
        if (recyclerView != null) {
            if (mGravity == Gravity.START || mGravity == Gravity.END) {
                mIsRtlHorizontal = false
            }
            if (mSnapListener != null) {
                recyclerView.addOnScrollListener(mScrollListener)
            }
        }
        super.attachToRecyclerView(recyclerView)
    }

    override fun calculateDistanceToFinalSnap(
        @NonNull layoutManager: RecyclerView.LayoutManager,
        @NonNull targetView: View
    ): IntArray {
        val out = IntArray(2)
        if (layoutManager.canScrollHorizontally()) {
            if (mGravity == Gravity.START) {
                out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager), false)
            } else { // END
                out[0] = distanceToEnd(targetView, getHorizontalHelper(layoutManager), false)
            }
        } else {
            out[0] = 0
        }
        if (layoutManager.canScrollVertically()) {
            if (mGravity == Gravity.TOP) {
                out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager), false)
            } else { // BOTTOM
                out[1] = distanceToEnd(targetView, getVerticalHelper(layoutManager), false)
            }
        } else {
            out[1] = 0
        }
        return out
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        var snapView: View? = null
        if (layoutManager is LinearLayoutManager) {
            when (mGravity) {
                Gravity.START -> snapView =
                    findStartView(layoutManager, getHorizontalHelper(layoutManager))
                Gravity.END -> snapView =
                    findEndView(layoutManager, getHorizontalHelper(layoutManager))
                Gravity.TOP -> snapView =
                    findStartView(layoutManager, getVerticalHelper(layoutManager))
                Gravity.BOTTOM -> snapView =
                    findEndView(layoutManager, getVerticalHelper(layoutManager))
            }
        }
        mSnapping = snapView != null
        return snapView
    }

    */
/**
     * Enable snapping of the last item that's snappable.
     * The default value is false, because you can't see the last item completely
     * if this is enabled.
     *
     * @param snap true if you want to enable snapping of the last snappable item
     *//*

    fun enableLastItemSnap(snap: Boolean) {
        mSnapLastItemEnabled = snap
    }

    private fun distanceToStart(
        targetView: View,
        helper: OrientationHelper?,
        fromEnd: Boolean
    ): Int {
        return if (mIsRtlHorizontal && !fromEnd) {
            distanceToEnd(targetView, helper, true)
        } else helper.getDecoratedStart(targetView) - helper.getStartAfterPadding()
    }

    private fun distanceToEnd(
        targetView: View,
        helper: OrientationHelper?,
        fromStart: Boolean
    ): Int {
        return if (mIsRtlHorizontal && !fromStart) {
            distanceToStart(targetView, helper, true)
        } else helper.getDecoratedEnd(targetView) - helper.getEndAfterPadding()
    }

    */
/**
     * Returns the first view that we should snap to.
     *
     * @param layoutManager the recyclerview's layout manager
     * @param helper        orientation helper to calculate view sizes
     * @return the first view in the LayoutManager to snap to
     *//*

    private fun findStartView(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper?
    ): View? {
        if (layoutManager is LinearLayoutManager) {
            val firstChild: Int =
                (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            if (firstChild == RecyclerView.NO_POSITION) {
                return null
            }
            val child: View = layoutManager.findViewByPosition(firstChild)
            val visibleWidth: Float

            // We should return the child if it's visible width
            // is greater than 0.5 of it's total width.
            // In a RTL configuration, we need to check the start point and in LTR the end point
            if (mIsRtlHorizontal) {
                visibleWidth =
                    ((helper.getTotalSpace() - helper.getDecoratedStart(child)) as Float
                            / helper.getDecoratedMeasurement(child))
            } else {
                visibleWidth = (helper.getDecoratedEnd(child) as Float
                        / helper.getDecoratedMeasurement(child))
            }

            // If we're at the end of the list, we shouldn't snap
            // to avoid having the last item not completely visible.
            val endOfList = ((layoutManager as LinearLayoutManager)
                .findLastCompletelyVisibleItemPosition()
                    === layoutManager.getItemCount() - 1)
            return if (visibleWidth > 0.5f && !endOfList) {
                child
            } else if (mSnapLastItemEnabled && endOfList) {
                child
            } else if (endOfList) {
                null
            } else {
                // If the child wasn't returned, we need to return
                // the next view close to the start.
                layoutManager.findViewByPosition(firstChild + 1)
            }
        }
        return null
    }

    private fun findEndView(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper?
    ): View? {
        if (layoutManager is LinearLayoutManager) {
            val lastChild: Int =
                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            if (lastChild == RecyclerView.NO_POSITION) {
                return null
            }
            val child: View = layoutManager.findViewByPosition(lastChild)
            val visibleWidth: Float
            if (mIsRtlHorizontal) {
                visibleWidth = (helper.getDecoratedEnd(child) as Float
                        / helper.getDecoratedMeasurement(child))
            } else {
                visibleWidth =
                    ((helper.getTotalSpace() - helper.getDecoratedStart(child)) as Float
                            / helper.getDecoratedMeasurement(child))
            }

            // If we're at the start of the list, we shouldn't snap
            // to avoid having the first item not completely visible.
            val startOfList = (layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition() === 0
            return if (visibleWidth > 0.5f && !startOfList) {
                child
            } else if (mSnapLastItemEnabled && startOfList) {
                child
            } else if (startOfList) {
                null
            } else {
                // If the child wasn't returned, we need to return the previous view
                layoutManager.findViewByPosition(lastChild - 1)
            }
        }
        return null
    }

    fun getSnappedPosition(recyclerView: RecyclerView): Int {
        val layoutManager: RecyclerView.LayoutManager = recyclerView.getLayoutManager()
        if (layoutManager is LinearLayoutManager) {
            if (mGravity == Gravity.START || mGravity == Gravity.TOP) {
                return (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            } else if (mGravity == Gravity.END || mGravity == Gravity.BOTTOM) {
                return (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            }
        }
        return RecyclerView.NO_POSITION
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper? {
        if (mVerticalHelper == null) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return mVerticalHelper
    }

    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper? {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return mHorizontalHelper
    }

    interface SnapListener {
        fun onSnap(position: Int)
    }

    init {
        require(!(gravity != Gravity.START && gravity != Gravity.END && gravity != Gravity.BOTTOM && gravity != Gravity.TOP)) {
            "Invalid gravity value. Use START " +
                    "| END | BOTTOM | TOP constants"
        }
        mSnapListener = snapListener
        mGravity = gravity
        mSnapLastItemEnabled = enableSnapLastItem
    }
}
*/

//recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());

/*
@Nullable
private View findCenterView(RecyclerView.LayoutManager layoutManager, OrientationHelper helper) {
    if (layoutManager instanceof LinearLayoutManager) {
        LinearLayoutManager llm = (LinearLayoutManager) layoutManager;
        if (llm.findFirstCompletelyVisibleItemPosition() == 0) { //첫번째 아이템은 그냥 무시
            return llm.getChildAt(0);
        } else if (llm.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
            //스크롤에 의해 마지막 아이템이 완전히 보이면 센터가 아닌 마지막 아이템을 강제로 타겟뷰로 지정한다.
            return llm.getChildAt(layoutManager.getItemCount() - 1);
        }
    }
    ...
}
 */



/*
public class SnapPagerScrollListener extends RecyclerView.OnScrollListener {

    // Constants
    public static final int ON_SCROLL = 0;
    public static final int ON_SETTLED = 1;

    @IntDef({ON_SCROLL, ON_SETTLED})
    public @interface Type {
    }

    public interface OnChangeListener {
        void onSnapped(int position);
    }

    // Properties
    private final PagerSnapHelper snapHelper;
    private final int type;
    private final boolean notifyOnInit;
    private final OnChangeListener listener;
    private int snapPosition;

    // Constructor
    public SnapPagerScrollListener(PagerSnapHelper snapHelper, @Type int type, boolean notifyOnInit, OnChangeListener listener) {
        this.snapHelper = snapHelper;
        this.type = type;
        this.notifyOnInit = notifyOnInit;
        this.listener = listener;
        this.snapPosition = RecyclerView.NO_POSITION;
    }

    // Methods
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if ((type == ON_SCROLL) || !hasItemPosition()) {
            notifyListenerIfNeeded(getSnapPosition(recyclerView));
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (type == ON_SETTLED && newState == RecyclerView.SCROLL_STATE_IDLE) {
            notifyListenerIfNeeded(getSnapPosition(recyclerView));
        }
    }

    private int getSnapPosition(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) {
            return RecyclerView.NO_POSITION;
        }

        View snapView = snapHelper.findSnapView(layoutManager);
        if (snapView == null) {
            return RecyclerView.NO_POSITION;
        }

        return layoutManager.getPosition(snapView);
    }

    private void notifyListenerIfNeeded(int newSnapPosition) {
        if (snapPosition != newSnapPosition) {
            if (notifyOnInit && !hasItemPosition()) {
                listener.onSnapped(newSnapPosition);
            } else if (hasItemPosition()) {
                listener.onSnapped(newSnapPosition);
            }

            snapPosition = newSnapPosition;
        }
    }

    private boolean hasItemPosition() {
        return snapPosition != RecyclerView.NO_POSITION;
    }
}



SnapPagerScrollListener listener = new SnapPagerScrollListener(
    pagerSnapHelper,
    SnapPagerScrollListener.ON_SCROLL,
    true,
    new SnapPagerScrollListener.OnChangeListener() {
        @Override
        public void onSnapped(int position) {
            //position 받아서 이벤트 처리
        }
    }
);
recyclerView.addOnScrollListener(listener);


ON_SCROLL : 스크롤이 될때 콜백을 받고 싶다면 이 타입을 사용
ON_SETTLED : RecyclerView의 State가 SCROLL_STATE_IDLE일때, 즉 페이지가 변경되고 완전히 멈출때 이벤콜백을 받고 싶다면 이 타입을 사용합니다.
 */