package com.wenming.weiswift.app.home.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.MyApplication;
import com.wenming.weiswift.app.common.BarManager;
import com.wenming.weiswift.app.common.StatusBarUtils;
import com.wenming.weiswift.app.common.base.BaseAppCompatActivity;
import com.wenming.weiswift.app.discover.DiscoverFragment;
import com.wenming.weiswift.app.home.fragment.HomeFragment;
import com.wenming.weiswift.app.login.fragment.post.PostSwipeActivity;
import com.wenming.weiswift.app.message.fragment.fragment.MessageFragment;
import com.wenming.weiswift.app.myself.fragment.MySelfFragment;
import com.wenming.weiswift.utils.LogUtil;
import com.wenming.weiswift.utils.SharedPreferencesUtil;

import java.lang.reflect.Field;


public class MainActivity extends BaseAppCompatActivity {

    private static final String TAB_HOME_FRAGMENT = "home";
    private static final String TAB_MESSAGE_FRAGMENT = "message";
    private static final String TAB_DISCOVERY_FRAGMENT = "discovery";
    private static final String TAB_PROFILE_FRAGMENT = "profile";

    private HomeFragment mHomeFragment;
    private MessageFragment mMessageFragment;
    private DiscoverFragment mDiscoverFragment;
    private MySelfFragment mMySelfFragment;
    private FragmentManager mFragmentManager;
    private RelativeLayout mHomeTabRl, mMessageTabRl, mDiscoverTabRl, mMySelfTabRl;
    private ImageView mPostTabIv;
    private LinearLayout mButtonBarLl;
    private FragmentTransaction mTransaction;
    private BarManager mBarManager;

    private String mCurrentIndex;
    private boolean mComeFromAccoutActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareView();
        initData();
        initView();
        initListener();
        //如果是从崩溃中恢复，还需要加载之前的缓存
        if (savedInstanceState != null) {
            restoreFragment(savedInstanceState);
        } else {
            setTabFragment(TAB_HOME_FRAGMENT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO 能否抽出来作为一个公共方法
        fixInputMethodManagerLeak(this);
    }

    private void prepareView() {
        mHomeTabRl = (RelativeLayout) findViewById(R.id.tv_home);
        mMessageTabRl = (RelativeLayout) findViewById(R.id.tv_message);
        mDiscoverTabRl = (RelativeLayout) findViewById(R.id.tv_discovery);
        mMySelfTabRl = (RelativeLayout) findViewById(R.id.tv_profile);
        mPostTabIv = (ImageView) findViewById(R.id.fl_post);
        mButtonBarLl = (LinearLayout) findViewById(R.id.buttonBarId);
    }

    private void initData() {
        mComeFromAccoutActivity = getIntent().getBooleanExtra("comeFromAccoutActivity", false);
    }

    private void initView() {
        //LogReport.getInstance().upload(mContext);
        mBarManager = new BarManager();
        mBarManager.showBottomBar(mButtonBarLl);
        mFragmentManager = getSupportFragmentManager();
        initStatusBar();
    }

    private void initStatusBar() {
        if (!(boolean) SharedPreferencesUtil.get(this, "setNightMode", false)) {
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

    private void initListener() {
        mHomeTabRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(TAB_HOME_FRAGMENT);
            }
        });
        mMessageTabRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(TAB_MESSAGE_FRAGMENT);
            }
        });
        mPostTabIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostSwipeActivity.class);
                startActivity(intent);
            }
        });

        mDiscoverTabRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(TAB_DISCOVERY_FRAGMENT);
            }
        });

        mMySelfTabRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(TAB_PROFILE_FRAGMENT);
            }
        });
    }

    /**
     * 如果fragment因为内存不够或者其他原因被销毁掉，在这个方法中执行恢复操作
     */
    private void restoreFragment(Bundle savedInstanceState) {
        mCurrentIndex = savedInstanceState.getString("index");
        mHomeFragment = (HomeFragment) mFragmentManager.findFragmentByTag(TAB_HOME_FRAGMENT);
        mMessageFragment = (MessageFragment) mFragmentManager.findFragmentByTag(TAB_MESSAGE_FRAGMENT);
        mDiscoverFragment = (DiscoverFragment) mFragmentManager.findFragmentByTag(TAB_DISCOVERY_FRAGMENT);
        mMySelfFragment = (MySelfFragment) mFragmentManager.findFragmentByTag(TAB_PROFILE_FRAGMENT);
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
     * 执行切换fragment 的操作
     * 注意：
     * 1. 切换页面的时候，还要调用showBottomBar来保证底部导航栏的显示
     *
     * @param index
     */
    private void switchToFragment(String index) {
        mButtonBarLl.clearAnimation();
        mButtonBarLl.setVisibility(View.VISIBLE);
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
        mHomeTabRl.setSelected(true);
        if (mHomeFragment == null) {
            mHomeFragment = HomeFragment.newInstance(mComeFromAccoutActivity);
            mTransaction.add(R.id.main_content_fl, mHomeFragment, TAB_HOME_FRAGMENT);
        } else {
            mTransaction.show(mHomeFragment);
            if (mCurrentIndex.equals(TAB_HOME_FRAGMENT) && mHomeFragment != null && mHomeFragment.mRecyclerView != null) {
                mHomeFragment.scrollToTop(false);
            }
        }
        mHomeFragment.setOnBarListener(new HomeFragment.onButtonBarListener() {
            @Override
            public void showButtonBar() {
                mBarManager.showBottomBar(mButtonBarLl);
            }

            @Override
            public void hideButtonBar() {
                mBarManager.hideBottomBar(mButtonBarLl);
            }
        });
    }

    /**
     * 切换到消息模块
     */
    private void showMessageFragment() {
        mMessageTabRl.setSelected(true);
        if (mMessageFragment == null) {
            mMessageFragment = new MessageFragment();
            mTransaction.add(R.id.main_content_fl, mMessageFragment, TAB_MESSAGE_FRAGMENT);
        } else {
            mTransaction.show(mMessageFragment);
        }
    }

    /**
     * 切换到发现模块
     */
    private void showDiscoveryFragment() {
        mDiscoverTabRl.setSelected(true);
        if (mDiscoverFragment == null) {
            mDiscoverFragment = new DiscoverFragment();
            mTransaction.add(R.id.main_content_fl, mDiscoverFragment, TAB_DISCOVERY_FRAGMENT);
        } else {
            mTransaction.show(mDiscoverFragment);
        }
    }

    /**
     * 切换到关于我模块
     */
    private void showProfileFragment() {
        mMySelfTabRl.setSelected(true);
        if (mMySelfFragment == null) {
            if (mHomeFragment != null && mHomeFragment.getCurrentUser() != null) {
                mMySelfFragment = MySelfFragment.newInstance(mHomeFragment.getCurrentUser());
            } else {
                mMySelfFragment = MySelfFragment.newInstance();
            }
            mTransaction.add(R.id.main_content_fl, mMySelfFragment, TAB_PROFILE_FRAGMENT);
        } else {
            mTransaction.show(mMySelfFragment);

        }
    }


    /**
     * 显示指定的fragment，并且把对应的导航栏的icon设置成高亮状态
     * 注意：
     * 1. 如果选项卡已经位于当前页，则执行其他操作
     *
     * @param tabName 需要切换到的具体页面
     */
    private void setTabFragment(String tabName) {
        if (mHomeFragment != null) {
            mBarManager.showBottomBar(mButtonBarLl);
        }

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
     *
     * @param currentIndex
     */
    private void alreadyAtFragment(String currentIndex) {
        //如果在当前页
        switch (currentIndex) {
            case TAB_HOME_FRAGMENT:
                if (mHomeFragment != null) {
                    mHomeFragment.scrollToTop(true);
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
        if (mMySelfFragment != null) {
            transaction.hide(mMySelfFragment);
        }
        mHomeTabRl.setSelected(false);
        mMessageTabRl.setSelected(false);
        mDiscoverTabRl.setSelected(false);
        mMySelfTabRl.setSelected(false);
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
     */
    public void fixInputMethodManagerLeak(Context context) {
        if (context == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f;
        Object obj_get;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == context) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        LogUtil.d("fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext() + " dest_context=" + context);
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
