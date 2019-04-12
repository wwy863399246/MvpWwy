package com.elson.lib.download_library.notify;

import android.util.Log;

import com.elson.lib.download_library.bean.DownLoadBean;

import java.util.Observable;


public class DownLoadObservable extends Observable
{
    private DownLoadBean data;

    private DownLoadObservable()
    {
    }

    private final static class Instance
    {
        static final DownLoadObservable instance = new DownLoadObservable();
    }

    public static DownLoadObservable getInstance()
    {
        return Instance.instance;
    }

    public void setData(DownLoadBean data)
    {
        this.data = data;
        dataChange(data);
    }

    private void dataChange(DownLoadBean data)
    {
        this.setChanged();
        this.notifyObservers(data);
    }

    public DownLoadBean getData()
    {
        return data;
    }
}
