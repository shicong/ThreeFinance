/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.finance;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by shicong on 2016/7/28.
 */
public class StockTradeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout RootLayout = new LinearLayout(this);
        RootLayout.setOrientation(LinearLayout.VERTICAL);
        RootLayout.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));

        Button button1 = new Button(this);
        button1.setText("联系人和定位权限申请");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23){
                    int permis = checkSelfPermission(Manifest.permission.READ_CONTACTS);
                    checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permis != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission
                                .WRITE_CONTACTS,Manifest.permission.ACCESS_FINE_LOCATION},99);
                    }
                }
            }
        });


        Button button2 = new Button(this);
        button2.setText("读取联系人");
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cur = null;
                try {
                    cur =
                            getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new
                                    String[] { "_id",
                                    ContactsContract.Contacts.DISPLAY_NAME }, null, null, null);
                    if ((cur != null) && cur.moveToFirst()) {
                        do {
                            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        } while (cur.moveToNext());
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    if (cur != null) {
                        cur.close();
                        cur = null;
                    }
                }
            }
        });



        Button button3 = new Button(this);
        button3.setText("SD卡权限申请");
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23){
                    int permis = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permis != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                    }
                }
            }
        });

        Button button4 = new Button(this);
        button4.setText("写SD卡");
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f1 = Environment.getExternalStorageDirectory();
                File f2 = new File(f1.getAbsolutePath() + "/baidu.txt");
                File f3 = getExternalFilesDir(null);
                File f4 = new File(f3.getAbsolutePath() + "/baidu.txt");
                Log.e("shicong","f1 = " + f1.getAbsolutePath() + "\n" + "f2 = " + f2.getAbsolutePath() +
                        "\nfile3 =" + f3.getAbsolutePath() + "\nf4 = " + f4.getAbsolutePath());
                try {
                    f2.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(),f2.getAbsolutePath()+"!!!创建失败!!!",Toast.LENGTH_LONG).show();
                }

                try {
                    f4.createNewFile();
                    Toast.makeText(getApplication(),f4.getAbsolutePath()+"!!!创建成功!!!",Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        Button button5 = new Button(this);
        button5.setText("读取系统信息权限申请");
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23){
                    int permis = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
                    if (permis != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},101);
                    }
                }
            }
        });

        Button button6 = new Button(this);
        button6.setText("读取系统信息——imei");
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String imei = mTelephonyMgr.getDeviceId();
                Toast.makeText(getApplication(),"imei：" + imei,Toast.LENGTH_LONG).show();
            }
        });

        Button button7 = new Button(this);
        button7.setText("读取系统信息——已安装应用列表");
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PackageInfo> alist = getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);
                Toast.makeText(getApplication(),"安装列表数量：" + alist.size(),Toast.LENGTH_LONG).show();
            }
        });

        RootLayout.addView(button1);
        RootLayout.addView(button2);
        RootLayout.addView(button3);
        RootLayout.addView(button4);
        RootLayout.addView(button5);
        RootLayout.addView(button6);
        RootLayout.addView(button7);

        setContentView(RootLayout);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        Log.e("shicong","recode = " + requestCode );
        if (permissions != null && permissions.length > 0){
            for (String item:permissions
                    ) {
                Log.e("shicong ","permison = " + item);
            }

            for (int x:grantResults
                    ) {
                Log.e("shicong ","res = " + x);
            }
        }

    }
}
