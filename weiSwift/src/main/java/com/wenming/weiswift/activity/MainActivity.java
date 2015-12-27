package com.wenming.weiswift.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TabHost;
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
import com.sina.weibo.sdk.utils.LogUtil;
import com.wenming.weiswift.R;
import com.wenming.weiswift.fragment.TabDB;
import com.wenming.weiswift.weiboAccess.AccessTokenKeeper;
import com.wenming.weiswift.weiboAccess.Constants;

public class MainActivity extends FragmentActivity {
    private AuthInfo mAuthInfo;
    private TextView mTokenText;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private StatusesAPI mStatusesAPI;


    private FragmentTabHost mFragmentTabHost;
    private Context mContext;

    private TabHost.OnTabChangeListener onTabChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);

        mContext = this;
        initTab();


//		mAuthInfo = new AuthInfo(this, Constants.APP_KEY,
//				Constants.REDIRECT_URL, Constants.SCOPE);
//		mTokenText = (TextView) findViewById(R.id.textview);
//		mSsoHandler = new SsoHandler(MainActivity.this, mAuthInfo);
//
//		mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
//
//		findViewById(R.id.button).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mSsoHandler.authorize(new AuthListener());
//			}
//		});
//		findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mStatusesAPI.friendsTimeline(0L, 0L, 10, 1, false, 0, false,
//						mListener);
//			}
//		});
    }

    private void initTab() {
        mFragmentTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mFragmentTabHost.setup(mContext, getSupportFragmentManager(), R.id.contentLayout);
        mFragmentTabHost.getTabWidget().setDividerDrawable(null);
        mFragmentTabHost.setOnTabChangedListener(onTabChangeListener);

        TabHost.TabSpec tabSpec;
        String tabs[] = TabDB.getTabText();
        for (int i = 0; i < tabs.length; i++) {
            tabSpec = mFragmentTabHost.newTabSpec(tabs[i]).setIndicator(getTabView(i));
            mFragmentTabHost.addTab(tabSpec, TabDB.getFragments()[i], null);
            mFragmentTabHost.setTag(i);
        }
        onTabChangeListener = new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {


            }
        };
    }

    private View getTabView(int index) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tabitem, null);
        TextView textView = (TextView) view.findViewById(R.id.itemTextView);
        textView.setText(TabDB.getTabText()[index]);
        Drawable drawable = getResources().getDrawable(TabDB.getTabImg()[index]);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
        return view;
    }


//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		// SSO 授权回调
//		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
//		if (mSsoHandler != null) {
//			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
//		}
//
//	}
//
//	class AuthListener implements WeiboAuthListener {
//
//		@Override
//		public void onComplete(Bundle values) {
//			// TODO Auto-generated method stub
//			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
//			if (mAccessToken.isSessionValid()) {
//
//				AccessTokenKeeper.writeAccessToken(MainActivity.this,
//						mAccessToken);
//				Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT)
//						.show();
//			}
//
//		}
//
//		@Override
//		public void onCancel() {
//			// TODO Auto-generated method stub
//			// Toast.makeText(MainActivity.this, "取消授权",
//			// Toast.LENGTH_LONG).show();
//		}
//
//		@Override
//		public void onWeiboException(WeiboException e) {
//			// TODO Auto-generated method stub
//			Toast.makeText(MainActivity.this,
//					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
//					.show();
//		}
//
//	}
//
//	/**
//	 * 微博 OpenAPI 回调接口。
//	 */
//	private RequestListener mListener = new RequestListener() {
//		@Override
//		public void onComplete(String response) {
//			if (!TextUtils.isEmpty(response)) {
//				LogUtil.i(TAG, response);
//				if (response.startsWith("{\"statuses\"")) {
//					// 调用 StatusList#parse 解析字符串成微博列表对象
//					StatusList statuses = StatusList.parse(response);
//					if (statuses != null && statuses.total_number > 0) {
//						Toast.makeText(MainActivity.this,
//								"获取微博信息流成功, 条数: " + statuses.statusList.size(),
//								Toast.LENGTH_LONG).show();
//
//					}
//				} else if (response.startsWith("{\"created_at\"")) {
//					// 调用 Status#parse 解析字符串成微博对象
//					Status status = Status.parse(response);
//					Toast.makeText(MainActivity.this,
//							"发送一送微博成功, id = " + status.id, Toast.LENGTH_LONG)
//							.show();
//				} else {
//					Toast.makeText(MainActivity.this, response,
//							Toast.LENGTH_LONG).show();
//				}
//			}
//		}
//
//		@Override
//		public void onWeiboException(WeiboException e) {
//			LogUtil.e(TAG, e.getMessage());
//			ErrorInfo info = ErrorInfo.parse(e.getMessage());
//			Toast.makeText(MainActivity.this, info.toString(),
//					Toast.LENGTH_LONG).show();
//		}
//	};
//
}
