package com.example.wwy.mvpwwy.app;

import com.app.common.baseapp.BaseApplication;

public class MyApplication extends BaseApplication {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized MyApplication getInstance() {
        return instance;
    }
}
