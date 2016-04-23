package com.wenming.weiswift.fragment.home.weiboitemdetail.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.emojitextview.EmojiTextView;
import com.wenming.weiswift.common.util.NetUtil;
import com.wenming.weiswift.common.util.ToastUtil;
import com.wenming.weiswift.fragment.home.imagelist.ImageItemSapce;
import com.wenming.weiswift.fragment.home.util.FillWeiBoItem;
import com.wenming.weiswift.fragment.home.weiboitemdetail.util.FillCommentDetail;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/21.
 */
public class RetweetPicTextActivity extends DetailActivity {
    private LinearLayout retweet_weibo_layout;
    private ImageView profile_img;
    private ImageView profile_verified;
    private TextView profile_name;
    private TextView profile_time;
    private TextView weibo_comefrom;
    private EmojiTextView retweet_content;
    private LinearLayout bottombar_layout;
    private EmojiTextView origin_nameAndcontent;
    private RecyclerView retweet_imageList;
    private TextView commentView;
    private TextView retweetView;
    private TextView likeView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView retweetRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshData();
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                pullToRefreshData();
            }
        });
    }


    @Override
    protected void initWeiBoContent() {
        retweet_imageList.addItemDecoration(new ImageItemSapce((int) mContext.getResources().getDimension(R.dimen.home_weiboitem_imagelist_space)));
        FillWeiBoItem.fillTitleBar(mWeiboItem, profile_img, profile_verified, profile_name, profile_time, weibo_comefrom);
        FillWeiBoItem.fillWeiBoContent(mWeiboItem.text, mContext, retweet_content);
        FillWeiBoItem.fillRetweetContent(mWeiboItem, mContext, origin_nameAndcontent);
        FillWeiBoItem.fillWeiBoImgList(mWeiboItem.retweeted_status, mContext, retweet_imageList);
        FillWeiBoItem.showButtonBar(View.GONE, bottombar_layout);
        FillCommentDetail.FillBar(mWeiboItem, commentView, retweetView, likeView);
    }

    @Override
    protected void pullToRefreshData() {
        if (NetUtil.isConnected(mContext)) {
            if (mAccessToken != null && mAccessToken.isSessionValid()) {
                mCommentsAPI.show(Long.valueOf(mWeiboItem.id), 0, 0, 40, 1, 0, new RequestListener() {
                    @Override
                    public void onComplete(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            ArrayList<Comment> commentArrayList = CommentList.parse(response).commentList;
                            FillCommentDetail.FillCommentBarViewPager(mContext, commentArrayList, retweetRecyclerView, retweetView, commentView, likeView);
                        } else {
                            ToastUtil.showShort(mContext, "网络请求太快，服务器返回空数据，请注意请求频率");
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        ErrorInfo info = ErrorInfo.parse(e.getMessage());
                        ToastUtil.showShort(mContext, info.toString());
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        } else {
            ToastUtil.showShort(mContext, "没有网络,读取本地缓存");
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void setContntView() {
        setContentView(R.layout.home_weiboitem_detail_retweet_pictext);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        retweet_weibo_layout = (LinearLayout) findViewById(R.id.retweet_weibo_layout);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        profile_verified = (ImageView) findViewById(R.id.profile_verified);
        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_time = (TextView) findViewById(R.id.profile_time);
        retweet_content = (EmojiTextView) findViewById(R.id.retweet_content);
        weibo_comefrom = (TextView) findViewById(R.id.weiboComeFrom);
        bottombar_layout = (LinearLayout) findViewById(R.id.bottombar_layout);
        origin_nameAndcontent = (EmojiTextView) findViewById(R.id.origin_nameAndcontent);
        retweet_imageList = (RecyclerView) findViewById(R.id.origin_imageList);
        commentView = (TextView) findViewById(R.id.commentBar_comment);
        retweetView = (TextView) findViewById(R.id.commentBar_retweet);
        likeView = (TextView) findViewById(R.id.commentBar_like);
        retweetRecyclerView = (RecyclerView) findViewById(R.id.comment_recyelerview);
    }
}
