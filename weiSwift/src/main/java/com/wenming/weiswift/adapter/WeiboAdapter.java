package com.wenming.weiswift.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Status;
import com.wenming.weiswift.R;
import com.wenming.weiswift.util.HttpUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 2015/12/29.
 */
public class WeiboAdapter extends RecyclerView.Adapter<WeiboAdapter.ViewHolder> {
    private ArrayList<Status> mData;
    private Context mContext;

    public WeiboAdapter(ArrayList<Status> datas, Context context) {
        this.mData = datas;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mainfragment_weiboitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //微博用户信息
        holder.profile_name.setText(mData.get(position).user.name);
        holder.profile_time.setText("刚刚   ");
        holder.weiboComeFrom.setText("来自 " + mData.get(position).source);
        //HttpUtil.doGetAsyn(mData.statusList.get(position).user.profile_image_url,HttpCallBack);


        //微博内容
        holder.weibo_Content.setText(mData.get(position).text);


        //微博转发，评论，赞的数量
        holder.comment.setText(mData.get(position).comments_count + "");
        holder.redirect.setText(mData.get(position).reposts_count+ "");
        holder.feedlike.setText(mData.get(position).attitudes_count+ "");

    }



    HttpUtil.CallBack HttpCallBack = new HttpUtil.CallBack() {
        @Override
        public void onPostComplete(String result) {

        }

        @Override
        public void onGetComplete(byte[] result) {
            // mBitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
        }
    };


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_img;
        public TextView profile_name;
        public TextView profile_time;
        public TextView weiboComeFrom;
        public TextView weibo_Content;
        public TextView redirect;
        public TextView comment;
        public TextView feedlike;

        public ViewHolder(View v) {
            super(v);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_name = (TextView) v.findViewById(R.id.profile_name);
            profile_time = (TextView) v.findViewById(R.id.profile_time);
            weibo_Content = (TextView) v.findViewById(R.id.weibo_Content);
            weiboComeFrom = (TextView) v.findViewById(R.id.weiboComeFrom);
            redirect = (TextView) v.findViewById(R.id.redirect);
            comment = (TextView) v.findViewById(R.id.comment);
            feedlike = (TextView) v.findViewById(R.id.feedlike);
        }
    }

    public void setData(ArrayList<Status> data){
        this.mData = data;
    }

}
