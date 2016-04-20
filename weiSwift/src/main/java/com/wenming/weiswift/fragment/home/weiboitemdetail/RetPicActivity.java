package com.wenming.weiswift.fragment.home.weiboitemdetail;

import android.os.Bundle;

import com.wenming.weiswift.R;

/**
 * Created by wenmingvs on 16/4/21.
 */
public class RetPicActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContntView();
    }

    @Override
    public void setContntView() {
        setContentView(R.layout.home_weiboitem_detail_retweet_pictext);
    }
}
