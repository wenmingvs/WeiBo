package com.wenming.weiswift.ui.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.login.activity.MainActivity;
import com.wenming.weiswift.ui.unlogin.activity.UnLoginActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wenmingvs on 16/5/4.
 */
public class WelcomeActivity extends Activity {
    private Intent mStartIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        if (AccessTokenKeeper.readAccessToken(this).isSessionValid()) {
            mStartIntent = new Intent(WelcomeActivity.this, MainActivity.class);
        } else {
            mStartIntent = new Intent(WelcomeActivity.this, UnLoginActivity.class);
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendMessage(Message.obtain());
            }
        }, 500);
    }


    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startActivity(mStartIntent);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
