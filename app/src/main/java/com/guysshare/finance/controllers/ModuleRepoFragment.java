/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.guysshare.finance.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * Created by shicong on 2016/7/21.
 */
public class ModuleRepoFragment extends Fragment {

    private String[] mTitleList;
    private String[] mContentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView modules = new ListView(container.getContext());
        modules.setAdapter(new SimpleAdapter(container.getContext(),getModulsList(),android.R.layout
                .simple_list_item_2,new
                String[]{"title","content"},new int[]{android.R.id.text1,android.R.id.text2}));
        return modules;
    }

    private ArrayList<HashMap<String,String>> getModulsList(){
        ArrayList<HashMap<String,String>> moduleList = new ArrayList<HashMap<String,String>>();
        for (int i = 0; i < mTitleList.length; i++){
            HashMap<String,String> map = new HashMap<>();
            map.put("title",mTitleList[i]);
            map.put("content",mContentList[i]);
            moduleList.add(map);
        }
        return moduleList;
    }

    private void initDatas(){
        mTitleList = getResources().getStringArray(R.array.module_titles);
        mContentList = getResources().getStringArray(R.array.module_contents);
    }
}
