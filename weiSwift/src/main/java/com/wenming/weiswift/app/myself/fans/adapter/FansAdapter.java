package com.wenming.weiswift.app.myself.fans.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.entity.User;
import com.wenming.weiswift.app.common.FillContent;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/1.
 */
public abstract class FansAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<User> mDatas = new ArrayList<User>();
    private Context mContext;
    private View mView;


    public FansAdapter(ArrayList<User> datas, Context context) {
        this.mDatas = datas;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.profilefragment_follower_item, parent, false);
        FollowerViewHolder viewHolder = new FollowerViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        FillContent.fillProfileImg(mContext, mDatas.get(position), ((FollowerViewHolder) holder).followerImg, ((FollowerViewHolder) holder).followerVerf);
        FillContent.setWeiBoName(((FollowerViewHolder) holder).followerName, mDatas.get(position));
        FillContent.fillFollowerDescription(mDatas.get(position), ((FollowerViewHolder) holder).follower_firstcontent);
        FillContent.setFollowerComeFrom(((FollowerViewHolder) holder).profile_comefrom, mDatas.get(position).status);
        FillContent.updateRealtionShip(mContext,mDatas.get(position), ((FollowerViewHolder) holder).follwerIcon, ((FollowerViewHolder) holder).follwerText);

        //设置点击事件
        ((FollowerViewHolder) holder).follower_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followerLayoutClick(mDatas.get(position), position, ((FollowerViewHolder) holder).follwerIcon, ((FollowerViewHolder) holder).follwerText);
            }
        });
        
    }

    public abstract void followerLayoutClick(User user, int position, ImageView follwerIcon, TextView follwerText);

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        } else {
            return 0;
        }
    }


    public void setData(ArrayList<User> data) {
        this.mDatas = data;
    }

    protected class FollowerViewHolder extends ViewHolder {
        public ImageView followerImg;
        public ImageView followerVerf;
        public TextView followerName;
        public TextView follower_firstcontent;
        public TextView profile_comefrom;
        public ImageView follwerIcon;
        public TextView follwerText;
        public RelativeLayout follower_layout;


        public FollowerViewHolder(View view) {
            super(view);
            followerImg = (ImageView) view.findViewById(R.id.follower_img);
            followerVerf = (ImageView) view.findViewById(R.id.follower_verified);
            followerName = (TextView) view.findViewById(R.id.follower_name);
            follower_firstcontent = (TextView) view.findViewById(R.id.follower_firstcontent);
            profile_comefrom = (TextView) view.findViewById(R.id.profile_comefrom);
            follwerIcon = (ImageView) view.findViewById(R.id.follwer_relation_icon);
            follwerText = (TextView) view.findViewById(R.id.follwer_relation_text);
            follower_layout = (RelativeLayout) view.findViewById(R.id.follow_layout);
        }
    }
}
