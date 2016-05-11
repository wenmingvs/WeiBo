package com.wenming.weiswift.login.fragment.home;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.GroupAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.NewFeature;
import com.wenming.weiswift.common.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wenming.weiswift.common.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.common.endlessrecyclerview.RecyclerViewUtils;
import com.wenming.weiswift.common.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wenming.weiswift.common.endlessrecyclerview.weight.LoadingFooter;
import com.wenming.weiswift.common.login.AccessTokenKeeper;
import com.wenming.weiswift.common.login.Constants;
import com.wenming.weiswift.common.util.LogUtil;
import com.wenming.weiswift.common.util.NetUtil;
import com.wenming.weiswift.common.util.SDCardUtil;
import com.wenming.weiswift.common.util.ToastUtil;
import com.wenming.weiswift.login.fragment.home.weiboitem.IWeiboListRecyclerView;
import com.wenming.weiswift.login.fragment.home.weiboitem.SeachHeadView;
import com.wenming.weiswift.login.fragment.home.weiboitem.WeiboAdapter;
import com.wenming.weiswift.login.fragment.home.weiboitem.WeiboItemSapce;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class HomeFragment extends Fragment implements IWeiboListRecyclerView {

    public RecyclerView mRecyclerView;
    public WeiboAdapter mAdapter;
    public LinearLayoutManager mLayoutManager;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private ArrayList<Status> mDatas;
    private boolean mNoMoreData;
    public AuthInfo mAuthInfo;
    public Oauth2AccessToken mAccessToken;
    public StatusesAPI mStatusesAPI;
    public UsersAPI mUsersAPI;
    public SsoHandler mSsoHandler;
    public Context mContext;
    public Activity mActivity;
    public TextView mUserName;
    public View mView;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public boolean mRefrshAllData;
    private Timer mTimer;
    private TimerTask mTimeTask;
    private Boolean mFirstStart;
    private LinearLayout mGroup;
    private GroupAPI mGroupAPI;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        mContext = getActivity();
        initAccessToken();
        mView = inflater.inflate(R.layout.mainfragment_layout, container, false);
        mFirstStart = mActivity.getIntent().getBooleanExtra("fisrtstart", false);
        initTimeTask();
        initRecyclerView();
        initRefreshLayout();
        initGroupWindows();
        return mView;
    }

    private void initGroupWindows() {

        mGroup = (LinearLayout) mView.findViewById(R.id.group);
        mUserName = (TextView) mView.findViewById(R.id.name);

        mGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToastUtil.showShort(getActivity(), "正在开发中...");

//                mGroupAPI.groups(new RequestListener() {
//                    @Override
//                    public void onComplete(String s) {
//                        LogUtil.d(s);
//                        ArrayList<Group> groupList = GroupList.parse(s).groupList;
//                    }
//
//                    @Override
//                    public void onWeiboException(WeiboException e) {
//                        ToastUtil.showShort(mContext, e.getMessage());
//                        LogUtil.d(e.getMessage());
//                    }
//                });
            }
        });
    }

    @Override
    public void initRecyclerView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.weiboRecyclerView);
        mDatas = new ArrayList<Status>();
        mAdapter = new WeiboAdapter(mDatas, mContext);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        RecyclerViewUtils.setHeaderView(mRecyclerView, new SeachHeadView(mContext));
        mRecyclerView.addItemDecoration(new WeiboItemSapce((int) mContext.getResources().getDimension(R.dimen.home_weiboitem_space)));
    }

    @Override
    public void firstLoadData() {
        String response = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博列表缓存_" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt");
        if (response != null && !mFirstStart) {
            mDatas = StatusList.parse(response).statusList;
            updateList();
        } else {
            refreshAllData();
        }
    }


    /**
     * 发起 SSO 登陆的 Activity 必须重写 onActivityResults
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d("onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void initTimeTask() {
        mTimeTask = new TimerTask() {
            @Override
            public void run() {
                mRefrshAllData = true;
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTimeTask, 0, 15 * 60 * 1000);
    }

    private void initAccessToken() {
        Context context = mContext;
        mAuthInfo = new AuthInfo(mContext, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(mActivity, mAuthInfo);
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        mStatusesAPI = new StatusesAPI(mContext, Constants.APP_KEY, mAccessToken);
        mUsersAPI = new UsersAPI(mContext, Constants.APP_KEY, mAccessToken);
        mGroupAPI = new GroupAPI(mContext, Constants.APP_KEY, mAccessToken);

    }

    private void refreshUserName() {


        long uid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(uid, new RequestListener() {
            @Override
            public void onComplete(String response) {
                // 调用 User#parse 将JSON串解析成User对象
                mGroup.setVisibility(View.VISIBLE);
                User user = User.parse(response);
                if (user != null) {
                    mUserName.setText(user.name);
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                ToastUtil.showShort(mContext, info.toString());
            }
        });
    }


    /**
     * 初始化下拉刷新控件
     * 1. 设置下拉刷新执行的逻辑
     * 2. 第一次进来就自动下拉刷新
     */
    private void initRefreshLayout() {
        mRefrshAllData = true;
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshData(mRefrshAllData);
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshUserName();
                firstLoadData();
            }
        });

    }

    class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);// 从 Bundle 中解析 Token
            if (mAccessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(mContext,
                        mAccessToken);//保存Token
                Toast.makeText(mContext, "授权成功,请重启App", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT)
                        .show();
            }

        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(mContext,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(mContext, "取消授权",
                    Toast.LENGTH_LONG).show();
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
        mStatusesAPI.friendsTimeline(Long.valueOf((mDatas == null || mDatas.size() == 0 ? "0" : mDatas.get(0).id.toString())), 0, NewFeature.GET_WEIBO_NUMS, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    LogUtil.d("wenming", "局部刷新");
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
                    LogUtil.d("wenming", "全部刷新");
                    if (NewFeature.CACHE_WEIBOLIST) {
                        SDCardUtil.put(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博列表缓存_" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt", response);
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
                    String response = SDCardUtil.get(mContext, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博列表缓存_" + AccessTokenKeeper.readAccessToken(mContext).getUid() + ".txt");
                    if (response != null) {
                        mDatas = StatusList.parse(response).statusList;
                        updateList();
                    }

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
            if (!mNoMoreData && mDatas != null && mDatas.size() > 0) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(mActivity, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
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

    public void scrollToTop(boolean refresh) {
        mRecyclerView.scrollToPosition(0);
        if (refresh) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    pullToRefreshData(false);
                }
            });
        }
    }

}
