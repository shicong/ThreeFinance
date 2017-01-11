/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.models.stocklist;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.guysshare.managers.GlobalManager;
import com.guysshare.publics.IOUtils;

import android.text.TextUtils;
import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shicong on 2016/9/8.
 */
public class StockDataSina implements StockDataManager.IStockDataProcess {


    @Override
    public void updateToGlobalStockDataList(final int[] indexs) {
        String url = new StockURLManager(StockDataManager.DataType.TYPE_SINA).getStocksURL(indexs);
        OkHttpClient mClient = GlobalManager.getDefaultOkHttpClient();
        Request.Builder mOkHttpBuild = new Request.Builder();
        Request mRequest = mOkHttpBuild.url(url).build();

        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                List<StockBaseData> tmpStockDatas = parseStocksData(body);
                updateGlobalStockDataList(indexs,tmpStockDatas);
                response.close();
            }
        });

    }

    @Override
    public void  updateToGlobalStockDataList(final int index){
        String url = new StockURLManager(StockDataManager.DataType.TYPE_SINA).getOneStockURL(index);
        OkHttpClient mClient = GlobalManager.getDefaultOkHttpClient();
        Request.Builder mOkHttpBuild = new Request.Builder();
        Request mRequest = mOkHttpBuild.url(url).build();

        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                StockBaseData tmpStockBaseData = parseOneStockData(body);
                updateGlobalStockDataList(index,tmpStockBaseData);
                response.close();
            }
        });

    }

    /**
     * 解析所有返回的数据
     *
     * @param rspDatas
     *
     * @return
     */
    private List<StockBaseData> parseStocksData(String rspDatas) throws IOException {
        final List<StockBaseData> tmpList = new ArrayList<StockBaseData>();
        IOUtils.readLines(new StringReader(rspDatas), new IOUtils.IOReadLinesCallback() {
            @Override
            public void onCallback(String line) throws IOException {
                if (!TextUtils.isEmpty(line)) {
                    StockBaseData tmpData = parseOneStockData(line);
                    if (tmpData != null) {
                        tmpList.add(tmpData);
                    }
                }
            }
        });
        return tmpList;
    }

    /**
     * 解析单条数据
     *
     * @param rspDate
     *
     * @return
     */
    private StockBaseData parseOneStockData(String rspDate) {
        if (rspDate == null) {
            return null;
        }
        StockBaseData tmpStockBaseData = new StockBaseData();
        int equalIndex = rspDate.indexOf("=");
        String sid = rspDate.substring(equalIndex - 6, equalIndex);
        String otherDatas = rspDate.substring(equalIndex + 2, rspDate.length() - 2);
        String otherDataArray[] = otherDatas.split(",");
        float[] buyCntArray =
                new float[] {Float.valueOf(otherDataArray[10]), Float.valueOf(otherDataArray[12]), Float
                        .valueOf(otherDataArray[14]), Float.valueOf(otherDataArray[16]),
                        Float.valueOf(otherDataArray[18])};
        float[] buyPriceArray =
                new float[] {Float.valueOf(otherDataArray[11]), Float.valueOf(otherDataArray[13]), Float
                        .valueOf(otherDataArray[15]), Float.valueOf(otherDataArray[17]),
                        Float.valueOf(otherDataArray[19])};
        float[] sellCntArray =
                new float[] {Float.valueOf(otherDataArray[20]), Float.valueOf(otherDataArray[22]), Float
                        .valueOf(otherDataArray[24]), Float.valueOf(otherDataArray[26]),
                        Float.valueOf(otherDataArray[28])};
        float[] sellPriceArray =
                new float[] {Float.valueOf(otherDataArray[21]), Float.valueOf(otherDataArray[23]), Float
                        .valueOf(otherDataArray[25]), Float.valueOf(otherDataArray[27]),
                        Float.valueOf(otherDataArray[29])};
        float openPrice = Float.valueOf(otherDataArray[1]);
        float closePrice = Float.valueOf(otherDataArray[2]);
        float currPrice = Float.valueOf(otherDataArray[3]);
        float percent = (currPrice - closePrice)*100/closePrice;
        tmpStockBaseData.setSID(sid)
                .setName(otherDataArray[0])
                .setOpenPrice(openPrice)
                .setClosePrice(closePrice)
                .setCurrPrice(currPrice)
                .setMaxPrice(Float.valueOf(otherDataArray[4]))
                .setMinPrice(Float.valueOf(otherDataArray[5]))
                .setDealCnt(Float.valueOf(otherDataArray[8]))
                .setDealGMV(Float.valueOf(otherDataArray[9]))
                .setBuyCnt(buyCntArray)
                .setBuyPrice(buyPriceArray)
                .setSellCnt(sellCntArray)
                .setSellPrice(sellPriceArray)
                .setDate(otherDataArray[30])
                .setTime(otherDataArray[31])
                .setPercent(percent);
        return tmpStockBaseData;
    }

    /**
     * 将数据更新到全局列表
     * @param index：单个index
     * @param data
     */
    private void updateGlobalStockDataList(int index ,StockBaseData data){
        GlobalManager.mGlobalStockList.get(index).setSID(data.getSID())
                .setName(data.getName())
                .setOpenPrice(data.getOpenPrice())
                .setClosePrice(data.getClosePrice())
                .setCurrPrice(data.getCurrPrice())
                .setMaxPrice(data.getMaxPrice())
                .setMinPrice(data.getMinPrice())
                .setDealCnt(data.getDealCnt())
                .setDealGMV(data.getDealGMV())
                .setBuyCnt(data.getBuyCnt())
                .setBuyPrice(data.getBuyPrice())
                .setSellCnt(data.getSellCnt())
                .setSellPrice(data.getSellPrice())
                .setDate(data.getDate())
                .setTime(data.getTime())
                .setPercent(data.getPercent());
    }

    /**
     * 将数据更新到全局列表
     * @param indexs：index列表
     * @param datalist
     */
    private void updateGlobalStockDataList(int[] indexs,List<StockBaseData> datalist){
        if ((indexs == null) || (datalist == null)){
            return;
        }

        for (int i = 0; i < indexs.length; i++){
            updateGlobalStockDataList(indexs[i],datalist.get(i));
        }
    }

}
