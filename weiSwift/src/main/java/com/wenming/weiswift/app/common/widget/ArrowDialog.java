package com.wenming.weiswift.app.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.mvp.presenter.WeiBoArrowPresent2;
import com.wenming.weiswift.app.mvp.presenter.imp.WeiBoArrowPresenterImp2;
import com.wenming.weiswift.app.home.adapter.WeiboAdapter;
import com.wenming.weiswift.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by wenmingvs on 2016/8/22.
 */
public abstract class ArrowDialog extends Dialog {

    public Context mContext;
    public TextView mDeleteTextView;
    public TextView mFavoriteTextView;
    public TextView mFriendShipTextView;
    public TextView mShareTv;
    public LinearLayout mDeleteLayout;
    public LinearLayout mFollerLayout;
    public LinearLayout mShareLayout;
    public WeiBoArrowPresent2 mWeiBoArrowPresent;
    public Status mStatus;
    public WeiboAdapter mWeiboAdapter;
    public int mItemPosition;
    public String mGroupName;
    public Bitmap mBitmap;

    public ArrowDialog(Context context, Status status, WeiboAdapter weiboAdapter, int position, String groupName) {
        super(context, R.style.ArrowDialog);
        mContext = context;
        mStatus = status;
        mWeiboAdapter = weiboAdapter;
        mItemPosition = position;
        mGroupName = groupName;
    }

    public ArrowDialog(Context context, Status status) {
        super(context);
        mContext = context;
        mStatus = status;
    }


    public ArrowDialog(Context context, Status status, Bitmap bitmap) {
        super(context);
        mContext = context;
        mStatus = status;
        mBitmap = bitmap;
    }

    public ArrowDialog(Context context, Status status, WeiboAdapter weiboAdapter, int position, String groupName, Bitmap bitmap) {
        super(context, R.style.ArrowDialog);
        mContext = context;
        mStatus = status;
        mWeiboAdapter = weiboAdapter;
        mItemPosition = position;
        mGroupName = groupName;
        mBitmap = bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_weiboitem_arrow_popwindow);
        if (mWeiboAdapter == null) {
            mWeiBoArrowPresent = new WeiBoArrowPresenterImp2(this);
        }
        mWeiBoArrowPresent = new WeiBoArrowPresenterImp2(this, mWeiboAdapter);

        mDeleteTextView = (TextView) findViewById(R.id.pop_deleteweibo);
        mFavoriteTextView = (TextView) findViewById(R.id.pop_collectweibo);
        mFriendShipTextView = (TextView) findViewById(R.id.pop_disfollow);
        mShareTv = (TextView) findViewById(R.id.pop_share);
        mDeleteLayout = (LinearLayout) findViewById(R.id.deleteLayout);
        mFollerLayout = (LinearLayout) findViewById(R.id.followLayout);
        mShareLayout = (LinearLayout) findViewById(R.id.sharelayout);
        initContent();
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    @Override
    public void show() {
        super.show();
    }

    public void initContent() {
        setFavoriteTextContext(mStatus, mFavoriteTextView);
        setFriendShipContext(mStatus, mFriendShipTextView);
        setDeleteViewContent(mStatus, mDeleteTextView);
        setShareViewContent(mStatus, mShareTv);
    }


    /**
     * 设置收藏的TextView的内容，如果收藏了此微博，则显示取消收藏，如果没有收藏，则显示收藏
     */
    public abstract void setFavoriteTextContext(final Status status, TextView textView);

    /**
     * 设置朋友的关系内容，如果已经关注，则显示取消关注，如果没有关注，则显示关注
     */
    public abstract void setFriendShipContext(final Status status, TextView textView);

    /**
     * 设置是否显示删除按钮，如果不是自己的微博，要隐藏他
     */
    public abstract void setDeleteViewContent(final Status status, final TextView textView);

    /**
     * 设置是否分享
     */
    public void setShareViewContent(final Status status, final TextView textView) {
        mShareLayout.setVisibility(View.VISIBLE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBitmap != null) {
                    File imgFile = bitmap2File(mBitmap);
                    showShare(imgFile.getAbsolutePath());
                } else {
                    ToastUtil.showShort(mContext, "截取的图片太大，返回null");
                }
                if (mBitmap != null && !mBitmap.isRecycled()) {
                    mBitmap.recycle();
                }
                dismiss();

            }
        });
    }

    private void showShare(String imgPath) {
        ShareSDK.initSDK(mContext);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setImagePath(imgPath);
        oks.show(mContext);
    }

    private File bitmap2File(Bitmap bitmap) {
        String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + ".jpg";
        String dir =  Environment.getExternalStorageDirectory().getPath() + "/weiSwift/.cache/";
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File bitmapFile = new File(dir, filename);
        if (!bitmapFile.exists()) {
            try {
                bitmapFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(mContext, "截图成功!", Toast.LENGTH_SHORT).show();
        return bitmapFile;
    }

}
