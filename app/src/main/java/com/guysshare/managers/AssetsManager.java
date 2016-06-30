/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.managers;

import java.io.InputStream;

import com.guysshare.publics.FileUtils;
import com.guysshare.publics.IOUtils;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * 对于assets里面资源的访问
 * 文件以FILE开头，目录以DIR开头，LANGUAGE目录下面的文件则为FILE_LANGUAGE_XXX。
 * @author zhazongxun
 */
public class AssetsManager {
    /**
     * 配置文件名
     */
    public static final String FILE_PREFERENCE_KEY = "preference_key.properties";
    public static final String DIR_IPT = "ipt/";

    /**
     * 判断assets目录下指定文件是否存在
     *
     * @param con
     * @param filename
     * @return
     */
    public static boolean exists(Context con, String filename) {
        InputStream is = null;
        try {
            is = FileUtils.sourceAssetsFile(con, filename, AssetManager.ACCESS_STREAMING);
            return is != null && is.available() > 0;
        } catch (Exception e) {

        } finally {
            IOUtils.closeQuietly(is);
        }
        return false;
    }


    public static byte[] load(Context con, String filename) {
        return load(con, filename, AssetManager.ACCESS_STREAMING);
    }

    /**
     * 从应用的assets目录里读取指定名字的资源
     *
     * @param con        上下文
     * @param filename   文件名
     * @param accessMode 参考AssetManager
     *
     * @return 字节数组
     */
    public static byte[] load(Context con, String filename, int accessMode) {
        try {
            return IOUtils.read(FileUtils.sourceAssetsFile(con, filename, accessMode));
        } catch (Exception e) {
        }
        return null;
    }
}
