package com.wenming.weiswift.ui.login.fragment.home.weiboitem;

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
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity.OriginPicTextCommentActivity;
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity.RetweetPicTextCommentActivity;
import com.wenming.weiswift.widget.emojitextview.EmojiTextView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 2015/12/29.
 */
public class WeiboAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int TYPE_ORINGIN_ITEM = 0;
    private static final int TYPE_RETWEET_ITEM = 3;

    private ArrayList<Status> mDatas;
    private Context mContext;
    private View mView;


    public WeiboAdapter(ArrayList<Status> datas, Context context) {
        this.mDatas = datas;
        this.mContext = context;
    }

    /**
     * 设置图片间距，注意要保证addItemDecoration只被调用一次，多次调用间距会累加
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ORINGIN_ITEM) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.mainfragment_weiboitem_original_pictext, parent, false);
            OriginViewHolder originViewHolder = new OriginViewHolder(mView);
            return originViewHolder;
        } else if (viewType == TYPE_RETWEET_ITEM) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.mainfragment_weiboitem_retweet_pictext, parent, false);
            RetweetViewHolder retweetViewHolder = new RetweetViewHolder(mView);
            return retweetViewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof OriginViewHolder) {

            FillContent.fillTitleBar(mContext, mDatas.get(position), ((OriginViewHolder) holder).profile_img, ((OriginViewHolder) holder).profile_verified, ((OriginViewHolder) holder).profile_name, ((OriginViewHolder) holder).profile_time, ((OriginViewHolder) holder).weibo_comefrom);
            FillContent.fillWeiBoContent(mDatas.get(position).text, mContext, ((OriginViewHolder) holder).weibo_content);
            FillContent.fillButtonBar(mContext, mDatas.get(position), ((OriginViewHolder) holder).bottombar_retweet, ((OriginViewHolder) holder).bottombar_comment, ((OriginViewHolder) holder).bottombar_attitude, ((OriginViewHolder) holder).comment, ((OriginViewHolder) holder).redirect, ((OriginViewHolder) holder).feedlike);
            FillContent.fillWeiBoImgList(mDatas.get(position), mContext, ((OriginViewHolder) holder).imageList);

            //微博背景的点击事件
            ((OriginViewHolder) holder).origin_weibo_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OriginPicTextCommentActivity.class);
                    intent.putExtra("weiboitem", mDatas.get(position));
                    mContext.startActivity(intent);
                }
            });

        } else if (holder instanceof RetweetViewHolder) {

            FillContent.fillTitleBar(mContext, mDatas.get(position), ((RetweetViewHolder) holder).profile_img, ((RetweetViewHolder) holder).profile_verified, ((RetweetViewHolder) holder).profile_name, ((RetweetViewHolder) holder).profile_time, ((RetweetViewHolder) holder).weibo_comefrom);
            FillContent.fillRetweetContent(mDatas.get(position), mContext, ((RetweetViewHolder) holder).origin_nameAndcontent);
            FillContent.fillWeiBoContent(mDatas.get(position).text, mContext, ((RetweetViewHolder) holder).retweet_content);
            FillContent.fillButtonBar(mContext, mDatas.get(position), ((RetweetViewHolder) holder).bottombar_retweet, ((RetweetViewHolder) holder).bottombar_comment, ((RetweetViewHolder) holder).bottombar_attitude, ((RetweetViewHolder) holder).comment, ((RetweetViewHolder) holder).redirect, ((RetweetViewHolder) holder).feedlike);
            FillContent.fillWeiBoImgList(mDatas.get(position).retweeted_status, mContext, ((RetweetViewHolder) holder).retweet_imageList);

            //微博背景的点击事件
            ((RetweetViewHolder) holder).retweet_weibo_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, RetweetPicTextCommentActivity.class);
                    intent.putExtra("weiboitem", mDatas.get(position));
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        } else {
            return 0;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position).retweeted_status != null && mDatas.get(position).retweeted_status.user != null) {
            return TYPE_RETWEET_ITEM;
        } else {
            return TYPE_ORINGIN_ITEM;
        }
    }

    public void setData(ArrayList<Status> data) {
        this.mDatas = data;
    }

    public static class OriginViewHolder extends ViewHolder {
        public LinearLayout origin_weibo_layout;
        public ImageView profile_img;
        public ImageView profile_verified;
        public TextView profile_name;
        public TextView profile_time;
        public TextView weibo_comefrom;
        public EmojiTextView weibo_content;
        public TextView redirect;
        public TextView comment;
        public TextView feedlike;
        public RecyclerView imageList;
        public LinearLayout bottombar_retweet;
        public LinearLayout bottombar_comment;
        public LinearLayout bottombar_attitude;

        public OriginViewHolder(View v) {
            super(v);
            origin_weibo_layout = (LinearLayout) v.findViewById(R.id.origin_weibo_layout);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_verified = (ImageView) v.findViewById(R.id.profile_verified);
            profile_name = (TextView) v.findViewById(R.id.profile_name);
            profile_time = (TextView) v.findViewById(R.id.profile_time);
            weibo_content = (EmojiTextView) v.findViewById(R.id.weibo_Content);
            weibo_comefrom = (TextView) v.findViewById(R.id.weiboComeFrom);
            redirect = (TextView) v.findViewById(R.id.redirect);
            comment = (TextView) v.findViewById(R.id.comment);
            feedlike = (TextView) v.findViewById(R.id.feedlike);
            imageList = (RecyclerView) v.findViewById(R.id.weibo_image);
            bottombar_retweet = (LinearLayout) v.findViewById(R.id.bottombar_retweet);
            bottombar_comment = (LinearLayout) v.findViewById(R.id.bottombar_comment);
            bottombar_attitude = (LinearLayout) v.findViewById(R.id.bottombar_attitude);
        }
    }

    public static class RetweetViewHolder extends ViewHolder {
        public LinearLayout retweet_weibo_layout;
        public ImageView profile_img;
        public ImageView profile_verified;
        public TextView profile_name;
        public TextView profile_time;
        public TextView weibo_comefrom;
        public EmojiTextView retweet_content;
        public TextView redirect;
        public TextView comment;
        public TextView feedlike;
        public EmojiTextView origin_nameAndcontent;
        public RecyclerView retweet_imageList;
        public LinearLayout bottombar_retweet;
        public LinearLayout bottombar_comment;
        public LinearLayout bottombar_attitude;


        public RetweetViewHolder(View v) {
            super(v);
            retweet_weibo_layout = (LinearLayout) v.findViewById(R.id.retweet_weibo_layout);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_verified = (ImageView) v.findViewById(R.id.profile_verified);
            profile_name = (TextView) v.findViewById(R.id.profile_name);
            profile_time = (TextView) v.findViewById(R.id.profile_time);
            retweet_content = (EmojiTextView) v.findViewById(R.id.retweet_content);
            weibo_comefrom = (TextView) v.findViewById(R.id.weiboComeFrom);
            redirect = (TextView) v.findViewById(R.id.redirect);
            comment = (TextView) v.findViewById(R.id.comment);
            feedlike = (TextView) v.findViewById(R.id.feedlike);
            origin_nameAndcontent = (EmojiTextView) v.findViewById(R.id.origin_nameAndcontent);
            retweet_imageList = (RecyclerView) v.findViewById(R.id.origin_imageList);
            bottombar_retweet = (LinearLayout) v.findViewById(R.id.bottombar_retweet);
            bottombar_comment = (LinearLayout) v.findViewById(R.id.bottombar_comment);
            bottombar_attitude = (LinearLayout) v.findViewById(R.id.bottombar_attitude);
        }
    }

}
