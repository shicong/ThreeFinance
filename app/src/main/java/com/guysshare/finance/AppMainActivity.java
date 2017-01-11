/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance;

import java.util.ArrayList;

import com.guysshare.finance.controllers.AllStockListFragment;
import com.guysshare.finance.controllers.AppMainFragmentAdapter;
import com.guysshare.finance.controllers.DIYStockListFragment;
import com.guysshare.finance.controllers.IViewPageInterface;
import com.guysshare.finance.controllers.ModuleRepoFragment;
import com.guysshare.finance.models.stocklist.StockBaseData;
import com.guysshare.finance.models.stocklist.StockDataManager;
import com.guysshare.finance.models.stocklist.StockURLManager;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class AppMainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener,IViewPageInterface,
        View.OnClickListener {

    private ArrayList<Fragment> mPageFragments;
    private LinearLayout mMainPageTitles;
    private ViewPager mMainPages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appmain);
        initDatas();
        initViews();
        updateDBData();
    }

    private void initDatas(){
        mPageFragments = new ArrayList<Fragment>();
        mPageFragments.add(new DIYStockListFragment());
        mPageFragments.add(new AllStockListFragment());
        mPageFragments.add(new ModuleRepoFragment());
    }

    private void initViews(){
        mMainPages = (ViewPager) findViewById(R.id.id_appmain_pages);
        mMainPages.setAdapter(new AppMainFragmentAdapter(getSupportFragmentManager(),this));
        mMainPages.addOnPageChangeListener(this);
        mMainPages.setCurrentItem(0);


        mMainPageTitles = (LinearLayout) findViewById(R.id.id_appmain_titles);
        mMainPageTitles.getChildAt(0).setBackgroundColor(Color.YELLOW);
        for (int i = 0; i < mPageFragments.size(); i++){
            mMainPageTitles.getChildAt(i).setOnClickListener(this);
        }
    }

    private void updateDBData(){
        new StockDataManager(StockDataManager.DataType.TYPE_JD).updateToGlobalStockDataList(new int[]{0,1,2000});
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mPageFragments.size(); i++){
            if (i == position){
                mMainPageTitles.getChildAt(i).setBackgroundColor(Color.YELLOW);
            }else {
                mMainPageTitles.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
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
        return null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.id_appmain_page_diy:
                mMainPages.setCurrentItem(0);
                break;
            case R.id.id_appmain_page_all:
                mMainPages.setCurrentItem(1);
                break;
            case R.id.id_appmain_page_module:
                mMainPages.setCurrentItem(2);
                break;
        }
    }
}
