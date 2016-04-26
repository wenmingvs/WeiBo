package com.wenming.weiswift.fragment.home.weiboitemdetail.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Comment;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.util.NetUtil;
import com.wenming.weiswift.fragment.home.weiboitemdetail.adapter.CommentAdapter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/23.
 */
public class FillCommentDetail {

    public static void FillDetailBar(int comments_count, int reposts_count, int attitudes_count, TextView comment, TextView redirect, TextView feedlike) {
        comment.setText("评论 " + comments_count);
        redirect.setText("转发 " + reposts_count);
        feedlike.setText("赞 " + attitudes_count);
    }

    public static void RefreshNoneView(Context context, int comments_count, View noneView) {

        if (NetUtil.isConnected(context)) {
            if (comments_count > 0) {
                noneView.setVisibility(View.GONE);
            } else if (comments_count == 0) {
                noneView.setVisibility(View.VISIBLE);
            }
        } else {
            noneView.setVisibility(View.VISIBLE);
            TextView textView = (TextView) noneView.findViewById(R.id.tv_normal_refresh_footer_status);
            textView.setText("网络出错啦");
        }
    }


    /**
     * 初始化用于显示评论，转发，赞的viewpager
     *
     * @param context
     * @param commentCount
     * @param commentArrayList
     * @param recyclerView
     * @param commentView
     */
    public static void FillCommentList(Context context, int commentCount, ArrayList<Comment> commentArrayList, final RecyclerView recyclerView, TextView commentView) {
        CommentAdapter commentAdapter = new CommentAdapter(context, commentArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(commentAdapter);
    }


}
