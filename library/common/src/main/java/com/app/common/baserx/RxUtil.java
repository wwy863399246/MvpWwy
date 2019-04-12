package com.app.common.baserx;

import android.text.TextUtils;

import com.app.common.R;
import com.app.common.baseapp.BaseApplication;
import com.app.common.basebean.BaseResponse;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wwy on 2016/8/3.
 */
public class RxUtil
{

    /**
     * 统一线程处理调度
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper()
    {    //compose简化线程
        return new FlowableTransformer<T, T>()
        {
            @Override
            public Flowable<T> apply(Flowable<T> flowable)
            {
                return flowable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 没有基类  直接处理返回结果
     *
     * @param <T>
     */
    public static <T> FlowableTransformer<T, T> handleResponseResult()
    {   //compose判断结果
        return new FlowableTransformer<T, T>()
        {
            @Override
            public Publisher<T> apply(Flowable<T> response)
            {
                return response.flatMap(new Function<T, Publisher<T>>()
                {
                    @Override
                    public Publisher<T> apply(T t) throws Exception
                    {
                        if (t != null)
                        {
                            return createData(t);
                        }
                        else
                        {
                            return Flowable.error(new AppException(BaseApplication.getAppContext().getString(R.string.server_excception), "-1"));
                        }
                    }
                });
            }
        };
    }


    /**
     * 返回三个个参数(result)的 返回结果处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<BaseResponse<T>, T> handleBaseResult()
    {   //compose判断结果
        return new FlowableTransformer<BaseResponse<T>, T>()
        {
            @Override
            public Flowable<T> apply(Flowable<BaseResponse<T>> httpResponseFlowable)
            {
                return httpResponseFlowable.flatMap(new Function<BaseResponse<T>, Flowable<T>>()
                {
                    @Override
                    public Flowable<T> apply(BaseResponse<T> result)
                    {
//                        if (result.success())
//                        {//返回有数据的结果
                            return createData(result.getResults());
//                        }
                        //这里可以根据返回结果码对返回数据做处理
//                        else if (!TextUtils.equals(result.getErrno(), "1000"))
//                        {
//                            return Flowable.error(new AppException(result.getErrmsg(), result.getErrno()));
//                        }
//                        else
//                        {
//                            return Flowable.error(new AppException(BaseApplication.getAppContext().getString(R.string.server_excception), result.getErrno()));
//                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 自定义 -- 创建
     *
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> createData(final T t)
    {
        return Flowable.create(new FlowableOnSubscribe<T>()
        {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception
            {
                try
                {
                    emitter.onNext(t);
                    emitter.onComplete();
                }
                catch (Exception e)
                {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);//BUFFER--缓存策略，LATEST,DROP--需要什么就发射什么数据
    }
    
}
