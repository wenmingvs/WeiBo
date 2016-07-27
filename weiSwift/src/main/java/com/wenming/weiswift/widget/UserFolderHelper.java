package com.wenming.weiswift.widget;

import java.io.File;

/**
 * Created by wenmingvs on 16/7/27.
 */
public interface UserFolderHelper {

    public static final int FOLDER_TYPE_SOUND = 0x1;
    public static final int FOLDER_TYPE_VIDEO = 0x2;
    public static final int FOLDER_TYPE_PICTURE = 0x3;
    public static final int FOLDER_TYPE_DOWNLOAD = 0x4;
    public static final int FOLDER_TYPE_OTHER = 0x5;

    public static File currentUserFolder = null;


    /**
     * 用于切换用户文件夹，如果文件夹不存在，会创建，最后会更新currentUserFolder
     *
     * @param userId 用户id
     * @return 返回用户的file文件夹
     */
    public File checkToUser(String userId);

    /**
     * 获取user文件夹下更加精确的子文件夹，并且创建文件夹，最后会更新currentUserFolder。
     *
     * @param userId     用户id
     * @param folderType 需要切换的类型 {@link FOLDER_TYPE_SOUND}{@link FOLDER_TYPE_VIDEO}{@link FOLDER_TYPE_SOUND}{@link FOLDER_TYPE_PICTURE}{@link FOLDER_TYPE_DOWNLOAD}{@link FOLDER_TYPE_OTHER}
     * @return 返回user文件夹
     */
    public File checkToUser(String userId, String folderType);


    /**
     * 获取当前聊天用户对应的user文件夹
     *
     * @return 返回user文件夹
     */
    public File getCurrentUserFolder();

    /**
     * 获取当前聊天用户对应的user文件夹下的子文件
     *
     * @param userType 需要切换的类型 {@link FOLDER_TYPE_SOUND}{@link FOLDER_TYPE_VIDEO}{@link FOLDER_TYPE_SOUND}{@link FOLDER_TYPE_PICTURE}{@link FOLDER_TYPE_DOWNLOAD}{@link FOLDER_TYPE_OTHER}
     * @return 返回user文件夹
     */
    public File getCurrentUserFolder(String userType);


    /**
     * 更新用户folder文件夹，如果newUserFolder和currentUserFolder的文件名不同，做一下两重更新
     * <p/>
     * 1. 内存层的更新，给变量currentUserFolder赋新的值
     * 2. 以sharePreference的形式保存当前的CurrentFolder文件夹的绝对路径到本地
     */
    public void updateCurrentFolder(File newUserFolder);

    /**
     * 清空缓存
     *
     * @return 清空完成，返回空
     */
    public boolean clearUserFolderCache();

    /**
     * 读取本地缓存的值
     *
     * @return
     */
    public String getCurrentUserPathInSP();


}
