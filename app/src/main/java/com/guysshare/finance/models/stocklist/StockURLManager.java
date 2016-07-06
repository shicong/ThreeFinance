/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.models.stocklist;

/**
 * Created by shicong on 2016/7/6.
 */
public class StockURLManager {
    private final static String URL_STOCK = "http://hq.sinajs.cn/list=";

    public String getStockURL(String[] stockSids){
        if (stockSids != null){
            StringBuilder url = new StringBuilder(URL_STOCK);
            for (String sid:stockSids) {
                if (sid.startsWith("000") || sid.startsWith("300")){
                    url.append("sz" + sid + ",");
                }else if (sid.startsWith("600")){
                    url.append("sh" + sid + ",");
                }
            }
            url.deleteCharAt(url.length() - 1);
            return url.toString();
        }
        return null;
    }
}
