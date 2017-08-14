package com.wenming.weiswift.app.home.cache;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.AppDirManager;
import com.wenming.weiswift.app.common.ApplicationHelper;
import com.wenming.weiswift.app.utils.ToastUtils;
import com.wenming.weiswift.common.file.exception.NotEnoughSpaceException;
import com.wenming.weiswift.common.file.exception.SDCardStateException;

/**
 * Created by wenmingvs on 2017/8/14.
 */

public class HomeCacheConfig {
    private static final String DIR_GROUPS = "groups";
    public static final String FILE_GROUPS = "groups.json";

    /**
     * 分组缓存保存的路径
     *
     * @return 返回分组文件夹目录
     */
    public static String getGroupsDir(long uid) {
        try {
            return AppDirManager.getUserDir(uid, DIR_GROUPS);
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
