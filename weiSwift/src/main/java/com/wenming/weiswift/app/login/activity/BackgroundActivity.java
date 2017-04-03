package com.wenming.weiswift.app.login.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.entity.User;
import com.wenming.weiswift.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wenmingvs on 2016/9/4.
 */
public class BackgroundActivity extends Activity {
    private Context mContext;
    private ImageView mProfile_myimg;
    private TextView mProfile_mydescribe;
    private TextView mProfile_myname;
    private TextView mStatuses_count;
    private TextView mFriends_count;
    private TextView mFollowers_count;
    private CheckBox mCheckBox;
    private User mUser;
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.avator_default)
            .showImageForEmptyUri(R.drawable.avator_default)
            .showImageOnFail(R.drawable.avator_default)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new CircleBitmapDisplayer(14671839, 1))
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bgactivity_layout);
        mContext = this;
        mUser = getIntent().getParcelableExtra("user");
        if (mUser != null){
            initContent();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post("yes");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(0x11);
                    }
                }, 500);
            }
        });

    }

    private void initContent() {
        mProfile_myimg = (ImageView) findViewById(R.id.profile_myimg);
        mProfile_myname = (TextView) findViewById(R.id.profile_myname);
        mProfile_mydescribe = (TextView) findViewById(R.id.profile_mydescribe);
        mStatuses_count = (TextView) findViewById(R.id.profile_statuses_count);
        mFollowers_count = (TextView) findViewById(R.id.profile_followers_count);
        mFriends_count = (TextView) findViewById(R.id.profile_friends_count);
        mCheckBox = (CheckBox) findViewById(R.id.nightMode_cb);

        boolean isNightMode = (boolean) SharedPreferencesUtil.get(mContext, "setNightMode", false);
        mCheckBox.setChecked(isNightMode);
        ImageLoader.getInstance().displayImage(mUser.avatar_hd, mProfile_myimg, options);
        mProfile_myname.setText(mUser.name);
        mProfile_mydescribe.setText("简介:" + mUser.description);
        mStatuses_count.setText(mUser.statuses_count + "");
        mFriends_count.setText(mUser.friends_count + "");
        mFollowers_count.setText(mUser.followers_count + "");
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.bg_activity_out);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finish();
        }
    };
}
