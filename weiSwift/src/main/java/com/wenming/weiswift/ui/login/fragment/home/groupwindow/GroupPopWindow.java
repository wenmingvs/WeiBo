package com.wenming.weiswift.ui.login.fragment.home.groupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Group;
import com.wenming.weiswift.mvp.presenter.GroupListPresenter;
import com.wenming.weiswift.mvp.presenter.imp.GroupListPresenterImp;
import com.wenming.weiswift.mvp.view.GroupPopWindowView;
import com.wenming.weiswift.utils.ToastUtil;
import com.wenming.weiswift.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/12.
 */
public class GroupPopWindow extends PopupWindow implements GroupPopWindowView {

    private RecyclerView mRecyclerView;
    private Context mContext;
    private ArrayList<Group> mDatas;
    private View mView;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAdapter;
    private TextView mGroupEdit;
    private GroupAdapter mAdapter;
    private int mWidth;
    private int mHeight;
    private final GroupListPresenter mGroupListPresenter;

    public GroupPopWindow(Context context, int width, int height) {
        super(context);
        this.mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.home_grouplist_pop, null);
        setContentView(mView);
        mWidth = width;
        mHeight = height;
        initPopWindow();
        initListView();
        setUpListener();
        mGroupListPresenter = new GroupListPresenterImp(this);
        mGroupListPresenter.updateListView(mContext);
    }

    private void initPopWindow() {
        this.setWidth(mWidth);
        this.setHeight(mHeight);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    private void initListView() {
        mDatas = new ArrayList<Group>();
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
        mAdapter = new GroupAdapter(mContext, mDatas);
        mHeaderAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        mHeaderAdapter.addHeaderView(new GroupHeadView(mContext));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHeaderAdapter);

    }


    private void setUpListener() {
        mGroupEdit = (TextView) mView.findViewById(R.id.editgroup);
        mGroupEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "编辑我的分组");
            }
        });
    }


    @Override
    public void updateListView(ArrayList<Group> datas) {
        mDatas.addAll(datas);
        mHeaderAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String error) {
        ToastUtil.showShort(mContext, error);
    }


}
