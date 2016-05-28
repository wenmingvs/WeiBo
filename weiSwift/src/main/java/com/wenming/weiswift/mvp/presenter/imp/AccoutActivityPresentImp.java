package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.mvp.model.TokenListModel;
import com.wenming.weiswift.mvp.model.UserModel;
import com.wenming.weiswift.mvp.model.imp.TokenListModelImp;
import com.wenming.weiswift.mvp.model.imp.UserModelImp;
import com.wenming.weiswift.mvp.presenter.AccoutActivityPresent;
import com.wenming.weiswift.mvp.view.AccoutActivityView;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.unlogin.activity.UnLoginActivity;
import com.wenming.weiswift.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/18.
 */
public class AccoutActivityPresentImp implements AccoutActivityPresent {


    private AccoutActivityView accoutActivityView;
    private TokenListModel tokenListModel;
    private UserModel userModel;
    private static DisplayImageOptions mAvatorOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.avator_default)
            .showImageForEmptyUri(R.drawable.avator_default)
            .showImageOnFail(R.drawable.avator_default)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .resetViewBeforeLoading(false)
            .displayer(new CircleBitmapDisplayer(14671839, 1))
            .build();

    public AccoutActivityPresentImp(AccoutActivityView accoutActivityView) {
        this.accoutActivityView = accoutActivityView;
        tokenListModel = new TokenListModelImp();
        userModel = new UserModelImp();
    }

    @Override
    public void logoutCurrentAccout(final Context context) {
        logout(context, AccessTokenKeeper.readAccessToken(context).getUid());
    }

    public void logout(final Context context, String uid) {
        tokenListModel.deleteToken(context, uid);
        userModel.deleteUserByUid(Long.valueOf(uid), context, new UserModel.OnUserDeleteListener() {
            @Override
            public void onSuccess(ArrayList<User> userlist) {
                accoutActivityView.updateListView(userlist);
            }

            @Override
            public void onEmpty() {
                Intent intent = new Intent(context, UnLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                accoutActivityView.finishItself();
            }

            @Override
            public void onError(String error) {
                ToastUtil.showShort(context, error);
                accoutActivityView.hideProgressDialog();
            }
        });
    }

    @Override
    public void switchAccout(Context context, String uid) {
        tokenListModel.switchToken(context, uid);
    }

    @Override
    public void obtainUserListDetail(final Context context) {
        accoutActivityView.hideListView();
        accoutActivityView.showProgressDialog();
        userModel.getUserDetailList(context, new UserModel.OnUserListRequestFinish() {
            @Override
            public void noMoreDate() {

            }

            @Override
            public void onDataFinish(ArrayList<User> userlist) {
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putSerializable("mUserArrayList", userlist);
                message.setData(bundle);
                mHandle.sendMessage(message);
            }

            @Override
            public void onError(String error) {
                accoutActivityView.hideProgressDialog();
                ToastUtil.showShort(context, error);
            }
        });
    }

    public Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            accoutActivityView.showListView();
            accoutActivityView.hideProgressDialog();
            accoutActivityView.initListView((ArrayList<User>) msg.getData().getSerializable("mUserArrayList"));
            accoutActivityView.setUpListener();
        }
    };


}
