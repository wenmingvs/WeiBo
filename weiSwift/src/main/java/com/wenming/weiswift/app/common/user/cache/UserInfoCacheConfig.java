package com.wenming.weiswift.app.common.user.cache;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.AppDirManager;
import com.wenming.weiswift.app.common.ApplicationHelper;
import com.wenming.weiswift.app.utils.ToastUtils;
import com.wenming.weiswift.common.file.exception.NotEnoughSpaceException;
import com.wenming.weiswift.common.file.exception.SDCardStateException;

/**
 * Created by wenmingvs on 2017/8/15.
 */

public class UserInfoCacheConfig {
    private static final String DIR_USER_INFO = "user_info";
    public static final String FILE_USER_INFO = "user_info.json";

    /**
     * 获取用户信息缓存记录
     *
     * @return
     */
    public static String getUserInfoDir(long uid) {
        try {
            return AppDirManager.getUserDir(uid, DIR_USER_INFO);
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
