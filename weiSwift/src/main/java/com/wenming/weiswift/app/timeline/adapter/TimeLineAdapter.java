package com.wenming.weiswift.app.timeline.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wenming.weiswift.app.common.entity.Status;

import java.util.List;

/**
 * Created by wenmingvs on 2017/7/23.
 */

public class TimeLineAdapter extends BaseMultiItemQuickAdapter<Status, BaseViewHolder> {

    public TimeLineAdapter(@Nullable List<Status> data) {
        super(data);

    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Status item) {
        
    }
}
