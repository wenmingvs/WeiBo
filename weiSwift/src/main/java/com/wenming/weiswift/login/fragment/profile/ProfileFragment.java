package com.wenming.weiswift.login.fragment.profile;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.login.AccessTokenKeeper;
import com.wenming.weiswift.common.login.Constants;
import com.wenming.weiswift.login.fragment.profile.followers.FollowerActivity;
import com.wenming.weiswift.login.fragment.profile.friends.FriendsActivity;
import com.wenming.weiswift.login.fragment.profile.myweibo.MyWeiBoActivity;
import com.wenming.weiswift.login.fragment.profile.setting.SettingActivity;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class ProfileFragment extends Fragment {
    private Activity mActivity;
    private View mToolBar;
    private View mView;
    private TextView mSettings;
    private Context mContext;
    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;
    private StatusesAPI mStatusesAPI;
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
    private long mUid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = mActivity;
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
        initAccessToken();
        initUser();
        return mView;
    }

    private void initUser() {
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


        mUid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(mUid, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    // 调用 User#parse 将JSON串解析成User对象
                    User user = User.parse(response);
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

            @Override
            public void onWeiboException(WeiboException e) {

            }
        });

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

    private void initAccessToken() {
        mAuthInfo = new AuthInfo(mContext, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(mActivity, mAuthInfo);
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        mStatusesAPI = new StatusesAPI(mContext, Constants.APP_KEY, mAccessToken);
        mUsersAPI = new UsersAPI(mContext, Constants.APP_KEY, mAccessToken);
    }


}
