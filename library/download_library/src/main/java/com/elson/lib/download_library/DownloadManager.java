package com.elson.lib.download_library;

import android.content.Context;
import android.content.Intent;

import com.elson.lib.download_library.bean.DownLoadBean;

public class DownloadManager
{
    private static DownloadManager instance;
    private final Context context;

    private DownloadManager(Context context)
    {
        this.context = context;
        context.startService(new Intent(context, DownLoadService.class));
    }

    public static DownloadManager getInstance(Context context)
    {
        if (instance == null)
        {
            synchronized (DownloadManager.class)
            {
                if (instance == null)
                {
                    instance = new DownloadManager(context);
                }
            }
        }
        return instance;
    }


    public void down(DownLoadBean item)
    {
        Intent intent = new Intent(context, DownLoadService.class);
        intent.setAction("com.elson.lib.download_library.downloadservice");
        intent.setPackage("com.elson.lib.download_library");
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, item);
        intent.putExtra(Constants.KEY_OPERATING_STATE, false);
        context.startService(intent);
    }

    public void delete(DownLoadBean item)
    {
        Intent intent = new Intent(context, DownLoadService.class);
        intent.setAction("com.elson.lib.download_library.downloadservice");
        intent.setPackage("com.elson.lib.download_library");
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, item);
        intent.putExtra(Constants.KEY_OPERATING_STATE, true);
        context.startService(intent);
    }
}
