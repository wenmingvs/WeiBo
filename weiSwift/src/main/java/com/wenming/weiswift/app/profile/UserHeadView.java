package com.wenming.weiswift.app.profile;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.entity.User;
import com.wenming.weiswift.app.common.FillContent;
import com.wenming.weiswift.app.profile.activity.ProfileSwipeActivity;

/**
 * Created by wenmingvs on 16/4/27.
 */
public abstract class UserHeadView extends RelativeLayout {
    private User user;
    private ImageView userCoverimg;
    private ImageView userImg;
    private ImageView userVerified;
    private TextView userName;
    private ImageView userSex;
    private TextView userFriends;
    private TextView userFollows;
    private TextView userVerifiedreason;
    private LinearLayout homepageLayout;
    private TextView homepageTextview;
    private LinearLayout weiboLayout;
    private TextView weiboTextview;
    private LinearLayout photoLayout;
    private TextView photoTextview;


    private ImageView userInfoIndicator;
    private ImageView photoIndicator;
    private ImageView weiboIndicator;
    private View mView;


    public UserHeadView(Context context, String group, User user) {
        super(context);
        init(context, user, group);
    }

    public void init(Context context, User user, String group) {
        mView = inflate(context, R.layout.user_profile_layout_headview, this);
        this.user = user;
        userCoverimg = (ImageView) findViewById(R.id.user_coverimg);
        userImg = (ImageView) findViewById(R.id.user_img);
        userVerified = (ImageView) findViewById(R.id.user_verified);
        userName = (TextView) findViewById(R.id.user_name);
        userSex = (ImageView) findViewById(R.id.user_sex);
        userFriends = (TextView) findViewById(R.id.user_friends);
        userFollows = (TextView) findViewById(R.id.user_follows);
        userVerifiedreason = (TextView) findViewById(R.id.user_verifiedreason);
        homepageLayout = (LinearLayout) findViewById(R.id.homepage_layout);
        homepageTextview = (TextView) findViewById(R.id.homepage_textview);
        userInfoIndicator = (ImageView) findViewById(R.id.homepage_indicator);
        weiboLayout = (LinearLayout) findViewById(R.id.weibo_layout);
        weiboTextview = (TextView) findViewById(R.id.weibo_textview);
        weiboIndicator = (ImageView) findViewById(R.id.weibo_indicator);
        photoLayout = (LinearLayout) findViewById(R.id.photo_layout);
        photoTextview = (TextView) findViewById(R.id.photo_textview);
        photoIndicator = (ImageView) findViewById(R.id.photo_indicator);
        FillContent.fillUserHeadView(context, user, userCoverimg, userImg, userVerified, userName, userSex, userFriends, userFollows, userVerifiedreason);
        homepageTextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onIndicatorClick(ProfileSwipeActivity.USER_ACTIVITY_USER_INFO);
            }
        });
        weiboTextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onIndicatorClick(ProfileSwipeActivity.USER_ACTIVITY_USER_STATUS);
            }
        });
        photoTextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onIndicatorClick(ProfileSwipeActivity.USER_ACTIVITY__USER_PHOTO);
            }
        });

        highlightIndicator(group);

    }

    public void highlightIndicator(String group) {
        userInfoIndicator.setVisibility(View.INVISIBLE);
        weiboIndicator.setVisibility(View.INVISIBLE);
        photoIndicator.setVisibility(View.INVISIBLE);

        homepageTextview.setTextColor(getResources().getColor(R.color.user_topbar_text));
        weiboTextview.setTextColor(getResources().getColor(R.color.user_topbar_text));
        photoTextview.setTextColor(getResources().getColor(R.color.user_topbar_text));

        switch (group) {
            case ProfileSwipeActivity.USER_ACTIVITY_USER_INFO:
                homepageTextview.setTextColor(getResources().getColor(R.color.user_topbar_text_highlight));
                userInfoIndicator.setVisibility(View.VISIBLE);
                break;
            case ProfileSwipeActivity.USER_ACTIVITY_USER_STATUS:
                weiboTextview.setTextColor(getResources().getColor(R.color.user_topbar_text_highlight));
                weiboIndicator.setVisibility(View.VISIBLE);
                break;
            case ProfileSwipeActivity.USER_ACTIVITY__USER_PHOTO:
                photoTextview.setTextColor(getResources().getColor(R.color.user_topbar_text_highlight));
                photoIndicator.setVisibility(View.VISIBLE);
                break;
        }
    }


    public abstract void onIndicatorClick(String group);

}
