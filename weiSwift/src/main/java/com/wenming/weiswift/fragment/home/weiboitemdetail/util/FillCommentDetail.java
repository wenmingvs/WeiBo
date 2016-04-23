package com.wenming.weiswift.fragment.home.weiboitemdetail.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.Status;
import com.wenming.weiswift.fragment.home.weiboitemdetail.commentdetail.adapter.CommentAdapter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/23.
 */
public class FillCommentDetail {

    public static void FillBar(Status status, TextView comment, TextView redirect, TextView feedlike) {
        comment.setText("评论 " + status.comments_count);
        redirect.setText("转发 " + status.reposts_count);
        feedlike.setText("赞 " + status.attitudes_count);
    }

    /**
     * 初始化用于显示评论，转发，赞的viewpager
     *
     * @param context
     * @param commentArrayList
     * @param recyclerView
     * @param retweetText
     * @param commentText
     * @param likeText
     */
    public static void FillCommentBarViewPager(Context context, ArrayList<Comment> commentArrayList, final RecyclerView recyclerView, TextView retweetText, TextView commentText, TextView likeText) {

        CommentAdapter commentAdapter = new CommentAdapter(context, commentArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(commentAdapter);

    }


}
