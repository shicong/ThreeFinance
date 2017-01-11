/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.models.stocklist;

import com.guysshare.managers.GlobalManager;

import android.util.Log;

/**
 * Created by shicong on 2016/9/8.
 */
public class StockURLJD implements StockURLManager.IStockURLProcess {
    private final static String URL_STOCK_JD = "http://apis.baidu"
            + ".com/wxlink/getequ/getequ?equTypeCD=A&field=totalShares,nonrestFloatShares,nonrestfloatA&";

    @Override
    public String getOneStockURL(int index) {
        return getStocksURL(new int[] {index});
    }

    @Override
    public String getStocksURL(int[] indexs) {
        if (indexs != null) {
            StringBuilder url = new StringBuilder(URL_STOCK_JD);
            String[] exts = new String[2];
            getURLExt(indexs,exts);
            url.append("secID=");
            url.append(exts[0]);
            url.append("&ticker=");
            url.append(exts[1]);
            return url.toString();
        }
        return null;
    }

    private void getURLExt(int[] indexs,String[] exts) {
        StringBuilder secIDs = new StringBuilder();
        StringBuilder tickers = new StringBuilder();

        for (int index:indexs) {
            String sid = GlobalManager.mGlobalStockList.get(index).getSID();
            if (sid.startsWith("00") || sid.startsWith("30")) {
                secIDs.append(sid + ".XSHE,");
                tickers.append(sid + ",");
            } else if (sid.startsWith("60")) {
                secIDs.append(sid + ".XSHG,");
                tickers.append(sid + ",");
            }
        }
        secIDs.deleteCharAt(secIDs.length() - 1);
        tickers.deleteCharAt(tickers.length() - 1);
        exts[0] = secIDs.toString();
        exts[1] = tickers.toString();
    }

    @Override
    public String getOneStockTimeLineURL(int index) {
        return "";
    }

    @Override
    public String getOneStockDayLineURL(int index) {
        return "";
    }

    @Override
    public String getOneStockWeekLineURL(int index) {
        return "";
    }

    @Override
    public String getOneStockMonthLineURL(int index) {
        return "";
    }
}
