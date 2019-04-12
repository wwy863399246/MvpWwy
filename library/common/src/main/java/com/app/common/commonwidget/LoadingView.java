package com.app.common.commonwidget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.common.R;
import com.app.common.commonwidget.roundview.RoundTextView;


/**
 * Created by Dev on 2017/4/20.
 */

public class LoadingView extends FrameLayout
{

    private TextView tv_tip;
    private TextView tv_error_tip;
    private ImageView iv_tip;
    private ProgressBar pb_loading;
    private RoundTextView rtv_retry;
    private onReloadListener onReloadListener;
    private View view;

    private LoadStatus state;

    public LoadingView(Context context)
    {
        super(context);
        initView();
    }

    public LoadingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView()
    {
        View.inflate(getContext(), R.layout.loading_view, this);
        tv_tip = ((TextView) findViewById(R.id.tv_tip));
        tv_error_tip = ((TextView) findViewById(R.id.tv_error_tip));
        rtv_retry = ((RoundTextView) findViewById(R.id.rtv_retry));
        iv_tip = ((ImageView) findViewById(R.id.iv_tip));
        pb_loading = ((ProgressBar) findViewById(R.id.pb_loading));
        view = findViewById(R.id.view);
        rtv_retry.setOnClickListener(v ->
        {
            if (null != onReloadListener)
            {
                onReloadListener.reload();
            }
        });
        setVisibility(View.GONE);
    }

    //分为服务器失败，网络加载失败、数据为空、加载中、完成四种状态
    public static enum LoadStatus
    {
        sereverError, error, empty, loading, finish
    }

    /**
     * 根据状态显示不同的提示
     *
     * @param loadStatus
     */
    public void setLoadingTip(LoadStatus loadStatus)
    {
        state = loadStatus;
        switch (loadStatus)
        {
            case empty:
                setVisibility(View.VISIBLE);
                iv_tip.setVisibility(View.VISIBLE);
                tv_tip.setVisibility(GONE);
                pb_loading.setVisibility(View.GONE);
                tv_error_tip.setText(getContext().getText(R.string.empty).toString());
                tv_error_tip.setVisibility(VISIBLE);
                iv_tip.setImageResource(R.mipmap.tip_nothing);
                break;
            case sereverError:
                setVisibility(View.VISIBLE);
                iv_tip.setVisibility(View.VISIBLE);
                pb_loading.setVisibility(View.GONE);
                tv_error_tip.setVisibility(VISIBLE);
                tv_tip.setVisibility(GONE);
                tv_error_tip.setText(getContext().getText(R.string.net_error).toString());
                rtv_retry.setVisibility(VISIBLE);
                iv_tip.setImageResource(R.mipmap.ic_wrong);
                break;
            case error:
                setVisibility(View.VISIBLE);
                iv_tip.setVisibility(View.VISIBLE);
                tv_error_tip.setVisibility(View.VISIBLE);
                pb_loading.setVisibility(View.GONE);
                rtv_retry.setVisibility(VISIBLE);
                tv_tip.setVisibility(GONE);
                tv_error_tip.setText(getContext().getText(R.string.no_net).toString());
                break;
            case loading:
                setVisibility(View.VISIBLE);
                iv_tip.setVisibility(View.GONE);
                pb_loading.setVisibility(View.VISIBLE);
                tv_tip.setVisibility(VISIBLE);
                tv_tip.setText(getContext().getText(R.string.please_later).toString());
                rtv_retry.setVisibility(GONE);
                tv_error_tip.setVisibility(View.GONE);
                break;
            case finish:
                setVisibility(View.GONE);
                break;
        }
    }

    public void setTip(String tip)
    {
        if (tv_tip != null)
        {
            tv_tip.setText(tip);
        }
    }

    public void setView()
    {
        view.setVisibility(VISIBLE);
    }


    public void setOnReloadListener(onReloadListener onReloadListener)
    {
        this.onReloadListener = onReloadListener;
    }

    /**
     * 重新尝试接口
     */
    public interface onReloadListener
    {
        void reload();
    }

    public LoadStatus getState()
    {
        return state;
    }

    public void setState(LoadStatus state)
    {
        this.state = state;
    }
}
