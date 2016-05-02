package com.wenming.weiswift.fragment.home.userdetail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.sina.weibo.sdk.openapi.models.User;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.DetailActivity;
import com.wenming.weiswift.common.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wenming.weiswift.common.endlessrecyclerview.RecyclerViewUtils;
import com.wenming.weiswift.common.util.DensityUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class UserActivity extends DetailActivity {

    public RecyclerView mRecyclerView;
    public HomePageAdapter mAdapter;
    public LinearLayoutManager mLayoutManager;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private ArrayList<String> mHomePageDatas;
    private boolean mNoMoreData;
    private User mUser;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

    }

    @Override
    public void requestWindowFeature() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void initTitleBar() {

    }


    @Override
    public void initRecyclerView() {
        mUser = getIntent().getParcelableExtra("user");
        mHomePageDatas = new ArrayList<>();
        mHomePageDatas.add(mUser.location);
        mHomePageDatas.add(mUser.description);
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mAdapter = new HomePageAdapter(mContext, mHomePageDatas);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        RecyclerViewUtils.setFooterView(mRecyclerView, new HomePageFooter(mContext));
        RecyclerViewUtils.setHeaderView(mRecyclerView, new UserHeadView(mContext, mUser));
        mRecyclerView.addItemDecoration(new HomePageItemSapce(DensityUtil.dp2px(mContext,10)));
    }


    @Override
    public void pullToRefreshData() {

    }

    @Override
    public void requestMoreData() {

    }

    @Override
    public void loadMoreData(String string) {

    }

    @Override
    public void updateList() {

    }


}
