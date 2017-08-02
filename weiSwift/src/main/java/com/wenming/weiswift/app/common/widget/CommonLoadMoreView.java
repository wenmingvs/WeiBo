package com.wenming.weiswift.app.common.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.wenming.weiswift.R;

/**
 * Created by wenmingvs on 2017/8/2.
 */

public class CommonLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.common_loadmore;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.common_loadmore_loading_ll;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.common_loadmore_fail_fl;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.common_loadmore_empty_fl;
    }
}
