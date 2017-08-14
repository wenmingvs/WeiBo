package com.wenming.weiswift.common.file.helper;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.wenming.weiswift.common.file.exception.NotEnoughSpaceException;
import com.wenming.weiswift.common.file.exception.SDCardStateException;
import com.wenming.weiswift.common.file.option.FileOptions;
import com.wenming.weiswift.common.file.option.GlobalFileOptions;

import java.io.File;

/**
 * SDCard管理类
 * Created by Li WenTao on 2016/9/24 11:30
 */
public class SDCardHelper {
    private static final String SD_CARD_EXCEPTION_MSG = "SDCard not enable.";
    private static final String SD_CARD_NO_SPACE_EXCEPTION_MSG = "There is no space left in SDCard.";

    private static final String HIDDEN_FILE_PREFIX = ".";

    public SDCardHelper() {
    }

    public String getSDCardPath() throws SDCardStateException {
        if (!isSDCardEnable()) {
            throw new SDCardStateException(SD_CARD_EXCEPTION_MSG);
        }

        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取sdcard/dirName的文件路径
     * @param dirName 文件路径
     * @param globalOptions 文件路径默认配置
     * @return 文件路径
     */
    public String getDir(String dirName, FileOptions options, GlobalFileOptions globalOptions) throws SDCardStateException, NotEnoughSpaceException {
        checkDirNameAndOptions(dirName, options, globalOptions);

        assert globalOptions.getNotEnoughSpaceLeftLine() == 0;
        if (getAvailableStorageSize() < globalOptions.getNotEnoughSpaceLeftLine()) {
            throw new NotEnoughSpaceException(SD_CARD_NO_SPACE_EXCEPTION_MSG);
        }

        if (dirName.startsWith(File.separator)) {
            dirName = dirName.substring(1);
        }

        String filePath = getSDCardPath();
        String companyName = globalOptions.getCompanyName();
        if (!TextUtils.isEmpty(companyName)) {
            filePath = filePath + File.separator + companyName;
        }

        String projectName = globalOptions.getProjectName();
        filePath = filePath + File.separator + projectName;

        if (null != options) {
            String moduleName = options.getModuleName();
            boolean isHideModule = options.isHideDir();
            if (!TextUtils.isEmpty(moduleName)) {
                if (!moduleName.startsWith(HIDDEN_FILE_PREFIX) && isHideModule) {
                    moduleName = HIDDEN_FILE_PREFIX + moduleName;
                }

                filePath = filePath + File.separator + moduleName;
            }
        }

        filePath = filePath + File.separator + dirName;
        ensureDirExist(filePath);
        return filePath;
    }

    private void checkDirNameAndOptions(String dirName, FileOptions options, GlobalFileOptions globalOptions) {
        if (null == globalOptions || globalOptions.getProjectName() == null) {
            throw new IllegalArgumentException("GlobalFileOptions projectName should not null.");
        }

        if (null == dirName || dirName.equals(File.separator)) {
            throw new IllegalArgumentException("dirName should not null or only one file separator.");
        }

        if (null != options && TextUtils.isEmpty(options.getModuleName())) {
            throw new IllegalArgumentException("ModuleName should not null while FileOptions not null.");
        }
    }

    private void ensureDirExist(String dir) {
        File dirF = new File(dir);
        dirF.mkdirs();
    }

    /**
     * 判断SDCard是否可用
     *
     * @return 可读写，为true，否则为false
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取外部存储卡Size
     * @return long
     */
    public static long getAvailableStorageSize() {
        try {
            long availableSize;
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().toString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availableSize = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
            } else {
                availableSize = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
            }
            return availableSize;
        } catch (RuntimeException ex) {
            return 0;
        }
    }
}
