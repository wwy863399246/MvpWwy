package com.elson.lib.download_library.bean;


import com.elson.lib.download_library.utils.DownLoadState;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/29 0029.
 */

public class DownLoadBean implements Serializable, Cloneable
{
    public String id;//文件id
    public String fileName;//文件名称
    public String fileIcon;//文件图标
    public long totalSize;//文件的size
    public long currentSize = 0;//当前的size
    public int downloadState = DownLoadState.STATE_NONE;//下载的状态
    public String url;//下载地址
    public String path;//保存路径
    public boolean isSupportRange = false;//是否支持断点下载

    public DownLoadBean()
    {
    }

    public DownLoadBean(String id, String fileName, String fileIcon, String url)
    {
        this.id = id;
        this.fileName = fileName;
        this.fileIcon = fileIcon;
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "DownLoadBean{" + "id='" + id + '\'' + ", fileName='" + fileName + '\'' + ", fileIcon='" + fileIcon + '\'' + ", totalSize=" + totalSize + ", "
                + "currentSize=" + currentSize + ", downloadState=" + downloadState + ", url='" + url + '\'' + ", path='" + path + '\'' + ", isSupportRange="
                + isSupportRange + '}';
    }

    @Override
    public boolean equals(Object o)
    {
        return o.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
