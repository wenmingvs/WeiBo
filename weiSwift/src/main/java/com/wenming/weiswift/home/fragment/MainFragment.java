package com.wenming.weiswift.home.fragment;

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
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.AccessTokenKeeper;
import com.wenming.weiswift.common.Constants;
import com.wenming.weiswift.common.DensityUtil;
import com.wenming.weiswift.common.LogUtil;
import com.wenming.weiswift.common.NetUtil;
import com.wenming.weiswift.common.SharedPreferencesUtil;
import com.wenming.weiswift.common.ToastUtil;
import com.wenming.weiswift.home.adapter.WeiboAdapter;
import com.wenming.weiswift.home.util.NewFeature;

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
    private TextView mUserName;
    private View mView;
    private ArrayList<Status> mDatas;
    private StatusesAPI mStatusesAPI;
    private boolean mFirstLoad;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Status> mWeiBoCache;
    private int mLastVisibleItemPositon;//count from 1
    private long lastWeiboID;
    private UsersAPI mUsersAPI;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.d("onAttach");
        mFirstLoad = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("onCreateView");
        if (NewFeature.LOGIN_STATUS == true) {
            mView = inflater.inflate(R.layout.mainfragment_layout, container, false);
            mActivity = getActivity();
            mContext = getActivity();
            initAccessToken();
            initToolBar();
            initRecyclerView();
            initRefreshLayout();
            return mView;
        } else {
            mView = inflater.inflate(R.layout.mainfragment_unlogin_layout, container, false);
            mActivity = getActivity();
            mContext = getActivity();
            initAccessToken();
            initToolBar();
            return mView;
        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtil.d("onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        if (NewFeature.LOGIN_STATUS == true) {
            mToolBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        LogUtil.d("onDestroyView");
        super.onDestroyView();
        if (NewFeature.LOGIN_STATUS == true) {
            mToolBar.setVisibility(View.GONE);
            mFirstLoad = false;
        }

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
        mUsersAPI = new UsersAPI(mContext, Constants.APP_KEY, mAccessToken);
    }

    private void initToolBar() {

        if (NewFeature.LOGIN_STATUS == true) {
            initLoginState();
        } else {
            initUnLoginState();
        }
    }

    private void initLoginState() {
        mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.toolbar_home_login);
        mToolBar = mActivity.findViewById(R.id.toolbar_home_login);
        mUserName = (TextView) mToolBar.findViewById(R.id.toolbar_username);
        refreshUserName();
    }

    private void refreshUserName() {
        long uid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(uid, new RequestListener() {
            @Override
            public void onComplete(String response) {
                // 调用 User#parse 将JSON串解析成User对象
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


    private void initUnLoginState() {
        mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.toolbar_home_unlogin);
        mToolBar = mActivity.findViewById(R.id.toolbar_home_unlogin);
        mLogin = (TextView) mToolBar.findViewById(R.id.login);
        mRegister = (TextView) mToolBar.findViewById(R.id.register);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorizeWeb(new AuthListener());
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
        mWeiBoCache = new ArrayList<Status>();
        mAdapter = new WeiboAdapter(mDatas, mContext);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPositon + 1 == mAdapter.getItemCount()) {
                    if (mDatas.size() - 1 < mWeiBoCache.size() && mDatas.size() != 0) {//读取本地缓存数据
                        addDataFromCache(mLastVisibleItemPositon - 1);
                        mAdapter.setData(mDatas);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        lastWeiboID = Long.parseLong(mDatas.get(mDatas.size() - 1).id);
                        ToastUtil.showShort(mContext, "本地数据已经被读取完，开始进行网络请求");
                        pullToLoadMoreDataFromURL();
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

    private void pullToLoadMoreDataFromURL() {
        if (NetUtil.isConnected(mContext)) {
            if (mAccessToken != null && mAccessToken.isSessionValid()) {
                mStatusesAPI.friendsTimeline(0, lastWeiboID, NewFeature.GET_WEIBO_NUMS, 1, false, NewFeature.WEIBO_TYPE, false,
                        new RequestListener() {
                            @Override
                            public void onComplete(String response) {
                                ArrayList<Status> status = StatusList.parse(response).statusList;
                                status.remove(0);
                                mWeiBoCache.addAll(status);
                                addDataFromCache(mLastVisibleItemPositon - 1);
                                mAdapter.setData(mDatas);
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onWeiboException(WeiboException e) {
                                ToastUtil.showShort(mContext, "服务器出现问题！");
                            }
                        });
            }
        } else {
            ToastUtil.showShort(mContext, "网络请求失败，没有网络");
        }
    }


    private void pullToRefreshData() {
        if (NetUtil.isConnected(mContext)) {
            if (mAccessToken != null && mAccessToken.isSessionValid()) {
                mStatusesAPI.friendsTimeline(0, 0, NewFeature.GET_WEIBO_NUMS, 1, false, NewFeature.WEIBO_TYPE, false,
                        new RequestListener() {
                            @Override
                            public void onComplete(String response) {
                                //短时间内疯狂请求数据，服务器会暂时返回空数据，所以在这里要判空
                                if (!TextUtils.isEmpty(response)) {
                                    SharedPreferencesUtil.put(mContext, "wenming", response);
                                    getWeiBoCache(0);
                                } else {
                                    ToastUtil.showShort(mContext, "网络请求太快，服务器返回空数据，请注意请求频率");
                                }
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onWeiboException(WeiboException e) {
                                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                                ToastUtil.showShort(mContext, info.toString());
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });
            }
        } else {
            getWeiBoCache(0);
            ToastUtil.showShort(mContext, "没有网络,读取本地缓存");
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 从本地缓存中拿到数据并且解析到mWeiBoCache
     *
     * @param start
     */
    private void getWeiBoCache(int start) {
        mWeiBoCache.clear();
        mDatas.clear();
        String response = (String) SharedPreferencesUtil.get(mContext, "wenming", new String());
        if (response.startsWith("{\"statuses\"")) {
            mWeiBoCache = StatusList.parse(response).statusList;
            mDatas.add(0, new Status());
            addDataFromCache(0);
        }
        mAdapter.setData(mDatas);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * @param start mWeiBoCache start to add
     */
    private void addDataFromCache(int start) {
        int count = 0;
        for (int i = start; i < mWeiBoCache.size(); i++) {
            if (start == mWeiBoCache.size()) {
                ToastUtil.showShort(mContext, "本地缓存已经读取完！");
                break;
            }
            if (count == NewFeature.LOAD_WEIBO_ITEM) {
                break;
            }
            mDatas.add(mWeiBoCache.get(i));
            count++;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("onResume in MainFragment");
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
