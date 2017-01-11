/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.models.stocklist;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.guysshare.finance.models.db.DBManager;
import com.guysshare.managers.FilesManager;
import com.guysshare.managers.GlobalManager;
import com.guysshare.publics.IOUtils;

import android.text.TextUtils;
import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by shicong on 2016/7/6.
 */
public class StockDataManager {

    public enum DataType{
        TYPE_SINA,
        TYPE_JD,
    }

    private IStockDataProcess mStockDataProcess;

    public StockDataManager(DataType type) {
        switch (type){
            case TYPE_JD:
                mStockDataProcess = new StockDataJD();
                break;
            case TYPE_SINA:
                mStockDataProcess = new StockDataSina();
                break;
        }

    }

    /**
     * 得到数据库中id和名字（隐藏包含Index信息）
     * @return
     */
    public static List<StockBaseData> getAllStocksDBInfos() {
        String[] sidAndName = DBManager.getInstance().getAllSidAndNames();
        List<StockBaseData> returnList = new ArrayList<>();
        if (sidAndName != null && sidAndName.length != 0) {
            for (String item : sidAndName) {
                StockBaseData tmpStock = new StockBaseData();
                String tmpData[] = item.split(",");
                tmpStock.setIndex(Integer.valueOf(tmpData[0]))
                        .setSID(tmpData[1])
                        .setName(tmpData[2]);
                returnList.add(tmpStock);
            }

            return returnList;
        }
        return null;
    }

    /**
     * 异步更新单条列表数据
     * @param index
     */
    public void updateToGlobalStockDataList(final int index){
        mStockDataProcess.updateToGlobalStockDataList(index);
    }

    /**
     * 异步更新多条全局列表数据
     * @param indexs
     */
    public void updateToGlobalStockDataList(final int[] indexs){
        mStockDataProcess.updateToGlobalStockDataList(indexs);
    }

    public interface IStockDataProcess{
        public void updateToGlobalStockDataList(final int index);
        public void updateToGlobalStockDataList(final int[] indexs);
    }

}
