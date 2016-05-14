package com.wenming.weiswift.ui.login.fragment.profile.followers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.FillContent;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/1.
 */
public class FollowerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<User> mDatas = new ArrayList<User>();
    private Context mContext;
    private View mView;


    public FollowerAdapter(ArrayList<User> datas, Context context) {
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FillContent.fillFollowContent(mContext, mDatas.get(position),
                ((FollowerViewHolder) holder).followerImg, ((FollowerViewHolder) holder).followerVerf,
                ((FollowerViewHolder) holder).followerName, ((FollowerViewHolder) holder).follower_firstcontent,
                ((FollowerViewHolder) holder).profile_comefrom, ((FollowerViewHolder) holder).follwerRelation);

    }

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
        public ImageView follwerRelation;

        public FollowerViewHolder(View view) {
            super(view);
            followerImg = (ImageView) view.findViewById(R.id.follower_img);
            followerVerf = (ImageView) view.findViewById(R.id.follower_verified);
            followerName = (TextView) view.findViewById(R.id.follower_name);
            follower_firstcontent = (TextView) view.findViewById(R.id.follower_firstcontent);
            profile_comefrom = (TextView) view.findViewById(R.id.profile_comefrom);
            follwerRelation = (ImageView) view.findViewById(R.id.follwer_relation);
        }
    }
}
