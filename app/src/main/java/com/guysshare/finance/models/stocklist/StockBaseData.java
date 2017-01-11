/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.models.stocklist;

/**
 * Created by shicong on 2016/7/1.
 */
public class StockBaseData {

    private String mSID;
    private String mName;
    private float mOpenPrice;
    private float mClosePrice;
    private float mCurrPrice;
    private float mMaxPrice;
    private float mMinPrice;
    private float[] mBuyPrice = new float[5];
    private float[] mBuyCnt = new float[5];
    private float[] mSellPrice = new float[5];
    private float[] mSellCnt = new float[5];
    private float mDealCnt;
    private float mDealGMV;
    private String mDate;
    private String mTime;

    private Double mTotalShares;
    private Double mNonrestFloatShares;
    private Double mNonrestFloatA;

    private int mIndex;
    private float mPercent;
    private String mURL;


    public StockBaseData setIndex(int index){
        mIndex = index;
        return this;
    }
    public int getIndex(){
        return mIndex;
    }

    public StockBaseData setSID(String sid){
        mSID = sid;
        return this;
    }
    public String getSID(){
        return mSID;
    }

    public StockBaseData setName(String name){
        mName = name;
        return this;
    }
    public String getName(){
        return mName;
    }

    public StockBaseData setOpenPrice(float price){
        mOpenPrice = price;
        return this;
    }
    public float getOpenPrice(){
        return mOpenPrice;
    }

    public StockBaseData setClosePrice(float price){
        mClosePrice = price;
        return this;
    }
    public float getClosePrice(){
        return mClosePrice;
    }

    public StockBaseData setMaxPrice(float price){
        mMaxPrice = price;
        return this;
    }
    public float getMaxPrice(){
        return mMaxPrice;
    }

    public StockBaseData setMinPrice(float price){
        mMinPrice = price;
        return this;
    }
    public float getMinPrice(){
        return mMinPrice;
    }

    public StockBaseData setBuyPrice(float[] priceArray){
        mBuyPrice = priceArray;
        return this;
    }
    public float[] getBuyPrice(){
        return mBuyPrice;
    }

    public StockBaseData setBuyCnt(float[] cntArray){
        mBuyCnt = cntArray;
        return this;
    }
    public float[] getBuyCnt(){
        return mBuyCnt;
    }

    public StockBaseData setSellPrice(float[] priceArray){
        mSellPrice = priceArray;
        return this;
    }
    public float[] getSellPrice(){
        return mSellPrice;
    }

    public StockBaseData setSellCnt(float[] cntArray){
        mSellCnt = cntArray;
        return this;
    }
    public float[] getSellCnt(){
        return mSellCnt;
    }

    public StockBaseData setDealCnt(float cnt){
        mDealCnt = cnt;
        return this;
    }
    public float getDealCnt(){
        return mDealCnt;
    }

    public StockBaseData setDealGMV(float gmv){
        mDealGMV = gmv;
        return this;
    }
    public float getDealGMV(){
        return mDealGMV;
    }

    public StockBaseData setDate(String date){
        mDate = date;
        return this;
    }
    public String getDate(){
        return mDate;
    }

    public StockBaseData setTime(String time){
        mTime = time;
        return this;
    }
    public String getTime(){
        return mTime;
    }

    public StockBaseData setCurrPrice(float price){
        mCurrPrice = price;
        return this;
    }
    public float getCurrPrice(){
        return mCurrPrice;
    }

    public StockBaseData setPercent(float percent){
        mPercent = percent;
        return this;
    }
    public float getPercent(){
        return mPercent;
    }

    public StockBaseData setURL(String url){
        mURL = url;
        return this;
    }
    public String getURL(){
        return mURL;
    }

    public StockBaseData setTotalShares(Double totalShares){
        mTotalShares = totalShares;
        return this;
    }
    public Double getTotalShares(){
        return mTotalShares;
    }

    public StockBaseData setNonrestFloatShares(Double nonrestFloatShares){
        mNonrestFloatShares = nonrestFloatShares;
        return this;
    }
    public Double getNonrestFloatShares(){
        return mNonrestFloatShares;
    }

    public StockBaseData setNonrestFloatA(Double nonrestFloatA){
        mNonrestFloatA = nonrestFloatA;
        return this;
    }
    public Double getNonrestFloatA(){
        return mNonrestFloatA;
    }

}
