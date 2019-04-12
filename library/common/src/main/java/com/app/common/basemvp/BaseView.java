package com.app.common.basemvp;

/**
 * des:baseview
 * Created by wwy
 * on 2018.06.11:53
 */
public interface BaseView {
    /*******内嵌加载*******/
    void showLoading(String title);
    void stopLoading();
    void showErrorTip(String msg);
}
