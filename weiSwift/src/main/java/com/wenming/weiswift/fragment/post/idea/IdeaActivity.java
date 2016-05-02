package com.wenming.weiswift.fragment.post.idea;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.wenming.weiswift.R;

/**
 * Created by wenmingvs on 16/5/2.
 */
public class IdeaActivity extends Activity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_idea_layout);
        mContext = this;
    }
}
