/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.managers;

import com.guysshare.finance.models.db.DBHelper;

import android.app.Application;
import android.content.Context;

/**
 * Created by shicong on 2016/6/30.
 */
public class GlobalManager {
    private static GlobalManager mGlobalManager;
    public static Context mAppContext;


    private GlobalManager(){
    }

    public static GlobalManager getInstance(){
        if (mGlobalManager == null){
            synchronized(GlobalManager.class){
                mGlobalManager = new GlobalManager();
            }
        }
        return mGlobalManager;
    }

    /**
     * 初始化系统全局变量
     * @param application
     */
    public static void initGlobals(Application application){
        mAppContext = application.getApplicationContext();
        initDatabase();
    }

    /**
     * 初始化数据库
     *
     */
    private static void initDatabase(){
        DBHelper dbHelper = new DBHelper(GlobalManager.mAppContext, DBHelper
                .PATH_FINANCE_MAIN_DB,null, DBHelper.DB_VERSION);
        dbHelper.getWritableDatabase();
        dbHelper.close();
    }

}
