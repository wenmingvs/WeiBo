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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.wenming.weiswift.R;
import com.wenming.weiswift.api.UsersAPI;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.mvp.presenter.ProfileFragmentPresent;
import com.wenming.weiswift.mvp.presenter.imp.ProfileFragmentPresentImp;
import com.wenming.weiswift.mvp.view.ProfileFragmentView;
import com.wenming.weiswift.ui.common.NewFeature;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.login.fragment.home.userdetail.UserActivity;
import com.wenming.weiswift.ui.login.fragment.profile.favorites.FavoritiesActivity;
import com.wenming.weiswift.ui.login.fragment.profile.followers.FollowerActivity;
import com.wenming.weiswift.ui.login.fragment.profile.friends.FriendsActivity;
import com.wenming.weiswift.ui.login.fragment.profile.myphoto.MyPhotoActivity;
import com.wenming.weiswift.ui.login.fragment.profile.myweibo.MyWeiBoActivity;
import com.wenming.weiswift.ui.login.fragment.profile.setting.SettingActivity;
import com.wenming.weiswift.widget.mdprogressbar.CircleProgressBar;

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
    private RelativeLayout mFavorities_Layout;
    private RelativeLayout mMyPhoto_Layout;
    private ProfileFragmentPresent mProfileFragmentPresent;
    private CircleProgressBar mProgressBar;
    private ScrollView mScrollView;
    private RelativeLayout mMyprofile_layout;
    private User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = getContext();
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
        mScrollView = (ScrollView) mView.findViewById(R.id.scrollview);
        mProfile_myimg = (ImageView) mView.findViewById(R.id.profile_myimg);
        mProfile_myname = (TextView) mView.findViewById(R.id.profile_myname);
        mProfile_mydescribe = (TextView) mView.findViewById(R.id.profile_mydescribe);
        mStatuses_count = (TextView) mView.findViewById(R.id.profile_statuses_count);
        mFollowers_count = (TextView) mView.findViewById(R.id.profile_followers_count);
        mFriends_count = (TextView) mView.findViewById(R.id.profile_friends_count);
        mMyWeiBo_Layout = (LinearLayout) mView.findViewById(R.id.yyweibo_layout);
        mFollowers_Layout = (LinearLayout) mView.findViewById(R.id.followers_layout);
        mFriends_Layout = (LinearLayout) mView.findViewById(R.id.friends_layout);
        mFavorities_Layout = (RelativeLayout) mView.findViewById(R.id.favorities_layout);
        mMyPhoto_Layout = (RelativeLayout) mView.findViewById(R.id.myphoto_layout);
        mSettings = (TextView) mView.findViewById(R.id.setting);
        mProgressBar = (CircleProgressBar) mView.findViewById(R.id.progressbar);
        mMyprofile_layout = (RelativeLayout) mView.findViewById(R.id.myprofile_layout);

        mProgressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshUserDetail(mContext, true);
        setUpListener();
        return mView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mProfile_myname == null || mProfile_myname.getText() == null || mProfile_myname.getText().length() == 0) {
                refreshUserDetail(mContext, false);
            }
        }
    }

    public void refreshUserDetail(Context context, boolean loadicon) {
        mProfileFragmentPresent.refreshUserDetail(Long.parseLong(AccessTokenKeeper.readAccessToken(context).getUid()), context, loadicon);
    }

    public boolean haveAlreadyRefresh() {
        if (mProfile_myname == null || mProfile_myname.getText().length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void setUpListener() {
        mMyWeiBo_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MyWeiBoActivity.class);
                startActivityForResult(intent, 0x1);
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
        mMyPhoto_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MyPhotoActivity.class);
                intent.putExtra("screeenName", mUser.screen_name);
                startActivity(intent);
            }
        });
        mFavorities_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, FavoritiesActivity.class);
                startActivity(intent);
            }
        });
        mMyprofile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra("screenName", mUser.screen_name);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (NewFeature.refresh_profileLayout == true) {
            refreshUserDetail(mContext, false);
            NewFeature.refresh_profileLayout = false;
        }
    }

    @Override
    public void setUserDetail(User user) {
        if (user != null) {
            mUser = user;
            ImageLoader.getInstance().displayImage(user.avatar_hd, mProfile_myimg, options);
            mProfile_myname.setText(user.name);
            mProfile_mydescribe.setText("简介:" + user.description);
            mStatuses_count.setText(user.statuses_count + "");
            mFriends_count.setText(user.friends_count + "");
            mFollowers_count.setText(user.followers_count + "");
        }
    }

    @Override
    public void showScrollView() {
        mScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideScrollView() {
        mScrollView.setVisibility(View.GONE);
    }

    @Override
    public void showProgressDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressDialog() {
        mProgressBar.setVisibility(View.GONE);
    }


}
