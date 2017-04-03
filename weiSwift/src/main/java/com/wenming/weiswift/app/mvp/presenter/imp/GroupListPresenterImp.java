package com.wenming.weiswift.app.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.app.common.entity.Group;
import com.wenming.weiswift.app.mvp.model.GroupListModel;
import com.wenming.weiswift.app.mvp.model.imp.GroupListModelImp;
import com.wenming.weiswift.app.mvp.presenter.GroupListPresenter;
import com.wenming.weiswift.app.mvp.view.GroupPopWindowView;
import com.wenming.weiswift.app.mvp.view.HomeFragmentView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class GroupListPresenterImp implements GroupListPresenter {

    private GroupPopWindowView mGroupPopView;
    private GroupListModel mGroupListModel;
    private HomeFragmentView mHomeFragmentView;

    public GroupListPresenterImp(GroupPopWindowView groupPopView) {
        this.mGroupPopView = groupPopView;
        this.mGroupListModel = new GroupListModelImp();
    }

    @Override
    public void updateGroupList(final Context context) {
        mGroupListModel.groupsOnlyOnce(context, new GroupListModel.OnGroupListFinishedListener() {
            @Override
            public void noMoreDate() {
                
            }

            @Override
            public void onDataFinish(ArrayList<Group> groupslist) {
                mGroupPopView.updateListView(groupslist);
            }

            @Override
            public void onError(String error) {
                mGroupPopView.showErrorMessage(error);
            }

        });
    }


}
