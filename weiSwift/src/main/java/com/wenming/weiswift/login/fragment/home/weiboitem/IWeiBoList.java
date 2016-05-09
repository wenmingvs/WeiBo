package com.wenming.weiswift.login.fragment.home.weiboitem;

/**
 * Created by wenmingvs on 16/4/27.
 */
public interface IWeiBoList {

    public abstract void initTimeTask();

    public abstract void initRecyclerView();

    public abstract void firstLoadData();

    public abstract void pullToRefreshData(boolean firstHttpGet);

    public abstract void requestMoreData();

    public abstract void loadMoreData(String response);

    public abstract void updateList();

    public abstract void getlatestWeiBo();

    public abstract void refreshAllData();


}
