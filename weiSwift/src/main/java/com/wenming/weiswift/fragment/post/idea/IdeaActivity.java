package com.wenming.weiswift.fragment.post.idea;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.FillContent;
import com.wenming.weiswift.common.login.AccessTokenKeeper;
import com.wenming.weiswift.common.login.Constants;
import com.wenming.weiswift.common.util.LogUtil;
import com.wenming.weiswift.common.util.ToastUtil;
import com.wenming.weiswift.fragment.home.util.WeiBoContentTextUtil;

/**
 * Created by wenmingvs on 16/5/2.
 */
public class IdeaActivity extends Activity {

    private StatusesAPI mStatusesAPI;
    private Oauth2AccessToken mAccessToken;
    private UsersAPI mUsersAPI;

    private Context mContext;
    private TextView mCancal;
    private TextView mUserName;
    private TextView mSendButton;
    private TextView publicbutton;
    private ImageView picture;
    private ImageView mentionbutton;
    private ImageView trendbutton;
    private ImageView emoticonbutton;
    private ImageView toolbarMore;
    private EditText mEditText;
    private TextView mLimitTextView;

    private LinearLayout mRepostlayout;
    private ImageView repostImg;
    private TextView repostName;
    private TextView repostContent;
    private Status mStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_idea_layout);
        mContext = this;

        mCancal = (TextView) findViewById(R.id.idea_cancal);
        mUserName = (TextView) findViewById(R.id.idea_username);
        mSendButton = (TextView) findViewById(R.id.idea_send);
        publicbutton = (TextView) findViewById(R.id.publicbutton);
        picture = (ImageView) findViewById(R.id.picture);
        mentionbutton = (ImageView) findViewById(R.id.mentionbutton);
        trendbutton = (ImageView) findViewById(R.id.trendbutton);
        emoticonbutton = (ImageView) findViewById(R.id.emoticonbutton);
        toolbarMore = (ImageView) findViewById(R.id.toolbar_more);
        mEditText = (EditText) findViewById(R.id.idea_content);
        mLimitTextView = (TextView) findViewById(R.id.limitTextView);

        mRepostlayout = (LinearLayout) findViewById(R.id.repost_layout);
        repostImg = (ImageView) findViewById(R.id.repost_img);
        repostName = (TextView) findViewById(R.id.repost_name);
        repostContent = (TextView) findViewById(R.id.repost_content);
        initAccessToken();
        initContent();
        setUpListener();
    }

    private void initAccessToken() {
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        mUsersAPI = new UsersAPI(mContext, Constants.APP_KEY, mAccessToken);
    }


    /**
     * 设置监听事件
     */
    private void setUpListener() {
        mCancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditText.getText().toString().length() == 0) {
                    ToastUtil.showShort(mContext, "发送的文本不能为空!");
                    return;
                }

                if (mStatus == null) {
                    update();
                } else {
                    repost();
                }
            }
        });

        mEditText.addTextChangedListener(watcher);
    }

    /**
     * 填充内容，
     * 1. 转发的内容是转发微博，
     * 2. 转发的内容是原创微博，
     */
    private void initContent() {
        refreshUserName();
        mStatus = getIntent().getParcelableExtra("status");

        if (mStatus == null) {
            return;
        }


        mRepostlayout.setVisibility(View.VISIBLE);
        mEditText.setHint("说说分享的心得");

        //1. 转发的内容是转发微博
        if (mStatus.retweeted_status != null) {
            mEditText.setText(WeiBoContentTextUtil.getWeiBoContent("//@" + mStatus.user.name + ":" + mStatus.text, mContext, mEditText));
            FillContent.FillCenterContent(mStatus.retweeted_status, repostImg, repostName, repostContent);
            changeSendButtonBg(mEditText.getText().toString().length());

        }
        //2. 转发的内容是原创微博
        else if (mStatus.retweeted_status == null) {
            FillContent.FillCenterContent(mStatus, repostImg, repostName, repostContent);
        }

        mEditText.setSelection(0);

    }

    private void refreshUserName() {
        long uid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(uid, new RequestListener() {
            @Override
            public void onComplete(String response) {
                User user = User.parse(response);
                if (user != null) {
                    mUserName.setText(user.name);
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                ToastUtil.showShort(mContext, info.toString());
            }
        });
    }

    /**
     * 根据输入的文本数量，决定发送按钮的背景
     *
     * @param length
     */
    private void changeSendButtonBg(int length) {

        if (length > 0) {
            mSendButton.setBackground(getResources().getDrawable(R.drawable.compose_send_corners_highlight_bg));
            mSendButton.setTextColor(Color.parseColor("#fbffff"));
            mSendButton.setEnabled(true);
        } else {
            mSendButton.setBackground(getResources().getDrawable(R.drawable.compose_send_corners_bg));
            mSendButton.setTextColor(Color.parseColor("#b3b3b3"));
            mSendButton.setEnabled(false);
        }
    }

    /**
     * 发布一条新微博（连续两次发布的微博不可以重复）。
     */
    private void update() {
        mStatusesAPI.update(mEditText.getText().toString(), "0.0", "0.0", new RequestListener() {
            @Override
            public void onComplete(String s) {
                ToastUtil.showShort(mContext, "发送成功");
                finish();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(mContext, "发送失败");
                LogUtil.d(e.toString());
            }
        });
    }

    /**
     * 转发一条微博
     */
    private void repost() {
        mStatusesAPI.repost(Long.valueOf(mStatus.id), mEditText.getText().toString(), 0, new RequestListener() {
            @Override
            public void onComplete(String s) {
                finish();
                ToastUtil.showShort(mContext, "转发成功");
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(mContext, "转发失败");
                LogUtil.d(e.toString());
            }
        });
    }


    private TextWatcher watcher = new TextWatcher() {
        private CharSequence inputString;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            inputString = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            changeSendButtonBg(inputString.toString().length());
            if (inputString.length() > 140) {
                int outofnum = inputString.length() - 140;
                mLimitTextView.setText("-" + outofnum + "");
            }
            LogUtil.d(inputString.toString());
        }
    };
}
