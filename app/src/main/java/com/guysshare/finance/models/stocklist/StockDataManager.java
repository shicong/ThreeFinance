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
import com.guysshare.publics.IOUtils;

import android.text.TextUtils;
import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shicong on 2016/7/6.
 */
public class StockDataManager {

    public StockDataManager(){

    }

    public List<StockBaseData> getCurrStockDatas(int cnt) throws IOException {
        String[] sids =  DBManager.getInstance().getTopXSidsFromTotalList(cnt);
        String url = new StockURLManager().getStockURL(sids);
        OkHttpClient mClient = new OkHttpClient();
        Request.Builder mOkHttpBuild = new Request.Builder();
        Request mRequest = mOkHttpBuild.url(url).build();
        Response mResponse = null;
        mResponse = mClient.newCall(mRequest).execute();
        if (mResponse == null){
            return null;
        }
        String body = mResponse.body().string();
        final List<StockBaseData> tmpList = new ArrayList<StockBaseData>();
        IOUtils.readLines(new StringReader(body), new IOUtils.IOReadLinesCallback() {
            @Override
            public void onCallback(String line) throws IOException {
                if (!TextUtils.isEmpty(line)) {
                    StockBaseData tmpData = getOneStockData(line);
                    if (tmpData != null){
                        tmpList.add(tmpData);
                    }
                }
            }
        });

        return tmpList;
    }

    private StockBaseData getOneStockData(String data){
        if (data == null){
            return null;
        }
        StockBaseData tmpStockBaseData = new StockBaseData();

        int equalIndex = data.indexOf("=");
        String sid = data.substring(equalIndex - 6,equalIndex);
        String otherDatas = data.substring(equalIndex + 2,data.length() - 2);
        String otherDataArray[] = otherDatas.split(",");
        float[] buyCntArray = new float[]{Float.valueOf(otherDataArray[10]),Float.valueOf(otherDataArray[12]),Float
                .valueOf(otherDataArray[14]),Float.valueOf(otherDataArray[16]),Float.valueOf(otherDataArray[18])};
        float[] buyPriceArray = new float[]{Float.valueOf(otherDataArray[11]),Float.valueOf(otherDataArray[13]),Float
                .valueOf(otherDataArray[15]),Float.valueOf(otherDataArray[17]),Float.valueOf(otherDataArray[19])};
        float[] sellCntArray = new float[]{Float.valueOf(otherDataArray[20]),Float.valueOf(otherDataArray[22]),Float
                .valueOf(otherDataArray[24]),Float.valueOf(otherDataArray[26]),Float.valueOf(otherDataArray[28])};
        float[] sellPriceArray = new float[]{Float.valueOf(otherDataArray[21]),Float.valueOf(otherDataArray[23]),Float
                .valueOf(otherDataArray[25]),Float.valueOf(otherDataArray[27]),Float.valueOf(otherDataArray[29])};
        tmpStockBaseData.setSID(sid)
                        .setName(otherDataArray[0])
                        .setOpenPrice(Float.valueOf(otherDataArray[1]))
                        .setClosePrice(Float.valueOf(otherDataArray[2]))
                        .setCurrPrice(Float.valueOf(otherDataArray[3]))
                        .setMaxPrice(Float.valueOf(otherDataArray[4]))
                        .setMinPrice(Float.valueOf(otherDataArray[5]))
                        .setDealCnt(Float.valueOf(otherDataArray[8]))
                        .setDealGMV(Float.valueOf(otherDataArray[9]))
                        .setBuyCnt(buyCntArray)
                        .setBuyPrice(buyPriceArray)
                        .setSellCnt(sellCntArray)
                        .setSellPrice(sellPriceArray)
                        .setDate(otherDataArray[30])
                        .setTime(otherDataArray[31]);
        return tmpStockBaseData;
    }


}
