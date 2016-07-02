package com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wenming.weiswift.R;
import com.wenming.weiswift.api.StatusesAPI;
import com.wenming.weiswift.entity.Comment;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.model.imp.StatusDetailModelImp;
import com.wenming.weiswift.mvp.presenter.DetailActivityPresent;
import com.wenming.weiswift.mvp.presenter.imp.DetailActivityPresentImp;
import com.wenming.weiswift.mvp.view.DetailActivityView;
import com.wenming.weiswift.ui.common.BaseSwipeActivity;
import com.wenming.weiswift.ui.common.FillContent;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.adapter.CommentDetailAdapter;
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.adapter.MentionDetailAdapter;
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.headview.OnDetailButtonClickListener;
import com.wenming.weiswift.utils.ToastUtil;
import com.wenming.weiswift.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wenming.weiswift.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.weight.LoadingFooter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by wenmingvs on 16/4/20.
 */
public abstract class BaseDetailActivity extends BaseSwipeActivity implements DetailActivityView {

    public Status mStatus;
    public ArrayList<Comment> mCommentDatas = new ArrayList<>();
    public ArrayList<Status> mRepostDatas = new ArrayList<>();
    public CommentDetailAdapter mCommentAdapter;
    public MentionDetailAdapter mRepostAdapter;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    private int mCurrentGroup = StatusDetailModelImp.COMMENT_PAGE;
    public DetailActivityPresent mDetailActivityPresent;
    public boolean mNoMoreData;
    public Context mContext;
    public int mLastestComments;
    public int mLastestReposts;
    public int mLastestAttitudes;
    private HeaderAndFooterRecyclerViewAdapter mRepostFooterAdapter;
    private HeaderAndFooterRecyclerViewAdapter mCommentFooterAdapter;
    private int lastOffset;
    private int lastPosition;
    public LinearLayout bottombar_retweet;
    public LinearLayout bottombar_comment;
    public LinearLayout bottombar_attitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.messagefragment_base_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_swipe_refresh_widget);
        bottombar_retweet = (LinearLayout) findViewById(R.id.bottombar_retweet);
        bottombar_comment = (LinearLayout) findViewById(R.id.bottombar_comment);
        bottombar_attitude = (LinearLayout) findViewById(R.id.bottombar_attitude);
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mStatus = getIntent().getParcelableExtra("weiboitem");
        mDetailActivityPresent = new DetailActivityPresentImp(this);
        initRefreshLayout();
        initRecyclerView();
        mLastestComments = mStatus.comments_count;
        mLastestReposts = mStatus.reposts_count;
        mLastestAttitudes = mStatus.attitudes_count;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getWeiBoCount();
                mDetailActivityPresent.pullToRefreshData(mCurrentGroup, mStatus, mContext);
            }
        });
        FillContent.fillDetailButtonBar(mContext, mStatus, bottombar_retweet, bottombar_comment, bottombar_attitude);
    }

    protected void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNoMoreData = false;
                getWeiBoCount();
                mDetailActivityPresent.pullToRefreshData(mCurrentGroup, mStatus, mContext);
            }
        });
    }


    public void initRecyclerView() {
        mCommentAdapter = new CommentDetailAdapter(mContext, mCommentDatas);
        mCommentFooterAdapter = new HeaderAndFooterRecyclerViewAdapter(mCommentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mCommentFooterAdapter);
        addHeaderView(mCurrentGroup);
        refreshDetailBar(mLastestComments, mLastestReposts, mLastestAttitudes);
    }

    public void onArrorClick(View view) {
        finish();
    }

    protected abstract void addHeaderView(int type);

    protected abstract int getHeaderViewHeight();

    /**
     * 异步请求评论，转发，赞数
     */
    public void getWeiBoCount() {
        mSwipeRefreshLayout.setRefreshing(true);
        StatusesAPI statusesAPI = new StatusesAPI(mContext, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(mContext));
        statusesAPI.count(new String[]{mStatus.id}, new RequestListener() {
            @Override
            public void onComplete(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    mLastestComments = jsonArray.getJSONObject(0).optInt("comments");
                    mLastestReposts = jsonArray.getJSONObject(0).optInt("reposts");
                    mLastestAttitudes = jsonArray.getJSONObject(0).optInt("attitudes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(mContext, e.getMessage());
            }
        });
    }


    protected abstract void refreshDetailBar(int comments, int reposts, int attitudes);


    public OnDetailButtonClickListener onDetailButtonClickListener = new OnDetailButtonClickListener() {
        @Override
        public void OnComment() {
            mNoMoreData = false;
            mCurrentGroup = StatusDetailModelImp.COMMENT_PAGE;
            getWeiBoCount();
            mDetailActivityPresent.pullToRefreshData(mCurrentGroup, mStatus, mContext);
        }

        @Override
        public void OnRetweet() {
            mNoMoreData = false;
            mCurrentGroup = StatusDetailModelImp.REPOST_PAGE;
            getWeiBoCount();
            mDetailActivityPresent.pullToRefreshData(mCurrentGroup, mStatus, mContext);
        }
    };


    @Override
    public void updateRepostListView(ArrayList<Status> mentionlist, boolean resetAdapter) {
        if (resetAdapter) {
            mNoMoreData = false;
            mRepostAdapter = new MentionDetailAdapter(mContext, mentionlist);
            mRepostFooterAdapter = new HeaderAndFooterRecyclerViewAdapter(mRepostAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mRepostFooterAdapter);
            addHeaderView(mCurrentGroup);
            //纠正微博的转发数
            if (mentionlist.size() > mLastestReposts) {
                mLastestReposts = mentionlist.size();
            }
            refreshDetailBar(mLastestComments, mLastestReposts, mLastestAttitudes);
        }
        mRecyclerView.clearOnScrollListeners();
        mRecyclerView.addOnScrollListener(mScrollListener);
        mRepostDatas = mentionlist;
        mRepostAdapter.setData(mentionlist);
        mRepostFooterAdapter = new HeaderAndFooterRecyclerViewAdapter(mRepostAdapter);
        mRepostFooterAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateCommentListView(ArrayList<Comment> commentlist, boolean resetAdapter) {
        if (resetAdapter) {
            mNoMoreData = false;
            mCommentAdapter = new CommentDetailAdapter(mContext, commentlist);
            mCommentFooterAdapter = new HeaderAndFooterRecyclerViewAdapter(mCommentAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mCommentFooterAdapter);
            addHeaderView(mCurrentGroup);
            if (commentlist.size() > mLastestComments) {
                mLastestComments = commentlist.size();
            }
            refreshDetailBar(mLastestComments, mLastestReposts, mLastestAttitudes);
        }
        mRecyclerView.clearOnScrollListeners();
        mRecyclerView.addOnScrollListener(mScrollListener);
        mCommentDatas = commentlist;
        mCommentAdapter.setData(commentlist);
        mCommentFooterAdapter = new HeaderAndFooterRecyclerViewAdapter(mCommentAdapter);
        mCommentFooterAdapter.notifyDataSetChanged();
    }


    public void updateEmptyRepostHeadView() {
        mNoMoreData = true;
        mRepostAdapter = new MentionDetailAdapter(mContext, mRepostDatas);
        mRepostFooterAdapter = new HeaderAndFooterRecyclerViewAdapter(mRepostAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mRepostFooterAdapter);
        mRecyclerView.clearOnScrollListeners();
        mRecyclerView.addOnScrollListener(mScrollListener);
        addHeaderView(mCurrentGroup);
        mRepostFooterAdapter.notifyDataSetChanged();
        refreshDetailBar(mLastestComments, 0, mLastestAttitudes);
    }

    public void updateEmptyCommentHeadView() {
        mNoMoreData = true;
        mCommentAdapter = new CommentDetailAdapter(mContext, mCommentDatas);
        mCommentFooterAdapter = new HeaderAndFooterRecyclerViewAdapter(mCommentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mCommentFooterAdapter);
        mRecyclerView.clearOnScrollListeners();
        mRecyclerView.addOnScrollListener(mScrollListener);
        addHeaderView(mCurrentGroup);
        mCommentFooterAdapter.notifyDataSetChanged();
        refreshDetailBar(0, mLastestReposts, mLastestAttitudes);
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
    public void showLoadFooterView(int currentGroup) {
        if (currentGroup == StatusDetailModelImp.REPOST_PAGE) {
            RecyclerViewStateUtils.setFooterViewState(BaseDetailActivity.this, mRecyclerView, mRepostAdapter.getItemCount(), LoadingFooter.State.Loading, null);
        } else if (currentGroup == StatusDetailModelImp.COMMENT_PAGE) {
            RecyclerViewStateUtils.setFooterViewState(BaseDetailActivity.this, mRecyclerView, mCommentAdapter.getItemCount(), LoadingFooter.State.Loading, null);
        }
    }

    @Override
    public void hideFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
    }

    @Override
    public void showEndFooterView() {
        mNoMoreData = true;
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
    }

    @Override
    public void showErrorFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.NetWorkError);
    }

    @Override
    public void restoreScrollOffset(boolean refreshData) {
        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(lastPosition, lastOffset);
        if (refreshData) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mDetailActivityPresent.pullToRefreshData(mCurrentGroup, mStatus, mContext);
                }
            });
        }
    }


    public EndlessRecyclerOnScrollListener mScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            switch (mCurrentGroup) {
                case StatusDetailModelImp.COMMENT_PAGE:
                    if (!mNoMoreData && mCommentDatas != null && mCommentDatas.size() > 0) {
                        showLoadFooterView(mCurrentGroup);
                        mDetailActivityPresent.requestMoreData(mCurrentGroup, mStatus, mContext);
                    }
                    break;
                case StatusDetailModelImp.REPOST_PAGE:
                    if (!mNoMoreData && mRepostDatas != null && mRepostDatas.size() > 0) {
                        showLoadFooterView(mCurrentGroup);
                        mDetailActivityPresent.requestMoreData(mCurrentGroup, mStatus, mContext);
                    }
                    break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            View topView = recyclerView.getLayoutManager().getChildAt(0);          //获取可视的第一个view
            lastOffset = topView.getTop();                                         //获取与该view的顶部的偏移量
            lastPosition = recyclerView.getLayoutManager().getPosition(topView);  //得到该View的数组位置

        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (recyclerView != null) {
                View topView = recyclerView.getLayoutManager().getChildAt(0);         //获取可视的第一个view
                lastOffset = topView.getTop();                                        //获取与该view的顶部的偏移量
                lastPosition = recyclerView.getLayoutManager().getPosition(topView);  //得到该View的数组位置
            }


        }
    };
}
