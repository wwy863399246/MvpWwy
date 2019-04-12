package com.elson.lib.download_library.utils;

import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;


public class FileUtilities
{
    //创建下载文件
    public synchronized static File getDownloadFile(String fileName)
    {
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return new File(downloadDir, fileName);
    }

    /**
     * 转换文件大小
     *
     * @param fileSize 文件大小
     * @return 格式化
     */
    public static String convertFileSize(long fileSize)
    {
        if (fileSize <= 0)
        {
            return "0M";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024)
        {
            fileSizeString = df.format((double) fileSize) + "B";
        }
        else if (fileSize < 1048576)
        {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        }
        else if (fileSize < 1073741824)
        {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        }
        else
        {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }
}
