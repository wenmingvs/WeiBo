package com.wenming.weiswift.ui.login.fragment.home.groupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Group;
import com.wenming.weiswift.ui.common.login.Constants;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/19.
 */
public class GroupAdapter extends BaseAdapter {

    private ArrayList<Group> mDatas = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private IGroupItemClick mIGroupItemClick;
    private static final int ITEM_TYPE_HEAD_FIRST = 0;
    private static final int ITEM_TYPE_HEAD_SECOND = 1;
    private static final int ITEM_TYPE_GROUP = 2;
    private ArrayList<Boolean> mSelectList = new ArrayList<Boolean>();
    private ArrayList<Group> datas;

    public GroupAdapter(Context context, ArrayList<Group> mDatas) {
        this.mDatas = mDatas;
        initSelectList();
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Group getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEAD_FIRST;
        } else if (position == 1) {
            return ITEM_TYPE_HEAD_SECOND;
        } else {
            return ITEM_TYPE_GROUP;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FirstHeadHolder firstHeadHolder = null;
        SecondHeadHolder secondHeadHolder = null;
        GroupViewHolder groupViewHolder = null;
        final int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case ITEM_TYPE_HEAD_FIRST:
                    convertView = layoutInflater.inflate(R.layout.home_grouplist_pop_firstheadview, parent, false);
                    firstHeadHolder = new FirstHeadHolder(convertView);
                    convertView.setTag(firstHeadHolder);
                    break;
                case ITEM_TYPE_HEAD_SECOND:
                    convertView = layoutInflater.inflate(R.layout.home_grouplist_pop_secondheadview, parent, false);
                    secondHeadHolder = new SecondHeadHolder(convertView);
                    convertView.setTag(secondHeadHolder);
                    break;

                case ITEM_TYPE_GROUP:
                    convertView = layoutInflater.inflate(R.layout.home_grouplist_pop_item, parent, false);
                    groupViewHolder = new GroupViewHolder(convertView);
                    convertView.setTag(groupViewHolder);
            }
        } else {
            switch (type) {
                case ITEM_TYPE_HEAD_FIRST:
                    firstHeadHolder = (FirstHeadHolder) convertView.getTag();
                    break;

                case ITEM_TYPE_HEAD_SECOND:
                    secondHeadHolder = (SecondHeadHolder) convertView.getTag();
                    break;

                case ITEM_TYPE_GROUP:
                    groupViewHolder = (GroupViewHolder) convertView.getTag();
                    break;
            }
        }

        switch (type) {
            case ITEM_TYPE_HEAD_FIRST:
                firstHeadHolder.home.setSelected(mSelectList.get(0));
                firstHeadHolder.home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIGroupItemClick.onGroupItemClick(position, Constants.GROUP_TYPE_ALL, "首页");
                        deSelectAll();
                        ((TextView) v).setSelected(true);
                        mSelectList.set(0, true);
                        notifyDataSetChanged();
                    }
                });
                break;
            case ITEM_TYPE_HEAD_SECOND:
                secondHeadHolder.friendCircle.setSelected(mSelectList.get(1));
                secondHeadHolder.friendCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIGroupItemClick.onGroupItemClick(position, Constants.GROUP_TYPE_FRIENDS_CIRCLE, "好友圈");
                        deSelectAll();
                        ((LinearLayout) v).setSelected(true);
                        mSelectList.set(1, true);
                        notifyDataSetChanged();
                    }
                });
                break;

            case ITEM_TYPE_GROUP:
                groupViewHolder.groupitem.setText(mDatas.get(position).name);
                if (mSelectList.size() > 2 && mSelectList.get(position).compareTo(Boolean.FALSE) == 0) {
                    groupViewHolder.groupitem.setSelected(false);
                } else {
                    groupViewHolder.groupitem.setSelected(true);
                }
                groupViewHolder.groupitem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIGroupItemClick.onGroupItemClick(position, Long.valueOf(mDatas.get(position).id), mDatas.get(position).name);
                        deSelectAll();
                        ((TextView) v).setSelected(true);
                        mSelectList.set(position, true);
                        notifyDataSetChanged();
                    }
                });
                break;
        }
        return convertView;
    }


    public void setOnGroupItemClickListener(IGroupItemClick groupItemClickListener) {
        this.mIGroupItemClick = groupItemClickListener;
    }

    private void deSelectAll() {
        for (int i = 0; i < mSelectList.size(); i++) {
            mSelectList.set(i, false);
        }
    }


    public void initSelectList() {
        if (mSelectList.size() == mDatas.size()) {
            return;
        }
        if (mDatas.size() > 0 && mSelectList.size() < mDatas.size()) {
            mSelectList.clear();
            for (int i = 0; i < mDatas.size(); i++) {
                if (i == 0) {
                    mSelectList.add(i, true);
                } else {
                    mSelectList.add(false);
                }
            }
        }
    }

    public void setDatas(ArrayList<Group> datas) {
        this.datas = datas;
        initSelectList();
    }

    public class FirstHeadHolder {
        public TextView home;

        public FirstHeadHolder(View view) {
            home = (TextView) view.findViewById(R.id.allweibo);
        }
    }

    public class SecondHeadHolder {
        public TextView home;
        public LinearLayout friendCircle;

        public SecondHeadHolder(View view) {
            home = (TextView) view.findViewById(R.id.allweibo);
            friendCircle = (LinearLayout) view.findViewById(R.id.bestfriend);
        }
    }

    protected class GroupViewHolder {
        private TextView groupitem;

        public GroupViewHolder(View view) {
            groupitem = (TextView) view.findViewById(R.id.groupitem);
        }
    }


}
