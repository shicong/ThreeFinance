/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.models.db;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import com.guysshare.managers.AssetsManager;
import com.guysshare.managers.GlobalManager;
import com.guysshare.publics.IOUtils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shicong on 2016/6/30.
 */
public class FinanceDBHelper extends SQLiteOpenHelper{

    public final static String PATH_FINANCE_MAIN_DB = "aa.db";
    public final static int DB_VERSION = 1;

    public FinanceDBHelper(Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public FinanceDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version,
                           DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createStocksBaseInfo(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    private void createStocksBaseInfo(SQLiteDatabase db){
        StringBuilder sqlExe = new StringBuilder("create table ");
        sqlExe.append(DBColumns.TABLE_BASEINFOS + "(");
        sqlExe.append(DBColumns.DBBaseInfos.DB_STOCK_C0_IDX + " integer primary key autoincrement,");
        sqlExe.append(DBColumns.DBBaseInfos.DB_STOCK_C1_ID + " char(10),");
        sqlExe.append(DBColumns.DBBaseInfos.DB_STOCK_C2_NAME + " char(128)");
        sqlExe.append(");");
        db.execSQL(sqlExe.toString());

        initStockBaseInfo(db);
    }

    private void initStockBaseInfo(final SQLiteDatabase db){
        byte[] buffer = AssetsManager.load(GlobalManager.mAppContext,"stocks.txt");
        try {
            String bufferstr = new String(buffer,"GBK");

            IOUtils.readLines(new StringReader(bufferstr), new IOUtils.IOReadLinesCallback() {
                @Override
                public void onCallback(String line) throws IOException {
                    if (line != null){
                        String sid = line.split(",")[0];
                        String name = line.split(",")[1];

                        StringBuilder sqlExe = new StringBuilder();
                        sqlExe.append("insert into " + DBColumns.TABLE_BASEINFOS);
                        sqlExe.append("(sid,name)");
                        sqlExe.append(" values ");
                        sqlExe.append("('" + sid + "','" + name + "');");
                        db.execSQL(sqlExe.toString());
                    }

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
