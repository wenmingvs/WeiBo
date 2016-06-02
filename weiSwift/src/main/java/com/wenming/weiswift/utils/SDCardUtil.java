package com.wenming.weiswift.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * SD卡相关的辅助类
 * Created by wenmingvs on 15/12/27.
 */
public class SDCardUtil {
    private SDCardUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath(Context context) {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    public static void put(Context context, String fileDir, String fileName, String content) {
        File filedir = new File(fileDir);
        File jsonfile = new File(filedir, fileName);
        if (!filedir.exists()) {
            filedir.mkdirs();
        }
        try {
            jsonfile.createNewFile();
            if (isSDCardEnable()) {
                FileOutputStream outputstream = new FileOutputStream(jsonfile);
                byte[] buffer = content.getBytes();
                outputstream.write(buffer);
                outputstream.flush();
                outputstream.close();
                //Toast.makeText(context, "文件写入成功", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            //Toast.makeText(context, "文件写入失败", Toast.LENGTH_SHORT).show();
            //LogUtil.d(ex.toString());
        }
    }

    public static String get(Context context, String fileDir, String fileName) {
        StringBuffer sb = null;
        try {
            File file = new File(fileDir, fileName);
            LogUtil.d(file.getPath());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readline = "";
            sb = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                System.out.println("readline:" + readline);
                sb.append(readline);
            }
            br.close();
            return sb.toString();
            //Toast.makeText(context, fileName + "文件读取成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(context, fileName + "文件读取失败", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


}
