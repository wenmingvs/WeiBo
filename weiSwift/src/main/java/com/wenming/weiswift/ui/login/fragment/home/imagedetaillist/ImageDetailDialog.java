package com.wenming.weiswift.ui.login.fragment.home.imagedetaillist;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.ImageUtil;
import com.wenming.weiswift.utils.ToastUtil;

import java.io.File;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by xiangflight on 2016/4/22.
 */
public class ImageDetailDialog extends Dialog {

    private TextView mCancalTextView;
    private TextView mSavePicTextView;
    private TextView mRetweetTextView;
    private TextView mShareWeChatFrTv;
    private TextView mShareFriendTimeLineTv;
    private Context mContext;
    private String mImgURL;

    private DisplayImageOptions mOpition = new DisplayImageOptions.Builder()
            .cacheOnDisk(true)
            .build();

    /**
     * 用于加载微博列表图片的配置，进行安全压缩，尽可能的展示图片细节
     */


    public ImageDetailDialog(String url, Context context) {
        super(context, R.style.ImageSaveDialog);
        mContext = context;
        mImgURL = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_image_detail_list_pop_window);
        mCancalTextView = (TextView) findViewById(R.id.pop_cancal);
        mSavePicTextView = (TextView) findViewById(R.id.pop_savcpic);
        mRetweetTextView = (TextView) findViewById(R.id.pop_retweet);
        mShareWeChatFrTv = (TextView) findViewById(R.id.pop_wechat_friend_share);
        mShareFriendTimeLineTv = (TextView) findViewById(R.id.pop_wechat_timeline_share);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        setUpListener(mContext);

    }

    @Override
    public void dismiss() {
        ShareSDK.stopSDK(mContext);
        super.dismiss();
    }

    private void setUpListener(final Context context) {
        mCancalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mSavePicTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ImageUtil.saveImg(mContext, mImgURL);
            }
        });

        mRetweetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ToastUtil.showShort(context, "转发微博");
            }
        });
        mShareWeChatFrTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageLoader.getInstance().loadImage(mImgURL, mOpition, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        File imgFile = DiskCacheUtils.findInCache(mImgURL, ImageLoader.getInstance().getDiskCache());
                        if (imgFile == null) {
                            ToastUtil.showShort(context, "保存文件失败，请检查SD卡是否已满！");
                            return;
                        }
                        shareToWechatFriend(imgFile.getAbsolutePath());


                    }
                });
            }
        });

        mShareFriendTimeLineTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoader.getInstance().loadImage(mImgURL, mOpition, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        File imgFile = DiskCacheUtils.findInCache(mImgURL, ImageLoader.getInstance().getDiskCache());
                        if (imgFile == null) {
                            ToastUtil.showShort(context, "保存文件失败，请检查SD卡是否已满！");
                            return;
                        }
                        shareToWechatTimeLine(imgFile.getAbsolutePath());
                    }
                });
            }
        });
    }

    private void shareToWechatFriend(String imgPath){
        ShareSDK.initSDK(mContext);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setText("测试分享的文本");
        sp.setImagePath(imgPath);
        Platform wcFriend = ShareSDK.getPlatform(Wechat.NAME);
        //wcFriend.setPlatformActionListener(paListener); // 设置分享事件回调
        wcFriend.share(sp);
    }

    private void shareToWechatTimeLine(String imgPath){
        ShareSDK.initSDK(mContext);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setText("测试分享的文本");
        sp.setImagePath(imgPath);
        Platform wcTimeline = ShareSDK.getPlatform(WechatMoments.NAME);
        //wcTimeline.setPlatformActionListener(paListener); // 设置分享事件回调
        wcTimeline.share(sp);
    }

}
