package com.app.common.basemvp;

import android.content.Context;

import com.app.common.baserx.RxManager;

/**
 * des:基类presenter
 * Created by wwy
 * on 2018.06.11:55
 */
public abstract class BasePresenter<T, E>
{
    public Context mContext;
    public E mModel;
    public T mView;
    public RxManager mRxManage = new RxManager();

    public void setVM(T v, E m)
    {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }

    public void onStart()
    {
    }

    ;

    public void onDestroy()
    {
        mRxManage.unSubscribe();
    }
}
