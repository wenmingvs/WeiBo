package com.wenming.weiswift.fragment.message.comment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.wenming.weiswift.NewFeature;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.DetailActivity;
import com.wenming.weiswift.common.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wenming.weiswift.common.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.common.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wenming.weiswift.common.endlessrecyclerview.weight.LoadingFooter;
import com.wenming.weiswift.common.util.DensityUtil;
import com.wenming.weiswift.common.util.NetUtil;
import com.wenming.weiswift.common.util.SDCardUtil;
import com.wenming.weiswift.common.util.ToastUtil;
import com.wenming.weiswift.fragment.message.ItemSapce;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/26.
 */
public class CommentActivity extends DetailActivity {
    private ArrayList<Comment> mDatas;

    private CommentAdapter mAdapter;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mNoMoreData;

    @Override
    public void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mAdapter = new CommentAdapter(mContext, mDatas);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mRecyclerView.addItemDecoration(new ItemSapce(DensityUtil.dp2px(mContext, 14)));
    }


    /**
     * 下拉刷新加载更多的逻辑
     */
    @Override
    public void pullToRefreshData() {
        mSwipeRefreshLayout.setRefreshing(true);
        mCommentsAPI.toME(0, 0, NewFeature.GET_COMMENT_ITEM, 1, 0, 0, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    if (NewFeature.CACHE_MESSAGE_COMMENT) {
                        SDCardUtil.put(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "message_comment.txt", response);
                    }
                    mDatas = CommentList.parse(response).commentList;
                    updateList();
                } else {
                    ToastUtil.showShort(mContext, "返回的微博数据为空");
                }

                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                if (NewFeature.CACHE_MESSAGE_COMMENT) {
                    String response = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "message_comment.txt");
                    if(response != null) {
                        mDatas = CommentList.parse(response).commentList;
                        updateList();
                    }

                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 下拉刷新逻辑
     */
    @Override
    public void requestMoreData() {
        mCommentsAPI.toME(0, Long.valueOf(mDatas.get(mDatas.size() - 1).id), NewFeature.LOADMORE_COMMENT_ITEM, 1, 0, 0, new RequestListener() {
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

    /**
     * 成功拿到下一页的评论数据，要根据数据的内容来决定是否已经加载到头了
     * 1. 如果请求下来的数据，数目为1，且id和mCommentDatas的最后一条评论的id相同，则表示服务器的数据已经请求完了
     * 2. 如果请求的数据大于1，则删掉重复的第一条，再添加到mCommentDatas中，并且刷新recyclerview的状态和底部的view
     *
     * @param string
     */
    public void loadMoreData(String string) {
        ArrayList<Comment> httpRespnse = CommentList.parse(string).commentList;
        if (httpRespnse != null && httpRespnse.size() == 1 && httpRespnse.get(0).id.equals(mDatas.get(mDatas.size() - 1).id)) {
            mNoMoreData = true;
            RecyclerViewStateUtils.setFooterViewState(CommentActivity.this, mRecyclerView, mDatas.size(), LoadingFooter.State.Normal, null);
        } else if (httpRespnse.size() > 1) {
            httpRespnse.remove(0);
            mDatas.addAll(httpRespnse);
            updateList();
            RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
        }
    }

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
                RecyclerViewStateUtils.setFooterViewState(CommentActivity.this, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
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


    @Override
    public void initTitleBar() {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.toolbar_message_detail_base);
        mToolBar = findViewById(R.id.toolbar_home_weiboitem_detail_title);
        mBackIcon = (ImageView) mToolBar.findViewById(R.id.toolbar_back);
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
