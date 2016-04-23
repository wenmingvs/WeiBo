package com.wenming.weiswift.fragment.home.weiboitemdetail.commentdetail.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.User;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.emojitextview.EmojiTextView;
import com.wenming.weiswift.fragment.home.util.FillWeiBoItem;

import java.util.ArrayList;


/**
 * 用于显示评论列表的adapter
 * Created by wenmingvs on 16/4/23.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Comment> mDatas;
    private View mView;


    public CommentAdapter(Context mContext, ArrayList<Comment> datas) {
        this.mContext = mContext;
        this.mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.mainfragment_weiboitem_detail_commentbar_comment_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mDatas.get(position).user;
        String time = mDatas.get(position).created_at;
        String content = mDatas.get(position).text;

        FillWeiBoItem.fillProfileImg(user, holder.profile_img, holder.profile_verified);
        holder.profile_name.setText(user.name);
        FillWeiBoItem.setWeiBoTime(holder.profile_time, time);
        FillWeiBoItem.fillWeiBoContent(content, mContext, holder.commment_content);

    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_img;
        public ImageView profile_verified;
        public TextView profile_name;
        public TextView profile_time;
        public EmojiTextView commment_content;

        public ViewHolder(View v) {
            super(v);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_verified = (ImageView) v.findViewById(R.id.profile_verified);
            profile_name = (TextView) v.findViewById(R.id.comment_profile_name);
            profile_time = (TextView) v.findViewById(R.id.comment_profile_time);
            commment_content = (EmojiTextView) v.findViewById(R.id.comment_content);
        }

    }

}
