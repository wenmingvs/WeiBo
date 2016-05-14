package com.wenming.weiswift.ui.login.fragment.post.picselect.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.utils.ScreenUtil;
import com.wenming.weiswift.ui.login.fragment.post.picselect.adapter.GirdViewAdapter;
import com.wenming.weiswift.ui.login.fragment.post.picselect.bean.AlbumFolderInfo;
import com.wenming.weiswift.ui.login.fragment.post.picselect.bean.ImageInfo;
import com.wenming.weiswift.ui.login.fragment.post.picselect.popwindow.ImgFolderPopWindow;
import com.wenming.weiswift.ui.login.fragment.post.picselect.util.ImageScan;

import java.util.ArrayList;


/**
 * Created by wenmingvs on 16/5/5.
 */
public class AlbumActivity extends Activity implements ImgFolderPopWindow.OnFolderClickListener, GirdViewAdapter.OnImgSelectListener {

    private Context mContext;
    private TextView mCancal;
    private LinearLayout folderLayout;
    private TextView foldername;
    private TextView mNext;
    private TextView preview;
    private TextView originpic;
    private GridView gridview;
    private ArrayList<AlbumFolderInfo> mFolderList;
    private ArrayList<ImageInfo> mSelectImgList;
    private GirdViewAdapter mAdapter;
    private ImgFolderPopWindow mPopWindow;
    private int mCurrentFolder;
    private RelativeLayout toolbarLayout;
    private ImageView mArrow;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_pic_layout);
        mSelectImgList = getIntent().getParcelableArrayListExtra("selectedImglist");
        mCurrentFolder = 0;
        mContext = this;
        toolbarLayout = (RelativeLayout) findViewById(R.id.toolbar_layout);
        mCancal = (TextView) findViewById(R.id.cancal);
        folderLayout = (LinearLayout) findViewById(R.id.folder);
        foldername = (TextView) findViewById(R.id.foldername);
        mNext = (TextView) findViewById(R.id.next);
        preview = (TextView) findViewById(R.id.preview);
        originpic = (TextView) findViewById(R.id.originpic);
        gridview = (GridView) findViewById(R.id.gridview);
        mArrow = (ImageView) findViewById(R.id.folder_arrow);

        //开始扫描图片
        ImageScan imageScan = new ImageScan(mContext, getLoaderManager()) {
            @Override
            public void onScanFinish(ArrayList<AlbumFolderInfo> folderList) {
                mFolderList = folderList;
                setAlreadySelectFile(mSelectImgList, mFolderList);
                Message message = Message.obtain();
                mHandler.sendMessage(message);
            }
        };
        changeSendButtonBg(mNext, mSelectImgList.size());
        setUpListener();

    }

    private void setAlreadySelectFile(ArrayList<ImageInfo> selectImgList, ArrayList<AlbumFolderInfo> folderList) {

        if (selectImgList == null || selectImgList.size() == 0) {
            return;
        }
        String selectPath = "";
        ArrayList<ImageInfo> allImg = (ArrayList<ImageInfo>) folderList.get(0).getImageInfoList();

        for (ImageInfo selectInfo : selectImgList) {
            //拿到绝对路径之后
            selectPath = selectInfo.getImageFile().getAbsolutePath();
            //根据绝对路路径取出对应的imageinfor，修改select的值
            for (int i = 0; i < allImg.size(); i++) {
                if (selectPath.equals(allImg.get(i).getImageFile().getAbsolutePath())) {
                    allImg.get(i).setIsSelected(true);
                }
            }
        }
    }


    private void setUpListener() {
        folderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow = new ImgFolderPopWindow(mContext, ScreenUtil.getScreenWidth(mContext), (int) (ScreenUtil.getScreenHeight(mContext) * 0.6), mFolderList, mCurrentFolder);
                mPopWindow.setOnFolderClickListener(AlbumActivity.this);
                mPopWindow.showAsDropDown(toolbarLayout);
                mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mArrow.setImageResource(R.drawable.navigationbar_arrow_down);
                    }
                });
                mArrow.setImageResource(R.drawable.navigationbar_arrow_up);
            }
        });
        mCancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("selectImgList", mSelectImgList);
                setResult(1, intent);
                finish();
            }
        });
    }


    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            initGridView(mCurrentFolder);
        }
    };

    private void initGridView(int position) {
        mAdapter = new GirdViewAdapter(mContext, mFolderList.get(position).getImageInfoList(), mSelectImgList);
        mAdapter.setOnImgSelectListener(this);
        gridview.setAdapter(mAdapter);

    }


    @Override
    public void OnFolderClick(int position) {
        foldername.setText(mFolderList.get(position).getFolderName());
        mCurrentFolder = position;
        initGridView(position);
        mPopWindow.dismiss();

    }

    @Override
    public void OnSelect(ArrayList<ImageInfo> imageInfos) {
        mSelectImgList = imageInfos;
        changeSendButtonBg(mNext, mSelectImgList.size());
    }

    @Override
    public void OnDisSelect(ArrayList<ImageInfo> imageInfos) {
        mSelectImgList = imageInfos;
        changeSendButtonBg(mNext, mSelectImgList.size());
    }

    /**
     * 根据输入的文本数量，决定发送按钮的背景
     *
     * @param textView
     * @param length
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void changeSendButtonBg(TextView textView, int length) {
        if (length > 0) {
            textView.setBackground(getResources().getDrawable(R.drawable.compose_send_corners_highlight_bg));
            textView.setTextColor(Color.parseColor("#fbffff"));
            textView.setText("下一步(" + length + ")");
            textView.setEnabled(true);
        } else {
            textView.setBackground(getResources().getDrawable(R.drawable.compose_send_corners_bg));
            textView.setTextColor(Color.parseColor("#b3b3b3"));
            textView.setText("下一步");
            textView.setEnabled(false);
        }
    }


}
