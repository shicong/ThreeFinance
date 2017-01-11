/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.models.stocklist;

import com.guysshare.managers.GlobalManager;

/**
 * Created by shicong on 2016/7/6.
 */
public class StockURLManager {

    private IStockURLProcess mStockURLProcess;
    public StockURLManager(StockDataManager.DataType type){
        switch (type){
            case TYPE_SINA:
                mStockURLProcess = new StockURLSina();
                break;
            case TYPE_JD:
                mStockURLProcess = new StockURLJD();
                break;
        }
    }


    public String getOneStockURL(int index) {
        return mStockURLProcess.getOneStockURL(index);
    }

    public String getStocksURL(int[] indexs) {
        return mStockURLProcess.getStocksURL(indexs);
    }

    public String getOneStockTimeLineURL(int index) {
        return mStockURLProcess.getOneStockTimeLineURL(index);
    }

    public String getOneStockDayLineURL(int index) {
        return mStockURLProcess.getOneStockDayLineURL(index);
    }

    public String getOneStockWeekLineURL(int index) {
        return mStockURLProcess.getOneStockWeekLineURL(index);
    }

    public String getOneStockMonthLineURL(int index) {
        return mStockURLProcess.getOneStockMonthLineURL(index);
    }


    public interface IStockURLProcess {
        public String getOneStockURL(int index);
        public String getStocksURL(int[] indexs);
        public String getOneStockTimeLineURL(int index);
        public String getOneStockDayLineURL(int index);
        public String getOneStockWeekLineURL(int index);
        public String getOneStockMonthLineURL(int index);
    }

}
