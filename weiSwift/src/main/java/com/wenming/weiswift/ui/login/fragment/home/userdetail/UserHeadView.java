package com.wenming.weiswift.ui.login.fragment.home.userdetail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.FillContent;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class UserHeadView extends RelativeLayout {
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
    private ImageView homepageIndicator;
    private LinearLayout weiboLayout;
    private TextView weiboTextview;
    private ImageView weiboIndicator;
    private LinearLayout photoLayout;
    private TextView photoTextview;
    private ImageView photoIndicator;


    public UserHeadView(Context context, User user) {
        super(context);
        init(context, user);
    }

    public UserHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, user);
    }

    public UserHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, user);
    }

    public void init(Context context, User user) {
        inflate(context, R.layout.user_profile_layout_headview, this);
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
        homepageIndicator = (ImageView) findViewById(R.id.homepage_indicator);
        weiboLayout = (LinearLayout) findViewById(R.id.weibo_layout);
        weiboTextview = (TextView) findViewById(R.id.weibo_textview);
        weiboIndicator = (ImageView) findViewById(R.id.weibo_indicator);
        photoLayout = (LinearLayout) findViewById(R.id.photo_layout);
        photoTextview = (TextView) findViewById(R.id.photo_textview);
        photoIndicator = (ImageView) findViewById(R.id.photo_indicator);
        FillContent.fillUserHeadView(context, user, userCoverimg, userImg, userVerified, userName, userSex, userFriends, userFollows, userVerifiedreason);
    }
}
