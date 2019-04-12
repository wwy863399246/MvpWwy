package com.app.common.commonwidget.swipemenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.app.common.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/1 0001.
 */

public class SwipeMenuLayout extends ViewGroup
{
    private final ArrayList<View> mMatchParentChildren;
    private int mLeftViewResID;
    private int mRightViewResID;
    private int mContentViewResID;
    private View mLeftView;
    private View mRightView;
    private View mContentView;
    private MarginLayoutParams mContentViewLp;
    private boolean isSwipeing;
    private PointF mLastP;
    private PointF mFirstP;
    private float mFraction;
    private boolean mCanLeftSwipe;
    private boolean mCanRightSwipe;
    private int mScaledTouchSlop;
    private Scroller mScroller;
    private static SwipeMenuLayout mViewCache;
    private static State mStateCache;
    private float distanceX;
    private float finalyDistanceX;
    State result;
    private boolean isCanScroll = true;

    public SwipeMenuLayout(Context context)
    {
        this(context, (AttributeSet) null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.mMatchParentChildren = new ArrayList(1);
        this.mFraction = 0.3F;
        this.mCanLeftSwipe = true;
        this.mCanRightSwipe = true;
        this.init(context, attrs, defStyleAttr);
    }

    /**
     * 设置其是否能滑动@param isCanScroll false 不能换页， true 可以滑动换页
     */
    public void setScanScroll(boolean isCanScroll)
    {
        this.isCanScroll = isCanScroll;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr)
    {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mScaledTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mScroller = new Scroller(context);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout, defStyleAttr, 0);

        try
        {
            int indexCount = typedArray.getIndexCount();

            for (int i = 0; i < indexCount; ++i)
            {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.SwipeMenuLayout_leftMenuView)
                {
                    this.mLeftViewResID = typedArray.getResourceId(R.styleable.SwipeMenuLayout_leftMenuView, -1);
                }
                else if (attr == R.styleable.SwipeMenuLayout_rightMenuView)
                {
                    this.mRightViewResID = typedArray.getResourceId(R.styleable.SwipeMenuLayout_rightMenuView, -1);
                }
                else if (attr == R.styleable.SwipeMenuLayout_contentView)
                {
                    this.mContentViewResID = typedArray.getResourceId(R.styleable.SwipeMenuLayout_contentView, -1);
                }
                else if (attr == R.styleable.SwipeMenuLayout_canLeftSwipe)
                {
                    this.mCanLeftSwipe = typedArray.getBoolean(R.styleable.SwipeMenuLayout_canLeftSwipe, true);
                }
                else if (attr == R.styleable.SwipeMenuLayout_canRightSwipe)
                {
                    this.mCanRightSwipe = typedArray.getBoolean(R.styleable.SwipeMenuLayout_canRightSwipe, true);
                }
                else if (attr == R.styleable.SwipeMenuLayout_fraction)
                {
                    this.mFraction = typedArray.getFloat(R.styleable.SwipeMenuLayout_fraction, 0.5F);
                }
            }
        }
        catch (Exception var12)
        {
            var12.printStackTrace();
        }
        finally
        {
            typedArray.recycle();
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setClickable(true);
        int count = this.getChildCount();
        boolean measureMatchParentChildren = MeasureSpec.getMode(widthMeasureSpec) != 1073741824 || MeasureSpec.getMode(heightMeasureSpec) != 1073741824;
        this.mMatchParentChildren.clear();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        int i;
        View child;
        MarginLayoutParams lp;
        for (i = 0; i < count; ++i)
        {
            child = this.getChildAt(i);
            if (child.getVisibility() != 8)
            {
                this.measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                lp = (MarginLayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren && (lp.width == -1 || lp.height == -1))
                {
                    this.mMatchParentChildren.add(child);
                }
            }
        }

        maxHeight = Math.max(maxHeight, this.getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, this.getSuggestedMinimumWidth());
        this.setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec, childState 
                << 16));
        count = this.mMatchParentChildren.size();
        if (count > 1)
        {
            for (i = 0; i < count; ++i)
            {
                child = (View) this.mMatchParentChildren.get(i);
                lp = (MarginLayoutParams) child.getLayoutParams();
                int childWidthMeasureSpec;
                int childHeightMeasureSpec;
                if (lp.width == -1)
                {
                    childHeightMeasureSpec = Math.max(0, this.getMeasuredWidth() - lp.leftMargin - lp.rightMargin);
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightMeasureSpec, 1073741824);
                }
                else
                {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, lp.leftMargin + lp.rightMargin, lp.width);
                }

                if (lp.height == -1)
                {
                    int height = Math.max(0, this.getMeasuredHeight() - lp.topMargin - lp.bottomMargin);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, 1073741824);
                }
                else
                {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.height);
                }

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }

    }

    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(this.getContext(), attrs);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int count = this.getChildCount();
        int left = 0 + this.getPaddingLeft();
        int right = 0 + this.getPaddingLeft();
        int top = 0 + this.getPaddingTop();
        int bottom = 0 + this.getPaddingTop();

        int cRight;
        for (cRight = 0; cRight < count; ++cRight)
        {
            View child = this.getChildAt(cRight);
            if (this.mLeftView == null && child.getId() == this.mLeftViewResID)
            {
                this.mLeftView = child;
                this.mLeftView.setClickable(true);
            }
            else if (this.mRightView == null && child.getId() == this.mRightViewResID)
            {
                this.mRightView = child;
                this.mRightView.setClickable(true);
            }
            else if (this.mContentView == null && child.getId() == this.mContentViewResID)
            {
                this.mContentView = child;
                this.mContentView.setClickable(true);
            }
        }

        int lTop;
        int lLeft;
        if (this.mContentView != null)
        {
            this.mContentViewLp = (MarginLayoutParams) this.mContentView.getLayoutParams();
            int cTop = top + this.mContentViewLp.topMargin;
            lTop = left + this.mContentViewLp.leftMargin;
            cRight = left + this.mContentViewLp.leftMargin + this.mContentView.getMeasuredWidth();
            lLeft = cTop + this.mContentView.getMeasuredHeight();
            this.mContentView.layout(lTop, cTop, cRight, lLeft);
        }

        int lRight;
        int lBottom;
        MarginLayoutParams rightViewLp;
        if (this.mLeftView != null)
        {
            rightViewLp = (MarginLayoutParams) this.mLeftView.getLayoutParams();
            lTop = top + rightViewLp.topMargin;
            lLeft = 0 - this.mLeftView.getMeasuredWidth() + rightViewLp.leftMargin + rightViewLp.rightMargin;
            lRight = 0 - rightViewLp.rightMargin;
            lBottom = lTop + this.mLeftView.getMeasuredHeight();
            this.mLeftView.layout(lLeft, lTop, lRight, lBottom);
        }

        if (this.mRightView != null)
        {
            rightViewLp = (MarginLayoutParams) this.mRightView.getLayoutParams();
            lTop = top + rightViewLp.topMargin;
            lLeft = this.mContentView.getRight() + this.mContentViewLp.rightMargin + rightViewLp.leftMargin;
            lRight = lLeft + this.mRightView.getMeasuredWidth();
            lBottom = lTop + this.mRightView.getMeasuredHeight();
            this.mRightView.layout(lLeft, lTop, lRight, lBottom);
        }

    }

    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (isCanScroll)
        {
            switch (ev.getAction())
            {
                case 0:
                    this.isSwipeing = false;
                    if (this.mLastP == null)
                    {
                        this.mLastP = new PointF();
                    }

                    this.mLastP.set(ev.getRawX(), ev.getRawY());
                    if (this.mFirstP == null)
                    {
                        this.mFirstP = new PointF();
                    }

                    this.mFirstP.set(ev.getRawX(), ev.getRawY());
                    if (mViewCache != null)
                    {
                        if (mViewCache != this)
                        {
                            mViewCache.handlerSwipeMenu(State.CLOSE);
                        }

                        this.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                case 1:
                case 3:
                    this.finalyDistanceX = this.mFirstP.x - ev.getRawX();
                    if (Math.abs(this.finalyDistanceX) > (float) this.mScaledTouchSlop)
                    {
                        this.isSwipeing = true;
                    }

                    this.result = this.isShouldOpen(this.getScrollX());
                    this.handlerSwipeMenu(this.result);
                    break;
                case 2:
                    float distanceX = this.mLastP.x - ev.getRawX();
                    float distanceY = this.mLastP.y - ev.getRawY();
                    if (Math.abs(distanceY) <= (float) this.mScaledTouchSlop || Math.abs(distanceY) <= Math.abs(distanceX))
                    {
                        this.scrollBy((int) distanceX, 0);
                        if (this.getScrollX() < 0)
                        {
                            if (this.mCanRightSwipe && this.mLeftView != null)
                            {
                                if (this.getScrollX() < this.mLeftView.getLeft())
                                {
                                    this.scrollTo(this.mLeftView.getLeft(), 0);
                                }
                            }
                            else
                            {
                                this.scrollTo(0, 0);
                            }
                        }
                        else if (this.getScrollX() > 0)
                        {
                            if (this.mCanLeftSwipe && this.mRightView != null)
                            {
                                if (this.getScrollX() > this.mRightView.getRight() - this.mContentView.getRight() - this.mContentViewLp.rightMargin)
                                {
                                    this.scrollTo(this.mRightView.getRight() - this.mContentView.getRight() - this.mContentViewLp.rightMargin, 0);
                                }
                            }
                            else
                            {
                                this.scrollTo(0, 0);
                            }
                        }

                        if (Math.abs(distanceX) > (float) this.mScaledTouchSlop)
                        {
                            this.getParent().requestDisallowInterceptTouchEvent(true);
                        }

                        this.mLastP.set(ev.getRawX(), ev.getRawY());
                    }
            }
        }
        else
        {
            if (mViewCache != null)
            {
                if (mViewCache != this)
                {
                    mViewCache.handlerSwipeMenu(State.CLOSE);
                }
                this.getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case 0:
            default:
                break;
            case 1:
            case 3:
                if (this.isSwipeing)
                {
                    this.isSwipeing = false;
                    this.finalyDistanceX = 0.0F;
                    return true;
                }
                break;
            case 2:
                if (Math.abs(this.finalyDistanceX) > (float) this.mScaledTouchSlop)
                {
                    return true;
                }
        }

        return super.onInterceptTouchEvent(event);
    }

    private void handlerSwipeMenu(State result)
    {
        if (result == State.LEFTOPEN)
        {
            this.mScroller.startScroll(this.getScrollX(), 0, this.mLeftView.getLeft() - this.getScrollX(), 0);
            mViewCache = this;
            mStateCache = result;
        }
        else if (result == State.RIGHTOPEN)
        {
            mViewCache = this;
            this.mScroller.startScroll(this.getScrollX(), 0, this.mRightView.getRight() - this.mContentView.getRight() - this.mContentViewLp.rightMargin - 
                    this.getScrollX(), 0);

            mStateCache = result;
        }
        else
        {
            this.mScroller.startScroll(this.getScrollX(), 0, -this.getScrollX(), 0);
            mViewCache = null;
            mStateCache = null;
        }

        this.invalidate();
    }

    public void computeScroll()
    {
        if (this.mScroller.computeScrollOffset())
        {
            this.scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            this.invalidate();
        }

    }

    private State isShouldOpen(int scrollX)
    {
        if ((float) this.mScaledTouchSlop >= Math.abs(this.finalyDistanceX))
        {
            return mStateCache;
        }
        else
        {
            Log.i("SwipeMenuLayout", ">>>finalyDistanceX:" + this.finalyDistanceX);
            if (this.finalyDistanceX < 0.0F)
            {
                if (this.getScrollX() < 0 && this.mLeftView != null && Math.abs((float) this.mLeftView.getWidth() * this.mFraction) < (float) Math.abs(this
                        .getScrollX()))
                {
                    return State.LEFTOPEN;
                }

                if (this.getScrollX() > 0 && this.mRightView != null)
                {
                    return State.CLOSE;
                }
            }
            else if (this.finalyDistanceX > 0.0F)
            {
                if (this.getScrollX() > 0 && this.mRightView != null && Math.abs((float) this.mRightView.getWidth() * this.mFraction) < (float) Math.abs(this
                        .getScrollX()))
                {
                    return State.RIGHTOPEN;
                }

                if (this.getScrollX() < 0 && this.mLeftView != null)
                {
                    return State.CLOSE;
                }
            }

            return State.CLOSE;
        }
    }

    protected void onDetachedFromWindow()
    {
        if (this == mViewCache)
        {
            mViewCache.handlerSwipeMenu(State.CLOSE);
        }

        super.onDetachedFromWindow();
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        if (this == mViewCache)
        {
            mViewCache.handlerSwipeMenu(mStateCache);
        }

    }

    public void resetStatus()
    {
        if (mViewCache != null && mStateCache != null && mStateCache != State.CLOSE && this.mScroller != null)
        {
            this.mScroller.startScroll(mViewCache.getScrollX(), 0, -mViewCache.getScrollX(), 0);
            mViewCache.invalidate();
            mViewCache = null;
            mStateCache = null;
        }

    }

    public float getFraction()
    {
        return this.mFraction;
    }

    public void setFraction(float mFraction)
    {
        this.mFraction = mFraction;
    }

    public boolean isCanLeftSwipe()
    {
        return this.mCanLeftSwipe;
    }

    public void setCanLeftSwipe(boolean mCanLeftSwipe)
    {
        this.mCanLeftSwipe = mCanLeftSwipe;
    }

    public boolean isCanRightSwipe()
    {
        return this.mCanRightSwipe;
    }

    public void setCanRightSwipe(boolean mCanRightSwipe)
    {
        this.mCanRightSwipe = mCanRightSwipe;
    }

    public static SwipeMenuLayout getViewCache()
    {
        return mViewCache;
    }

    public static State getStateCache()
    {
        return mStateCache;
    }

    private boolean isLeftToRight()
    {
        return this.distanceX < 0.0F;
    }
}
