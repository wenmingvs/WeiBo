package com.wenming.weiswift.app.home.data;

import com.wenming.weiswift.app.common.callback.INetWorkCallBack;

/**
 * Created by wenmingvs on 2017/6/5.
 */

public interface HomeDataSource {
    void requestGroups(String accessToken, INetWorkCallBack callBack);
}
