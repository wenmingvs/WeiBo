package com.wenming.weiswift.ui.login.fragment.home;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.presenter.HomePresent;
import com.wenming.weiswift.mvp.presenter.imp.HomePresentImp;
import com.wenming.weiswift.mvp.view.HomeView;
import com.wenming.weiswift.ui.login.fragment.home.groupwindow.GroupPop;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.SeachHeadView;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.WeiboAdapter;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.WeiboItemSapce;
import com.wenming.weiswift.utils.DensityUtil;
import com.wenming.weiswift.utils.ScreenUtil;
import com.wenming.weiswift.utils.ToastUtil;
import com.wenming.weiswift.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wenming.weiswift.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.widget.endlessrecyclerview.RecyclerViewUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.weight.LoadingFooter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class HomeFragment extends Fragment implements HomeView {

    private ArrayList<Status> mDatas;
    public Context mContext;
    public Activity mActivity;
    public View mView;
    private LinearLayout mGroup;
    public RecyclerView mRecyclerView;
    public TextView mUserName;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public WeiboAdapter mAdapter;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private HomePresent mHomePresent;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        mContext = getActivity();
        mHomePresent = new HomePresentImp(this);
        mView = inflater.inflate(R.layout.mainfragment_layout, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.weiboRecyclerView);
        mGroup = (LinearLayout) mView.findViewById(R.id.group);
        mUserName = (TextView) mView.findViewById(R.id.name);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_widget);
        initRecyclerView();
        initRefreshLayout();
        initGroupWindows();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mHomePresent.refreshUserName(mContext);
                mHomePresent.firstLoadData(mContext, mActivity.getIntent().getBooleanExtra("fisrtstart", false));
            }
        });
        return mView;
    }


    public void initRecyclerView() {
        mDatas = new ArrayList<Status>();
        mAdapter = new WeiboAdapter(mDatas, mContext);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        RecyclerViewUtils.setHeaderView(mRecyclerView, new SeachHeadView(mContext));
        mRecyclerView.addItemDecoration(new WeiboItemSapce((int) mContext.getResources().getDimension(R.dimen.home_weiboitem_space)));
    }


    private void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHomePresent.pullToRefreshData(mContext);
            }
        });
    }

    private void initGroupWindows() {
        mGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rect rect = new Rect();
                getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int statusBarHeight = rect.top;
                GroupPop popWindow = new GroupPop(mContext, ScreenUtil.getScreenWidth(mContext) * 3 / 5, ScreenUtil.getScreenHeight(mContext) * 2 / 3);
                popWindow.showAtLocation(mUserName, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, mUserName.getHeight() + statusBarHeight + DensityUtil.dp2px(mContext, 8));
            }
        });
    }

    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mHomePresent.pullToRefreshData(mContext);
            }
        });
    }


    @Override
    public void updateListView(ArrayList<Status> statuselist) {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mDatas = statuselist;
        mAdapter.setData(statuselist);
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void toggleRefreshIcon() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void showLoadingIcon() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIcon() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mActivity, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
    }

    @Override
    public void hideFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
    }

    @Override
    public void showEndFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.TheEnd);
    }

    @Override
    public void showErrorFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.NetWorkError);
    }


    @Override
    public void setUserName(String userName) {
        mUserName.setText(userName);
        mGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToast(String content) {
        ToastUtil.showShort(mContext, content);
    }

    public EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
            if (state == LoadingFooter.State.Loading) {
                Log.d("wenming", "the state is Loading, just wait..");
                return;
            }
            if (mDatas != null && mDatas.size() > 0) {
                // loading more
                showLoadFooterView();
                mHomePresent.requestMoreData(mContext);
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    ImageLoader.getInstance().pause();
                    break;
                case RecyclerView.SCROLL_STATE_IDLE:
                    ImageLoader.getInstance().resume();
                    break;
            }
        }
    };
}
