package com.wenming.weiswift.fragment.home.weiboitemdetail.commentdetail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Comment;
import com.wenming.weiswift.R;

import java.util.ArrayList;

/**
 * 用于显示转发列表的adapter
 * Created by wenmingvs on 16/4/23.
 */
public class RetweetAdapter extends RecyclerView.Adapter<RetweetAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Comment> mDatas;
    private View mView;


    public RetweetAdapter(Context mContext, ArrayList<Comment> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.mainfragment_weiboitem_detail_commentbar_retweet_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_img;
        public ImageView profile_verified;
        public TextView profile_name;
        public TextView profile_time;
        public TextView retweetItem_content;

        public ViewHolder(View v) {
            super(v);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_verified = (ImageView) v.findViewById(R.id.profile_verified);
            profile_name = (TextView) v.findViewById(R.id.retweetItem_profile_name);
            profile_time = (TextView) v.findViewById(R.id.retweetItem_profile_time);
            retweetItem_content = (TextView) v.findViewById(R.id.retweetItem_content);
        }

    }

}
