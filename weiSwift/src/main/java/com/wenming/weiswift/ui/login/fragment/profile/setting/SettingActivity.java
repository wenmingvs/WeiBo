package com.wenming.weiswift.ui.login.fragment.profile.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.utils.ToastUtil;

/**
 * Created by wenmingvs on 2016/1/7.
 */
public class SettingActivity extends Activity {

    private Context mContext;
    private RelativeLayout mExitLayout;
    private ImageView mBackImageView;
    private RelativeLayout mClearCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        mContext = this;
        initContent();
    }

    private void initContent() {
        mExitLayout = (RelativeLayout) findViewById(R.id.exitLayout);
        mBackImageView = (ImageView) findViewById(R.id.toolbar_back);
        mClearCache = (RelativeLayout) findViewById(R.id.clearCache_layout);
        mExitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定要注销并且退出微博？")
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AccessTokenKeeper.clear(getApplicationContext());
                                ((com.wenming.weiswift.ui.common.MyApplication) getApplication()).finishAll();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoader.getInstance().clearDiskCache();
                ImageLoader.getInstance().clearMemoryCache();
                Fresco.getImagePipeline().clearCaches();
                ToastUtil.showShort(mContext, "缓存清理成功！");
            }
        });

    }


}
