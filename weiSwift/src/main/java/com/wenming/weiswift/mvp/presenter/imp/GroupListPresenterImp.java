package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.entity.Group;
import com.wenming.weiswift.mvp.model.GroupListModel;
import com.wenming.weiswift.mvp.model.imp.GroupListModelImp;
import com.wenming.weiswift.mvp.presenter.GroupListPresenter;
import com.wenming.weiswift.mvp.view.GroupPopWindowView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class GroupListPresenterImp implements GroupListPresenter {

    private GroupPopWindowView mGroupPopView;
    private GroupListModel mGroupListModel;

    public GroupListPresenterImp(GroupPopWindowView groupPopView) {
        this.mGroupPopView = groupPopView;
        this.mGroupListModel = new GroupListModelImp();
    }


    @Override
    public void updateListView(final Context context) {
        mGroupListModel.groups(context, new GroupListModel.OnDataFinishedListener() {
            @Override
            public void onComplete(ArrayList<Group> groupslist) {
                mGroupPopView.updateListView(groupslist);
            }

            @Override
            public void onError(String error) {
                mGroupPopView.showErrorMessage(error);
            }

        });
    }
}
