/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.models.db;

import android.provider.BaseColumns;

/**
 * Created by shicong on 2016/6/30.
 */
public class DBColumns{

    public final static String TABLE_BASEINFOS = "baseinfos";
    public final static class DBBaseInfos implements BaseColumns{
        public static final String DB_STOCK_C0_IDX = DBBaseInfos._ID;
        public static final String DB_STOCK_C1_ID = "sid";
        public static final String DB_STOCK_C2_NAME = "name";
    }
}
