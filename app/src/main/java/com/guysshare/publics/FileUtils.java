/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.publics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources.NotFoundException;

/**
 * 文件操作工具类
 * Created by chengyuan02 on 7/13 0013.
 */
public class FileUtils {

    private static final String TAG = "FileUtils";

    public static final int ONE_KB = 1024;

    public static final int ONE_MB = ONE_KB * ONE_KB;

    public static final int ONE_GB = ONE_KB * ONE_MB;

    private static final int FILE_COPY_BUFFER_SIZE = ONE_MB * 32;

    /**
     * 创建指定文件的输入流
     *
     * @param file
     *
     * @return 创建失败返回null
     */
    public static FileInputStream sourceFile(File file) {
        FileInputStream in = null;
        try {
            if (!file.exists()) {
                throw new FileNotFoundException("File '" + file + "' does not exist");
            }
            if (file.isDirectory()) {
                throw new FileNotFoundException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new FileNotFoundException("File '" + file + "' cannot be read");
            }
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            IOUtils.closeQuietly(in);
        }
        return in;
    }

    /**
     * 创建指定文件路径的输入流
     *
     * @param path
     *
     * @return 创建失败返回null
     */
    public static FileInputStream sourceFile(String path) {
        return sourceFile(new File(path));
    }

    /**
     * 创建指定文件的输出流
     *
     * @param file
     * @param append
     *
     * @return 创建失败返回null
     */
    public static FileOutputStream targetFile(File file, boolean append) {
        FileOutputStream out = null;
        try {
            if (file.exists()) {
                if (file.isDirectory()) {
                    throw new FileNotFoundException("File '" + file + "' exists but is a directory");
                }
                if (file.canWrite() == false) {
                    throw new FileNotFoundException("File '" + file + "' cannot be written to");
                }
            } else {
                File parent = file.getParentFile();
                if (parent != null) {
                    if (!parent.mkdirs() && !parent.isDirectory()) {
                        throw new FileNotFoundException("Directory '" + parent + "' could not be created");
                    }
                }
            }
            out = new FileOutputStream(file, append);
        } catch (FileNotFoundException e) {
            IOUtils.closeQuietly(out);
        }
        return out;
    }

    /**
     * 创建指定文件路径的输出流
     *
     * @param path
     * @param append
     *
     * @return 创建失败返回null
     */
    public static FileOutputStream targetFile(String path, boolean append) {
        return targetFile(new File(path), append);
    }

    /**
     * 创建<b>/data/data/包名 /files/文件名</b>下的输入流
     *
     * @param context
     * @param name
     *
     * @return 创建失败返回null
     *
     */
    public static FileInputStream sourcePrivateFile(Context context, String name) {
        FileInputStream in = null;
        try {
            in = context.openFileInput(name);
        } catch (FileNotFoundException e) {
            IOUtils.closeQuietly(in);
        }
        return in;
    }

    /**
     * 创建<b>/data/data/包名 /files/文件名</b>下的输出流
     *
     * @param context
     * @param name
     * @param mode
     *
     * @return 创建失败返回null
     *
     * @throws FileNotFoundException
     */
    public static FileOutputStream targetPrivateFile(Context context, String name, int mode)
            throws FileNotFoundException {
        FileOutputStream out = null;
        try {
            out = context.openFileOutput(name, mode);
        } catch (FileNotFoundException e) {
            IOUtils.closeQuietly(out);
        }
        return out;
    }

    /**
     * 创建assert目录下的输入流
     *
     * @param context
     * @param fileName
     * @param accessMode
     *
     * @return 创建失败返回null
     */
    public static InputStream sourceAssetsFile(Context context, String fileName, int accessMode) {
        InputStream in = null;
        try {
            in = context.getAssets().open(fileName, accessMode);
        } catch (IOException e) {
            IOUtils.closeQuietly(in);
        }
        return in;
    }

    /**
     * 创建assert目录下的输入流
     *
     * @param context
     * @param fileName
     *
     * @return 创建失败返回null
     */
    public static InputStream sourceAssetsFile(Context context, String fileName) {
        return sourceAssetsFile(context, fileName, AssetManager.ACCESS_STREAMING);
    }

    /**
     * 创建/res/raw目录下的资源输入流
     *
     * @param context
     * @param id
     *
     * @return 创建失败返回null
     */
    public static InputStream sourceRawResource(Context context, int id) {
        InputStream in = null;
        try {
            in = context.getResources().openRawResource(id);
        } catch (NotFoundException e) {
            IOUtils.closeQuietly(in);
        }
        return in;
    }

    /**
     * 创建新目录
     *
     * @param directory
     *
     * @return 目录已存在或者目录创建失败返回false，否则返回true
     */
    public static boolean mkdirs(File directory) {
        if (directory.exists()) {
            if (directory.isDirectory()) {
                return false;
            }

            if (!directory.delete()) {
                return false;
            }
        }

        return directory.mkdirs();
    }

    /**
     * 创建新文件
     *
     * @param file
     * @return 文件已存在或者文件创建失败返回false，否则返回true
     */
    public static boolean createNewFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                return false;
            }

            if (!file.delete()) {
                return false;
            }
        }

        File directory = file.getParentFile();
        if (!directory.exists()) {
            if (!mkdirs(directory)) {
                return false;
            }
        }

        try {
            return file.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 重命名文件（包括纯文件及目录）
     *
     * @param from              源文件
     * @param to                目标文件
     * @param deleteSource      是否在重命名成功后删除源文件
     * @param deleteDestination 是否在重命名前删除目标文件
     *
     * @return 重命名操作是否成功
     */
    public static boolean renameTo(File from, File to, boolean deleteSource, boolean deleteDestination) {
        if (deleteDestination) {
            if (!delete(to)) {
                return false;
            }
        }

        if (!from.renameTo(to)) {
            return false;
        }

        if (deleteSource) {
            if (!delete(from)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 删除文件，如果是目录，会递归的删除其子目录及文件
     *
     * @param file
     *
     * @return 当文件不存在或文件删除成功返回true，其他情况返回false
     */
    public static boolean delete(File file) {
        if (!file.exists()) {
            return true;
        }

        if (file.isDirectory()) {
            cleanDirectory(file);
        }

        return file.delete();
    }

    /**
     * 清空目录下的文件，但不删除目录本身
     *
     * @param directory 目录文件
     *
     * @return 目录存在且清理成功返回true，其他情况返回false
     */
    public static boolean cleanDirectory(File directory) {
        if (!directory.exists()) {
            return false;
        }

        if (!directory.isDirectory()) {
            return false;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return true;
        }

        boolean result = true;
        for (File file : files) {
            if (!delete(file)) {
                result = false;
            }
        }
        return result;
    }

    /**
     * 重命名文件
     *
     * @param source 源文件
     * @param target 目标文件
     *
     * @return
     */
    public static boolean rename(File source, File target) {
        if (!source.exists()) {
            return false;
        }

        delete(target);
        return source.renameTo(target);
    }

    /**
     * 计算对应文件（包括目录）的大小
     *
     * @param file 文件
     *
     * @return
     */
    public static long sizeOf(File file) {
        if (!file.exists()) {
            return 0;
        }

        if (file.isDirectory()) {
            return sizeOfDirectory(file);
        } else {
            return file.length();
        }
    }

    /**
     * 递归的计算目录下所有文件的大小
     *
     * @param directory 目录文件
     *
     * @return
     */
    private static long sizeOfDirectory(File directory) {
        if (!directory.exists()) {
            return 0L;
        }

        if (!directory.isDirectory()) {
            return 0L;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return 0L;
        }

        long size = 0L;
        for (File file : files) {
            size += sizeOf(file);
        }

        return size;
    }

    /**
     * 复制文件（包括目录）
     *
     * @param srcPath 源文件路径
     * @param destPath 目标文件路径
     * @throws IOException
     */
    public static void copy(String srcPath, String destPath) throws IOException {
        copy(new File(srcPath), new File(destPath));
    }

    /**
     * 复制文件（包括目录）
     *
     * @param srcFile 源文件
     * @param destFile 目标文件
     *
     * @throws IOException
     */
    public static void copy(File srcFile, File destFile) throws IOException {
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        }
        File parentFile = destFile.getParentFile();
        if (parentFile != null) {
            if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IOException("Destination '" + parentFile + "' directory cannot be created");
            }
        }
        if (destFile.exists() && !destFile.canWrite()) {
            throw new IOException("Destination '" + destFile + "' exists but is read-only");
        }
        if (srcFile.isDirectory()) {
            doCopyDirectory(srcFile, destFile);
        } else {
            doCopyFile(srcFile, destFile);
        }
    }

    /**
     * 复制文件
     *
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    private static void doCopyFile(File srcFile, File destFile) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0;
            long count = 0;
            while (pos < size) {
                count = size - pos > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : size - pos;
                pos += output.transferFrom(input, pos, count);
            }
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(fis);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
        }
    }

    /**
     * 复制目录
     *
     * @param srcDir
     * @param destDir
     * @throws IOException
     */
    private static void doCopyDirectory(File srcDir, File destDir) throws IOException {
        File[] srcFiles = srcDir.listFiles();
        if (srcFiles == null) {
            throw new IOException("Failed to list contents of " + srcDir);
        }
        if (destDir.exists()) {
            if (!destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' exists but is not a directory");
            }
        } else {
            if (!destDir.mkdirs() && !destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' directory cannot be created");
            }
        }
        if (!destDir.canWrite()) {
            throw new IOException("Destination '" + destDir + "' cannot be written to");
        }
        for (File srcFile : srcFiles) {
            File dstFile = new File(destDir, srcFile.getName());
            if (srcFile.isDirectory()) {
                doCopyDirectory(srcFile, dstFile);
            } else {
                doCopyFile(srcFile, dstFile);
            }
        }
    }
}
