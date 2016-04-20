package com.wenming.weiswift.fragment.home.weiboitemdetail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.wenming.weiswift.R;

/**
 * Created by wenmingvs on 16/4/20.
 */
public class WeiBoDetailActivity extends Activity {

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_weiboitem_detail);
        mContext = this;

    }


}
