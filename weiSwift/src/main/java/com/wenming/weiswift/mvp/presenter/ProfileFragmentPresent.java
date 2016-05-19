package com.wenming.weiswift.mvp.presenter;

import android.content.Context;

/**
 * Created by wenmingvs on 16/5/16.
 */
public interface ProfileFragmentPresent {
    public void refreshUserDetail(long uid, Context context,boolean loadicon);
}
