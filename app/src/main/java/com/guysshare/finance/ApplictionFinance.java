/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance;

import com.guysshare.managers.GlobalManager;

import android.app.Application;
import android.service.notification.NotificationListenerService;

/**
 * Created by shicong on 2016/6/30.
 */
public class ApplictionFinance extends Application implements Thread.UncaughtExceptionHandler{

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalManager.initGlobals(this);
    }



    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

    }
}
