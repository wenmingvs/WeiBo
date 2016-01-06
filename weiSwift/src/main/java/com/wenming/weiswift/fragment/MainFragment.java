package com.wenming.weiswift.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.wenming.weiswift.R;
import com.wenming.weiswift.adapter.WeiboAdapter;
import com.wenming.weiswift.util.NewFeature;
import com.wenming.weiswift.util.androidutils.DensityUtil;
import com.wenming.weiswift.util.androidutils.LogUtil;
import com.wenming.weiswift.util.androidutils.NetUtil;
import com.wenming.weiswift.util.androidutils.SharedPreferencesUtil;
import com.wenming.weiswift.util.androidutils.ToastUtil;
import com.wenming.weiswift.weiboAccess.AccessTokenKeeper;
import com.wenming.weiswift.weiboAccess.Constants;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class MainFragment extends Fragment {

    private AuthInfo mAuthInfo;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private WeiboAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Activity mActivity;
    private View mToolBar;
    private TextView mLogin;
    private TextView mRegister;
    private View mView;
    private ArrayList<Status> mDatas;
    private StatusesAPI mStatusesAPI;
    private boolean mFirstLoad;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Status> mWeiBoItemDataList;
    private int mLastVisibleItemPositon;//count from 1
    private long lastWeiboID;

    private boolean mLoginState = true;

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {


        @Override
        public void onComplete(String response) {

        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.d("onAttach");
        mFirstLoad = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("onCreateView");
        mView = inflater.inflate(R.layout.mainfragment_layout, container, false);
        mActivity = getActivity();
        mContext = getActivity();
        initAccessToken();
        initToolBar();
        initRecyclerView();
        initRefreshLayout();
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtil.d("onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mToolBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        LogUtil.d("onDestroyView");
        super.onDestroyView();
        mToolBar.setVisibility(View.GONE);
        mFirstLoad = false;
    }

    public void hideToolBar() {
        mToolBar.setVisibility(View.GONE);
    }

    public void showToolBar() {
        mToolBar.setVisibility(View.VISIBLE);
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
            hideToolBar();
        } else {
            showToolBar();
        }
    }

    private void initAccessToken() {
        mAuthInfo = new AuthInfo(mContext, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(mActivity, mAuthInfo);
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        mStatusesAPI = new StatusesAPI(mContext, Constants.APP_KEY, mAccessToken);
    }

    private void initToolBar() {

        if (mLoginState == true) {
            initLoginState();
        } else {
            initUnLoginState();
        }
    }

    private void initLoginState() {
        mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.toolbar_home_login);
        mToolBar = mActivity.findViewById(R.id.toolbar_home_login);
    }

    private void initUnLoginState() {
        mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.toolbar_home_unlogin);
        mToolBar = mActivity.findViewById(R.id.toolbar_home_unlogin);
        mLogin = (TextView) mToolBar.findViewById(R.id.login);
        mRegister = (TextView) mToolBar.findViewById(R.id.register);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorize(new AuthListener());
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view
             */
            @Override
            public void onClick(View view) {
                mSsoHandler.registerOrLoginByMobile("验证码登陆", new AuthListener());
            }
        });
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_widget);
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
                mSwipeRefreshLayout.setRefreshing(true);
                pullToRefreshData();
            }
        });

    }


    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.weiboRecyclerView);
        if (mFirstLoad == true) {
            mRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtil.dp2px(mContext, 8)));
        }
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDatas = new ArrayList<Status>();
        mWeiBoItemDataList = new ArrayList<Status>();
        mAdapter = new WeiboAdapter(mDatas, mContext);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPositon + 1 == mAdapter.getItemCount()) {

//                    if (mAccessToken != null && mAccessToken.isSessionValid()) {
//                        if (mDatas.size() != 0) {
//                            FROM_BOTTOM_LOAD_MORE = true;
//                            lastWeiboID = Long.parseLong(mDatas.get(mDatas.size() - 1).id);
//                            mStatusesAPI.friendsTimeline(0L, lastWeiboID, 5, 1, false, 1, false,
//                                    mListener);
//                        }
//                    }


                    if (mDatas.size() - 1 != mWeiBoItemDataList.size()) {
                        for (int i = mLastVisibleItemPositon; i <= mLastVisibleItemPositon + NewFeature.LOAD_WEIBO_ITEM; i++) {
                            if (i == mWeiBoItemDataList.size()) {
                                mDatas.add(mWeiBoItemDataList.get(i));
                            }

                        }
                        mAdapter.setData(mDatas);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        lastWeiboID = Long.parseLong(mDatas.get(mDatas.size() - 1).id);
                        ToastUtil.showShort(mContext, "本地数据已经被读取完！！！");
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItemPositon = mLayoutManager.findLastVisibleItemPosition();
                LogUtil.d("mLastVisibleItemPositon = " + mLastVisibleItemPositon);
            }
        });
    }

    private void pullToRefreshData() {
        if (NetUtil.isConnected(mContext)) {
            if (mAccessToken != null && mAccessToken.isSessionValid()) {
                mStatusesAPI.friendsTimeline(0, 0, NewFeature.GET_WEIBO_NUMS, 1, false, NewFeature.WEIBO_TYPE, false,
                        new RequestListener() {
                            @Override
                            public void onComplete(String response) {
                                SharedPreferencesUtil.put(mContext, "wenming", response);
                                if (!TextUtils.isEmpty(response)) {
                                    if (response.startsWith("{\"statuses\"")) {
                                        // 调用 StatusList#parse 解析字符串成微博列表对象
                                        mWeiBoItemDataList.clear();
                                        mWeiBoItemDataList = StatusList.parse(response).statusList;
                                        mDatas.clear();
                                        mDatas.add(0, new Status());
                                        for (int i = 1; i <= NewFeature.LOAD_WEIBO_ITEM; i++) {
                                            mDatas.add(mWeiBoItemDataList.get(i));
                                        }
                                        mAdapter.setData(mDatas);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                } else if (response.startsWith("{\"created_at\"")) {
                                    // 调用 Status#parse 解析字符串成微博对象
                                    Status status = Status.parse(response);
                                    Toast.makeText(mContext,
                                            "发送一送微博成功, id = " + status.id, Toast.LENGTH_LONG)
                                            .show();
                                } else {
                                    Toast.makeText(mContext, response,
                                            Toast.LENGTH_LONG).show();
                                }
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onWeiboException(WeiboException e) {
                                mSwipeRefreshLayout.setRefreshing(false);
                                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                                Toast.makeText(mContext, info.toString(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        } else {
            //获取缓存的微博数据
            String response = (String) SharedPreferencesUtil.get(mContext, "wenming", new String());
            if (response.startsWith("{\"statuses\"")) {
                mWeiBoItemDataList.clear();
                mWeiBoItemDataList = StatusList.parse(response).statusList;
                mDatas.clear();
                mDatas.add(0, new Status());
                for (int i = 1; i <= NewFeature.LOAD_WEIBO_ITEM; i++) {
                    mDatas.add(mWeiBoItemDataList.get(i));
                }
                mAdapter.setData(mDatas);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

        }

    }

    class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);// 从 Bundle 中解析 Token
            if (mAccessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(mContext,
                        mAccessToken);//保存Token
                Toast.makeText(mContext, "授权成功", Toast.LENGTH_SHORT)
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

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;
        }

    }
}
