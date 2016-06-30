/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.managers;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

/**
 * 暂时只管理文件的路径
 * 文件路径都是基于files/,.config/,cache/以及external/ 4种文件根目录的，文件以file开头，目录以dir开头。
 * 基于files/的文件的路径为FILE_DATA_XXX,基于files/的目录的路径为DIR_DATA_XXX.
 * 基于它们子目录的文件则为FILE_RELATIVE_XXX，其余都类似。
 *
 * @author shicong
 */
public class FilesManager {
    // FILES
    public static final String FILE_DATA_SYM_HOT = "hot_sym";
    public static final String DIR_CONFIG_EVENTS = ".events";
    public static final String FILE_CACHE_FSW = "fsw";
    public static final String DIR_EXTERNAL_FONTS = ".font/";

    /**
     * 单例实例
     */
    private static volatile FilesManager instance = null;

    /**
     * 提供文件根目录的接口
     */
    private IDirProvider dirProvider;

    private final IDirProvider defaultDirProvider = new DefaultDirProvider();

    /**
     * 构造方法私有化，避免被外部new实例
     */
    private FilesManager() {
        super();
    }

    /**
     * 单例方法
     */
    public static FilesManager getInstance() {
        if (null == instance) {
            synchronized (FilesManager.class) {
                if (null == instance) {
                    instance = new FilesManager();
                }
            }
        }
        return instance;
    }

    /**
     * 提供对于DirProvider，注意不要直接使用dirProvider，否则可能没有进行初始化
     *
     * @return DirProvider实例
     */
    private IDirProvider getDirProvider() {
        if (null == dirProvider) {
            synchronized (FilesManager.class) {
                if (null == dirProvider) {
                    init();
                }
            }
        }

        if (null != dirProvider) {
            return dirProvider;
        }
        return defaultDirProvider;
    }

    //    public void setDirProvider(IDirProvider dirProvider) {
    //        this.dirProvider = dirProvider;
    //    }

    private class ContextDirProvider implements IDirProvider {
        final Context context;
        final String filesDir;
        final String cacheDir;
        final String configDir;
        final String externalDir;

        private ContextDirProvider(Context context) {
            this.context = context.getApplicationContext();
            filesDir = context.getFilesDir().getPath() + File.separator;
            cacheDir = context.getCacheDir().getPath() + File.separator;
            configDir = filesDir + ".config" + File.separator;
            externalDir = Environment.getExternalStorageDirectory().getPath() + "/baidu/ime/";
        }

        @Override
        public String getFilesRootDir() {
            return filesDir;
        }

        @Override
        public String getCacheRootDir() {
            return cacheDir;
        }

        @Override
        public String getConfigRootDir() {
            return configDir;
        }

        @Override
        public String getExternalRootDir() {
            return externalDir;
        }
    }

    private class DefaultDirProvider implements IDirProvider {
        final String filesDir;
        final String cacheDir;
        final String configDir;
        final String externalDir;

        private DefaultDirProvider() {
            filesDir = "/data/data/" + "com.guysshare.finance" + "/files/";
            cacheDir = "/data/data/" + "com.guysshare.finance" + "/cache/";
            configDir = filesDir + ".config" + File.separator;
            externalDir = Environment.getExternalStorageDirectory().getPath() + "/threefinance";
        }

        @Override
        public String getFilesRootDir() {
            return filesDir;
        }

        @Override
        public String getCacheRootDir() {
            return cacheDir;
        }

        @Override
        public String getConfigRootDir() {
            return configDir;
        }

        @Override
        public String getExternalRootDir() {
            return externalDir;
        }
    }

    /**
     * 初始化
     */
    private void init() {
        if (null == dirProvider) {
            dirProvider = new ContextDirProvider(GlobalManager.mAppContext);
        }
    }

    /**
     * 返回完整的文件的路径
     *
     * @param path 相对files的完整路径
     *
     * @return 完整的文件的路径
     */
    public String getFilesPath(String path) {
        return getDirProvider().getFilesRootDir() + path;
    }

    /**
     * 是否是files的路径
     *
     * @param path 完整路径
     *
     * @return true:是files路径
     */
    public boolean isFilesPath(String path) {
        return !TextUtils.isEmpty(path) && path.startsWith(getDirProvider().getFilesRootDir());
    }

    /**
     * 返回files根目录，请不要在外面拼接路径
     *
     * @return files根目录
     * @deprecated
     */
    public String getFilesRootDir() {
        return getDirProvider().getFilesRootDir();
    }

    /**
     * 返回完整的文件的路径
     *
     * @param path 相对cache的完整路径
     *
     * @return 完整的文件的路径
     */
    public String getCachePath(String path) {
        return getDirProvider().getCacheRootDir() + path;
    }

    /**
     * 是否是cache的路径
     *
     * @param path 完整路径
     *
     * @return true:是cache路径
     */
    public boolean isCachePath(String path) {
        return !TextUtils.isEmpty(path) && path.startsWith(getDirProvider().getCacheRootDir());
    }

    /**
     * 返回cache的根目录，请不要在外面拼接路径
     *
     * @return cache的根目录
     * @deprecated
     */
    public String getCacheRootDir() {
        return getDirProvider().getCacheRootDir();
    }

    /**
     * 返回完整的文件的路径
     *
     * @param path 相对files/.config的完整路径
     *
     * @return 完整的文件的路径
     */
    public String getConfigPath(String path) {
        return getDirProvider().getConfigRootDir() + path;
    }

    /**
     * 返回files/.config的根目录，请不要在外面拼接路径
     *
     * @return files/.config的根目录
     * @deprecated
     */
    public String getConfigRootDir() {
        return getDirProvider().getConfigRootDir();
    }

    /**
     * 返回external的根目录，请不要在外面拼接路径
     *
     * @return external的根目录
     * @deprecated
     */
    public String getExternalRootDir() {
        return getDirProvider().getExternalRootDir();
    }

    /**
     * 返回完整的文件的路径
     *
     * @param path 相对external的完整路径
     *
     * @return 完整的文件的路径
     */
    public String getExternalPath(String path) {
        return getDirProvider().getExternalRootDir() + path;
    }

    /**
     * 是否是files的路径
     *
     * @param path 完整路径
     *
     * @return true:是files路径
     */
    public boolean isExternalPath(String path) {
        return !TextUtils.isEmpty(path)
                && (path.startsWith(Environment.getExternalStorageDirectory().getPath())
                            || path.startsWith("/sdcard/"));
    }
}
