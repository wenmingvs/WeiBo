package com.wenming.weiswift.fragment.post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.common.util.ToastUtil;
import com.wenming.weiswift.fragment.post.idea.IdeaActivity;

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
                startActivity(intent);
                finish();
            }
        });
        composePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "API暂时不支持");
            }
        });
        composeHeadlines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "API暂时不支持");
            }
        });
        composeLbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "API暂时不支持");
            }
        });
        composeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "API暂时不支持");
            }
        });
        composeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "API暂时不支持");
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
