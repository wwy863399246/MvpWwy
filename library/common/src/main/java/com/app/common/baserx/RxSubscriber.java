package com.app.common.baserx;

import android.content.Context;
import android.text.TextUtils;
import com.app.common.R;
import com.app.common.commonutils.SystemUtil;
import com.app.common.commonutils.ToastUtil;
import com.app.common.commonwidget.LoadingDialog;
import com.google.gson.JsonParseException;
import io.reactivex.subscribers.ResourceSubscriber;

import java.net.SocketTimeoutException;

import static com.app.common.baseapp.BaseApplication.getAppContext;

/**
 * Created by wwy on 2018/4/20.
 */

public abstract class RxSubscriber<T> extends ResourceSubscriber<T> {
    private Context mContext;
    private boolean showDialog = false;
    private boolean compression = false;
    private String msg;

    public RxSubscriber(Context context) {
        this.mContext = context;

    }

    public RxSubscriber(Context context, boolean showDialog) {
        this.mContext = context;
        this.showDialog = showDialog;
    }

    public RxSubscriber(Context context, boolean showDialog, String msg) {
        this.mContext = context;
        this.showDialog = showDialog;
        this.msg = msg;
    }

    public RxSubscriber(Context context, boolean showDialog, String msg, boolean compression) {
        this.mContext = context;
        this.showDialog = showDialog;
        this.msg = msg;
        this.compression = compression;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始加载
        if (showDialog) {
            LoadingDialog.showDialogForLoading(mContext, msg, true);
        }
    }

    @Override
    public void onComplete() {
        //完成关闭动画
        if (showDialog) {
            LoadingDialog.cancelDialogForLoading();
        }
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    /**
     * 对错误信息在这里可以做统一处理
     */

    @Override
    public void onError(Throwable e) {
        //错误关闭动画
        if (showDialog) {
            LoadingDialog.cancelDialogForLoading();
        }
        e.printStackTrace();
        //网络
        if (!SystemUtil.isConnected()) {
            ToastUtil.showShort(getAppContext().getString(R.string.no_net));
            _onError(getAppContext().getString(R.string.no_net));
        }
        //服务器
        else if (e instanceof AppException) {
            if (!TextUtils.equals("1040", ((AppException) e).getCode())) {
                if (!TextUtils.isEmpty(e.getMessage())) {
                    ToastUtil.showShort(e.getMessage());
                }
            }
            _onError(e.getMessage());
        } else if (e instanceof JsonParseException) {
            ToastUtil.showShort("数据解析出错");
            _onError("数据格式错误");
        } else if (e instanceof SocketTimeoutException) {
            ToastUtil.showShort("网络请求超时，请稍后重试");
            _onError("网络请求超时，请稍后重试");
        }
        //其它
        else {
            if (!compression) {
                ToastUtil.showShort(getAppContext().getString(R.string.net_error));
                _onError(getAppContext().getString(R.string.net_error));
            } else {
                ToastUtil.showShort(getAppContext().getString(R.string.update_error));
            }

        }
    }

    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);
}
