package com.wenming.weiswift.ui.login.fragment.profile.friends;

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
public class FriendsAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<User> mDatas = new ArrayList<User>();
    private Context mContext;
    private View mView;


    public FriendsAdapter(ArrayList<User> datas, Context context) {
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        FillContent.fillFriendContent(mContext, mDatas.get(position),
                ((FriendsrViewHolder) holder).friendImg, ((FriendsrViewHolder) holder).friendVerified,
                ((FriendsrViewHolder) holder).follow_me,
                ((FriendsrViewHolder) holder).friendName, ((FriendsrViewHolder) holder).friendContent);

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

    protected class FriendsrViewHolder extends ViewHolder {
        public ImageView friendImg;
        public ImageView friendVerified;
        public ImageView follow_me;
        public TextView friendName;
        public TextView friendContent;

        public FriendsrViewHolder(View view) {
            super(view);
            friendImg = (ImageView) view.findViewById(R.id.friend_img);
            friendVerified = (ImageView) view.findViewById(R.id.friend_verified);
            follow_me = (ImageView) view.findViewById(R.id.follow_me);
            friendName = (TextView) view.findViewById(R.id.friend_name);
            friendContent = (TextView) view.findViewById(R.id.friend_content);

        }
    }
}
