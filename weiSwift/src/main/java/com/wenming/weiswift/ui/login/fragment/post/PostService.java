package com.wenming.weiswift.ui.login.fragment.post;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wenming.weiswift.api.StatusesAPI;
import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.ui.login.activity.MainActivity;
import com.wenming.weiswift.ui.login.fragment.post.picselect.bean.ImageInfo;
import com.wenming.weiswift.utils.ToastUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * Created by wenmingvs on 16/5/8.
 */
public class PostService extends Service {

    private StatusesAPI mStatusesAPI;
    private ArrayList<ImageInfo> mSelectImgList;
    private String mContent;
    private NotificationManager mSendNotifity;
    private Status mStatus;

    private static final int SEND_STATUS_SUCCESS = 1;
    private static final int SEND_STATUS_ERROR = 2;
    private static final int SEND_STATUS_SEND = 3;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));
        mSelectImgList = intent.getParcelableArrayListExtra("select_img");
        mContent = intent.getStringExtra("content").trim();
        mStatus = intent.getParcelableExtra("status");
        showSendNotifiy();

        if (mStatus != null) {
            repost();
        } else {
            if (mSelectImgList == null || mSelectImgList.size() == 0) {
                sendTextContent();
            } else {
                sendImgTextContent();
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 发送图文微博
     */
    private void sendImgTextContent() {

        Bitmap bitmap = getLoacalBitmap(mSelectImgList.get(0).getImageFile().getAbsolutePath());

        if (mContent.isEmpty()) {
            mContent = "分享图片";
        }
        mStatusesAPI.upload(mContent, bitmap, "0", "0", new RequestListener() {
            @Override
            public void onComplete(String s) {
                //ToastUtil.showShort(PostService.this, "发送成功！");
                mSendNotifity.cancel(SEND_STATUS_SEND);
                showSuccessNotifiy();
                final Message message = Message.obtain();
                message.what = SEND_STATUS_SEND;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendMessage(message);
                    }
                }, 2000);


            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(PostService.this, "发送失败！");
                mSendNotifity.cancel(SEND_STATUS_SEND);
                showErrorNotifiy();
                final Message message = Message.obtain();
                message.what = SEND_STATUS_ERROR;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendMessage(message);
                    }
                }, 2000);
            }
        });
    }


    /**
     * 发送纯文字的微博
     */
    private void sendTextContent() {
        mStatusesAPI.update(mContent.toString(), "0.0", "0.0", new RequestListener() {
            @Override
            public void onComplete(String s) {
                //ToastUtil.showShort(PostService.this, "发送成功");
                mSendNotifity.cancel(SEND_STATUS_SEND);
                showSuccessNotifiy();
                final Message message = Message.obtain();
                message.what = SEND_STATUS_SEND;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendMessage(message);
                    }
                }, 2000);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                //ToastUtil.showShort(PostService.this, "发送失败！");
                mSendNotifity.cancel(SEND_STATUS_SEND);
                showErrorNotifiy();
                final Message message = Message.obtain();
                message.what = SEND_STATUS_ERROR;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendMessage(message);
                    }
                }, 2000);
            }
        });

    }

    /**
     * 转发一条微博
     */
    private void repost() {
        mStatusesAPI.repost(Long.valueOf(mStatus.id), mContent.toString(), 0, new RequestListener() {
            @Override
            public void onComplete(String s) {
                //ToastUtil.showShort(PostService.this, "转发成功");
                mSendNotifity.cancel(SEND_STATUS_SEND);
                showSuccessNotifiy();
                final Message message = Message.obtain();
                message.what = SEND_STATUS_SEND;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendMessage(message);
                    }
                }, 2000);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(PostService.this, "转发失败");
                mSendNotifity.cancel(SEND_STATUS_SEND);
                showErrorNotifiy();
                final Message message = Message.obtain();
                message.what = SEND_STATUS_ERROR;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendMessage(message);
                    }
                }, 2000);
            }
        });
    }


    /**
     * 获取本地的图片
     *
     * @param absolutePath
     * @return
     */
    private Bitmap getLoacalBitmap(String absolutePath) {
        try {
            FileInputStream fis = new FileInputStream(absolutePath);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 显示发送的notify
     */
    private void showSendNotifiy() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(this, SEND_STATUS_SEND, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String ticker = "您有一条新通知";
        builder.setContentIntent(rightPendIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo));
        builder.setSmallIcon(R.drawable.queue_icon_send);
        builder.setTicker(ticker);
        builder.setContentTitle("WeiSwift");
        builder.setContentText("正在发送");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setProgress(0, 0, true);
        Notification notification = builder.build();
        // 发送该通知
        mSendNotifity = (NotificationManager) this.getSystemService(Activity.NOTIFICATION_SERVICE);
        mSendNotifity.notify(SEND_STATUS_SEND, notification);
    }

    /**
     * 发送成功的通知
     */
    private void showSuccessNotifiy() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(this, SEND_STATUS_SUCCESS, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String ticker = "您有一条新通知";
        builder.setContentIntent(rightPendIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo));
        builder.setSmallIcon(R.drawable.queue_icon_success);
        builder.setTicker(ticker);
        builder.setContentTitle("WeiSwift");
        builder.setContentText("发送成功");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        Notification notification = builder.build();
        // 发送该通知
        mSendNotifity = (NotificationManager) this.getSystemService(Activity.NOTIFICATION_SERVICE);
        mSendNotifity.notify(SEND_STATUS_SUCCESS, notification);
    }

    /**
     * 发送失败的通知
     */
    private void showErrorNotifiy() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(this, SEND_STATUS_ERROR, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String ticker = "您有一条新通知";
        builder.setContentIntent(rightPendIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo));
        builder.setSmallIcon(R.drawable.queue_icon_miss);
        builder.setTicker(ticker);
        builder.setContentTitle("WeiSwift");
        builder.setContentText("发送失败");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        Notification notification = builder.build();
        // 发送该通知
        mSendNotifity = (NotificationManager) this.getSystemService(Activity.NOTIFICATION_SERVICE);
        mSendNotifity.notify(SEND_STATUS_ERROR, notification);
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mSendNotifity.cancelAll();
            stopSelf();

        }
    };

}
