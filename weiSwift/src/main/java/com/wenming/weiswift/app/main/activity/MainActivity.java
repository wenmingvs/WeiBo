package com.wenming.weiswift.app.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.ApplicationHelper;
import com.wenming.weiswift.app.common.base.BaseAppCompatActivity;
import com.wenming.weiswift.app.common.entity.User;
import com.wenming.weiswift.app.common.oauth.AccessTokenManager;
import com.wenming.weiswift.app.common.oauth.constant.AppAuthConstants;
import com.wenming.weiswift.app.common.user.UserInfoCallBack;
import com.wenming.weiswift.app.common.user.UserManager;
import com.wenming.weiswift.app.debug.DebugTool;
import com.wenming.weiswift.app.discover.DiscoverFragment;
import com.wenming.weiswift.app.home.data.HomeDataManager;
import com.wenming.weiswift.app.home.fragment.HomeFragment;
import com.wenming.weiswift.app.home.presenter.HomePresenter;
import com.wenming.weiswift.app.message.fragment.fragment.MessageFragment;
import com.wenming.weiswift.app.myself.collect.activity.CollectSwipeActivity;
import com.wenming.weiswift.app.myself.fans.activity.FansSwipeActivity;
import com.wenming.weiswift.app.myself.focus.activity.FocusSwipeActivity;
import com.wenming.weiswift.app.myself.fragment.MySelfFragment;
import com.wenming.weiswift.app.myself.myweibo.activity.MyWeiBoSwipeActivity;
import com.wenming.weiswift.app.settings.activity.SettingSwipeActivity;


public class MainActivity extends BaseAppCompatActivity {
    private static final String TAB_HOME_FRAGMENT = "tab_home_fragment";
    private static final String TAB_MESSAGE_FRAGMENT = "tab_message_fragment";
    private static final String TAB_DISCOVERY_FRAGMENT = "tab_discovery_fragment";
    private static final String TAB_PROFILE_FRAGMENT = "tab_profile_fragment";
    public static final String EXTRA_REFRESH_ALL = "extra_refresh_all";

    //导航栏
    private Toolbar mToolBar;
    private TabLayout mTabLayout;
    //抽屉布局
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ImageView mDrawerAvatarIv;
    private TextView mDrawerDescriptionIv;
    private TextView mDrawerNickNameIv;
    private TextView mDrawerWeiBoCountTv;
    private TextView mDrawerFocusCountTv;
    private TextView mDrawerFollowersCountTv;
    private LinearLayout mDrawerWeiBoContainerLl;
    private LinearLayout mDrawerFocusContainerLl;
    private LinearLayout mDrawerFollowerCountLl;
    private View mNavigationHeader;
    //内容碎片
    private HomeFragment mHomeFragment;
    private MessageFragment mMessageFragment;
    private DiscoverFragment mDiscoverFragment;
    private MySelfFragment mMySelfFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;


    private String mCurrentIndex;
    private boolean mRefreshAll;
    private DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.avator_default)
            .showImageForEmptyUri(R.drawable.avator_default)
            .showImageOnFail(R.drawable.avator_default)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new CircleBitmapDisplayer(14671839, 1))
            .build();
    private Snackbar mSnackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareView();
        initData();
        initView();
        initListener();
    }

    private void prepareView() {
        mToolBar = (Toolbar) findViewById(R.id.main_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_container_dl);
        mNavigationView = (NavigationView) findViewById(R.id.main_drawer_nv);
        mNavigationHeader = mNavigationView.getHeaderView(0);
        mDrawerAvatarIv = (ImageView) mNavigationHeader.findViewById(R.id.drawer_avatar);
        mDrawerNickNameIv = (TextView) mNavigationHeader.findViewById(R.id.drawer_nickname);
        mDrawerDescriptionIv = (TextView) mNavigationHeader.findViewById(R.id.drawer_description);
        mDrawerWeiBoCountTv = (TextView) mNavigationHeader.findViewById(R.id.drawer_weibo_count_tv);
        mDrawerFocusCountTv = (TextView) mNavigationHeader.findViewById(R.id.drawer_focus_count_tv);
        mDrawerWeiBoContainerLl = (LinearLayout) mNavigationHeader.findViewById(R.id.drawer_weibo_count_ll);
        mDrawerFollowersCountTv = (TextView) mNavigationHeader.findViewById(R.id.drawer_follower_count_tv);
        mDrawerFocusContainerLl = (LinearLayout) mNavigationHeader.findViewById(R.id.drawer_followers_count_ll);
        mDrawerFollowerCountLl = (LinearLayout) mNavigationHeader.findViewById(R.id.drawer_focus_count_ll);
    }

    private void initData() {
        mRefreshAll = getIntent().getBooleanExtra(EXTRA_REFRESH_ALL, false);
    }

    private void initView() {
        DebugTool.showEnvironment(mContext);
        mFragmentManager = getSupportFragmentManager();
        //初始化顶部导航栏
        initToolBar();
        //初始化抽屉栏
        initDrawerLayout();
        //初始化内容栏，显示我的微博
        setTabFragment(TAB_HOME_FRAGMENT);
    }

    private void initToolBar() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initDrawerLayout() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.drawer_navigation_open, R.string.drawer_navigation_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        //显示抽屉栏目icon原来的颜色
        mNavigationView.setItemIconTintList(null);
        //显示我的信息
        initMySelfInfo();
    }

    /**
     * 显示我的信息
     */
    private void initMySelfInfo() {
        UserManager.getInstance().getUserInfo(AccessTokenManager.getInstance().getOAuthToken().getToken(), AppAuthConstants.APP_KEY, AccessTokenManager.getInstance().getOAuthToken().getUid(), new UserInfoCallBack() {
            @Override
            public void onSuccess(User user) {
                //设置头像
                ImageLoader.getInstance().displayImage(UserManager.getInstance().getUser().avatar_hd, mDrawerAvatarIv, mOptions);
                //设置昵称
                mDrawerNickNameIv.setText(UserManager.getInstance().getUser().name);
                //设置简介
                mDrawerDescriptionIv.setText(String.format(getString(R.string.drawer_desciption_format), UserManager.getInstance().getUser().description));
                //设置微博数
                mDrawerWeiBoCountTv.setText(String.valueOf(UserManager.getInstance().getUser().statuses_count));
                //设置关注数
                mDrawerFocusCountTv.setText(String.valueOf(UserManager.getInstance().getUser().friends_count));
                //设置粉丝数
                mDrawerFollowersCountTv.setText(String.valueOf(UserManager.getInstance().getUser().followers_count));
            }

            @Override
            public void onFail() {

            }
        });
    }

    private void initListener() {
        mNavigationHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.getInstance().getUser() == null) {
                    return;
                }
                Intent intent = new Intent(mContext, MyWeiBoSwipeActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
            }
        });
        mDrawerWeiBoContainerLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserManager.getInstance().getUser() == null) {
                    return;
                }
                Intent intent = new Intent(mContext, MyWeiBoSwipeActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
            }
        });
        mDrawerFocusContainerLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserManager.getInstance().getUser() == null) {
                    return;
                }
                Intent intent = new Intent(mContext, FocusSwipeActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
            }
        });
        mDrawerFollowerCountLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserManager.getInstance().getUser() == null) {
                    return;
                }
                Intent intent = new Intent(mContext, FansSwipeActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
            }
        });
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawer_menu_timeline:
                        setTabFragment(TAB_HOME_FRAGMENT);
                        break;
                    case R.id.drawer_menu_notication:
                        setTabFragment(TAB_MESSAGE_FRAGMENT);
                        break;
                    case R.id.drawer_menu_favorities:
                        if (UserManager.getInstance().getUser() == null) {
                            return false;
                        }
                        Intent intent = new Intent(mContext, CollectSwipeActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.drawer_menu_hotweibo:
                        setTabFragment(TAB_DISCOVERY_FRAGMENT);
                        break;
                    case R.id.drawer_menu_setting:
                        startActivity(new Intent(mContext, SettingSwipeActivity.class));
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /**
     * 执行切换fragment 的操作
     * 注意：
     * 1. 切换页面的时候，还要调用showBottomBar来保证底部导航栏的显示
     */
    private void switchToFragment(String index) {
        mTransaction = mFragmentManager.beginTransaction();
        hideAllFragments(mTransaction);
        switch (index) {
            case TAB_HOME_FRAGMENT:
                showHomeFragment();
                break;
            case TAB_MESSAGE_FRAGMENT:
                showMessageFragment();
                break;
            case TAB_DISCOVERY_FRAGMENT:
                showDiscoveryFragment();
                break;
            case TAB_PROFILE_FRAGMENT:
                showProfileFragment();
                break;
        }
        mCurrentIndex = index;
        mTransaction.commit();
    }

    /**
     * 切换到首页模块
     */
    private void showHomeFragment() {
        if (mHomeFragment == null) {
            mHomeFragment = HomeFragment.newInstance(mRefreshAll);
            mHomeFragment.setTabLayout(mTabLayout);
            new HomePresenter(new HomeDataManager(mContext), mHomeFragment);
            mTransaction.add(R.id.main_content_fl, mHomeFragment, TAB_HOME_FRAGMENT);
        } else {
            mTransaction.show(mHomeFragment);
        }
        mTabLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 切换到消息模块
     */
    private void showMessageFragment() {
        if (mMessageFragment == null) {
            mMessageFragment = new MessageFragment();
            mTransaction.add(R.id.main_content_fl, mMessageFragment, TAB_MESSAGE_FRAGMENT);
        } else {
            mTransaction.show(mMessageFragment);
        }
        mTabLayout.setVisibility(View.GONE);
    }

    /**
     * 切换到发现模块
     */
    private void showDiscoveryFragment() {
        if (mDiscoverFragment == null) {
            mDiscoverFragment = new DiscoverFragment();
            mTransaction.add(R.id.main_content_fl, mDiscoverFragment, TAB_DISCOVERY_FRAGMENT);
        } else {
            mTransaction.show(mDiscoverFragment);
        }
        mTabLayout.setVisibility(View.GONE);
    }

    /**
     * 切换到关于我模块
     */
    private void showProfileFragment() {
        if (mMySelfFragment == null) {
            User currentUser = UserManager.getInstance().getUser();
            if (mHomeFragment != null && currentUser != null) {
                mMySelfFragment = MySelfFragment.newInstance(currentUser);
            } else {
                mMySelfFragment = MySelfFragment.newInstance();
            }
            mTransaction.add(R.id.main_content_fl, mMySelfFragment, TAB_PROFILE_FRAGMENT);
        } else {
            mTransaction.show(mMySelfFragment);
        }
        mTabLayout.setVisibility(View.GONE);
    }


    /**
     * 显示指定的fragment，并且把对应的导航栏的icon设置成高亮状态
     * 注意：
     * 1. 如果选项卡已经位于当前页，则执行其他操作
     *
     * @param tabName 需要切换到的具体页面
     */
    private void setTabFragment(String tabName) {
        if (!tabName.equals(mCurrentIndex)) {
            switchToFragment(tabName);
        } else {
            alreadyAtFragment(mCurrentIndex);
        }
    }

    /**
     * 如果选项卡已经位于当前页
     * 1. 对于首页fragment，执行：滑动到顶部，并且刷新时间线，获取最新微博
     * 2. 对于消息fragment，执行：无
     * 3. 对于发现fragment，执行：无
     * 4. 对于关于我fragment，执行：无
     */
    private void alreadyAtFragment(String currentIndex) {
        //如果在当前页
        switch (currentIndex) {
            case TAB_HOME_FRAGMENT:
                if (mHomeFragment != null) {
                    mHomeFragment.scrollToTop();
                }
                break;
            case TAB_MESSAGE_FRAGMENT:
                break;
            case TAB_DISCOVERY_FRAGMENT:
                break;
            case TAB_PROFILE_FRAGMENT:
                break;
        }
    }


    /**
     * 隐藏所有的fragment，并且取消所有的底部导航栏的icon的高亮状态
     */
    private void hideAllFragments(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mMessageFragment != null) {
            transaction.hide(mMessageFragment);
        }

        if (mDiscoverFragment != null) {
            transaction.hide(mDiscoverFragment);
        }
        if (mMySelfFragment != null) {
            transaction.hide(mMySelfFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mHomeFragment != null) {
            mHomeFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 监听返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            if (mSnackbar == null || !mSnackbar.isShown()) {
                mSnackbar = Snackbar.make(mDrawerLayout, "确定要退出？", Toast.LENGTH_SHORT);
                mSnackbar.setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ApplicationHelper) getApplication()).finishAll();
                    }
                });
                mSnackbar.show();
            } else {
                mSnackbar.dismiss();
            }
        }
        return false;
    }
}
