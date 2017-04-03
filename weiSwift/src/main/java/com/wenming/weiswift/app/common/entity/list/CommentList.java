/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wenming.weiswift.app.common.entity.list;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.wenming.weiswift.app.common.entity.Comment;
import com.wenming.weiswift.app.common.FillContentHelper;

import java.util.ArrayList;

/**
 * 评论列表结构体。
 *
 * @author SINA
 * @since 2013-11-24
 */
public class CommentList {

    /**
     * 微博列表
     */
    public ArrayList<Comment> comments;
    public String previous_cursor;
    public String next_cursor;
    public int total_number;

    public static CommentList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        CommentList commentList = new Gson().fromJson(jsonString, CommentList.class);
        //对status中的本地私有字段进行赋值
        for (Comment comment : commentList.comments) {
            //提取微博来源的关键字
            FillContentHelper.setSource(comment);

            //服务器并没有返回我们单张图片的随机尺寸，这里我们手动需要随机赋值
            FillContentHelper.setSingleImgSizeType(comment.status);
            //提取微博来源的关键字
            FillContentHelper.setSource(comment.status);
            //设置三种类型图片的url地址
            FillContentHelper.setImgUrl(comment.status);

            if (comment.status.retweeted_status != null) {
                //服务器并没有返回我们单张图片的随机尺寸，这里我们手动需要随机赋值
                FillContentHelper.setSingleImgSizeType(comment.status.retweeted_status);
                //提取微博来源的关键字
                FillContentHelper.setSource(comment.status.retweeted_status);
                //设置三种类型图片的url地址
                FillContentHelper.setImgUrl(comment.status.retweeted_status);
            }
        }

        return commentList;

    }
}
