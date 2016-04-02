package com.wenming.weiswift.common.emojitextview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by wenmingvs on 16/3/9.
 */
public class EmojiTextView extends TextView {

    private static final String AT = "@[\u4e00-\u9fa5\\w]+";// @人
    private static final String TOPIC = "#[\u4e00-\u9fa5\\w]+#";// ##话题
    private static final String URL = "http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";//链接


    private Context mContext;

    public EmojiTextView(Context context) {
        super(context, null);
        mContext = context;
    }

    public EmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public EmojiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }





}
