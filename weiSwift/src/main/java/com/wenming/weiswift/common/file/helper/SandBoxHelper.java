package com.wenming.weiswift.common.file.helper;

import android.content.Context;

import com.wenming.weiswift.common.file.exception.NotEnoughSpaceException;
import com.wenming.weiswift.common.file.exception.SDCardStateException;
import com.wenming.weiswift.common.file.option.GlobalFileOptions;

import java.io.File;

/**
 * func: 沙盒目录管理器
 * Created by Li WenTao on 2016/9/24 11:08
 */
public class SandBoxHelper {
    private static final String EXTERNAL_CACHE_DIR_EXCEPTION_MSG = "ExternalCacheDir = null, is there no space left on device?";
    private static final String EXTERNAL_FILES_DIR_EXCEPTION_MSG = "ExternalFilesDir = null, is there no space left on device?";
    private static final String EXTERNAL_NO_SPACE_EXCEPTION_MSG = "External no space left in SDCard.";

    private static final String DEFAULT_DATA_PATH_RELATIVE = "/data/data/";
    private static final String DEFAULT_CACHE_PATH_RELATIVE = "/cache";
    private static final String DEFAULT_FILES_PATH_RELATIVE = "/files";

    private Context mContext;

    public SandBoxHelper(Context context) {
        mContext = context;
    }

    public String getCacheDir() {
        File cacheDir = mContext.getCacheDir();
        if (null != cacheDir) {
            return cacheDir.getAbsolutePath();
        } else {
            return DEFAULT_DATA_PATH_RELATIVE + mContext.getPackageName() + DEFAULT_CACHE_PATH_RELATIVE;
        }
    }

    public String getCacheDir(String name) {
        if (!name.startsWith(File.separator)) {
            name = File.separator + name;
        }

        File file = new File(getCacheDir() + File.separator + name);
        file.mkdirs();
        return file.getAbsolutePath();
    }

    public String getFilesDir() {
        File filesDir = mContext.getFilesDir();
        if (null != filesDir) {
            return filesDir.getAbsolutePath();
        } else {
            return DEFAULT_DATA_PATH_RELATIVE + mContext.getPackageName() + DEFAULT_FILES_PATH_RELATIVE;
        }
    }

    public String getFilesDir(String name) {
        if (!name.startsWith(File.separator)) {
            name = File.separator + name;
        }

        File file = new File(getFilesDir() + File.separator + name);
        file.mkdirs();
        return file.getAbsolutePath();
    }

    public String getExternalCacheDir(GlobalFileOptions globalOptions) throws SDCardStateException, NotEnoughSpaceException {
        File externalCacheDir = mContext.getExternalCacheDir();
        if (externalCacheDir == null) {
            throw new SDCardStateException(EXTERNAL_CACHE_DIR_EXCEPTION_MSG);
        }

        if (SDCardHelper.getAvailableStorageSize() < globalOptions.getNotEnoughSpaceLeftLine()) {
            throw new NotEnoughSpaceException(EXTERNAL_NO_SPACE_EXCEPTION_MSG);
        }

        return externalCacheDir.getAbsolutePath();
    }

    public String getExternalCacheDir(String name, GlobalFileOptions globalOptions) throws SDCardStateException, NotEnoughSpaceException {
        if (!name.startsWith(File.separator)) {
            name = File.separator + name;
        }

        File file = new File(getExternalCacheDir(globalOptions) + File.separator + name);
        file.mkdirs();
        return file.getAbsolutePath();
    }

    public String getExternalFilesDir(GlobalFileOptions globalOptions) throws SDCardStateException, NotEnoughSpaceException {
        File externalFilesDir = mContext.getExternalFilesDir("");
        if (externalFilesDir == null) {
            throw new SDCardStateException(EXTERNAL_FILES_DIR_EXCEPTION_MSG);
        }

        if (SDCardHelper.getAvailableStorageSize() < globalOptions.getNotEnoughSpaceLeftLine()) {
            throw new NotEnoughSpaceException(EXTERNAL_NO_SPACE_EXCEPTION_MSG);
        }

        return externalFilesDir.getAbsolutePath();
    }

    public String getExternalFilesDir(String type, GlobalFileOptions globalOptions) throws SDCardStateException, NotEnoughSpaceException {
        File externalFilesDir = mContext.getExternalFilesDir(type);
        if (externalFilesDir == null) {
            throw new SDCardStateException(EXTERNAL_FILES_DIR_EXCEPTION_MSG);
        }

        if (SDCardHelper.getAvailableStorageSize() < globalOptions.getNotEnoughSpaceLeftLine()) {
            throw new NotEnoughSpaceException(EXTERNAL_NO_SPACE_EXCEPTION_MSG);
        }

        return externalFilesDir.getAbsolutePath();
    }
}
