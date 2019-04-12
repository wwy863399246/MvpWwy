package com.elson.lib.download_library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.elson.lib.download_library.bean.DownLoadBean;
import com.elson.lib.download_library.db.DataBaseUtil;
import com.elson.lib.download_library.utils.DownLoadState;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Headers;
import okhttp3.Response;

/**
 * 连接的线程
 */
public class AsyncConnectCall extends NickRunnable
{
    private final Context context;
    private final Handler handler;
    private final ConcurrentHashMap<String, AsyncDownCall> mTaskMap;
    private final ExecutorService executorService;
    private DownLoadBean bean;
    private AtomicBoolean isRunning;

    @SuppressLint("SimpleDateFormat")
    public AsyncConnectCall(Context context, Handler handler, ConcurrentHashMap<String, AsyncDownCall> mTaskMap, ExecutorService executorService,
                            DownLoadBean bean)
    {
        super("AndroidHttp %s", new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(Calendar.getInstance().getTime()));
        this.context = context;
        this.handler = handler;
        this.mTaskMap = mTaskMap;
        this.executorService = executorService;
        this.bean = bean;
        this.isRunning = new AtomicBoolean(true);
    }


    @Override
    protected void execute()
    {
        bean.downloadState = DownLoadState.STATE_CONNECTION;
        DataBaseUtil.UpdateDownLoadById(context, bean);
        notifyDownloadStateChanged(bean, DownLoadState.STATE_CONNECTION);

        try
        {
            Response response = OkHttpManager.getInstance().initRequest(bean.url);
            if (response != null && response.isSuccessful())
            {
                Headers headers = response.headers();
                String ranges = headers.get("Accept-Ranges");
                if ("bytes".equalsIgnoreCase(ranges))
                {
                    bean.isSupportRange = true;
                }
                bean.totalSize = stringToLong(headers.get("Content-Length"));
            }
            else
            {
                bean.downloadState = DownLoadState.STATE_ERROR;
            }
            AsyncDownCall downLoadTask = new AsyncDownCall(context, handler, bean);
            mTaskMap.put(bean.id, downLoadTask);
            executorService.execute(downLoadTask);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            isRunning.set(false);
            bean.downloadState = DownLoadState.STATE_ERROR;
            DataBaseUtil.UpdateDownLoadById(context, bean);
            notifyDownloadStateChanged(bean, DownLoadState.STATE_ERROR);
        }
        
//        HttpURLConnection connection = null;
//        try
//        {
//            connection = (HttpURLConnection) new URL(bean.url).openConnection();
//            connection.setRequestMethod("GET");
//            connection.setConnectTimeout(Constants.CONNECT_TIME);
//            connection.setReadTimeout(Constants.READ_TIME);
//            int responseCode = connection.getResponseCode();
//            int contentLength = connection.getContentLength();
//            if (responseCode == HttpURLConnection.HTTP_OK)
//            {
//                String ranges = connection.getHeaderField("Accept-Ranges");
//                if ("bytes".equalsIgnoreCase(ranges))
//                {
//                    bean.isSupportRange = true;
//                }
//                bean.totalSize = contentLength;
//            }
//            else
//            {
//                bean.downloadState = DownLoadState.STATE_ERROR;
//            }
//            AsyncDownCall downLoadTask = new AsyncDownCall(context, handler, bean);
//            mTaskMap.put(bean.id, downLoadTask);
//            executorService.execute(downLoadTask);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//            isRunning.set(false);
//            bean.downloadState = DownLoadState.STATE_ERROR;
//            DataBaseUtil.UpdateDownLoadById(context, bean);
//            notifyDownloadStateChanged(bean, DownLoadState.STATE_ERROR);
//        }
//        finally
//        {
//            if (connection != null)
//            {
//                connection.disconnect();
//            }
//        }
    }


    public boolean isCanceled()
    {
        return isRunning.get();
    }

    public void cancel()
    {
        isRunning.set(true);
    }

    /**
     * 当下载状态发送改变的时候调用
     */
    private void notifyDownloadStateChanged(DownLoadBean bean, int state)
    {
        Message message = handler.obtainMessage();
        message.obj = bean;
        message.what = state;
        handler.sendMessage(message);
    }


    private long stringToLong(String s)
    {
        if (s == null)
        {
            return -1;
        }
        try
        {
            return Long.parseLong(s);
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }
}