package com.wenming.weiswift.fragment.message.comment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Comment;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.FillWeiBoItem;
import com.wenming.weiswift.common.emojitextview.EmojiTextView;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FillWeiBoItem.fillTitleBar(mDatas.get(position), ((CommentViewHolder) holder).profile_img, ((CommentViewHolder) holder).profile_verified, ((CommentViewHolder) holder).profile_name, ((CommentViewHolder) holder).profile_time, ((CommentViewHolder) holder).weibo_comefrom);
        FillWeiBoItem.fillWeiBoContent(mDatas.get(position).text, mContext, ((CommentViewHolder) holder).mention_content);
        FillWeiBoItem.FillCenterContent(mDatas.get(position).status, ((CommentViewHolder) holder).mentionitem_img, ((CommentViewHolder) holder).mentionitem_name, ((CommentViewHolder) holder).mentionitem_content);
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

        public CommentViewHolder(View v) {
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
        }
    }
}
