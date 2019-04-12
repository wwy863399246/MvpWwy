package me.dkzwm.widget.srl.header;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import me.dkzwm.widget.srl.AbsClassicRefreshView;
import me.dkzwm.widget.srl.ClassicConfig;
import me.dkzwm.widget.srl.R;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.indicator.IIndicator;

/**
 * @author dkzwm
 */
public class ClassicHeader<T extends IIndicator> extends AbsClassicRefreshView<T>
{
    @StringRes
    private int mPullDownToRefreshRes = R.string.sr_pull_down_to_refresh;
    @StringRes
    private int mPullDownRes = R.string.sr_pull_down;
    @StringRes
    private int mRefreshingRes = R.string.sr_refreshing;
    @StringRes
    private int mRefreshSuccessfulRes = R.string.sr_refresh_complete;
    @StringRes
    private int mRefreshFailRes = R.string.sr_refresh_failed;
    @StringRes
    private int mReleaseToRefreshRes = R.string.sr_release_to_refresh;
    private AnimationDrawable animationDrawable;

    public ClassicHeader(Context context)
    {
        this(context, null);
    }

    public ClassicHeader(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ClassicHeader(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mProgressBar.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        mLastUpdateTextView.setVisibility(GONE);
        animationDrawable = setFrameAnim(context, mArrowImageView);
        //mArrowImageView.setImageResource(R.drawable.lista44a44a);
    }

    public void setPullDownToRefreshRes(@StringRes int pullDownToRefreshRes)
    {
        mPullDownToRefreshRes = pullDownToRefreshRes;
    }

    public void setPullDownRes(@StringRes int pullDownRes)
    {
        mPullDownRes = pullDownRes;
    }

    public void setRefreshingRes(@StringRes int refreshingRes)
    {
        mRefreshingRes = refreshingRes;
    }

    public void setRefreshSuccessfulRes(@StringRes int refreshSuccessfulRes)
    {
        mRefreshSuccessfulRes = refreshSuccessfulRes;
    }

    public void setRefreshFailRes(@StringRes int refreshFailRes)
    {
        mRefreshFailRes = refreshFailRes;
    }

    public void setReleaseToRefreshRes(@StringRes int releaseToRefreshRes)
    {
        mReleaseToRefreshRes = releaseToRefreshRes;
    }

    @Override
    public int getType()
    {
        return TYPE_HEADER;
    }

    @Override
    public void onRefreshPrepare(SmoothRefreshLayout frame)
    {
        mShouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();
        if (!TextUtils.isEmpty(mLastUpdateTimeKey))
        {
            mLastUpdateTimeUpdater.start();
        }
        mProgressBar.setVisibility(GONE);
        mArrowImageView.setVisibility(VISIBLE);
        mTitleTextView.setVisibility(GONE);
        mLastUpdateTextView.setVisibility(GONE);
        if (frame.isEnabledPullToRefresh())
        {
            mTitleTextView.setText(mPullDownToRefreshRes);
        }
        else
        {
            mTitleTextView.setText(mPullDownRes);
        }
    }

    @Override
    public void onRefreshBegin(SmoothRefreshLayout frame, T indicator)
    {
        animationDrawable.start();
        // mArrowImageView.clearAnimation();
        mArrowImageView.setVisibility(VISIBLE);
        mProgressBar.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        mTitleTextView.setText(mRefreshingRes);
        // tryUpdateLastUpdateTime();
    }

    @Override
    public void onRefreshComplete(SmoothRefreshLayout frame, boolean isSuccessful)
    {
        // mArrowImageView.clearAnimation();
        animationDrawable.stop();
        mArrowImageView.setVisibility(VISIBLE);
        mProgressBar.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        if (frame.isRefreshSuccessful())
        {
            mTitleTextView.setText(mRefreshSuccessfulRes);
            mLastUpdateTime = System.currentTimeMillis();
            ClassicConfig.updateTime(getContext(), mLastUpdateTimeKey, mLastUpdateTime);
        }
        else
        {
            mTitleTextView.setText(mRefreshFailRes);
        }
    }

    @Override
    public void onRefreshPositionChanged(SmoothRefreshLayout frame, byte status, T indicator)
    {
        final int offsetToRefresh = indicator.getOffsetToRefresh();
        final int currentPos = indicator.getCurrentPos();
        final int lastPos = indicator.getLastPos();

        if (currentPos < offsetToRefresh && lastPos >= offsetToRefresh)
        {
            if (indicator.hasTouched() && status == SmoothRefreshLayout.SR_STATUS_PREPARE)
            {
                mTitleTextView.setVisibility(GONE);
                if (frame.isEnabledPullToRefresh())
                {
                    mTitleTextView.setText(mPullDownToRefreshRes);
                }
                else
                {
                    mTitleTextView.setText(mPullDownRes);
                }
                mArrowImageView.setVisibility(VISIBLE);
                // mArrowImageView.clearAnimation();
                // mArrowImageView.startAnimation(mReverseFlipAnimation);
            }
        }
        else if (currentPos > offsetToRefresh && lastPos <= offsetToRefresh)
        {
            if (indicator.hasTouched() && status == SmoothRefreshLayout.SR_STATUS_PREPARE)
            {
                mTitleTextView.setVisibility(GONE);
                if (!frame.isEnabledPullToRefresh())
                {
                    mTitleTextView.setText(mReleaseToRefreshRes);
                }
                mArrowImageView.setVisibility(VISIBLE);
                // mArrowImageView.clearAnimation();
                // mArrowImageView.startAnimation(mFlipAnimation);
            }
        }
    }

    /**
     * 通过代码添加帧动画方法
     */
    public static AnimationDrawable setFrameAnim(Context context, ImageView imageView)
    {

        AnimationDrawable animationDrawable = new AnimationDrawable();
        // 为AnimationDrawable添加动画帧  
        animationDrawable.addFrame(context.getResources().getDrawable(R.drawable.a1), 125);
        animationDrawable.addFrame(context.getResources().getDrawable(R.drawable.a2), 125);
        animationDrawable.addFrame(context.getResources().getDrawable(R.drawable.a3), 125);
        animationDrawable.addFrame(context.getResources().getDrawable(R.drawable.a4), 125);
        // 设置为循环播放  
        animationDrawable.setOneShot(false);
        imageView.setImageDrawable(animationDrawable);
        return animationDrawable;
    }
}
