package com.wenming.weiswift.fragment.message;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.login.AccessTokenKeeper;
import com.wenming.weiswift.common.login.Constants;

/**
 * Created by wenmingvs on 16/4/26.
 */
public abstract class DetailActivity extends Activity {
    public View mToolBar;
    public ImageView mBackIcon;
    private Context mContext;
    public AuthInfo mAuthInfo;
    public Oauth2AccessToken mAccessToken;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public CommentsAPI mCommentsAPI;
    public StatusesAPI mStatusesAPI;
    public SsoHandler mSsoHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.messagefragment_base_layout);
        mContext = this;
        initTitleBar();
        initAccessToken();
        initRefreshLayout();
    }

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

    public void initAccessToken() {
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(DetailActivity.this, mAuthInfo);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mCommentsAPI = new CommentsAPI(this, Constants.APP_KEY, mAccessToken);
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
    }

    protected void initRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.commmentlist_swipe_refresh_widget);
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

    /**
     * 下拉刷新，加载更多
     */
    protected abstract void pullToRefreshData();

    /**
     * 加载更多逻辑
     */
    protected abstract void requestMoreData();

}
