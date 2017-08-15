package com.wenming.weiswift.app.timeline.cache;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.AppDirManager;
import com.wenming.weiswift.app.common.ApplicationHelper;
import com.wenming.weiswift.app.utils.ToastUtils;
import com.wenming.weiswift.common.file.exception.NotEnoughSpaceException;
import com.wenming.weiswift.common.file.exception.SDCardStateException;

/**
 * Created by wenmingvs on 2017/8/14.
 */

public class TimeLineCacheConfig {
    private static final String DIR_GROUPS_TIMELINE = "groups_timeline";
    public static final String FILE_GROUPS_TIMELINE_PRRFIX = "groups_timeline_";
    public static final String FILE_GROUPS_TIMELINE_SUFFIX = ".json";

    private static final String DIR_FRIENDS_TIMELINE = "friends_timeline";
    public static final String FILE_FRIENDS_TIMELINE = "friends_timeline.json";


    /**
     * 分组时间线缓存路径
     *
     * @return 返回分组时间线缓存的文件夹
     */
    public static String getGroupsTimeLineDir(long uid) {
        try {
            return AppDirManager.getUserDir(uid, DIR_GROUPS_TIMELINE);
        } catch (SDCardStateException e) {
            e.printStackTrace();
            ToastUtils.show(ApplicationHelper.getContext(), R.string.common_not_enough_storage_size);
        } catch (NotEnoughSpaceException e) {
            e.printStackTrace();
            ToastUtils.show(ApplicationHelper.getContext(), R.string.common_less_storage_size);
        }
        return AppDirManager.getErrorOccurTempDir();
    }

    /**
     * 好友时间线缓存路径
     *
     * @return 返回好友时间线缓存的文件夹
     */
    public static String getFriendsTimeLine(long uid) {
        try {
            return AppDirManager.getUserDir(uid, DIR_FRIENDS_TIMELINE);
        } catch (SDCardStateException e) {
            e.printStackTrace();
            ToastUtils.show(ApplicationHelper.getContext(), R.string.common_not_enough_storage_size);
        } catch (NotEnoughSpaceException e) {
            e.printStackTrace();
            ToastUtils.show(ApplicationHelper.getContext(), R.string.common_less_storage_size);
        }
        return AppDirManager.getErrorOccurTempDir();
    }

}
