package com.wenming.weiswift.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 与保存相关的工具类
 * Created by xiang on 16-7-5.
 * Usage:
 * 　　　保存是个耗时操作，最好放在子线程执行，防止发生ANR
 *      默认图片保存目录是/sdcard/Android/data/com.wenming.weiswift.file/Pictures/weibo
 *      最好设置一下fileName
 *      SaveUtil.create(context)
 *              // 缺省
 *              //.setOutputDirectory(directory)
 *              //.setImageFormat(format)
 *              //.setImageQuality(quality)
 *              //.setFileName(name)
 *              .saveImage(bitmap)
 */
public class SaveUtil {

    private static final String TAG = SaveUtil.class.getSimpleName();

    private static final int IMG_DEFAULT_QUALITY = 70;

    private static final String FOLDER_NAME = "weibo";

    /**
     * Save a file in internal storage or external storage
     */
    private File mOutputDirectory;
    /**
     * The format of the picture which will be saved
     */
    private Bitmap.CompressFormat mFormat;
    /**
     * The quality of the picture saved.
     */
    private int mQuality;
    /**
     * The name of the file saved
     */
    private String mFileName;

    private static SaveUtil mSaveUtil;

    private SaveUtil(Context context) {
        this.mOutputDirectory = getDirectory(context, FOLDER_NAME);
        this.mFormat = Bitmap.CompressFormat.JPEG;
        this.mQuality = IMG_DEFAULT_QUALITY;
        this.mFileName = System.currentTimeMillis() + ".jpg";
    }

    public static SaveUtil create(Context context) {
        if (mSaveUtil == null) {
            mSaveUtil = new SaveUtil(context);
        }
        return mSaveUtil;
    }

    /**
     * @param directory 文件保存路径
     *                  Internal Storage(only access by your own app)
     *                      context.getFilesDir()
     *                      context.getCacheDir()
     *                  External Storage(when use this, should first check isSDCardEnable)
     *                      Environment.getExternalStoragePublicDirectory
     *                      context.getExternalFilesDir
     *
     * @return
     */
    public SaveUtil setOutputDirectory(File directory) {
        this.mOutputDirectory = directory;
        return this;
    }

    /**
     * @param format　图片保存格式
     * @return
     */
    public SaveUtil setImageFormat(Bitmap.CompressFormat format) {
        this.mFormat = format;
        return this;
    }

    /**
     *
     * @param quality  图片质量
     * @return
     */
    public SaveUtil setImageQuality(int quality) {
        this.mQuality = quality;
        return this;
    }

    /**
     * @param fileName 文件名
     * @return
     */
    public SaveUtil setFileName(String fileName) {
        this.mFileName = fileName;
        return this;
    }

    /**
     * @param bitmap 保存Bitmap
     */
    public void saveImage(Bitmap bitmap){
        LogUtil.d(TAG, "saveImage executed");
        File file = null;
        FileOutputStream fos = null;
        try {
            file = new File(mOutputDirectory, mFileName);
            fos = new FileOutputStream(file);
            if (bitmap.compress(mFormat, mQuality, fos)) {
                LogUtil.d(TAG, "save successfully");
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建保存图片的目录
     * @param context
     * @param folderName
     * @return
     */
    private File getDirectory(Context context, String folderName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), folderName);
        if (!file.mkdirs()) {
            LogUtil.e(TAG, "Directory not created");
        }
        return file;
    }

}
