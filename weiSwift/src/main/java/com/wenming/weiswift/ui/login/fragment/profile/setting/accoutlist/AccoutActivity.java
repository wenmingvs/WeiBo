package com.wenming.weiswift.ui.login.fragment.profile.setting.accoutlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.mvp.presenter.AccoutActivityPresent;
import com.wenming.weiswift.mvp.presenter.imp.AccoutActivityPresentImp;
import com.wenming.weiswift.mvp.view.AccoutActivityView;
import com.wenming.weiswift.ui.common.BaseSwipeActivity;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.ui.login.activity.MainActivity;
import com.wenming.weiswift.ui.unlogin.activity.UnLoginActivity;
import com.wenming.weiswift.ui.unlogin.activity.WebViewActivity;
import com.wenming.weiswift.utils.DensityUtil;
import com.wenming.weiswift.widget.mdprogressbar.CircleProgressBar;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/18.
 */
public class AccoutActivity extends BaseSwipeActivity implements AccoutActivityView {

    private ListView listview;
    private RelativeLayout mCurrentAccoutLogOut;
    private Context mContext;
    private AccoutActivityPresent mAccoutActivityPresent;
    private LinearLayout mAddAccountBut;
    private AccoutAdapter mAdapter;
    private CircleProgressBar mProgressBar;
    private boolean mCurrentTokenLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accout_layout);
        mContext = this;
        mAccoutActivityPresent = new AccoutActivityPresentImp(this);
        listview = (ListView) findViewById(R.id.listview);
        mCurrentAccoutLogOut = (RelativeLayout) findViewById(R.id.logoutLayout);
        mProgressBar = (CircleProgressBar) findViewById(R.id.progressbar);
        mProgressBar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mAccoutActivityPresent.obtainUserListDetail(mContext);
    }

    @Override
    public void setUpListener() {
        mCurrentAccoutLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentTokenLogout = true;
                mAccoutActivityPresent.logoutCurrentAccout(mContext);
            }
        });
        mAddAccountBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", Constants.authurl);
                intent.putExtra("comeFromAccoutActivity", true);
                startActivity(intent);
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uid = ((User) listview.getAdapter().getItem(position)).id;
                mAccoutActivityPresent.switchAccout(mContext, uid);
                Intent intent = new Intent(AccoutActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("comeFromAccoutActivity", true);
                startActivity(intent);
                finish();
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定要删除此帐户？")
                        .setCancelable(true)
                        .setIcon(R.drawable.logo)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String uid = ((User) listview.getAdapter().getItem(position)).id;
                                String currentUid = AccessTokenKeeper.readAccessToken(mContext).getUid();

                                if (!uid.equals(currentUid)) {
                                    mAccoutActivityPresent.logout(mContext, uid);
                                } else {
                                    mCurrentAccoutLogOut.performClick();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public void initListView(ArrayList<User> userArrayList) {
        mAddAccountBut = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.accout_layout_item_footerview, null);
        mAdapter = new AccoutAdapter(mContext, userArrayList);
        listview.addFooterView(mAddAccountBut);
        listview.setDivider(new ColorDrawable(Color.parseColor("#e5e5e5")));
        listview.setDividerHeight(DensityUtil.dp2px(mContext, 1));
        listview.setAdapter(mAdapter);
    }

    @Override
    public void finishItself() {
        finish();
        ;
    }

    @Override
    public void updateListView(ArrayList<User> userArrayList) {
        if (userArrayList == null || userArrayList.size() == 0) {
            mAdapter.notifyDataSetChanged();
            Intent intent = new Intent(mContext, UnLoginActivity.class);
            startActivity(intent);
            return;
        }
        mAdapter.setData(userArrayList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showListView() {
        listview.setVisibility(View.VISIBLE);
        mCurrentAccoutLogOut.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideListView() {
        listview.setVisibility(View.GONE);
        mCurrentAccoutLogOut.setVisibility(View.GONE);
    }

    @Override
    public void showProgressDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressDialog() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            exit(null);
        }
        return false;
    }

    public void exit(View View) {
        if (mCurrentTokenLogout) {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("comeFromAccoutActivity", true);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

}
