package com.wenming.weiswift.app.myself.focus.adapter;

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
public abstract class FocusAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<User> mDatas = new ArrayList<User>();
    private Context mContext;
    private View mView;


    public FocusAdapter(ArrayList<User> datas, Context context) {
        this.mDatas = datas;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.profilefragment_friend_item, parent, false);
        FriendsrViewHolder viewHolder = new FriendsrViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FillContent.fillProfileImg(mContext, mDatas.get(position), ((FriendsrViewHolder) holder).friendImg, ((FriendsrViewHolder) holder).friendVerified);
        FillContent.setWeiBoName(((FriendsrViewHolder) holder).friendName, mDatas.get(position));
        FillContent.fillFollowerDescription(mDatas.get(position), ((FriendsrViewHolder) holder).friendContent);
        FillContent.updateRealtionShip(mContext, mDatas.get(position), ((FriendsrViewHolder) holder).friendIcon, ((FriendsrViewHolder) holder).friendText);
        ((FriendsrViewHolder) holder).friend_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendLayoutClick(mDatas.get(position), position, ((FriendsrViewHolder) holder).friendIcon, ((FriendsrViewHolder) holder).friendText);
            }
        });

    }

    public abstract void friendLayoutClick(User user, int position, ImageView friendIcon, TextView friendText);

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

    protected class FriendsrViewHolder extends ViewHolder {
        public ImageView friendImg;
        public ImageView friendVerified;
        public TextView friendName;
        public TextView friendContent;
        public ImageView friendIcon;
        public TextView friendText;
        public RelativeLayout friend_layout;

        public FriendsrViewHolder(View view) {
            super(view);
            friendImg = (ImageView) view.findViewById(R.id.friend_img);
            friendVerified = (ImageView) view.findViewById(R.id.friend_verified);
            friendIcon = (ImageView) view.findViewById(R.id.friendIcon);
            friendName = (TextView) view.findViewById(R.id.friend_name);
            friendContent = (TextView) view.findViewById(R.id.friend_content);
            friendText = (TextView) view.findViewById(R.id.friendText);
            friend_layout = (RelativeLayout) view.findViewById(R.id.friend_layout);

        }
    }
}
