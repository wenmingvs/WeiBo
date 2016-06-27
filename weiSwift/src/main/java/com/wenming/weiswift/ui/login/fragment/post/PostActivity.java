package com.wenming.weiswift.ui.login.fragment.post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.login.fragment.post.idea.IdeaActivity;
import com.wenming.weiswift.utils.ToastUtil;

/**
 * Created by wenmingvs on 16/5/2.
 */
public class PostActivity extends Activity {

    private Context mContext;
    private ImageView composeIdea;
    private ImageView composePhoto;
    private ImageView composeHeadlines;
    private ImageView composeLbs;
    private ImageView composeReview;
    private ImageView composeMore;
    private ImageView composeClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postfragment_layout);
        mContext = this;
        composeIdea = (ImageView) findViewById(R.id.compose_idea);
        composePhoto = (ImageView) findViewById(R.id.compose_photo);
        composeHeadlines = (ImageView) findViewById(R.id.compose_headlines);
        composeLbs = (ImageView) findViewById(R.id.compose_lbs);
        composeReview = (ImageView) findViewById(R.id.compose_review);
        composeMore = (ImageView) findViewById(R.id.compose_more);
        composeClose = (ImageView) findViewById(R.id.compose_close);

        composeIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, IdeaActivity.class);
                intent.putExtra("ideaType", PostService.POST_SERVICE_CREATE_WEIBO);
                startActivity(intent);
                finish();
            }
        });
        composePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, IdeaActivity.class);
                intent.putExtra("ideaType", PostService.POST_SERVICE_CREATE_WEIBO);
                intent.putExtra("startAlumbAcitivity", true);
                startActivity(intent);
                finish();
            }
        });
        composeHeadlines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发中...");
            }
        });
        composeLbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发中...");
            }
        });
        composeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发中...");
            }
        });
        composeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发中...");
            }
        });
        composeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
