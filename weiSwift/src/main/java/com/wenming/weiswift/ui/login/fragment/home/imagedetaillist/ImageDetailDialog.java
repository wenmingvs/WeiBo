package com.wenming.weiswift.ui.login.fragment.home.imagedetaillist;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.ImageUtil;
import com.wenming.weiswift.utils.ToastUtil;

/**
 * Created by xiangflight on 2016/4/22.
 */
public class ImageDetailDialog extends Dialog {

    private TextView mCancalTextView;
    private TextView mSavePicTextView;
    private TextView mRetweetTextView;
    private Context mContext;
    private String mImgURL;

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
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        setUpListener(mContext);
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

    }

}
