/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.controllers;

import com.guysshare.finance.R;
import com.guysshare.finance.models.stocklist.StockBaseData;
import com.guysshare.finance.models.stocklist.StockDataManager;
import com.guysshare.finance.models.stocklist.StockURLManager;
import com.guysshare.managers.GlobalManager;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by shicong on 2016/7/22.
 */
public class OneStockDayLine extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ImageView img = new ImageView(container.getContext());

        int pos = getArguments().getInt("intent.key.stock.detail.line");
//        StockBaseData stockData = GlobalManager.mGlobalStockList.get(pos);
        String url = new StockURLManager(StockDataManager.DataType.TYPE_SINA).getOneStockDayLineURL(pos);
        Picasso.with(container.getContext()).load(url).into(img);

        return img;
    }
}
