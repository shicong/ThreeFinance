/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.controllers;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by shicong on 2016/7/19.
 */
public class AppMainFragmentAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> mFragmentList;

    public AppMainFragmentAdapter(FragmentManager fm,IViewPageInterface interfaceEntry) {
        super(fm);
        mFragmentList = interfaceEntry.getFragementList();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
