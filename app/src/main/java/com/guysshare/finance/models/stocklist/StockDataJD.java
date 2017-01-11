/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.models.stocklist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guysshare.managers.GlobalManager;

import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shicong on 2016/9/8.
 */
public class StockDataJD implements StockDataManager.IStockDataProcess {
    @Override
    public void updateToGlobalStockDataList(final int index) {
        String url = new StockURLManager(StockDataManager.DataType.TYPE_JD).getOneStockURL(index);
        Log.e("shicong","jd url = " + url);
        OkHttpClient mClient = GlobalManager.getDefaultOkHttpClient();
        Request.Builder mOkHttpBuild = new Request.Builder();
        mOkHttpBuild.addHeader("apikey","1ef01ab3e82b1cb24730a42827a0e2b3");
        Request mRequest = mOkHttpBuild.url(url).build();

        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.e("shicong","jd = " + body);
                StockBaseData tmpStockBaseData = parseOneStockData(body);
                updateGlobalStockDataList(index,tmpStockBaseData);
                response.close();
            }
        });

    }

    @Override
    public void updateToGlobalStockDataList(final int[] indexs) {
        String url = new StockURLManager(StockDataManager.DataType.TYPE_JD).getStocksURL(indexs);
        Log.e("shicong","jd urls = " + url);
        OkHttpClient mClient = GlobalManager.getDefaultOkHttpClient();
        Request.Builder mOkHttpBuild = new Request.Builder();
        mOkHttpBuild.addHeader("apikey","1ef01ab3e82b1cb24730a42827a0e2b3");
        Request mRequest = mOkHttpBuild.url(url).build();

        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.e("shicong","jds = " + body);
                List<StockBaseData> tmpStockDatalist = parseStocksData(body);
                updateGlobalStockDataList(indexs,tmpStockDatalist);
                response.close();
            }
        });

    }

    /**
     * 对单条数据进行解析
     * @param rspDate
     * @return
     */
    private StockBaseData parseOneStockData(String rspDate) {
        List<StockBaseData> tmpDataList = parseStocksData(rspDate);
        if (tmpDataList != null && tmpDataList.size() > 0){
            return tmpDataList.get(0);
        }
        return null;
    }

    /**
     * 解析一次性返回的所有数据
     * @param rspData
     * @return
     */
    private List<StockBaseData> parseStocksData(String rspData){

        if (rspData == null){
            return null;
        }

        try {
            JSONObject rspRoot = new JSONObject(rspData);
            String code = rspRoot.optString("code");
            if (!code.equals("10000")){
                return null;
            }

            JSONObject result = rspRoot.optJSONObject("result");
            if (result == null){
                return null;
            }

            int retCode = result.optInt("retCode");
            if (retCode != 1){
                return null;
            }

            JSONArray data = result.optJSONArray("data");
            if (data == null){
                return null;
            }

            List<StockBaseData> tmpDataList = new ArrayList<>();
            for (int i = 0; i < data.length(); i++){
                JSONObject detail = data.optJSONObject(i);
                tmpDataList.add(parseOneStockDetailData(detail));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * 解析单条详细数据
     * @param detail
     * @return
     */
    private StockBaseData parseOneStockDetailData(JSONObject detail){
        if (detail == null){
            return null;
        }

        StockBaseData tmpData = new StockBaseData();
        tmpData .setTotalShares(detail.optDouble("totalShares"))
                .setNonrestFloatA(detail.optDouble("nonrestfloatA"))
                .setNonrestFloatShares(detail.optDouble("nonrestFloatShares"));
        return tmpData;
    }


    /**
     * 更新单条数据
     * @param index
     * @param data
     */
    private void updateGlobalStockDataList(int index ,StockBaseData data){
        GlobalManager.mGlobalStockList.get(index).setSID(data.getSID())
                .setTotalShares(data.getTotalShares())
                .setNonrestFloatA(data.getNonrestFloatA())
                .setNonrestFloatShares(data.getNonrestFloatShares());
    }
    /**
     * 更新多条数据
     * @param index
     * @param data
     */
    private void updateGlobalStockDataList(int[] indexs ,List<StockBaseData> datas){
        if ((indexs == null) || (datas == null)){
            return;
        }

        for (int i = 0; i < indexs.length; i++){
            updateGlobalStockDataList(indexs[i],datas.get(i));
        }
    }
}
