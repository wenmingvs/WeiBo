package com.wenming.weiswift.app.myself.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.wenming.weiswift.app.MyApplication;
import com.wenming.weiswift.R;
import com.wenming.weiswift.app.api.UsersAPI;
import com.wenming.weiswift.app.common.entity.User;
import com.wenming.weiswift.app.mvp.presenter.ProfileFragmentPresent;
import com.wenming.weiswift.app.mvp.presenter.imp.ProfileFragmentPresentImp;
import com.wenming.weiswift.app.mvp.view.ProfileFragmentView;
import com.wenming.weiswift.app.common.NewFeature;
import com.wenming.weiswift.app.login.AccessTokenKeeper;
import com.wenming.weiswift.app.login.activity.BackgroundActivity;
import com.wenming.weiswift.app.profile.activity.ProfileSwipeActivity;
import com.wenming.weiswift.app.myself.collect.activity.CollectSwipeActivity;
import com.wenming.weiswift.app.myself.fans.activity.FansSwipeActivity;
import com.wenming.weiswift.app.myself.focus.activity.FocusSwipeActivity;
import com.wenming.weiswift.app.myself.myphoto.activity.MyPhotoSwipeActivity;
import com.wenming.weiswift.app.myself.myweibo.activity.MyWeiBoSwipeActivity;
import com.wenming.weiswift.app.settings.activity.SettingSwipeActivity;
import com.wenming.weiswift.utils.SharedPreferencesUtil;
import com.wenming.weiswift.widget.mdprogressbar.CircleProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class MySelfFragment extends Fragment implements ProfileFragmentView {
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
    private RelativeLayout mSettingRl;
    private CheckBox mCheckBox;
    private RelativeLayout mNightModeRl;
    private User mUser;

    public MySelfFragment() {
    }

    public static MySelfFragment newInstance(User currentUser) {
        MySelfFragment mySelfFragment = new MySelfFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentUser",currentUser);
        mySelfFragment.setArguments(args);
        return mySelfFragment;
    }

    public static MySelfFragment newInstance() {
        MySelfFragment mySelfFragment = new MySelfFragment();
        return mySelfFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = getContext();
        mProfileFragmentPresent = new ProfileFragmentPresentImp(this);
        Bundle bundle = getArguments();
        if (bundle != null){
            mUser = bundle.getParcelable("currentUser");
        }
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.avator_default)
                .showImageForEmptyUri(R.drawable.avator_default)
                .showImageOnFail(R.drawable.avator_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(14671839, 1))
                .build();
        EventBus.getDefault().register(this);
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
        mSettingRl = (RelativeLayout) mView.findViewById(R.id.settingRl);
        mCheckBox = (CheckBox) mView.findViewById(R.id.nightMode_cb);
        mNightModeRl = (RelativeLayout) mView.findViewById(R.id.nightmode_rl);

        mProgressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        initContent();
        setUpListener();
        return mView;
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setNightMode(String message) {
        ((MyApplication) mContext.getApplicationContext()).recreateForNightMode();
    }

    private void initContent() {
        boolean isNightMode = (boolean) SharedPreferencesUtil.get(mContext, "setNightMode", false);
        mCheckBox.setChecked(isNightMode);
        setUserDetail(mUser);
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
                Intent intent = new Intent(mActivity, MyWeiBoSwipeActivity.class);
                startActivityForResult(intent, 0x1);
            }
        });

        mFriends_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, FocusSwipeActivity.class);
                startActivity(intent);
            }
        });
        mFollowers_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, FansSwipeActivity.class);
                startActivity(intent);
            }
        });
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingSwipeActivity.class));
            }
        });
        mMyPhoto_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser != null) {
                    Intent intent = new Intent(mActivity, MyPhotoSwipeActivity.class);
                    intent.putExtra("screeenName", mUser.screen_name);
                    startActivity(intent);
                }
            }
        });
        mFavorities_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CollectSwipeActivity.class);
                startActivity(intent);
            }
        });
        mMyprofile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser != null) {
                    Intent intent = new Intent(mContext, ProfileSwipeActivity.class);
                    intent.putExtra("screenName", mUser.screen_name);
                    mContext.startActivity(intent);
                }
            }
        });
        mSettingRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingSwipeActivity.class));
            }
        });
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferencesUtil.put(mContext, "changeTheme", true);

                if (mCheckBox.isChecked()) {
                    SharedPreferencesUtil.put(mContext, "setNightMode", true);
                    ((AppCompatActivity) getActivity()).getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    SharedPreferencesUtil.put(mContext, "setNightMode", false);
                    ((AppCompatActivity) getActivity()).getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                Intent intent = new Intent(mContext, BackgroundActivity.class);
                intent.putExtra("user",mUser);
                mContext.startActivity(intent);
            }
        });

        mNightModeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckBox.isChecked()) {
                    mCheckBox.setChecked(false);
                } else {
                    mCheckBox.setChecked(true);
                }
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
