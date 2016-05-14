package com.wenming.weiswift.ui.login.fragment.message.mention;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.list.StatusList;
import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.NewFeature;
import com.wenming.weiswift.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wenming.weiswift.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.widget.endlessrecyclerview.IEndlessRecyclerView;
import com.wenming.weiswift.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.weight.LoadingFooter;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.utils.SDCardUtil;
import com.wenming.weiswift.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/26.
 */
public class MentionActivity extends Activity implements IEndlessRecyclerView {
    private ArrayList<Status> mDatas;

    private MentionAdapter mAdapter;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mNoMoreData;

    public Context mContext;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public CommentsAPI mCommentsAPI;
    public StatusesAPI mStatusesAPI;
    public FriendshipsAPI mFriendshipsAPI;
    public boolean mRefrshAllData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagefragment_mention_layout);
        mContext = this;
        initAccessToken();
        initRefreshLayout();
        initRecyclerView();

    }

    public void initAccessToken() {
        mCommentsAPI = new CommentsAPI(this, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));
        mFriendshipsAPI = new FriendshipsAPI(this, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));
    }

    protected void initRefreshLayout() {
        mRefrshAllData = true;
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_swipe_refresh_widget);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshData();
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                pullToRefreshData();
            }
        });
    }


    @Override
    public void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mAdapter = new MentionAdapter(mContext, mDatas);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
    }


    /**
     * 第一次下拉刷新加载的逻辑
     */
    @Override
    public void pullToRefreshData() {
        mSwipeRefreshLayout.setRefreshing(true);
        mStatusesAPI.mentions(0L, 0L, 10, 1, 0, 0, 0, true, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    if (NewFeature.CACHE_MESSAGE_MENTION) {
                        SDCardUtil.put(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "message_mention.txt", response);
                    }
                    mDatas = StatusList.parse(response).statusList;
                    updateList();
                } else {
                    ToastUtil.showShort(mContext, "返回的微博数据为空");
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                if (NewFeature.CACHE_MESSAGE_MENTION) {
                    String response = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "message_mention.txt");
                    if (response != null) {
                        mDatas = StatusList.parse(response).statusList;
                        updateList();
                    }

                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void updateList() {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mAdapter.setData(mDatas);
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }


    @Override
    public void requestMoreData() {
        ToastUtil.showShort(mContext, "加载下拉刷新逻辑");
    }

    @Override
    public void loadMoreData(String string) {

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
            if (!mNoMoreData && mDatas != null) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(MentionActivity.this, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
                requestMoreData();
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
