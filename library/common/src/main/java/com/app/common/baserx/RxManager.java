package com.app.common.baserx;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by wwy on 2018/4/20.
 */

public class RxManager
{
    public RxBus mRxBus = RxBus.getInstance();

    /*管理Observables 和 Subscribers订阅*/
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();;

    //管理rxbus订阅
    private Map<String, Flowable<?>> mFlowables = new HashMap<>();

    /**
     * 将RxJava事件流返回的Disposable添加到CompositeDisposable进行统一管理
     *
     * @param
     */
    public void on(String eventName, Consumer<Object> consumer)
    {
        Flowable<Object> mFlowable = mRxBus.register(eventName);
        mFlowables.put(eventName, mFlowable);
        mCompositeDisposable.add(mFlowable.onBackpressureBuffer().compose(RxUtil.rxSchedulerHelper()).subscribe(consumer));
    }

    /**
     * 当前页面退出时  切断所有的事件流，不再接受事件上流的数据
     */
    public void unSubscribe()
    {
        if (mCompositeDisposable != null)
        {
            mCompositeDisposable.clear();
        }
        for (Map.Entry<String, Flowable<?>> stringFlowableEntry : mFlowables.entrySet())
        {
            mRxBus.unregister(stringFlowableEntry.getKey(), stringFlowableEntry.getValue());
        }
    }

    public void addSubscribe(Disposable subscription)
    {
        if (mCompositeDisposable == null)
        {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    /**
     * 发送rxbus
     */
    public void post(Object tag, Object content)
    {
        mRxBus.post(tag, content);
    }

}
