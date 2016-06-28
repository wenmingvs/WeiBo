package com.wenming.weiswift.ui.login.fragment.home.userdetail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenming.weiswift.R;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/2.
 */
public class UserInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mDatas;
    private View mView;


    public UserInfoAdapter(Context context, ArrayList<String> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.user_profile_layout_homepage_item, parent, false);
        HomePageViewHolder homePageViewHolder = new HomePageViewHolder(mView);
        return homePageViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((HomePageViewHolder) holder).property.setText("所在地");
            ((HomePageViewHolder) holder).content.setText(mDatas.get(position));

        } else if (position == 1) {
            ((HomePageViewHolder) holder).property.setText("简介");
            ((HomePageViewHolder) holder).content.setText(mDatas.get(position));
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

    public void setData(ArrayList<String> data) {
        this.mDatas = data;
    }

    public class HomePageViewHolder extends RecyclerView.ViewHolder {
        private TextView property;
        private TextView content;

        public HomePageViewHolder(View view) {
            super(view);
            property = (TextView) view.findViewById(R.id.property);
            content = (TextView) view.findViewById(R.id.content);
        }
    }


}
