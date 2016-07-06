/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.models.db;

import com.guysshare.managers.GlobalManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 对数据看数据的管理
 * Created by shicong on 2016/7/6.
 */
public class DBManager {

    private static DBManager mDBManager;

    private DBManager(){
    }

    public static DBManager getInstance(){
        if (mDBManager == null){
            synchronized(DBManager.class){
                mDBManager = new DBManager();
            }
        }
        return mDBManager;
    }

    private SQLiteDatabase getStockDB(){
        String dbPath = GlobalManager.mAppContext.getDatabasePath(DBHelper.PATH_FINANCE_MAIN_DB)
                .getAbsolutePath();
        return SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READONLY);
    }

    public String[] getTopXSidsFromTotalList(int count){
        String[] resultList;
        SQLiteDatabase db = getStockDB();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(DBColumns.DBBaseInfos.DB_STOCK_C1_ID);
        sql.append(" from ");
        sql.append(DBColumns.TABLE_BASEINFOS);
        sql.append(" limit " + count + ";");
        Cursor c = db.rawQuery(sql.toString(),null);
        int resultCnt = c.getCount();
        if (resultCnt != 0){
            resultList = new String[resultCnt];
            c.moveToFirst();
        }else {
            return null;
        }
        for (int i = 0; i < resultCnt; i++){
            resultList[i] = c.getString(0);
            c.moveToNext();
        }
        db.close();
        return resultList;
    }

}
