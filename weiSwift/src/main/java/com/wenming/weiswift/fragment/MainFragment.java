package com.wenming.weiswift.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.wenming.weiswift.util.DensityUtil;
import com.wenming.weiswift.util.LogUtil;
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
    private RecyclerView.LayoutManager mLayoutManager;
    private Activity mActivity;
    private View mToolBar;
    private TextView mLogin;
    private TextView mRegister;
    private TextView mRefresh;
    private TextView mGetData;
    private View mView;
    private ArrayList<Status> mDatas;
    private StatusesAPI mStatusesAPI;
    private boolean mFirstLoad;
    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            LogUtil.d("wenming", response);
            if (!TextUtils.isEmpty(response)) {

                if (response.startsWith("{\"statuses\"")) {
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    mDatas = StatusList.parse(response).statusList;
                    if (mDatas != null && mDatas.size() > 0) {
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
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            LogUtil.e("wenming", e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(mContext, info.toString(),
                    Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.d("onAttach");
        mFirstLoad = true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtil.d("onCreate");
        super.onCreate(savedInstanceState);
        initToolBar();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.mainfragment_layout, container, false);
            LogUtil.d("onCreateView");
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }


        initRecyclerView();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtil.d("onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mToolBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        LogUtil.d("onActivityCreated");
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        LogUtil.d("onStart");
        super.onStart();
    }

    @Override
    public void onPause() {
        LogUtil.d("onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtil.d("onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        LogUtil.d("onDestroyView");
        super.onDestroyView();
        mToolBar.setVisibility(View.GONE);
        mFirstLoad = false;
    }

    @Override
    public void onDestroy() {
        LogUtil.d("onDestroy");
        super.onDestroy();
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

    private void initToolBar() {
        mActivity = getActivity();
        mContext = mActivity;
        mAuthInfo = new AuthInfo(mContext, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(mActivity, mAuthInfo);
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

        mRegister.setText("获取与展示");
        mRegister.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view
             */
            @Override
            public void onClick(View view) {

                // mSsoHandler.registerOrLoginByMobile("验证码登陆",new AuthListener());

                // 获取当前已保存过的 Token
                mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
                // 对statusAPI实例化
                mStatusesAPI = new StatusesAPI(mContext, Constants.APP_KEY, mAccessToken);
                if (mAccessToken != null && mAccessToken.isSessionValid()) {
                    mStatusesAPI.friendsTimeline(0L, 0L, 10, 1, false, 0, false,
                            mListener);
                }
            }
        });
    }

    private void initRecyclerView(){
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.weiboRecyclerView);
        if (mFirstLoad == true) {
            mRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtil.dp2px(mContext,8)));
        }
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDatas = new ArrayList<Status>();
        mAdapter = new WeiboAdapter(mDatas, mContext);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void setData(ArrayList<Status> data) {
        this.mDatas = data;
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
                outRect.top = space;
        }
    }

}
