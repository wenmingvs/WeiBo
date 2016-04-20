package com.wenming.weiswift.fragment.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.common.DensityUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/1/4.
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context mContext;
    private ArrayList<Integer> mData;
    private ArrayList<String> mText;

    public MessageAdapter(Context mContext, ArrayList<Integer> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mText = new ArrayList<String>();
        mText.add("@我的");
        mText.add("评论");
        mText.add("赞");
        mText.add("订阅消息");
        mText.add("未关注的消息");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.headsearchview, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(mContext, 40));
            view.setLayoutParams(params);
            SearchViewHolder viewHolder = new SearchViewHolder(view);
            return viewHolder;
        } else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.messagefragment_layout_item, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageAdapter.ItemViewHolder) {
            ((ItemViewHolder) holder).textView.setText(mText.get(position - 1));
            ((ItemViewHolder) holder).imageView.setImageResource(mData.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.message_icon);
            textView = (TextView) itemView.findViewById(R.id.message_title);
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder {

        public SearchViewHolder(View itemView) {
            super(itemView);
        }
    }

}
