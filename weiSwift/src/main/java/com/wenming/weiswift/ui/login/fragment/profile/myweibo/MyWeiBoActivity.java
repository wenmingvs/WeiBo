package com.wenming.weiswift.ui.login.fragment.profile.myweibo;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.api.StatusesAPI;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.presenter.MyWeiBoActivityPresent;
import com.wenming.weiswift.mvp.presenter.imp.MyWeiBoActivityPresentImp;
import com.wenming.weiswift.mvp.view.MyWeiBoActivityView;
import com.wenming.weiswift.ui.common.BaseSwipeActivity;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.SeachHeadView;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.WeiboAdapter;
import com.wenming.weiswift.ui.login.fragment.message.IGroupItemClick;
import com.wenming.weiswift.utils.DensityUtil;
import com.wenming.weiswift.utils.ScreenUtil;
import com.wenming.weiswift.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wenming.weiswift.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.widget.endlessrecyclerview.RecyclerViewUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.weight.LoadingFooter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class MyWeiBoActivity extends BaseSwipeActivity implements MyWeiBoActivityView {
    public WeiboAdapter mAdapter;
    private ArrayList<Status> mDatas;
    public Context mContext;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public StatusesAPI mStatusesAPI;
    public boolean mRefrshAllData;
    private LinearLayout mGroup;
    private TextView mGroupName;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private MyWeiBoActivityPresent mMyWeiBoActivityPresent;
    private MyWeiBoPopWindow mMyWeiBoPopWindow;
    private int mCurrentGroup = Constants.GROUP_MYWEIBO_TYPE_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.profilefragment_myweibo_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_swipe_refresh_widget);
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mGroup = (LinearLayout) findViewById(R.id.myweibo_group);
        mGroupName = (TextView) findViewById(R.id.myweibo_name);
        mMyWeiBoActivityPresent = new MyWeiBoActivityPresentImp(this);
        initRefreshLayout();
        initRecyclerView();
        initGroupWindows();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mMyWeiBoActivityPresent.pullToRefreshData(Long.valueOf(AccessTokenKeeper.readAccessToken(mContext).getUid()), mCurrentGroup, mContext);
            }
        });
    }

    protected void initRefreshLayout() {
        mRefrshAllData = true;
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMyWeiBoActivityPresent.pullToRefreshData(Long.valueOf(AccessTokenKeeper.readAccessToken(mContext).getUid()), mCurrentGroup, mContext);
            }
        });
    }


    public void initRecyclerView() {
        mAdapter = new WeiboAdapter(mDatas, mContext) {
            @Override
            public void arrowClick(Status status, int position) {
                MyWeiBoArrowWindow popupWindow = new MyWeiBoArrowWindow(mContext, mDatas.get(position), mAdapter, position, "我的" + mGroupName.getText().toString());
                popupWindow.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);
            }
        };
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        RecyclerViewUtils.setHeaderView(mRecyclerView, new SeachHeadView(mContext));
        //mRecyclerView.addItemDecoration(new WeiboItemSapce((int) mContext.getResources().getDimension(R.dimen.home_weiboitem_space)));
    }

    private void initGroupWindows() {
        mGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int statusBarHeight = rect.top;
                mMyWeiBoPopWindow = MyWeiBoPopWindow.getInstance(mContext, ScreenUtil.getScreenWidth(mContext) * 3 / 5, ScreenUtil.getScreenHeight(mContext) * 2 / 3);
                mMyWeiBoPopWindow.showAtLocation(mGroupName, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, mGroupName.getHeight() + statusBarHeight + DensityUtil.dp2px(mContext, 8));
                mMyWeiBoPopWindow.setOnGroupItemClickListener(new IGroupItemClick() {
                    @Override
                    public void onGroupItemClick(long groupId, String groupName) {
                        mCurrentGroup = (int) groupId;
                        setGroupName(groupName);
                        mMyWeiBoPopWindow.dismiss();
                        mMyWeiBoActivityPresent.pullToRefreshData(Long.valueOf(AccessTokenKeeper.readAccessToken(mContext).getUid()), mCurrentGroup, mContext);
                    }
                });
            }
        });
    }


    public void setGroupName(String groupName) {
        mGroupName.setText(groupName);
    }

    public EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (mDatas != null && mDatas.size() > 0) {
                showLoadFooterView();
                mMyWeiBoActivityPresent.requestMoreData(Long.valueOf(AccessTokenKeeper.readAccessToken(mContext).getUid()), mCurrentGroup, mContext);
            }
        }
    };

    public void onArrorClick(View view) {
        finish();
    }

    @Override
    public void updateListView(ArrayList<Status> statuselist) {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mDatas = statuselist;
        mAdapter.setData(statuselist);
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
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
        RecyclerViewStateUtils.setFooterViewState(MyWeiBoActivity.this, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
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
    public void scrollToTop(boolean refreshData) {
        mRecyclerView.scrollToPosition(0);
        if (refreshData) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mMyWeiBoActivityPresent.pullToRefreshData(Long.valueOf(AccessTokenKeeper.readAccessToken(mContext).getUid()), mCurrentGroup, mContext);
                }
            });
        }
    }

    @Override
    public void delete_item(int positon) {

    }

    @Override
    protected void onDestroy() {
        if (mMyWeiBoPopWindow != null) {
            mMyWeiBoPopWindow.onDestory();
        }
        super.onDestroy();
    }
}
