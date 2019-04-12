package com.app.common.baserx;

import android.support.annotation.NonNull;
import org.reactivestreams.Processor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created by wwy on 2018/5/31.
 */

public class RxBus
{
    private static RxBus instance;

    public static synchronized RxBus getInstance()
    {
        if (null == instance)
        {
            instance = new RxBus();
        }
        return instance;
    }

    @SuppressWarnings("rawtypes")
    private ConcurrentHashMap<Object, List<FlowableProcessor>> processorMapper = new ConcurrentHashMap<Object, List<FlowableProcessor>>();

    private RxBus()
    {
    }

    /**
     * 注册事件源
     *
     * @param tag
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    public <T> Flowable<T> register(@NonNull Object tag)
    {
        List<FlowableProcessor> flowableProcessors = processorMapper.get(tag);
        if (null == flowableProcessors)
        {
            flowableProcessors = new ArrayList<FlowableProcessor>();
            processorMapper.put(tag, flowableProcessors);
        }
        FlowableProcessor<T> processor;
        flowableProcessors.add(processor = PublishProcessor.<T>create().toSerialized());

        return processor;
    }

    /**
     * 取消监听
     *
     * @param tag
     * @param flowable
     * @return
     */
    @SuppressWarnings("rawtypes")
    public RxBus unregister(@NonNull Object tag, @NonNull Flowable<?> flowable)
    {
        if (null == flowable)
        {
            return getInstance();
        }
        List<FlowableProcessor> flowableProcessors = processorMapper.get(tag);
        if (null != flowableProcessors)
        {
            flowableProcessors.remove((Processor<?, ?>) flowable);
            if (isEmpty(flowableProcessors))
            {
                processorMapper.remove(tag);
            }
        }
        return getInstance();
    }

    /**
     * 触发事件
     *
     * @param content
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void post(@NonNull Object tag, @NonNull Object content)
    {

        List<FlowableProcessor> flowableProcessors = processorMapper.get(tag);
        if (!isEmpty(flowableProcessors))
        {
            for (FlowableProcessor flowableProcessor : flowableProcessors)
            {
                flowableProcessor.onNext(content);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection<FlowableProcessor> collection)
    {
        return null == collection || collection.isEmpty();
    }
}
