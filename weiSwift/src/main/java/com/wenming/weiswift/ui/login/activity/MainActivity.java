package com.wenming.weiswift.ui.login.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wenming.weiswift.MyApplication;
import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.BarManager;
import com.wenming.weiswift.ui.common.StatusBarUtils;
import com.wenming.weiswift.ui.login.fragment.discovery.DiscoverFragment;
import com.wenming.weiswift.ui.login.fragment.home.HomeFragment;
import com.wenming.weiswift.ui.login.fragment.message.MessageFragment;
import com.wenming.weiswift.ui.login.fragment.post.PostActivity;
import com.wenming.weiswift.ui.login.fragment.profile.ProfileFragment;
import com.wenming.weiswift.utils.LogUtil;
import com.wenming.weiswift.utils.SharedPreferencesUtil;

import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity {

    /**
     * 首页fragment的标识
     */
    private static final String HOME_FRAGMENT = "home";
    /**
     * 消息fragment的标识
     */
    private static final String MESSAGE_FRAGMENT = "message";
    /**
     * 发现fragment的标识
     */
    private static final String DISCOVERY_FRAGMENT = "discovery";
    /**
     * 关于我fragment的标识
     */
    private static final String PROFILE_FRAGMENT = "profile";

    /**
     * 标识处于哪个fragment
     */
    private String mCurrentIndex;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 首页fragment
     */
    private HomeFragment mHomeFragment;
    /**
     * 消息fragment
     */
    private MessageFragment mMessageFragment;
    /**
     * 发现fragment
     */
    private DiscoverFragment mDiscoverFragment;
    /**
     * 关于我fragment
     */
    private ProfileFragment mProfileFragment;

    /**
     * 管理fragment的类
     */
    private FragmentManager mFragmentManager;

    /**
     * 底部icon的点击区域，分别标识首页， 消息，发现，关于我
     */
    private RelativeLayout mHomeTab, mMessageTab, mDiscoeryTab, mProfile;
    /**
     * 发微博按钮
     */
    private ImageView mPostTab;
    /**
     * 标识此Activity是否来自AccoutActivity的跳转
     */
    private boolean mComeFromAccoutActivity;
    /**
     * 底部导航栏
     */
    private LinearLayout mButtonBar;

    /**
     * 对fragment进行添加,移除,替换,以及执行其他动作。
     */
    private FragmentTransaction mTransaction;

    /**
     * 管理顶部与底部的导航栏的显示与隐藏
     */
    private BarManager mBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_layout);
        mContext = this;
        mHomeTab = (RelativeLayout) findViewById(R.id.tv_home);
        mMessageTab = (RelativeLayout) findViewById(R.id.tv_message);
        mDiscoeryTab = (RelativeLayout) findViewById(R.id.tv_discovery);
        mProfile = (RelativeLayout) findViewById(R.id.tv_profile);
        mPostTab = (ImageView) findViewById(R.id.fl_post);
        mButtonBar = (LinearLayout) findViewById(R.id.buttonBarId);
        //mSnackBarContainer = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutId);

        //LogReport.getInstance().upload(mContext);
        mBarManager = new BarManager(mContext);
        mFragmentManager = getSupportFragmentManager();
        mComeFromAccoutActivity = getIntent().getBooleanExtra("comeFromAccoutActivity", false);

        //如果是从崩溃中恢复，还需要加载之前的缓存
        if (savedInstanceState != null) {
            restoreFragment(savedInstanceState);
        } else {
            setTabFragment(HOME_FRAGMENT);
        }
        setUpListener();
        boolean isNightMode = (boolean) SharedPreferencesUtil.get(this, "setNightMode", false);
        if (!isNightMode) {
            StatusBarUtils.from(this)
                    .setTransparentStatusbar(true)
                    .setStatusBarColor(getResources().getColor(R.color.home_status_bg))
                    .setLightStatusBar(true)
                    .process(this);
        } else {
            StatusBarUtils.from(this)
                    .setTransparentStatusbar(true)
                    .setStatusBarColor(getResources().getColor(R.color.home_status_bg))
                    .process(this);
        }
    }

    @Override
    protected void onDestroy() {
        fixInputMethodManagerLeak(this);
        super.onDestroy();
    }

    /**
     * 如果fragment因为内存不够或者其他原因被销毁掉，在这个方法中执行恢复操作
     */
    private void restoreFragment(Bundle savedInstanceState) {
        mCurrentIndex = savedInstanceState.getString("index");
        mHomeFragment = (HomeFragment) mFragmentManager.findFragmentByTag(HOME_FRAGMENT);
        mMessageFragment = (MessageFragment) mFragmentManager.findFragmentByTag(MESSAGE_FRAGMENT);
        mDiscoverFragment = (DiscoverFragment) mFragmentManager.findFragmentByTag(DISCOVERY_FRAGMENT);
        mProfileFragment = (ProfileFragment) mFragmentManager.findFragmentByTag(PROFILE_FRAGMENT);
        switchToFragment(mCurrentIndex);
    }

    /**
     * Activity被销毁的时候，要记录当前处于哪个页面
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("index", mCurrentIndex);
        super.onSaveInstanceState(outState);
    }

    /**
     * 设计底部导航栏5个按钮的点击事件
     */
    private void setUpListener() {
        mHomeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(HOME_FRAGMENT);
            }
        });
        mMessageTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(MESSAGE_FRAGMENT);
            }
        });
        mPostTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        mDiscoeryTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(DISCOVERY_FRAGMENT);
            }
        });

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(PROFILE_FRAGMENT);
            }
        });
    }

    /**
     * 执行切换fragment 的操作
     * 注意：
     * 1. 切换页面的时候，还要调用showBottomBar来保证底部导航栏的显示
     *
     * @param index
     */
    private void switchToFragment(String index) {
        mButtonBar.clearAnimation();
        mButtonBar.setVisibility(View.VISIBLE);
        mTransaction = mFragmentManager.beginTransaction();
        hideAllFragments(mTransaction);
        switch (index) {
            case HOME_FRAGMENT:
                showHomeFragment();
                break;
            case MESSAGE_FRAGMENT:
                showMessageFragment();
                break;
            case DISCOVERY_FRAGMENT:
                showDiscoveryFragment();
                break;
            case PROFILE_FRAGMENT:
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
        mHomeTab.setSelected(true);
        if (mHomeFragment == null) {
            mHomeFragment = HomeFragment.newInstance(mComeFromAccoutActivity);
            mTransaction.add(R.id.contentLayout, mHomeFragment, HOME_FRAGMENT);
        } else {
            mTransaction.show(mHomeFragment);
            if (mCurrentIndex.equals(HOME_FRAGMENT) && mHomeFragment != null && mHomeFragment.mRecyclerView != null) {
                mHomeFragment.scrollToTop(false);
            }
        }
        mHomeFragment.setOnBarListener(new HomeFragment.onButtonBarListener() {
            @Override
            public void showButtonBar() {
                mBarManager.showBottomBar(mButtonBar);
            }

            @Override
            public void hideButtonBar() {
                mBarManager.hideBottomBar(mButtonBar);
            }
        });
    }

    /**
     * 切换到消息模块
     */
    private void showMessageFragment() {
        mMessageTab.setSelected(true);
        if (mMessageFragment == null) {
            mMessageFragment = new MessageFragment();
            mTransaction.add(R.id.contentLayout, mMessageFragment, MESSAGE_FRAGMENT);
        } else {
            mTransaction.show(mMessageFragment);
        }
    }

    /**
     * 切换到发现模块
     */
    private void showDiscoveryFragment() {
        mDiscoeryTab.setSelected(true);
        if (mDiscoverFragment == null) {
            mDiscoverFragment = new DiscoverFragment();
            mTransaction.add(R.id.contentLayout, mDiscoverFragment, DISCOVERY_FRAGMENT);
        } else {
            mTransaction.show(mDiscoverFragment);
        }
    }

    /**
     * 切换到关于我模块
     */
    private void showProfileFragment() {
        mProfile.setSelected(true);
        if (mProfileFragment == null) {
            mProfileFragment = new ProfileFragment();
            mTransaction.add(R.id.contentLayout, mProfileFragment, PROFILE_FRAGMENT);
        } else {
            mTransaction.show(mProfileFragment);

        }
    }


    /**
     * 显示指定的fragment，并且把对应的导航栏的icon设置成高亮状态
     * 注意：
     * 1. 如果选项卡已经位于当前页，则执行其他操作
     *
     * @param index 需要切换到的具体页面
     */
    private void setTabFragment(String index) {
        if (mHomeFragment != null) {
            mBarManager.showBottomBar(mButtonBar);
        }

        if (!index.equals(mCurrentIndex)) {
            switchToFragment(index);
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
     *
     * @param currentIndex
     */
    private void alreadyAtFragment(String currentIndex) {
        //如果在当前页
        switch (currentIndex) {
            case HOME_FRAGMENT:
                if (mHomeFragment != null) {
                    mHomeFragment.scrollToTop(true);
                }
                break;
            case MESSAGE_FRAGMENT:
                break;
            case DISCOVERY_FRAGMENT:
                break;
            case PROFILE_FRAGMENT:
                break;
        }
    }


    /**
     * 隐藏所有的fragment，并且取消所有的底部导航栏的icon的高亮状态
     *
     * @param transaction
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
        if (mProfileFragment != null) {
            transaction.hide(mProfileFragment);
        }
        mHomeTab.setSelected(false);
        mMessageTab.setSelected(false);
        mDiscoeryTab.setSelected(false);
        mProfile.setSelected(false);
    }


    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mHomeFragment != null) {
            mHomeFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 监听返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            showExitDialog();
        }
        return false;
    }

    /**
     * 解决输入法中的内存泄漏问题
     *
     * @param destContext
     */
    public void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        LogUtil.d("fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext() + " dest_context=" + destContext);
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * 显示退出窗口
     */
    public void showExitDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确定要退出？")
                .setCancelable(true)
                .setIcon(R.drawable.logo)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((MyApplication) getApplication()).finishAll();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


}
