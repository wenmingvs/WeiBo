package com.wenming.weiswift.app.common;

import android.content.Context;

import com.wenming.weiswift.common.file.exception.NotEnoughSpaceException;
import com.wenming.weiswift.common.file.exception.SDCardStateException;
import com.wenming.weiswift.common.file.manger.FileManager;
import com.wenming.weiswift.common.file.option.GlobalFileOptions;
import com.wenming.weiswift.utils.SDCardUtils;

import java.io.File;

/**
 * Created by wenmingvs on 2017/8/14.
 */

public class AppDirManager {
    private static final String COMPANY_NAME = "wenming";
    private static final String PROJECT_NAME = "WeiSwift";
    public static final long NOT_ENOUGH_SPACE_LINE = 50 * 1024 * 1024;

    public static void initFileManager(Context context) {
        GlobalFileOptions options = new GlobalFileOptions
                .Builder()
                .setCompanyName(COMPANY_NAME)
                .setProjectName(PROJECT_NAME)
                .setNotEnoughSpaceLeftLine(NOT_ENOUGH_SPACE_LINE)
                .build();
        FileManager.getInstance().init(context, options);
    }

    /**
     * 用户文件存放目录，目前用于存放照片和视频，可以在相册app中看到（后面可能用于接收的文件的存放）
     * <p>
     * path = sdcard/Android/com.xxx/Photo
     *
     * @return 返回相册图片的路径
     * @throws SDCardStateException
     * @throws NotEnoughSpaceException
     */
    public static String getPhotoDir() throws SDCardStateException, NotEnoughSpaceException {
        return getSDCardDir("Photo");
    }

    /**
     * 用户目录
     * <p>
     * path = sdcard/Android/{package name}/user/{uid}/name
     *
     * @return 用户文件路径根目录
     */
    public static String getUserDir(long uid, String name) throws SDCardStateException, NotEnoughSpaceException {
        String userDir = "user" + File.separator + String.valueOf(uid) + File.separator + name;
        return getSandBoxFileDir(userDir);
    }

    /**
     * @return 获取外部沙盒目录，若外部沙盒不存在则取内部沙盒目录
     */
    public static String getSandBoxCacheDir() throws SDCardStateException, NotEnoughSpaceException {
        String cacheDir;
        if (SDCardUtils.isSdAvailable()) {
            cacheDir = FileManager.getInstance().getExternalSandBoxCacheDir();
        } else {
            cacheDir = FileManager.getInstance().getSandBoxCacheDir();
        }

        if (!cacheDir.endsWith(File.separator)) {
            cacheDir += File.separator;
        }
        return cacheDir;
    }

    /**
     * @return cache/name目录。获取外部沙盒目录，若外部沙盒不存在则取内部沙盒目录
     */
    public static String getSandBoxCacheDir(String name) throws SDCardStateException, NotEnoughSpaceException {
        String dir;
        if (SDCardUtils.isSdAvailable()) {
            dir = FileManager.getInstance().getExternalSandBoxCacheDir(name);
        } else {
            dir = FileManager.getInstance().getSandBoxCacheDir(name);
        }

        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        return dir;
    }

    /**
     * 用于一些文件，但不用暴露给用户的文件存放，如emoticon，用户头像
     */
    public static String getSandBoxFileDir(String name) throws SDCardStateException, NotEnoughSpaceException {
        String dir;
        if (SDCardUtils.isSdAvailable()) {
            dir = FileManager.getInstance().getExternalSandBoxCacheDir(name);
        } else {
            dir = FileManager.getInstance().getSandBoxFilesDir(name);
        }

        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        return dir;
    }

    /**
     * @param name 目录名
     * @return 位于SDCard/meitu/shanliao/name的绝对路径
     */
    public static String getSDCardDir(String name) throws SDCardStateException, NotEnoughSpaceException {
        String dir;
        if (SDCardUtils.isSdAvailable()) {
            dir = FileManager.getInstance().getSDCardDir(name);
        } else {
            dir = FileManager.getInstance().getSandBoxFilesDir(name);
        }

        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        return dir;
    }

    /**
     * 该方法不能随意引用，目前场景是发生异常但必须要一个路径时调用
     *
     * @return sandbox cache dir.
     */
    public static String getErrorOccurTempDir() {
        String dir = FileManager.getInstance().getSandBoxCacheDir();
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        return dir;
    }
}