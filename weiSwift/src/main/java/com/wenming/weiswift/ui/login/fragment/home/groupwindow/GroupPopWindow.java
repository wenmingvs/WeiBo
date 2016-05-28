package com.wenming.weiswift.ui.login.fragment.home.groupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Group;
import com.wenming.weiswift.mvp.presenter.GroupListPresenter;
import com.wenming.weiswift.mvp.presenter.imp.GroupListPresenterImp;
import com.wenming.weiswift.mvp.view.GroupPopWindowView;
import com.wenming.weiswift.ui.common.NewFeature;
import com.wenming.weiswift.ui.login.fragment.home.imagedetaillist.ImageOptionPopupWindow;
import com.wenming.weiswift.utils.DensityUtil;
import com.wenming.weiswift.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/12.
 */
public class GroupPopWindow extends PopupWindow implements GroupPopWindowView {

    private static ListView mListView;
    private Context mContext;
    private ArrayList<Group> mDatas;
    private View mView;
    private TextView mGroupEdit;
    private GroupAdapter mAdapter;
    private int mWidth;
    private int mHeight;
    private final GroupListPresenter mGroupListPresenter;
    private IGroupItemClick mIGroupItemClick;
    private int mSelectIndex = 0;
    private int scrolledX;
    private int scrolledY;

    /**
     * 使用单例模式创建ImageOPtionPopupWindow
     */
    private static GroupPopWindow mGroupPopWindow;


    public static GroupPopWindow getInstance(Context context, int width, int height) {
        if (mGroupPopWindow == null) {
            synchronized (ImageOptionPopupWindow.class) {
                if (mGroupPopWindow == null) {
                    mGroupPopWindow = new GroupPopWindow(context.getApplicationContext(), width, height);
                }
            }
        }
        return mGroupPopWindow;
    }

    private GroupPopWindow(Context context, int width, int height) {
        super(context);
        this.mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.home_grouplist_pop, null);
        setContentView(mView);
        mWidth = width;
        mHeight = height;
        initPopWindow();
        initListView();
        setUpListener();
        mListView.scrollTo(scrolledX, scrolledY);
        mGroupListPresenter = new GroupListPresenterImp(this);
        mGroupListPresenter.updateGroupList(mContext);
    }

    private void initPopWindow() {
        this.setWindowLayoutMode(mWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(mWidth);
        this.setHeight(mHeight);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    private void initListView() {
        mDatas = new ArrayList<Group>();
        mDatas.add(new Group());
        mDatas.add(new Group());
        mListView = (ListView) mView.findViewById(R.id.listview);
        mAdapter = new GroupAdapter(mContext, mDatas);

        mAdapter.setOnGroupItemClickListener(new IGroupItemClick() {
            @Override
            public void onGroupItemClick(int position, long groupId, String groupName) {
                mSelectIndex = position;
                mIGroupItemClick.onGroupItemClick(position, groupId, groupName);
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setDivider(null);
        mListView.setItemsCanFocus(true);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            /**
             * 滚动状态改变时调用
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 不滚动时保存当前滚动到的位置
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    scrolledX = mListView.getScrollX();
                    scrolledY = mListView.getScrollY();
                }
            }

            /**
             * 滚动时调用
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public void scrollToSelectIndex() {
        mListView.scrollTo(scrolledX, scrolledY);
    }


    private void setUpListener() {
        mGroupEdit = (TextView) mView.findViewById(R.id.editgroup);
        mGroupEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "编辑我的分组");
            }
        });
    }


    @Override
    public void updateListView(ArrayList<Group> datas) {
        mDatas.addAll(datas);
        int height = 0;
        if (datas.size() > NewFeature.GROUP_SHOW_NUM) {
            mListView.getLayoutParams().height = DensityUtil.dp2px(mContext, 37) * (NewFeature.GROUP_SHOW_NUM + 3);//最多显示7个
        } else {
            mListView.getLayoutParams().height = DensityUtil.dp2px(mContext, 37) * (datas.size() + 3);
        }
        mAdapter.setDatas(mDatas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String error) {
        ToastUtil.showShort(mContext, error);
    }

    public void setOnGroupItemClickListener(IGroupItemClick groupItemClickListener) {
        this.mIGroupItemClick = groupItemClickListener;
    }

    public void onDestory() {
        if (mGroupPopWindow != null) {
            mGroupPopWindow = null;
        }
    }

}
