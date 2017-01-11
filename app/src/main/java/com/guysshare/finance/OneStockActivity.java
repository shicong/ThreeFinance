/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance;

import java.util.ArrayList;

import com.guysshare.finance.controllers.AppMainFragmentAdapter;
import com.guysshare.finance.controllers.IViewPageInterface;
import com.guysshare.finance.controllers.OneStockDayLine;
import com.guysshare.finance.controllers.OneStockFragmentAdapter;
import com.guysshare.finance.controllers.OneStockMonthLine;
import com.guysshare.finance.controllers.OneStockTimeLine;
import com.guysshare.finance.controllers.OneStockWeekLine;
import com.guysshare.finance.models.stocklist.StockBaseData;
import com.guysshare.managers.GlobalManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by shicong on 2016/7/8.
 */
public class OneStockActivity extends FragmentActivity implements ViewPager.OnPageChangeListener,IViewPageInterface,View.OnClickListener{

    private StockBaseData mStockBaseData;
    private ArrayList<Fragment> mPageFragments;
    private LinearLayout mTitlesRoot;
    private ViewPager mMainPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_stock_detail);

        initDatas();

        initViews();


    }

    private void initDatas(){

        int pos = getIntent().getIntExtra("intent.key.stock.detail.pos",0);
        mStockBaseData = GlobalManager.mGlobalStockList.get(pos);

        mPageFragments = new ArrayList<>();
        Fragment tmpFragment;
        Bundle tmpBundle = new Bundle();
        tmpBundle.putInt("intent.key.stock.detail.line",pos);

        tmpFragment = new OneStockTimeLine();
        tmpFragment.setArguments(tmpBundle);
        mPageFragments.add(tmpFragment);

        tmpFragment = new OneStockDayLine();
        tmpFragment.setArguments(tmpBundle);
        mPageFragments.add(tmpFragment);

        tmpFragment = new OneStockWeekLine();
        tmpFragment.setArguments(tmpBundle);
        mPageFragments.add(tmpFragment);

        tmpFragment = new OneStockMonthLine();
        tmpFragment.setArguments(tmpBundle);
        mPageFragments.add(tmpFragment);

    }

    private void initViews(){

        mMainPages = (ViewPager) findViewById(R.id.id_one_stock_detail_pages);
        mMainPages.setAdapter(new OneStockFragmentAdapter(getSupportFragmentManager(),this));
        mMainPages.addOnPageChangeListener(this);
        mMainPages.setCurrentItem(0);

        mTitlesRoot = (LinearLayout) findViewById(R.id.id_one_stock_detail_pages_tile);
        mTitlesRoot.getChildAt(0).setBackgroundColor(Color.YELLOW);
        for (int i = 0; i < mPageFragments.size(); i++){
            mTitlesRoot.getChildAt(i).setOnClickListener(this);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mPageFragments.size(); i++){
            if (i == position){
                mTitlesRoot.getChildAt(i).setBackgroundColor(Color.YELLOW);
            }else {
                mTitlesRoot.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public ArrayList<Fragment> getFragementList() {
        return mPageFragments;
    }

    @Override
    public StockBaseData getStockBaseData() {
        return mStockBaseData;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.id_one_stock_detail_time_line:
                mMainPages.setCurrentItem(0);
                break;
            case R.id.id_one_stock_detail_day_line:
                mMainPages.setCurrentItem(1);
                break;
            case R.id.id_one_stock_detail_week_line:
                mMainPages.setCurrentItem(2);
                break;
            case R.id.id_one_stock_detail_month_line:
                mMainPages.setCurrentItem(3);
                break;
        }
    }
}
