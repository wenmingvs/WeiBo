package com.wenming.weiswift.fragment.home;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.wenming.weiswift.NewFeature;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wenming.weiswift.common.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.common.endlessrecyclerview.RecyclerViewUtils;
import com.wenming.weiswift.common.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wenming.weiswift.common.endlessrecyclerview.weight.LoadingFooter;
import com.wenming.weiswift.common.util.NetUtil;
import com.wenming.weiswift.common.util.SDCardUtil;
import com.wenming.weiswift.common.util.ToastUtil;
import com.wenming.weiswift.fragment.home.weiboitem.SeachHeadView;
import com.wenming.weiswift.fragment.home.weiboitem.WeiboAdapter;
import com.wenming.weiswift.fragment.home.weiboitem.WeiboItemSapce;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class HomeFragment extends MainFragment {

    public RecyclerView mRecyclerView;
    public WeiboAdapter mAdapter;
    public LinearLayoutManager mLayoutManager;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private ArrayList<Status> mDatas;
    private boolean mNoMoreData;


    @Override
    public void initRecyclerView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.weiboRecyclerView);
        mAdapter = new WeiboAdapter(mDatas, mContext);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        RecyclerViewUtils.setHeaderView(mRecyclerView, new SeachHeadView(mContext));
        if (mFirstLoad == true) {
            mRecyclerView.addItemDecoration(new WeiboItemSapce((int) mContext.getResources().getDimension(R.dimen.home_weiboitem_space)));
        }
    }

    @Override
    public void firstLoadData() {
        String response = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博列表缓存.txt");
        if (response != null) {
            mDatas = StatusList.parse(response).statusList;
            updateList();
        } else {
            refreshAllData();
        }
    }


    @Override
    public void pullToRefreshData(boolean refrshAllData) {
        if (refrshAllData) {
            refreshAllData();
        } else {
            getlatestWeiBo();
        }
    }


    @Override
    public void requestMoreData() {
        mStatusesAPI.friendsTimeline(0, Long.valueOf(mDatas.get(mDatas.size() - 1).id), NewFeature.GET_WEIBO_NUMS, 1, false, 0, false, new RequestListener() {
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
            RecyclerViewStateUtils.setFooterViewState(mActivity, mRecyclerView, mDatas.size(), LoadingFooter.State.Normal, null);
        } else if (httpRespnse.size() > 1) {
            httpRespnse.remove(0);
            mDatas.addAll(httpRespnse);
            mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
            RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
        }
    }

    @Override
    public void updateList() {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mAdapter.setData(mDatas);
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void getlatestWeiBo() {
        mSwipeRefreshLayout.setRefreshing(true);
        mStatusesAPI.friendsTimeline(Long.valueOf(mDatas.get(0).id), 0, NewFeature.GET_WEIBO_NUMS, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ToastUtil.showShort(mContext, "局部刷新");
                    ArrayList<Status> latestWeiBo = StatusList.parse(response).statusList;
                    //latestWeiBo.remove(0);
                    if (latestWeiBo.size() > 0) {
                        mDatas.addAll(0, latestWeiBo);
                        updateList();
                    } else {
                        ToastUtil.showShort(mContext, "没有更新的内容了");
                    }

                } else {
                    ToastUtil.showShort(mContext, "加载到的内容为空");
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(mContext, "onWeiboException");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void refreshAllData() {
        mSwipeRefreshLayout.setRefreshing(true);
        mStatusesAPI.friendsTimeline(0, 0, NewFeature.GET_WEIBO_NUMS, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                //短时间内疯狂请求数据，服务器会返回数据，但是是空数据。为了防止这种情况出现，要在这里要判空
                if (!TextUtils.isEmpty(response)) {
                    ToastUtil.showShort(mContext, "全部刷新");
                    if (NewFeature.CACHE_WEIBOLIST) {
                        SDCardUtil.put(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博列表缓存.txt", response);
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
                    String response = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博列表缓存.txt");
                    mDatas = StatusList.parse(response).statusList;
                    updateList();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mRefrshAllData = false;
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
                RecyclerViewStateUtils.setFooterViewState(mActivity, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
                requestMoreData();
            }
        }
    };
}
