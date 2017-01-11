/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.controllers;

import java.util.ArrayList;

import com.guysshare.finance.models.stocklist.StockBaseData;

import android.support.v4.app.Fragment;

/**
 * Created by shicong on 2016/7/22.
 */
public interface IViewPageInterface {
    ArrayList<Fragment> getFragementList();
    StockBaseData getStockBaseData();
}
