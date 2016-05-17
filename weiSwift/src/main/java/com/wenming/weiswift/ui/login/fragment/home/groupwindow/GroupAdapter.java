package com.wenming.weiswift.ui.login.fragment.home.groupwindow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Group;
import com.wenming.weiswift.ui.common.login.Constants;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/12.
 */
public class GroupAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<Group> mDatas;
    private Context mContext;
    private View mView;
    private IGroupItemClick mIGroupItemClick;
    private ArrayList<Boolean> mSelectList = new ArrayList<Boolean>();
    private ArrayList<Boolean> mHeadSelectList = new ArrayList<Boolean>();

    private int ITEM_TYPE_HEAD = 0;
    private int ITEM_TYPE_GROUP = 1;

    public GroupAdapter(Context mContext, ArrayList<Group> datas) {
        this.mDatas = datas;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEAD) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.home_grouplist_pop_headview, parent, false);
            HeadViewHolder headViewHolder = new HeadViewHolder(mView);
            mHeadSelectList.add(true);
            mHeadSelectList.add(false);
            return headViewHolder;
        } else if (viewType == ITEM_TYPE_GROUP) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.home_grouplist_pop_item, parent, false);
            GroupViewHolder groupViewHolder = new GroupViewHolder(mView);
            initSelectListt();
            return groupViewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof HeadViewHolder) {
            ((HeadViewHolder) holder).home.setSelected(mHeadSelectList.get(0));
            ((HeadViewHolder) holder).friendCircle.setSelected(mHeadSelectList.get(1));
            ((HeadViewHolder) holder).home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIGroupItemClick.onGroupItemClick(Constants.GROUP_TYPE_ALL, "首页");
                    deSelectAll();
                    ((HeadViewHolder) holder).home.setSelected(true);
                    mHeadSelectList.set(0, true);
                    notifyDataSetChanged();
                }
            });

            ((HeadViewHolder) holder).friendCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIGroupItemClick.onGroupItemClick(Constants.GROUP_TYPE_FRIENDS_CIRCLE, "好友圈");
                    deSelectAll();
                    ((HeadViewHolder) holder).friendCircle.setSelected(true);
                    mHeadSelectList.set(1, true);
                    notifyDataSetChanged();
                }
            });

        } else if (holder instanceof GroupViewHolder) {
            final int newPosition = position - 1;
            ((GroupViewHolder) holder).groupitem.setText(mDatas.get(newPosition).name);
            if (mSelectList.size() > 0 && mSelectList.get(newPosition).compareTo(Boolean.FALSE) == 0) {
                ((GroupViewHolder) holder).groupitem.setSelected(false);
            } else {
                ((GroupViewHolder) holder).groupitem.setSelected(true);
            }
            ((GroupViewHolder) holder).groupitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIGroupItemClick.onGroupItemClick(Long.valueOf(mDatas.get(newPosition).id), mDatas.get(newPosition).name);
                    ((GroupViewHolder) holder).groupitem.setSelected(true);
                    deSelectAll();
                    mSelectList.set(newPosition, true);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size() + 1;
        } else {
            return 1;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEAD;
        } else {
            return ITEM_TYPE_GROUP;
        }
    }

    public void setOnGroupItemClickListener(IGroupItemClick groupItemClickListener) {
        this.mIGroupItemClick = groupItemClickListener;
    }


    private void deSelectAll() {
        mHeadSelectList.set(0, false);
        mHeadSelectList.set(1, false);
        for (int i = 0; i < mDatas.size(); i++) {
            mSelectList.set(i, false);
        }
        notifyDataSetChanged();
    }

    public void initSelectListt() {
        if (mDatas.size() > 0 && mSelectList.size() < mDatas.size()) {
            for (int i = 0; i < mDatas.size(); i++) {
                mSelectList.add(false);
            }
        }
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder {
        public TextView home;
        public LinearLayout friendCircle;
        public TextView friendtext;

        public HeadViewHolder(View v) {
            super(v);
            home = (TextView) mView.findViewById(R.id.allweibo);
            friendCircle = (LinearLayout) mView.findViewById(R.id.bestfriend);
            friendtext = (TextView) mView.findViewById(R.id.friendcircle);
        }
    }


    public class GroupViewHolder extends RecyclerView.ViewHolder {
        public TextView groupitem;

        public GroupViewHolder(View v) {
            super(v);
            groupitem = (TextView) v.findViewById(R.id.groupitem);
        }
    }

}
