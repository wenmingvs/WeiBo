package com.wenming.weiswift.fragment.profile.friends;

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
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.openapi.models.UserList;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.NewFeature;
import com.wenming.weiswift.common.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wenming.weiswift.common.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.common.endlessrecyclerview.IEndlessRecyclerView;
import com.wenming.weiswift.common.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wenming.weiswift.common.endlessrecyclerview.weight.LoadingFooter;
import com.wenming.weiswift.common.login.AccessTokenKeeper;
import com.wenming.weiswift.common.login.Constants;
import com.wenming.weiswift.common.util.NetUtil;
import com.wenming.weiswift.common.util.SDCardUtil;
import com.wenming.weiswift.common.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/1.
 */
public class FriendsActivity extends Activity implements IEndlessRecyclerView {

    public RecyclerView mRecyclerView;
    public FriendsAdapter mAdapter;
    public LinearLayoutManager mLayoutManager;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private ArrayList<User> mDatas;
    private boolean mNoMoreData;
    private String mNext_cursor;

    public Context mContext;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public CommentsAPI mCommentsAPI;
    public StatusesAPI mStatusesAPI;
    public FriendshipsAPI mFriendshipsAPI;
    public boolean mRefrshAllData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_friends_layout);
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
        mAdapter = new FriendsAdapter(mDatas, mContext);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        //RecyclerViewUtils.setHeaderView(mRecyclerView, new SeachHeadView(mContext));
        //mRecyclerView.addItemDecoration(new WeiboItemSapce((int) mContext.getResources().getDimension(R.dimen.home_weiboitem_space)));
    }

    @Override
    public void pullToRefreshData() {
        mSwipeRefreshLayout.setRefreshing(true);
        mNext_cursor = "0";
        mFriendshipsAPI.friends(Long.parseLong(AccessTokenKeeper.readAccessToken(this).getUid()), 50, Integer.valueOf(mNext_cursor), false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                //短时间内疯狂请求数据，服务器会返回数据，但是是空数据。为了防止这种情况出现，要在这里要判空
                if (!TextUtils.isEmpty(response)) {
                    if (NewFeature.CACHE_WEIBOLIST) {
                        SDCardUtil.put(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "我的关注列表缓存.txt", response);
                    }
                    mNext_cursor = UserList.parse(response).next_cursor;
                    mDatas = UserList.parse(response).usersList;
                    updateList();
                } else {
                    ToastUtil.showShort(mContext, "网络请求太快，服务器返回空数据，请注意请求频率");
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                if (NewFeature.CACHE_MESSAGE_COMMENT) {
                    String response = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "我的关注列表缓存.txt");

                    if (response != null) {
                        mDatas = UserList.parse(response).usersList;
                        updateList();

                    }

                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void requestMoreData() {
        mFriendshipsAPI.friends(Long.parseLong(AccessTokenKeeper.readAccessToken(this).getUid()), 50, Integer.valueOf(mNext_cursor), false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    loadMoreData(response);
                } else {
                    ToastUtil.showShort(mContext, "返回的数据为空");
                    mNoMoreData = true;
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(mContext, e.getMessage());
                if (!NetUtil.isConnected(mContext)) {
                    RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.NetWorkError);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void loadMoreData(String string) {
        ArrayList<User> httpRespnse = UserList.parse(string).usersList;
        mNext_cursor = UserList.parse(string).next_cursor;
        mDatas.addAll(httpRespnse);
        updateList();
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
    }

    @Override
    public void updateList() {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mAdapter.setData(mDatas);
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
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
                RecyclerViewStateUtils.setFooterViewState(FriendsActivity.this, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
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
