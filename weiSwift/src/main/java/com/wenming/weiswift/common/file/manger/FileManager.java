package com.wenming.weiswift.common.file.manger;


import android.content.Context;

import com.wenming.weiswift.common.file.exception.NotEnoughSpaceException;
import com.wenming.weiswift.common.file.exception.SDCardStateException;
import com.wenming.weiswift.common.file.helper.CleanHelper;
import com.wenming.weiswift.common.file.helper.SDCardHelper;
import com.wenming.weiswift.common.file.helper.SandBoxHelper;
import com.wenming.weiswift.common.file.option.FileOptions;
import com.wenming.weiswift.common.file.option.GlobalFileOptions;

/**
 * func:对外接口
 * Created by Li WenTao on 2016/9/23 17:30
 */
public class FileManager {
    private static FileManager sInstance;

    private CleanHelper mCleanHelper;
    private SandBoxHelper mSandBoxHelper;
    private SDCardHelper mSDCardHelper;
    private GlobalFileOptions mGlobalOptions;

    private FileManager() {
    }

    public static FileManager getInstance() {
        if (sInstance == null) {
            synchronized (FileManager.class) {
                if (sInstance == null) {
                    sInstance = new FileManager();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context, GlobalFileOptions fileOptions) {
        Context applicationContext = context.getApplicationContext();
        mGlobalOptions = fileOptions;
        mCleanHelper = new CleanHelper(applicationContext);
        mSandBoxHelper = new SandBoxHelper(applicationContext);
        mSDCardHelper = new SDCardHelper();
    }

    /**
     * @return sdcard上应用缓存目录
     */
    public String getExternalSandBoxCacheDir() throws SDCardStateException, NotEnoughSpaceException {
        return mSandBoxHelper.getExternalCacheDir(mGlobalOptions);
    }

    /**
     * @return sdcard 上应用缓存目录 cache/name
     */
    public String getExternalSandBoxCacheDir(String name) throws SDCardStateException, NotEnoughSpaceException {
        return mSandBoxHelper.getExternalCacheDir(name, mGlobalOptions);
    }

    /**
     * @return sdcard上应用File目录
     */
    public String getExternalSandBoxFilesDir() throws SDCardStateException, NotEnoughSpaceException {
        return mSandBoxHelper.getExternalFilesDir(mGlobalOptions);
    }

    /**
     * @param type 文件类型（名称）
     * @return sdcard上type文件夹绝对路径
     */
    public String getExternalSandBoxFilesDir(String type) throws SDCardStateException, NotEnoughSpaceException {
        return mSandBoxHelper.getExternalFilesDir(type, mGlobalOptions);
    }

    /**
     * @return the absolute path to the application specific cache directory
     * on the filesystem. These files will be ones that get deleted first when the
     * device runs low on storage.
     * There is no guarantee when these files will be deleted.
     */
    public String getSandBoxCacheDir() {
        return mSandBoxHelper.getCacheDir();
    }

    /**
     * @return An absolute path to the given file.
     */
    public String getSandBoxCacheDir(String name) {
        return mSandBoxHelper.getCacheDir(name);
    }

    /**
     * @return The path of the directory holding application files.
     */
    public String getSandBoxFilesDir() {
        return mSandBoxHelper.getFilesDir();
    }

    /**
     * @return An absolute path to the given file.
     */
    public String getSandBoxFilesDir(String name) {
        return mSandBoxHelper.getFilesDir(name);
    }

    /**
     * 获取sdcard/dirName的文件路径
     * @param dirName 文件路径
     * @return 文件路径
     */
    public String getSDCardDir(String dirName) throws SDCardStateException, NotEnoughSpaceException {
        return getSDCardDir(dirName, null);
    }

    /**
     * 获取sdcard/dirName的文件路径
     * @param dirName 文件路径
     * @param options 文件路径默认配置
     * @return 文件路径
     */
    public String getSDCardDir(String dirName, FileOptions options) throws SDCardStateException, NotEnoughSpaceException {
        return mSDCardHelper.getDir(dirName, options, mGlobalOptions);
    }

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     */
    public void cleanInternalCache() {
        mCleanHelper.cleanInternalCache();
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     */
    public void cleanDatabases() {
        mCleanHelper.cleanDatabases();
    }

    /**
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     */
    public void cleanSharedPreference() {
        mCleanHelper.cleanSharedPreference();
    }

    /**
     * 按名字清除本应用数据库
     * @param dbName dbName
     */
    public void cleanDatabaseByName(String dbName) {
        mCleanHelper.cleanDatabaseByName(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容 * *
     */
    public void cleanFiles() {
        mCleanHelper.cleanFiles();
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/mCache)
     */
    public void cleanExternalCache() {
        mCleanHelper.cleanExternalCache();
    }

    /**
     * * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * *
     * @param filePath 文件路径
     */
    public void cleanCustomCache(String filePath) {
        mCleanHelper.cleanCustomCache(filePath);
    }

    /**
     * 清除本应用所有的数据
     * @param filepath 文件路径
     */
    public void cleanApplicationData(String... filepath) {
        mCleanHelper.cleanApplicationData(filepath);
    }
}
