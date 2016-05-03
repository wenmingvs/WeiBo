package com.wenming.weiswift.fragment.message.mention;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Status;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.FillContent;
import com.wenming.weiswift.common.emojitextview.EmojiTextView;
import com.wenming.weiswift.fragment.home.weiboitem.WeiboAdapter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/26.
 */
public class MentionAdapter extends RecyclerView.Adapter<ViewHolder> {

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        FillContent.fillTitleBar(mContext,mDatas.get(position), ((MentionViewHolder) holder).profile_img, ((MentionViewHolder) holder).profile_verified, ((MentionViewHolder) holder).profile_name, ((MentionViewHolder) holder).profile_time, ((MentionViewHolder) holder).weibo_comefrom);
        FillContent.fillWeiBoContent(mDatas.get(position).text, mContext, ((MentionViewHolder) holder).mention_content);
        FillContent.FillCenterContent(mDatas.get(position).retweeted_status, ((MentionViewHolder) holder).mentionitem_img, ((MentionViewHolder) holder).mentionitem_name, ((MentionViewHolder) holder).mentionitem_content);
        FillContent.fillButtonBar(mContext,mDatas.get(position), ((WeiboAdapter.OriginViewHolder) holder).bottombar_retweet, ((WeiboAdapter.OriginViewHolder) holder).bottombar_comment, ((WeiboAdapter.OriginViewHolder) holder).bottombar_attitude, ((MentionViewHolder) holder).comment, ((MentionViewHolder) holder).redirect, ((MentionViewHolder) holder).feedlike);


    }

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

        public ImageView profile_img;
        public ImageView profile_verified;
        public TextView profile_name;
        public TextView profile_time;
        public TextView weibo_comefrom;
        public ImageView popover_arrow;

        public EmojiTextView mention_content;

        //长方形内的内容
        public ImageView mentionitem_img;
        public TextView mentionitem_name;
        public TextView mentionitem_content;

        //转发栏目
        public TextView redirect;
        public TextView comment;
        public TextView feedlike;

        public MentionViewHolder(View v) {
            super(v);

            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_verified = (ImageView) v.findViewById(R.id.profile_verified);
            profile_name = (TextView) v.findViewById(R.id.profile_name);
            profile_time = (TextView) v.findViewById(R.id.profile_time);
            weibo_comefrom = (TextView) v.findViewById(R.id.weiboComeFrom);
            popover_arrow = (ImageView) v.findViewById(R.id.popover_arrow);

            mention_content = (EmojiTextView) v.findViewById(R.id.mention_content);

            mentionitem_img = (ImageView) v.findViewById(R.id.mentionitem_img);
            mentionitem_name = (TextView) v.findViewById(R.id.mentionitem_name);
            mentionitem_content = (TextView) v.findViewById(R.id.mentionitem_content);

            redirect = (TextView) v.findViewById(R.id.redirect);
            comment = (TextView) v.findViewById(R.id.comment);
            feedlike = (TextView) v.findViewById(R.id.feedlike);

        }
    }
}
