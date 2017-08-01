package com.wenming.weiswift.app.timeline.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.timeline.constants.Constants;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineContract;
import com.wenming.weiswift.app.timeline.data.viewholder.origin.OriginPresenter;
import com.wenming.weiswift.app.timeline.data.viewholder.origin.OriginViewHolder;
import com.wenming.weiswift.app.timeline.data.viewholder.retweet.RetweetPresenter;
import com.wenming.weiswift.app.timeline.data.viewholder.retweet.RetweetViewHolder;

import java.util.List;

/**
 * Created by wenmingvs on 2017/7/23.
 */

public class TimeLineAdapter extends BaseMultiItemQuickAdapter<Status, BaseViewHolder> {
    private Context mContext;

    public TimeLineAdapter(Context context, @Nullable List<Status> data) {
        super(data);
        this.mContext = context;
        addItemType(Constants.TYPE_ORINGIN_ITEM, R.layout.timeline_original_pictext);
        addItemType(Constants.TYPE_RETWEET_ITEM, R.layout.timeline_retweet_pictext);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Constants.TYPE_ORINGIN_ITEM:
                return new OriginViewHolder(mContext, getItemView(R.layout.timeline_original_pictext, parent));
            case Constants.TYPE_RETWEET_ITEM:
                return new RetweetViewHolder(mContext, getItemView(R.layout.timeline_retweet_pictext, parent));
            default:
                return super.onCreateDefViewHolder(parent, viewType);
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, final Status status) {
        switch (holder.getItemViewType()) {
            case Constants.TYPE_ORINGIN_ITEM:
                onBindOriginHolder((OriginViewHolder) holder, status);
                break;
            case Constants.TYPE_RETWEET_ITEM:
                onBindRetweetHolder((RetweetViewHolder) holder, status);
                break;
        }
    }

    private void onBindOriginHolder(final OriginViewHolder holder, final Status status) {
        BaseTimeLineContract.Presenter presenter = holder.getPresenter();
        if (presenter == null) {
            presenter = new OriginPresenter(holder, status);
        } else {
            presenter.reset();
        }
        presenter.start();
    }

    private void onBindRetweetHolder(final RetweetViewHolder holder, final Status status) {
        BaseTimeLineContract.Presenter presenter = holder.getPresenter();
        if (presenter == null) {
            presenter = new RetweetPresenter(holder, status);
        } else {
            presenter.reset();
        }
        presenter.start();
    }
}
