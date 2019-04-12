package com.app.common.baseview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.app.common.basemvp.BaseModel;
import com.app.common.basemvp.BasePresenter;
import com.app.common.baserx.RxManager;
import com.app.common.commonutils.TUtil;
import com.app.common.commonwidget.LoadingView;
import com.gyf.barlibrary.ImmersionBar;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

/**
 * Created by wwy on 2017/12/12.
 */
public abstract class BaseMvpFragment<T extends BasePresenter, E extends BaseModel> extends Fragment {
    private Unbinder mBinder;
    protected View rootView;
    public RxManager mRxManager;
    public T mPresenter;
    public E mModel;
    public LoadingView mLoadingView;
    protected ImmersionBar mImmersionBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // clearFragments();
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResource(), container, false);
        }
        mBinder = ButterKnife.bind(this, rootView);
        mRxManager = new RxManager();
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        mLoadingView = new LoadingView(getActivity());
        if (mPresenter != null) {
            mPresenter.mContext = this.getActivity();
        }
        //初始化沉浸式

        initPresenter();
        initView();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    //获取布局文件
    protected abstract int getLayoutResource();

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    //初始化view
    protected abstract void initView();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        mBinder.unbind();
        mRxManager.unSubscribe();
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    //沉浸式状态栏
    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
    }

    /**
     * 点击处理
     **/
    public void eventClick(View view, Consumer<Object> consumer) {
        mRxManager.addSubscribe(eventClick(view, 1000).subscribe(consumer));
    }

    public Observable<Object> eventClick(View view, int milliseconds) {
        return RxView.clicks(view).throttleFirst(milliseconds, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread());
    }

    public void eventClick(View view, int milliseconds, Consumer<Object> consumer) {
        mRxManager.addSubscribe(eventClick(view, milliseconds).subscribe(consumer));
    }
}
