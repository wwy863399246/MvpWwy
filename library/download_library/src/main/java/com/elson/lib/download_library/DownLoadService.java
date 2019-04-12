package com.elson.lib.download_library;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.elson.lib.download_library.bean.DownLoadBean;
import com.elson.lib.download_library.db.DataBaseUtil;
import com.elson.lib.download_library.notify.DownLoadObservable;
import com.elson.lib.download_library.utils.DownLoadConfig;
import com.elson.lib.download_library.utils.DownLoadState;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;


public class DownLoadService extends Service
{
    private DownLoadExecutors downLoadExecutors;
    private ThreadPoolExecutor downLoadExecutor;
    private ConcurrentHashMap<String, AsyncDownCall> mTaskMap;
    private LinkedBlockingDeque<DownLoadBean> mWaitingQueue;

    public DownLoadService()
    {
        downLoadExecutors = new DownLoadExecutors();
        downLoadExecutor = new DownLoadExecutors().executorService();
        mTaskMap = new ConcurrentHashMap<>();
        mWaitingQueue = new LinkedBlockingDeque<>();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    /**
     * 当下载状态发送改变的时候回调
     */
    private Handler handler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            DownLoadBean bean = (DownLoadBean) msg.obj;
            int what = msg.what;
            switch (what)
            {
                case DownLoadState.STATE_ERROR:
                case DownLoadState.STATE_DOWNLOADED:
                case DownLoadState.STATE_DELETE:
                case DownLoadState.STATE_PAUSED:
                    mTaskMap.remove(bean.id);
                    DownLoadBean poll = mWaitingQueue.poll();
                    if (poll != null)
                    {
                        downNone(poll);
                    }
                    break;
                default:
                    break;
            }

            DownLoadObservable.getInstance().setData(bean);
        }
    };

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

    /**
     * 下载
     *
     * @param loadBean object
     */
    public void download(DownLoadBean loadBean)
    {
        //先判断是否有这个app的下载信息,更新信息
        if (DataBaseUtil.getDownLoadById(getApplicationContext(), loadBean.id) != null)
        {
            DataBaseUtil.UpdateDownLoadById(getApplicationContext(), loadBean);
        }
        else
        {
            //插入数据库
            DataBaseUtil.insertDown(getApplicationContext(), loadBean);
        }

        int state = loadBean.downloadState;
        switch (state)
        {
            case DownLoadState.STATE_NONE://默认
                downNone(loadBean);
                break;
            case DownLoadState.STATE_WAITING://等待中
                downWaiting(loadBean);
                break;
            case DownLoadState.STATE_PAUSED://暂停
                downPaused(loadBean);
                break;
            case DownLoadState.STATE_DOWNLOADING://下载中
                downLoading(loadBean);
                break;
            case DownLoadState.STATE_CONNECTION://连接中
                break;
            case DownLoadState.STATE_ERROR://下载失败
                downError(loadBean);
                break;
            case DownLoadState.STATE_DOWNLOADED://下载完毕
                break;
            default:
                break;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        try
        {
            if (intent.getAction() != null && "com.elson.lib.download_library.downloadservice".equalsIgnoreCase(intent.getAction()))
            {
                DownLoadBean bean = (DownLoadBean) intent.getSerializableExtra(Constants.KEY_DOWNLOAD_ENTRY);
                if (bean != null)
                {
                    boolean booleanExtra = intent.getBooleanExtra(Constants.KEY_OPERATING_STATE, false);
                    if (booleanExtra)
                    {
                        deleteDownTask(bean);
                    }
                    else
                    {
                        download(bean);
                    }
                }
                return START_STICKY;
            }
            else
            {
                return super.onStartCommand(intent, flags, startId);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return super.onStartCommand(intent, flags, startId);
        }
    }

    public void deleteDownTask(DownLoadBean item)
    {
        try
        {
            AsyncDownCall remove = mTaskMap.remove(item.id);
            if (remove != null)
            {
                remove.cancel();
            }
            else
            {
                mWaitingQueue.remove(item);
            }
            item.downloadState = DownLoadState.STATE_DELETE;
            DataBaseUtil.DeleteDownLoadById(getApplicationContext(), item.id);
            notifyDownloadStateChanged(item, DownLoadState.STATE_DELETE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void downNone(DownLoadBean loadBean)
    {
        if (mTaskMap.size() >= DownLoadConfig.getConfig().getMaxTasks())
        {
            mWaitingQueue.offer(loadBean);
            loadBean.downloadState = DownLoadState.STATE_WAITING;
            DataBaseUtil.UpdateDownLoadById(this, loadBean);
            notifyDownloadStateChanged(loadBean, DownLoadState.STATE_WAITING);
        }
        else
        {
            if (loadBean.totalSize <= 0)
            {
                AsyncConnectCall connectThread = new AsyncConnectCall(this, handler, mTaskMap, downLoadExecutor, loadBean);
                downLoadExecutor.execute(connectThread);
            }
            else
            {
                AsyncDownCall downLoadTask = new AsyncDownCall(this, handler, loadBean);
                mTaskMap.put(loadBean.id, downLoadTask);
                downLoadExecutors.execute(downLoadTask);
            }
        }
    }

    /**
     * 等待状态
     */
    private void downWaiting(DownLoadBean loadBean)
    {
        mWaitingQueue.remove(loadBean);
        AsyncDownCall downLoadTask = mTaskMap.get(loadBean.id);
        if (downLoadTask != null)
        {
            downLoadTask.cancel();
        }
        mTaskMap.remove(loadBean.id);
        loadBean.downloadState = DownLoadState.STATE_PAUSED;
        DataBaseUtil.UpdateDownLoadById(getApplicationContext(), loadBean);
        notifyDownloadStateChanged(loadBean, DownLoadState.STATE_PAUSED);
    }


    /**
     * 暂停状态
     */
    private void downPaused(DownLoadBean loadBean)
    {
        loadBean.downloadState = DownLoadState.STATE_WAITING;
        downNone(loadBean);
    }


    /**
     * 下载状态
     */
    private void downLoading(DownLoadBean loadBean)
    {
        AsyncDownCall downLoadTask = mTaskMap.get(loadBean.id);
        if (downLoadTask != null)
        {
            downLoadTask.cancel();
            mTaskMap.remove(loadBean.id);
        }
        else
        {
            mWaitingQueue.remove(loadBean);
        }
    }

    /**
     * 下载失败
     */
    private void downError(DownLoadBean loadBean)
    {
        DataBaseUtil.UpdateDownLoadById(getApplicationContext(), loadBean);
        loadBean.downloadState = DownLoadState.STATE_NONE;
        download(loadBean);
    }

    @Override
    public void onDestroy()
    {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
