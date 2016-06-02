package com.wenming.weiswift.ui.login.fragment.message.comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cesards.cropimageview.CropImageView;
import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Comment;
import com.wenming.weiswift.ui.common.FillContent;
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity.OriginPicTextCommentActivity;
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity.RetweetPicTextCommentActivity;
import com.wenming.weiswift.widget.emojitextview.EmojiTextView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/26.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Comment> mDatas;
    private View mView;


    public CommentAdapter(Context context, ArrayList<Comment> datas) {
        this.mDatas = datas;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.messagefragment_commentlist_item, parent, false);
        CommentViewHolder mentionViewHolder = new CommentViewHolder(mView);
        return mentionViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        FillContent.fillTitleBar(mContext, mDatas.get(position), ((CommentViewHolder) holder).profile_img, ((CommentViewHolder) holder).profile_verified, ((CommentViewHolder) holder).profile_name, ((CommentViewHolder) holder).profile_time, ((CommentViewHolder) holder).weibo_comefrom);
        FillContent.fillWeiBoContent(mDatas.get(position).text, mContext, ((CommentViewHolder) holder).comment_content);
        FillContent.fillCommentCenterContent(mContext, mDatas.get(position), ((CommentViewHolder) holder).bg_layout, ((CommentViewHolder) holder).comment_weibolayout, ((CommentViewHolder) holder).comment_reply, ((CommentViewHolder) holder).mentionitem_img, ((CommentViewHolder) holder).mentionitem_name, ((CommentViewHolder) holder).mentionitem_content);

        //整个背景的点击事件
        ((CommentViewHolder) holder).comment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDatas.get(position).status.retweeted_status == null) {
                    Intent intent = new Intent(mContext, OriginPicTextCommentActivity.class);
                    intent.putExtra("weiboitem", mDatas.get(position).status);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, RetweetPicTextCommentActivity.class);
                    intent.putExtra("weiboitem", mDatas.get(position).status);
                    mContext.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {

        if (mDatas != null) {
            return mDatas.size();
        } else {
            return 0;
        }
    }

    public void setData(ArrayList<Comment> data) {
        this.mDatas = data;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout comment_layout;
        public ImageView profile_img;
        public ImageView profile_verified;
        public TextView profile_name;
        public TextView profile_time;
        public TextView weibo_comefrom;
        public ImageView popover_arrow;


        public EmojiTextView comment_content;

        //长方形内的内容
        public LinearLayout bg_layout;
        public EmojiTextView comment_reply;
        public CropImageView mentionitem_img;
        public LinearLayout comment_weibolayout;
        public TextView mentionitem_name;
        public TextView mentionitem_content;


        public CommentViewHolder(View v) {
            super(v);
            comment_layout = (LinearLayout) v.findViewById(R.id.comment_layout);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_verified = (ImageView) v.findViewById(R.id.profile_verified);
            profile_name = (TextView) v.findViewById(R.id.profile_name);
            profile_time = (TextView) v.findViewById(R.id.profile_time);
            weibo_comefrom = (TextView) v.findViewById(R.id.weiboComeFrom);
            popover_arrow = (ImageView) v.findViewById(R.id.popover_arrow);

            comment_content = (EmojiTextView) v.findViewById(R.id.commentitem_content);


            bg_layout = (LinearLayout) v.findViewById(R.id.bg_layout);
            comment_reply = (EmojiTextView) v.findViewById(R.id.commentitem_reply);
            mentionitem_img = (CropImageView) v.findViewById(R.id.commentitem_img);
            comment_weibolayout = (LinearLayout) v.findViewById(R.id.comment_weibolayout);
            mentionitem_name = (TextView) v.findViewById(R.id.commentitem_name);
            mentionitem_content = (TextView) v.findViewById(R.id.comment_weibocontent);
        }
    }
}
