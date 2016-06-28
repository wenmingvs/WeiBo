package com.wenming.weiswift.ui.login.fragment.post.idea;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.wenming.weiswift.R;
import com.wenming.weiswift.api.UsersAPI;
import com.wenming.weiswift.entity.Comment;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.ui.common.FillContent;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.ui.login.fragment.post.PostService;
import com.wenming.weiswift.ui.login.fragment.post.bean.CommentReplyBean;
import com.wenming.weiswift.ui.login.fragment.post.bean.WeiBoCommentBean;
import com.wenming.weiswift.ui.login.fragment.post.bean.WeiBoCreateBean;
import com.wenming.weiswift.ui.login.fragment.post.idea.imagelist.ImgListAdapter;
import com.wenming.weiswift.ui.login.fragment.post.picselect.activity.AlbumActivity;
import com.wenming.weiswift.ui.login.fragment.post.picselect.bean.AlbumFolderInfo;
import com.wenming.weiswift.ui.login.fragment.post.picselect.bean.ImageInfo;
import com.wenming.weiswift.utils.KeyBoardUtil;
import com.wenming.weiswift.utils.ToastUtil;
import com.wenming.weiswift.widget.emojitextview.WeiBoContentTextUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/2.
 */
public class IdeaActivity extends Activity implements ImgListAdapter.OnFooterViewClickListener {
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
    private ImageView more_button;
    private EditText mEditText;
    private TextView mLimitTextView;
    private TextView mInputType;
    private LinearLayout mRepostlayout;
    private ImageView repostImg;
    private TextView repostName;
    private TextView repostContent;
    private RecyclerView mRecyclerView;
    private ImageView mBlankspace;
    private LinearLayout mIdea_linearLayout;

    private ArrayList<AlbumFolderInfo> mFolderList = new ArrayList<AlbumFolderInfo>();
    private ArrayList<ImageInfo> mSelectImgList = new ArrayList<ImageInfo>();
    private Status mStatus;
    private Comment mComment;
    private String mIdeaType;


    /**
     * 最多输入140个字符
     */
    private static final int TEXT_LIMIT = 140;

    /**
     * 在只剩下10个字可以输入的时候，做提醒
     */
    private static final int TEXT_REMIND = 10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_idea_layout);
        mContext = this;
        mUsersAPI = new UsersAPI(mContext, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));
        mInputType = (TextView) findViewById(R.id.inputType);
        mCancal = (TextView) findViewById(R.id.idea_cancal);
        mUserName = (TextView) findViewById(R.id.idea_username);
        mSendButton = (TextView) findViewById(R.id.idea_send);
        publicbutton = (TextView) findViewById(R.id.publicbutton);
        picture = (ImageView) findViewById(R.id.picture);
        mentionbutton = (ImageView) findViewById(R.id.mentionbutton);
        trendbutton = (ImageView) findViewById(R.id.trendbutton);
        emoticonbutton = (ImageView) findViewById(R.id.emoticonbutton);
        more_button = (ImageView) findViewById(R.id.more_button);
        mEditText = (EditText) findViewById(R.id.idea_content);
        mLimitTextView = (TextView) findViewById(R.id.limitTextView);
        mRepostlayout = (LinearLayout) findViewById(R.id.repost_layout);
        repostImg = (ImageView) findViewById(R.id.repost_img);
        repostName = (TextView) findViewById(R.id.repost_name);
        repostContent = (TextView) findViewById(R.id.repost_content);
        mRecyclerView = (RecyclerView) findViewById(R.id.ImgList);
        mBlankspace = (ImageView) findViewById(R.id.blankspace);
        mIdea_linearLayout = (LinearLayout) findViewById(R.id.idea_linearLayout);
        mIdeaType = getIntent().getStringExtra("ideaType");
        mStatus = getIntent().getParcelableExtra("status");
        mComment = getIntent().getParcelableExtra("comment");
        mInputType.setText(mIdeaType);
        refreshUserName();
        initContent();
        setUpListener();
        mEditText.setTag(false);
        if (getIntent().getBooleanExtra("startAlumbAcitivity", false) == true) {
            Intent intent = new Intent(IdeaActivity.this, AlbumActivity.class);
            intent.putParcelableArrayListExtra("selectedImglist", mSelectImgList);
            startActivityForResult(intent, 0);
        }
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                setLimitTextColor(mLimitTextView, mEditText.getText().toString());
                mEditText.requestFocus();
            }
        });
    }


    /**
     * 填充内容，
     * 1. 转发的内容是转发微博，
     * 2. 转发的内容是原创微博，
     */
    private void initContent() {
        switch (mIdeaType) {
            case PostService.POST_SERVICE_CREATE_WEIBO:
                break;
            case PostService.POST_SERVICE_REPOST_STATUS:
                //填充转发的内容
                repostWeiBo();
                break;
            case PostService.POST_SERVICE_COMMENT_STATUS:
                break;
            case PostService.POST_SERVICE_REPLY_COMMENT:
                break;
        }
    }


    /**
     * 填充转发的内容
     */
    private void repostWeiBo() {

        if (mStatus == null) {
            return;
        }
        mRepostlayout.setVisibility(View.VISIBLE);
        mEditText.setHint("说说分享的心得");

        //1. 转发的内容是转发微博
        if (mStatus.retweeted_status != null) {
            mEditText.setText(WeiBoContentTextUtil.getWeiBoContent("//@" + mStatus.user.name + ":" + mStatus.text, mContext, mEditText));
            FillContent.fillMentionCenterContent(mStatus.retweeted_status, repostImg, repostName, repostContent);
            mEditText.setSelection(0);
        }
        //2. 转发的内容是原创微博
        else if (mStatus.retweeted_status == null) {
            FillContent.fillMentionCenterContent(mStatus, repostImg, repostName, repostContent);
        }
        changeSendButtonBg();
    }

    /**
     * 刷新顶部的名字
     */
    private void refreshUserName() {
        long uid = Long.parseLong(AccessTokenKeeper.readAccessToken(this).getUid());
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
     * 设置监听事件
     */
    private void setUpListener() {
        mCancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdeaActivity.this, AlbumActivity.class);
                intent.putParcelableArrayListExtra("selectedImglist", mSelectImgList);
                startActivityForResult(intent, 0);
            }
        });
        mentionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.getText().insert(mEditText.getSelectionStart(), "@");
            }
        });
        trendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.getText().insert(mEditText.getSelectionStart(), "##");
                mEditText.setSelection(mEditText.getSelectionStart() - 1);
            }
        });
        emoticonbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发此功能...");
            }
        });
        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发此功能...");
            }
        });
        mEditText.addTextChangedListener(watcher);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在发微博状态下，如果发的微博没有图片，且也没有文本内容，识别为空
                if (!isRetweetWeiBoState() && mStatus == null && mSelectImgList.size() == 0 && (mEditText.getText().toString().isEmpty() || mEditText.getText().toString().length() == 0)) {
                    ToastUtil.showShort(mContext, "发送的内容不能为空");
                    return;
                }

                if (calculateWeiboLength(mEditText.getText().toString()) > TEXT_LIMIT) {
                    ToastUtil.showShort(mContext, "文本超出限制" + TEXT_LIMIT + "个字！请做调整");
                    return;
                }
                if (mSelectImgList.size() > 1) {
                    ToastUtil.showShort(mContext, "由于新浪的限制，第三方微博客户端只允许上传一张图，请做调整");
                    return;
                }
                Intent intent = new Intent(mContext, PostService.class);
                Bundle bundle = new Bundle();

                switch (mIdeaType) {
                    case PostService.POST_SERVICE_CREATE_WEIBO:
                        WeiBoCreateBean weiboBean = new WeiBoCreateBean(mEditText.getText().toString(), mSelectImgList);
                        intent.putExtra("postType", PostService.POST_SERVICE_CREATE_WEIBO);
                        bundle.putParcelable("weiBoCreateBean", weiboBean);
                        intent.putExtras(bundle);
                        break;
                    case PostService.POST_SERVICE_REPOST_STATUS:
                        WeiBoCreateBean repostBean = new WeiBoCreateBean(mEditText.getText().toString(), mSelectImgList, mStatus);
                        intent.putExtra("postType", PostService.POST_SERVICE_REPOST_STATUS);
                        bundle.putParcelable("weiBoCreateBean", repostBean);
                        intent.putExtras(bundle);
                        break;
                    case PostService.POST_SERVICE_COMMENT_STATUS:
                        intent.putExtra("postType", PostService.POST_SERVICE_COMMENT_STATUS);
                        WeiBoCommentBean weiBoCommentBean = new WeiBoCommentBean(mEditText.getText().toString(), mStatus);
                        bundle.putParcelable("weiBoCommentBean", weiBoCommentBean);
                        intent.putExtras(bundle);
                        break;
                    case PostService.POST_SERVICE_REPLY_COMMENT:
                        intent.putExtra("postType", PostService.POST_SERVICE_REPLY_COMMENT);
                        CommentReplyBean commentReplyBean = new CommentReplyBean(mEditText.getText().toString(), mComment);
                        bundle.putParcelable("commentReplyBean", commentReplyBean);
                        intent.putExtras(bundle);
                        break;
                }
                startService(intent);
                finish();
            }
        });
        mSendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pressSendButton();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    changeSendButtonBg();
                }
                return false;
            }
        });

        mBlankspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtil.openKeybord(mEditText, mContext);
            }
        });

    }


    /**
     * 根据输入的文本数量，决定发送按钮的背景
     */
    private void changeSendButtonBg() {
        //如果有文本，或者有图片，或者是处于转发微博状态
        if (mEditText.getText().toString().length() > 0 || mSelectImgList.size() > 0 || (isRetweetWeiBoState())) {
            highlightSendButton();
        } else {
            sendNormal();
        }
    }

    private void pressSendButton() {
        mSendButton.setBackgroundResource(R.drawable.compose_send_corners_highlight_press_bg);
        mSendButton.setTextColor(Color.parseColor("#ebeef3"));
    }

    private void highlightSendButton() {
        mSendButton.setBackgroundResource(R.drawable.compose_send_corners_highlight_bg);
        mSendButton.setTextColor(Color.parseColor("#fbffff"));
        mSendButton.setEnabled(true);
    }

    private void sendNormal() {
        mSendButton.setBackgroundResource(R.drawable.compose_send_corners_bg);
        mSendButton.setTextColor(Color.parseColor("#b3b3b3"));
        mSendButton.setEnabled(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data != null) {
                    mSelectImgList = data.getParcelableArrayListExtra("selectImgList");
                    initImgList();
                    changeSendButtonBg();
                }
                break;
        }
    }


    public void initImgList() {
        mRecyclerView.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        ImgListAdapter imgListAdapter = new ImgListAdapter(mSelectImgList, mContext);
        imgListAdapter.setOnFooterViewClickListener(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(imgListAdapter);
    }

    @Override
    public void OnFooterViewClick() {
        Intent intent = new Intent(IdeaActivity.this, AlbumActivity.class);
        intent.putParcelableArrayListExtra("selectedImglist", mSelectImgList);
        startActivityForResult(intent, 0);
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
            changeSendButtonBg();
            setLimitTextColor(mLimitTextView, inputString.toString());
        }
    };


    /**
     * 计算微博文本的长度，统计是否超过140个字，其中中文和全角的符号算1个字符，英文字符和半角字符算半个字符
     *
     * @param c
     * @return 微博的长度，结果四舍五入
     */
    public long calculateWeiboLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int temp = (int) c.charAt(i);
            if (temp > 0 && temp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    public void setLimitTextColor(TextView limitTextView, String content) {
        long length = calculateWeiboLength(content);
        if (length > TEXT_LIMIT) {
            long outOfNum = length - TEXT_LIMIT;
            limitTextView.setTextColor(Color.parseColor("#e03f22"));
            limitTextView.setText("-" + outOfNum + "");
        } else if (length == TEXT_LIMIT) {
            limitTextView.setText(0 + "");
            limitTextView.setTextColor(Color.parseColor("#929292"));
        } else if (TEXT_LIMIT - length <= TEXT_REMIND) {
            limitTextView.setText(TEXT_LIMIT - length + "");
            limitTextView.setTextColor(Color.parseColor("#929292"));
        } else {
            limitTextView.setText("");
        }
    }

    /**
     * 判断此页是处于转发微博还是发微博状态
     *
     * @return
     */
    public boolean isRetweetWeiBoState() {
        if (mStatus != null) {
            return true;
        } else {
            return false;
        }
    }


}
