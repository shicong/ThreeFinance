/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.controllers;

import com.guysshare.finance.OneStockActivity;
import com.guysshare.finance.StockTradeActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by shicong on 2016/7/19.
 */
public class DIYStockListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        LinearLayout RootLayout = new LinearLayout(container.getContext());
        RootLayout.setOrientation(LinearLayout.VERTICAL);
        RootLayout.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));

        Button button1 = new Button(container.getContext());
        button1.setText("Button1");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), StockTradeActivity.class);
                startActivity(intent);


            }
        });

        RootLayout.addView(button1);
        return RootLayout;
    }
}
