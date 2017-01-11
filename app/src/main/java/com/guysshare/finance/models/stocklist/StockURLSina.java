/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.models.stocklist;

import com.guysshare.managers.GlobalManager;

/**
 * Created by shicong on 2016/9/8.
 */
public class StockURLSina implements StockURLManager.IStockURLProcess {
    private final static String URL_STOCK_SINA = "http://hq.sinajs.cn/list=";
    @Override
    public String getOneStockURL(int index) {
        return getStocksURL(new int[] {index});
    }

    @Override
    public String getStocksURL(int[] indexs) {
        if (indexs != null) {
            StringBuilder url = new StringBuilder(URL_STOCK_SINA);
            for (int index : indexs) {
                url.append(getURLExt(index) + ",");
            }
            url.deleteCharAt(url.length() - 1);
            return url.toString();
        }
        return null;
    }

    private String getURLExt(int index) {
        String sid = GlobalManager.mGlobalStockList.get(index).getSID();
        if (sid.startsWith("00") || sid.startsWith("30")) {
            return "sz" + sid;
        } else if (sid.startsWith("60")) {
            return "sh" + sid;
        }
        return "";
    }

    @Override
    public String getOneStockTimeLineURL(int index) {
        String url = "http://image.sinajs.cn/newchart/min/n/00.gif";
        return url.replace("00", getURLExt(index));

    }

    @Override
    public String getOneStockDayLineURL(int index) {
        String url = "http://image.sinajs.cn/newchart/daily/n/00.gif";
        return url.replace("00", getURLExt(index));
    }

    @Override
    public String getOneStockWeekLineURL(int index) {
        String url = "http://image.sinajs.cn/newchart/weekly/n/00.gif";
        return url.replace("00", getURLExt(index));
    }

    @Override
    public String getOneStockMonthLineURL(int index) {
        String url = "http://image.sinajs.cn/newchart/monthly/n/00.gif";
        return url.replace("00", getURLExt(index));
    }
}
