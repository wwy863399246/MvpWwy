package com.example.wwy.mvpwwy.utils;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import java.util.List;

/**
 * Created by Sunny on 2018/4/28.
 */

public class BaseUtils

{
    private static ProgressDialog mProgressDialog;

    /***
     * 判断List是否为空
     */
    public static boolean isList(List<?> s) {
        if (s == null) {
            return false;
        }
        if (s.size() <= 0) {
            return false;
        }
        return true;
    }

    /***
     * 判断String是否为空
     */

    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    /***
     * 判断进度条是否显示
     */
    public static boolean isProgressShow() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            return true;
        }
        return false;
    }

    /**
     * 显示进度条
     *
     * @param msg
     */
    public static void showProgress(Context mContext, String msg) {
        if (null == mProgressDialog && null != mContext) {
            mProgressDialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (null != mProgressDialog && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        if (null != mProgressDialog) {
            mProgressDialog.setMessage(msg);
        }
    }

    /**
     * 关闭进度条
     */
    public static void dismissProgress() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

}
