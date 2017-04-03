package com.wenming.weiswift.app.imgpreview;

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
import com.wenming.weiswift.app.common.ImageUtil;
import com.wenming.weiswift.utils.ToastUtil;

import java.io.File;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by xiangflight on 2016/4/22.
 */
public class ImageDetailDialog extends Dialog {

    private TextView mCancalTextView;
    private TextView mSavePicTextView;
    private TextView mRetweetTextView;
    private TextView mShareTv;
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
        mShareTv = (TextView) findViewById(R.id.pop_wechat_friend_share);
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
        mShareTv.setOnClickListener(new View.OnClickListener() {
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
                        showShare(imgFile.getAbsolutePath());
                        dismiss();
                    }
                });
            }
        });

    }

    private void showShare(String imgPath) {
        ShareSDK.initSDK(mContext);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        //oks.setTitle(mContext.getString(R.string.share_title));
        // text是分享文本，所有平台都需要这个字段
        //oks.setText("来自WeiSwift微博客户端");
        //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(imgPath);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        //oks.setUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(mContext);
    }

}
