package com.wenming.weiswift.ui.login.fragment.message.mention;

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
import com.wenming.weiswift.entity.Comment;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.presenter.MentionActivityPresent;
import com.wenming.weiswift.mvp.presenter.imp.MentionActivityPresentImp;
import com.wenming.weiswift.mvp.view.MentionActivityView;
import com.wenming.weiswift.ui.common.BaseSwipeActivity;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.ui.login.fragment.message.IGroupItemClick;
import com.wenming.weiswift.ui.login.fragment.message.comment.CommentAdapter;
import com.wenming.weiswift.utils.DensityUtil;
import com.wenming.weiswift.utils.ScreenUtil;
import com.wenming.weiswift.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wenming.weiswift.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wenming.weiswift.widget.endlessrecyclerview.weight.LoadingFooter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/26.
 */
public class MentionActivity extends BaseSwipeActivity implements MentionActivityView {
    private ArrayList<Status> mMentionDatas;
    private ArrayList<Comment> mCommentDatas;
    private MentionAdapter mMentionAdapter;
    private CommentAdapter mCommentAdapter;
    private HeaderAndFooterRecyclerViewAdapter mMentionFooterAdapter;
    private HeaderAndFooterRecyclerViewAdapter mCommentFooterAdapter;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private MentionActivityPresent mMentionPresent;
    private LinearLayout mGroup;
    private TextView mGroupName;
    private GroupPopWindow mMentionPopWindow;
    private int mCurrentGroup = Constants.GROUP_RETWEET_TYPE_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.messagefragment_mention_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_swipe_refresh_widget);
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mGroup = (LinearLayout) findViewById(R.id.mention_group);
        mGroupName = (TextView) findViewById(R.id.mention_name);
        mMentionPresent = new MentionActivityPresentImp(this);
        initRefreshLayout();
        initRecyclerView();
        initGroupWindows();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mMentionPresent.pullToRefreshData(mCurrentGroup, mContext);
            }
        });
    }

    private void initGroupWindows() {
        mGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int statusBarHeight = rect.top;
                mMentionPopWindow = GroupPopWindow.getInstance(mContext, ScreenUtil.getScreenWidth(mContext) * 3 / 5, ScreenUtil.getScreenHeight(mContext) * 2 / 3);
                mMentionPopWindow.showAtLocation(mGroupName, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, mGroupName.getHeight() + statusBarHeight + DensityUtil.dp2px(mContext, 8));
                mMentionPopWindow.setOnGroupItemClickListener(new IGroupItemClick() {
                    @Override
                    public void onGroupItemClick(long groupId, String groupName) {
                        mCurrentGroup = (int) groupId;
                        setGroupName(groupName);
                        mMentionPopWindow.dismiss();
                        mMentionPresent.pullToRefreshData(mCurrentGroup, mContext);
                    }
                });
            }
        });
    }

    public void setGroupName(String groupName) {
        mGroupName.setText(groupName);
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMentionPresent.pullToRefreshData(mCurrentGroup, mContext);
            }
        });
    }


    private void initRecyclerView() {
        mMentionAdapter = new MentionAdapter(mContext, mMentionDatas) {
            @Override
            public void arrowClick(Status status, int position) {
                //TODO 完善点击事件
                MentionArrowWindow popupWindow = new MentionArrowWindow(mContext, status);
                popupWindow.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);
            }
        };
        mMentionFooterAdapter = new HeaderAndFooterRecyclerViewAdapter(mMentionAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMentionFooterAdapter);
        //mRecyclerView.addItemDecoration(new ItemSapce(DensityUtil.dp2px(mContext, 14)));
    }


    private EndlessRecyclerOnScrollListener mOnMentionScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (mMentionDatas != null && mMentionDatas.size() > 0) {
                showLoadFooterView(mCurrentGroup);
                mMentionPresent.requestMoreData(mCurrentGroup, mContext);
            }
        }
    };

    public EndlessRecyclerOnScrollListener mOnCommentScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (mCommentDatas != null && mCommentDatas.size() > 0) {
                showLoadFooterView(mCurrentGroup);
                mMentionPresent.requestMoreData(mCurrentGroup, mContext);
            }
        }
    };


    @Override
    public void updateMentionListView(ArrayList<Status> mentionlist, boolean resetAdapter) {
        if (resetAdapter) {
            mMentionAdapter = new MentionAdapter(mContext, mMentionDatas) {
                @Override
                public void arrowClick(Status status, int position) {
                    //TODO
                    MentionArrowWindow popupWindow = new MentionArrowWindow(mContext, status);
                    popupWindow.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);
                }
            };
            mMentionFooterAdapter = new HeaderAndFooterRecyclerViewAdapter(mMentionAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mMentionFooterAdapter);
        }
        mRecyclerView.clearOnScrollListeners();
        mRecyclerView.addOnScrollListener(mOnMentionScrollListener);
        mMentionDatas = mentionlist;
        mMentionAdapter.setData(mentionlist);
        mMentionFooterAdapter = new HeaderAndFooterRecyclerViewAdapter(mMentionAdapter);
        mMentionFooterAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateCommentListView(ArrayList<Comment> commentlist, boolean resetAdapter) {
        if (resetAdapter) {
            mCommentAdapter = new CommentAdapter(mContext, commentlist);
            mCommentFooterAdapter = new HeaderAndFooterRecyclerViewAdapter(mCommentAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mCommentFooterAdapter);
        }

        mRecyclerView.clearOnScrollListeners();
        mRecyclerView.addOnScrollListener(mOnCommentScrollListener);
        mCommentDatas = commentlist;
        mCommentAdapter.setData(commentlist);
        mCommentFooterAdapter = new HeaderAndFooterRecyclerViewAdapter(mCommentAdapter);
        mCommentFooterAdapter.notifyDataSetChanged();
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
        if (currentGroup == Constants.GROUP_RETWEET_TYPE_ALL || currentGroup == Constants.GROUP_RETWEET_TYPE_FRIENDS || currentGroup == Constants.GROUP_RETWEET_TYPE_ORIGINWEIBO) {
            RecyclerViewStateUtils.setFooterViewState(MentionActivity.this, mRecyclerView, mMentionDatas.size(), LoadingFooter.State.Loading, null);
        } else if (currentGroup == Constants.GROUP_RETWEET_TYPE_ALLCOMMENT || currentGroup == Constants.GROUP_RETWEET_TYPE_FRIEDNSCOMMENT) {
            RecyclerViewStateUtils.setFooterViewState(MentionActivity.this, mRecyclerView, mCommentDatas.size(), LoadingFooter.State.Loading, null);
        }
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
                    mMentionPresent.pullToRefreshData(mCurrentGroup, mContext);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        if (mMentionPopWindow != null) {
            mMentionPopWindow.onDestory();
        }
        super.onDestroy();
    }

}
