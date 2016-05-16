package com.wenming.weiswift.ui.login.fragment.home.groupwindow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Group;
import com.wenming.weiswift.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/12.
 */
public class GroupAdapter extends RecyclerView.Adapter<ViewHolder> implements IGroupItemClick {

    private ArrayList<Group> mDatas;
    private Context mContext;
    private View mView;

    public GroupAdapter(Context mContext, ArrayList<Group> datas) {
        this.mDatas = datas;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.home_grouplist_pop_item, parent, false);
        GroupViewHolder viewHolder = new GroupViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((GroupViewHolder) holder).groupitem.setText(mDatas.get(position).name);
        ((GroupViewHolder) holder).groupitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ToastUtil.showShort(mContext, "...");
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

    @Override
    public void onGroupItemClick(int groupId) {

    }


    public class GroupViewHolder extends RecyclerView.ViewHolder {
        public TextView groupitem;

        public GroupViewHolder(View v) {
            super(v);
            groupitem = (TextView) v.findViewById(R.id.groupitem);
        }
    }


}
