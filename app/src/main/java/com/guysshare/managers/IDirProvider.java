/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.managers;

/**
 * 提供文件目录根目录的接口
 * @author zhazongxun
 */
public interface IDirProvider {
    /**
     * 获得files目录
     * @return files根目录
     */
    String getFilesRootDir();

    /**
     * 获得cache根目录
     * @return cache根目录
     */
    String getCacheRootDir();

    /**
     * 获得files/.config目录
     * @return files根目录
     */
    String getConfigRootDir();

    /**
     * 获得files/.config目录
     * @return files根目录
     */
    String getExternalRootDir();
}
