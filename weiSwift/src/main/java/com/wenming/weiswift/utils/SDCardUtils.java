/**
 * Copyright (C) 2008-2015 MEITU Technologies.
 * All rights reserved.
 * Created on 2015-3-29 下午12:16:40
 *
 * @author liwentao
 * @version 1.0
 */
package com.wenming.weiswift.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class SDCardUtils {

    public static String getSdcardPath() {
        String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        if (!sdcardPath.endsWith(File.separator)) {
            sdcardPath += File.separator;
        }

        return sdcardPath;
    }

    /**
     * Description:SD Card available
     *
     * @return true if SD card available
     */
    public static boolean isSdAvailable() {
        return Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState());
    }

    /**
     * 判断存储卡是否可读
     *
     * @return true if readable
     */
    public static boolean isExternalStorageReadable() {
        final String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED) || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    /**
     * Description:uint bits
     *
     * @return long
     */
    public static long getAvailableStorageSize() {
        String storageDirectory = Environment.getExternalStorageDirectory().toString();
        try {
            StatFs stat = new StatFs(storageDirectory);
            long availableSize = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
            return availableSize;
        } catch (RuntimeException ex) {
            return 0;
        }
    }


    public static boolean isSdcardStorageEnough(long size) {
        long availableSize = SDCardUtils.getAvailableStorageSize();
        return availableSize > size;
    }
}
