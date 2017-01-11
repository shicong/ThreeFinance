/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.models.stocklist;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.guysshare.finance.R;
import com.guysshare.managers.GlobalManager;

import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by shicong on 2016/7/1.
 */
public class StockListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<StockBaseData> mListData;
    private Timer mUpdateTimer;
    private Handler mUpdateRefreshHandler;
    public StockListAdapter(ArrayList<StockBaseData> list){
        mInflater = LayoutInflater.from(GlobalManager.mAppContext);
        mListData = list;
        mUpdateRefreshHandler = new Handler();
        mUpdateRefreshHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StockListAdapter.this.notifyDataSetChanged();
                mUpdateRefreshHandler.postDelayed(this,3000);
            }
        }, 5000);
    }
    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null){
            mViewHolder = new ViewHolder();
                        convertView = mInflater.inflate(R.layout.stock_list_item,null);
            mViewHolder.mViewSname = (TextView) convertView.findViewById(R.id.id_stock_list_sname);
            mViewHolder.mViewSid = (TextView) convertView.findViewById(R.id.id_stock_list_sid);
            mViewHolder.mViewCurrprice = (TextView) convertView.findViewById(R.id.id_stock_list_currprice);
            mViewHolder.mViewPercent = (Button) convertView.findViewById(R.id.id_stock_list_percent);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        StockBaseData oneStockData = mListData.get(position);

        mViewHolder.mViewSname.setText(oneStockData.getName());
        mViewHolder.mViewSid.setText(oneStockData.getSID());

        if (oneStockData.getCurrPrice() <= 0){
            mViewHolder.mViewCurrprice.setText("--");
        }else {
            mViewHolder.mViewCurrprice.setText(oneStockData.getCurrPrice()+"");
        }

        if (oneStockData.getPercent() < 0){
            mViewHolder.mViewPercent.setBackgroundColor(Color.GREEN);
        }else {
            mViewHolder.mViewPercent.setBackgroundColor(Color.RED);
        }

        if (oneStockData.getCurrPrice() <= 0){
            mViewHolder.mViewPercent.setText("--");
        }else {
            mViewHolder.mViewPercent.setText(String.format("%.2f",oneStockData.getPercent()) + "%");
        }


        if (isTime2Update(getUpdateTime(oneStockData.getDate(),oneStockData.getTime()))){
                new StockDataManager(StockDataManager.DataType.TYPE_SINA).updateToGlobalStockDataList(position);
        }

        return convertView;
    }

    private class ViewHolder{
        TextView mViewSname;
        TextView mViewSid;
        TextView mViewCurrprice;
        Button mViewPercent;
    }

    /**
     * 得到上次更新的时间
     * @param date
     * @param time
     * @return
     */
    private long getUpdateTime(String date,String time){
        if (date == null || time == null){
            return 0;
        }
        try {
            SimpleDateFormat t1Formate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat t2Formate = new SimpleDateFormat("HH:mm:ss");
            long t1 = t1Formate.parse(date).getTime();
            long t2 = t2Formate.parse(time).getTime();
            return (t1 + t2);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * 确定是否更新数据
     * @param time
     * @return
     */
    private boolean isTime2Update(long time){
        if (time == 0){
            return true;
        }
        return (System.currentTimeMillis() - time) > 5000;
    }
}
