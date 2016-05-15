package com.wenming.weiswift.mvp.view;

import com.wenming.weiswift.entity.Group;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public interface GroupPopWindowView {

    /**
     * 将网络请求返回的数据，添加到ListView上,需要返回返回
     */
    public void updateListView(ArrayList<Group> datas);


    /**
     * Toast显示网络请求失败的错误信息
     *
     * @param error
     */
    public void showErrorMessage(String error);
}
