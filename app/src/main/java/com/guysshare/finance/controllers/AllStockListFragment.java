/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.controllers;

import java.util.ArrayList;

import com.guysshare.finance.OneStockActivity;
import com.guysshare.finance.models.stocklist.StockListAdapter;
import com.guysshare.managers.GlobalManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by shicong on 2016/7/19.
 */
public class AllStockListFragment extends Fragment {

    private StockListAdapter mStockListAdp;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView stocksListview = new ListView(container.getContext());
        stocksListview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mStockListAdp = new StockListAdapter(GlobalManager.mGlobalStockList);
        stocksListview.setAdapter(mStockListAdp);

        stocksListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), OneStockActivity.class);
                intent.putExtra("intent.key.stock.detail.pos", position);
                startActivity(intent);
            }
        });

        return stocksListview;
    }
}
