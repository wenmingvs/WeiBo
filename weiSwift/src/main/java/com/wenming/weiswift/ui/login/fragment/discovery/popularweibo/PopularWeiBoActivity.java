package com.wenming.weiswift.ui.login.fragment.discovery.popularweibo;

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
import com.wenming.weiswift.widget.endlessrecyclerview.RecyclerViewUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.weight.LoadingFooter;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.utils.NetUtil;
import com.wenming.weiswift.utils.SDCardUtil;
import com.wenming.weiswift.utils.ToastUtil;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.SeachHeadView;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.WeiboAdapter;
import com.wenming.weiswift.ui.login.fragment.home.weiboitem.WeiboItemSapce;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class PopularWeiBoActivity extends Activity implements IEndlessRecyclerView {

    public WeiboAdapter mAdapter;
    public LinearLayoutManager mLayoutManager;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private ArrayList<Status> mDatas;
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
        setContentView(R.layout.discover_popweibo_layout);
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
        mAdapter = new WeiboAdapter(mDatas, mContext);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        RecyclerViewUtils.setHeaderView(mRecyclerView, new SeachHeadView(mContext));
        mRecyclerView.addItemDecoration(new WeiboItemSapce((int) mContext.getResources().getDimension(R.dimen.home_weiboitem_space)));
    }


    @Override
    public void pullToRefreshData() {
        mSwipeRefreshLayout.setRefreshing(true);
        mStatusesAPI.publicTimeline(NewFeature.GET_PUBLICWEIBO_NUMS, 1, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                //短时间内疯狂请求数据，服务器会返回数据，但是是空数据。为了防止这种情况出现，要在这里要判空
                if (!TextUtils.isEmpty(response)) {
                    if (NewFeature.CACHE_WEIBOLIST) {
                        SDCardUtil.put(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "我的微博列表缓存.txt", response);
                    }
                    mDatas = StatusList.parse(response).statusList;
                    updateList();
                } else {
                    ToastUtil.showShort(mContext, "网络请求太快，服务器返回空数据，请注意请求频率");
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                if (NewFeature.CACHE_MESSAGE_COMMENT) {
                    String response = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "我的微博列表缓存.txt");
                    if (response != null) {
                        mDatas = StatusList.parse(response).statusList;
                        updateList();
                    }

                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void requestMoreData() {
        mStatusesAPI.publicTimeline(NewFeature.LOAD_PUBLICWEIBO_ITEM, 1, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    loadMoreData(response);
                } else {
                    ToastUtil.showShort(mContext, "返回的微博数据为空");
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
        ArrayList<Status> httpRespnse = StatusList.parse(string).statusList;
        if (httpRespnse != null && httpRespnse.size() == 1 && httpRespnse.get(0).id.equals(mDatas.get(mDatas.size() - 1).id)) {
            mNoMoreData = true;
            RecyclerViewStateUtils.setFooterViewState(this, mRecyclerView, mDatas.size(), LoadingFooter.State.Normal, null);
        } else if (httpRespnse.size() > 1) {
            httpRespnse.remove(0);
            mDatas.addAll(httpRespnse);
            updateList();
            RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
        }
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
                RecyclerViewStateUtils.setFooterViewState(PopularWeiBoActivity.this, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
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
