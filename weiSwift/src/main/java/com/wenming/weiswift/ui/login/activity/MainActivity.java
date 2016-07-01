package com.wenming.weiswift.ui.login.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.MyApplication;
import com.wenming.weiswift.ui.login.fragment.discovery.DiscoverFragment;
import com.wenming.weiswift.ui.login.fragment.home.HomeFragment;
import com.wenming.weiswift.ui.login.fragment.message.MessageFragment;
import com.wenming.weiswift.ui.login.fragment.post.PostActivity;
import com.wenming.weiswift.ui.login.fragment.profile.ProfileFragment;
import com.wenming.weiswift.utils.LogUtil;

import java.lang.reflect.Field;


public class MainActivity extends FragmentActivity {

    private static final String HOME_FRAGMENT = "home";
    private static final String MESSAGE_FRAGMENT = "message";
    private static final String DISCOVERY_FRAGMENT = "discovery";
    private static final String PROFILE_FRAGMENT = "profile";

    private String mCurrentIndex;
    private Context mContext;
    private HomeFragment mHomeFragment;
    private MessageFragment mMessageFragment;
    private DiscoverFragment mDiscoverFragment;
    private ProfileFragment mProfileFragment;


    private FragmentManager mFragmentManager;
    private RelativeLayout mHomeTab, mMessageTab, mDiscoeryTab, mProfile;
    private ImageView mPostTab;
    private boolean mComeFromAccoutActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_layout);
        mComeFromAccoutActivity = getIntent().getBooleanExtra("comeFromAccoutActivity", false);
        mHomeTab = (RelativeLayout) findViewById(R.id.tv_home);
        mMessageTab = (RelativeLayout) findViewById(R.id.tv_message);
        mDiscoeryTab = (RelativeLayout) findViewById(R.id.tv_discovery);
        mProfile = (RelativeLayout) findViewById(R.id.tv_profile);
        mPostTab = (ImageView) findViewById(R.id.fl_post);
        mContext = this;
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getString("index");
            mHomeFragment = (HomeFragment) mFragmentManager.findFragmentByTag(HOME_FRAGMENT);
            mMessageFragment = (MessageFragment) mFragmentManager.findFragmentByTag(MESSAGE_FRAGMENT);
            mDiscoverFragment = (DiscoverFragment) mFragmentManager.findFragmentByTag(DISCOVERY_FRAGMENT);
            mProfileFragment = (ProfileFragment) mFragmentManager.findFragmentByTag(PROFILE_FRAGMENT);
            retoreFragment(mCurrentIndex, true);
        } else {
            setTabFragment(HOME_FRAGMENT);
        }
        setUpListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("index", mCurrentIndex);
        super.onSaveInstanceState(outState);
    }

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
//
//                if (mCurrentIndex.equals(PROFILE_FRAGMENT) && mProfileFragment != null && mProfileFragment.haveAlreadyRefresh()) {
//                    mProfileFragment.refreshUserDetail(mContext, false);
//                }
            }
        });
    }


    /**
     * @param index
     * @param screenRotate
     */
    private void retoreFragment(String index, boolean screenRotate) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideAllFragments(transaction);
        switch (index) {
            case HOME_FRAGMENT:
                mHomeTab.setSelected(true);
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment(mComeFromAccoutActivity);
                    transaction.add(R.id.contentLayout, mHomeFragment, HOME_FRAGMENT);
                } else {
                    transaction.show(mHomeFragment);
                    if (!screenRotate && mCurrentIndex.equals(HOME_FRAGMENT) && mHomeFragment != null) {
                        mHomeFragment.scrollToTop(false);
                    }
                }
                mCurrentIndex = HOME_FRAGMENT;
                break;
            case MESSAGE_FRAGMENT:
                mMessageTab.setSelected(true);
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                    transaction.add(R.id.contentLayout, mMessageFragment, MESSAGE_FRAGMENT);
                } else {
                    transaction.show(mMessageFragment);
                }
                mCurrentIndex = MESSAGE_FRAGMENT;
                break;

            case DISCOVERY_FRAGMENT:
                mDiscoeryTab.setSelected(true);
                if (mDiscoverFragment == null) {
                    mDiscoverFragment = new DiscoverFragment();
                    transaction.add(R.id.contentLayout, mDiscoverFragment, DISCOVERY_FRAGMENT);
                } else {
                    transaction.show(mDiscoverFragment);
                }
                mCurrentIndex = DISCOVERY_FRAGMENT;
                break;
            case PROFILE_FRAGMENT:
                mProfile.setSelected(true);
                if (mProfileFragment == null) {
                    mProfileFragment = new ProfileFragment();
                    transaction.add(R.id.contentLayout, mProfileFragment, PROFILE_FRAGMENT);
                } else {
                    transaction.show(mProfileFragment);

                }
                mCurrentIndex = PROFILE_FRAGMENT;
                break;
        }
        transaction.commit();
    }

    /**
     * 用于切换fragment，并且设置底部button的焦点
     *
     * @param index 需要切换到的具体页面
     */
    private void setTabFragment(String index) {
        //如果不位于当前页
        if (!index.equals(mCurrentIndex)) {
            retoreFragment(index, false);
        } else {
            //如果在当前页
            switch (mCurrentIndex) {
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
    }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mHomeFragment != null) {
            mHomeFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
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
        return false;
    }

    @Override
    protected void onDestroy() {
        fixInputMethodManagerLeak(this);
        super.onDestroy();
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
}
