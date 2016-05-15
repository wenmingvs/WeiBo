package com.wenming.weiswift.ui.login.fragment.profile;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.mvp.presenter.ProfileFragmentPresent;
import com.wenming.weiswift.mvp.presenter.imp.ProfileFragmentPresentImp;
import com.wenming.weiswift.mvp.view.ProfileFragmentView;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.login.fragment.profile.followers.FollowerActivity;
import com.wenming.weiswift.ui.login.fragment.profile.friends.FriendsActivity;
import com.wenming.weiswift.ui.login.fragment.profile.myweibo.MyWeiBoActivity;
import com.wenming.weiswift.ui.login.fragment.profile.setting.SettingActivity;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class ProfileFragment extends Fragment implements ProfileFragmentView {
    private Activity mActivity;
    private View mView;
    private TextView mSettings;
    private Context mContext;
    private UsersAPI mUsersAPI;
    private ImageView mProfile_myimg;
    private TextView mProfile_mydescribe;
    private TextView mProfile_myname;
    private TextView mStatuses_count;
    private TextView mFriends_count;
    private TextView mFollowers_count;
    private DisplayImageOptions options;
    private LinearLayout mMyWeiBo_Layout;
    private LinearLayout mFollowers_Layout;
    private LinearLayout mFriends_Layout;
    private ProfileFragmentPresent mProfileFragmentPresent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = mActivity;
        mProfileFragmentPresent = new ProfileFragmentPresentImp(this);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.avator_default)
                .showImageForEmptyUri(R.drawable.avator_default)
                .showImageOnFail(R.drawable.avator_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(14671839, 1))
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.profilefragment_layout, null);
        mProfile_myimg = (ImageView) mView.findViewById(R.id.profile_myimg);
        mProfile_myname = (TextView) mView.findViewById(R.id.profile_myname);
        mProfile_mydescribe = (TextView) mView.findViewById(R.id.profile_mydescribe);
        mStatuses_count = (TextView) mView.findViewById(R.id.profile_statuses_count);
        mFollowers_count = (TextView) mView.findViewById(R.id.profile_followers_count);
        mFriends_count = (TextView) mView.findViewById(R.id.profile_friends_count);
        mMyWeiBo_Layout = (LinearLayout) mView.findViewById(R.id.yyweibo_layout);
        mFollowers_Layout = (LinearLayout) mView.findViewById(R.id.followers_layout);
        mFriends_Layout = (LinearLayout) mView.findViewById(R.id.friends_layout);
        mSettings = (TextView) mView.findViewById(R.id.setting);
        mProfileFragmentPresent.refreshUserDetail(Long.parseLong(AccessTokenKeeper.readAccessToken(mContext).getUid()), mContext);
        setUpListener();
        return mView;
    }

    private void setUpListener() {
        mMyWeiBo_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MyWeiBoActivity.class);
                startActivity(intent);
            }
        });

        mFriends_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, FriendsActivity.class);
                startActivity(intent);
            }
        });
        mFollowers_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, FollowerActivity.class);
                startActivity(intent);
            }
        });
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
    }


    @Override
    public void setUserDetail(User user) {
        if (user != null) {
            ImageLoader.getInstance().displayImage(user.avatar_hd, mProfile_myimg, options);
            mProfile_myname.setText(user.name);
            mProfile_mydescribe.setText("简介:" + user.description);
            mStatuses_count.setText(user.statuses_count + "");
            mFriends_count.setText(user.friends_count + "");
            mFollowers_count.setText(user.followers_count + "");
        }

    }
}
