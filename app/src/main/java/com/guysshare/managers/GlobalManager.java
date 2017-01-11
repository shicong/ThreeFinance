/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.managers;

import java.util.ArrayList;

import com.guysshare.finance.models.db.DBHelper;
import com.guysshare.finance.models.stocklist.StockBaseData;
import com.guysshare.finance.models.stocklist.StockDataManager;

import android.app.Application;
import android.content.Context;
import android.support.annotation.WorkerThread;
import okhttp3.OkHttpClient;

/**
 * Created by shicong on 2016/6/30.
 */
public class GlobalManager {
    private static GlobalManager mGlobalManager;
    public static Context mAppContext;
    public static ArrayList<StockBaseData> mGlobalStockList;
    private static OkHttpClient mOkHttpClient;

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
        mGlobalStockList =  (ArrayList<StockBaseData>) StockDataManager.getAllStocksDBInfos();
        mOkHttpClient = new OkHttpClient();
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

    /**
     * 系统联网使用的默认Client
     * @return
     */
    public static OkHttpClient getDefaultOkHttpClient(){
        return mOkHttpClient;
    }

}
