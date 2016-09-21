package com.wenming.weiswift.ui.login.fragment.message.mention;

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

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.ui.common.FillContent;
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity.OriginPicTextCommentDetailActivity;
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity.RetweetPicTextCommentDetailActivity;
import com.wenming.weiswift.widget.emojitextview.EmojiTextView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/26.
 */
public abstract class MentionAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context mContext;
    private ArrayList<Status> mDatas;
    private View mView;


    public MentionAdapter(Context context, ArrayList<Status> datas) {
        this.mDatas = datas;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.messagefragment_mentionlist_item, parent, false);
        MentionViewHolder mentionViewHolder = new MentionViewHolder(mView);
        return mentionViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        FillContent.fillTitleBar(mContext, mDatas.get(position), ((MentionViewHolder) holder).profile_img, ((MentionViewHolder) holder).profile_verified, ((MentionViewHolder) holder).profile_name, ((MentionViewHolder) holder).profile_time, ((MentionViewHolder) holder).weibo_comefrom);
        FillContent.fillWeiBoContent(mDatas.get(position).text, mContext, ((MentionViewHolder) holder).mention_content);
        FillContent.fillButtonBar(mContext, mDatas.get(position), ((MentionViewHolder) holder).bottombar_retweet, ((MentionViewHolder) holder).bottombar_comment, ((MentionViewHolder) holder).bottombar_attitude, ((MentionViewHolder) holder).comment, ((MentionViewHolder) holder).redirect, ((MentionViewHolder) holder).feedlike);
        ((MentionViewHolder) holder).popover_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrowClick(mDatas.get(position), position);
            }
        });

        //是一条@我的转发的微博
        if (mDatas.get(position).retweeted_status != null) {
            ((MentionViewHolder) holder).mentionitem_layout.setVisibility(View.VISIBLE);
            ((MentionViewHolder) holder).imageList.setVisibility(View.GONE);
            FillContent.fillMentionCenterContent(mDatas.get(position).retweeted_status, ((MentionViewHolder) holder).mentionitem_img, ((MentionViewHolder) holder).mentionitem_name, ((MentionViewHolder) holder).mentionitem_content);
            //微博背景的点击事件
            ((MentionViewHolder) holder).bg_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, RetweetPicTextCommentDetailActivity.class);
                    intent.putExtra("weiboitem", mDatas.get(position));
                    mContext.startActivity(intent);
                }
            });
            ((MentionViewHolder) holder).mentionitem_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDatas.get(position).retweeted_status != null) {
                        Intent intent = new Intent(mContext, OriginPicTextCommentDetailActivity.class);
                        intent.putExtra("weiboitem", mDatas.get(position).retweeted_status);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
        //是一条@我的原创微博
        else {
            ((MentionViewHolder) holder).mentionitem_layout.setVisibility(View.GONE);
            ((MentionViewHolder) holder).imageList.setVisibility(View.VISIBLE);
            FillContent.fillWeiBoImgList(mDatas.get(position), mContext, ((MentionViewHolder) holder).imageList);
            //微博背景的点击事件
            ((MentionViewHolder) holder).bg_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OriginPicTextCommentDetailActivity.class);
                    intent.putExtra("weiboitem", mDatas.get(position));
                    mContext.startActivity(intent);
                }
            });
            ((MentionViewHolder) holder).mentionitem_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDatas.get(position).retweeted_status != null) {
                        Intent intent = new Intent(mContext, OriginPicTextCommentDetailActivity.class);
                        intent.putExtra("weiboitem", mDatas.get(position).retweeted_status);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

    public abstract void arrowClick(Status status, int position);

    @Override
    public int getItemCount() {

        if (mDatas != null) {
            return mDatas.size();
        } else {
            return 0;
        }
    }

    public void setData(ArrayList<Status> data) {
        this.mDatas = data;
    }

    public class MentionViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout bg_layout;
        public ImageView profile_img;
        public ImageView profile_verified;
        public TextView profile_name;
        public TextView profile_time;
        public TextView weibo_comefrom;
        public ImageView popover_arrow;
        public EmojiTextView mention_content;
        public RecyclerView imageList;

        //长方形内的内容
        public LinearLayout mentionitem_layout;
        public ImageView mentionitem_img;
        public TextView mentionitem_name;
        public TextView mentionitem_content;

        //转发栏目
        public TextView redirect;
        public TextView comment;
        public TextView feedlike;

        public LinearLayout bottombar_retweet;
        public LinearLayout bottombar_comment;
        public LinearLayout bottombar_attitude;

        public MentionViewHolder(View v) {
            super(v);
            bg_layout = (LinearLayout) v.findViewById(R.id.bg_layout);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_verified = (ImageView) v.findViewById(R.id.profile_verified);
            profile_name = (TextView) v.findViewById(R.id.profile_name);
            profile_time = (TextView) v.findViewById(R.id.profile_time);
            weibo_comefrom = (TextView) v.findViewById(R.id.weiboComeFrom);
            popover_arrow = (ImageView) v.findViewById(R.id.popover_arrow);

            mention_content = (EmojiTextView) v.findViewById(R.id.mention_content);
            imageList = (RecyclerView) v.findViewById(R.id.weibo_image);

            mentionitem_layout = (LinearLayout) v.findViewById(R.id.mentionitem_layout);
            mentionitem_img = (ImageView) v.findViewById(R.id.mentionitem_img);
            mentionitem_name = (TextView) v.findViewById(R.id.mentionitem_name);
            mentionitem_content = (TextView) v.findViewById(R.id.mentionitem_content);

            redirect = (TextView) v.findViewById(R.id.redirect);
            comment = (TextView) v.findViewById(R.id.comment);
            feedlike = (TextView) v.findViewById(R.id.feedlike);

            bottombar_retweet = (LinearLayout) v.findViewById(R.id.bottombar_retweet);
            bottombar_comment = (LinearLayout) v.findViewById(R.id.bottombar_comment);
            bottombar_attitude = (LinearLayout) v.findViewById(R.id.bottombar_attitude);
        }
    }
}
