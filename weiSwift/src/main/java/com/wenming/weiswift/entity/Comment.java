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

package com.wenming.weiswift.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 评论结构体。
 *
 * @author SINA
 * @since 2013-11-24
 */
public class Comment implements Parcelable {

    private static Pattern mpattern;
    private static Matcher mmatcher;

    /**
     * 评论创建时间
     */
    public String created_at;
    /**
     * 评论的 ID
     */
    public String id;
    /**
     * 评论的内容
     */
    public String text;
    /**
     * 评论的来源
     */
    public String source;
    /**
     * 评论作者的用户信息字段
     */
    public User user;
    /**
     * 评论的 MID
     */
    public String mid;
    /**
     * 字符串型的评论 ID
     */
    public String idstr;
    /**
     * 评论的微博信息字段
     */
    public Status status;
    /**
     * 评论来源评论，当本评论属于对另一评论的回复时返回此字段
     */
    public Comment reply_comment;

    public static Comment parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        Comment comment = new Comment();
        comment.created_at = jsonObject.optString("created_at");
        comment.id = jsonObject.optString("id");
        comment.text = jsonObject.optString("text");
        comment.source = jsonObject.optString("source");
        comment.source = getSource(jsonObject.optString("source"));
        comment.user = User.parse(jsonObject.optJSONObject("user"));
        comment.mid = jsonObject.optString("mid");
        comment.idstr = jsonObject.optString("idstr");
        comment.status = Status.parse(jsonObject.optJSONObject("status"));
        comment.reply_comment = Comment.parse(jsonObject.optJSONObject("reply_comment"));

        return comment;
    }

    public static Comment parse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return Comment.parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static String getSource(String string) {
        mpattern = Pattern.compile("<(.*?)>(.*?)</a>");
        mmatcher = mpattern.matcher(string);
        if (mmatcher.find()) {
            return mmatcher.group(2);
        } else {
            return string;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.created_at);
        dest.writeString(this.id);
        dest.writeString(this.text);
        dest.writeString(this.source);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.mid);
        dest.writeString(this.idstr);
        dest.writeParcelable(this.status, flags);
        dest.writeParcelable(this.reply_comment, flags);
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.created_at = in.readString();
        this.id = in.readString();
        this.text = in.readString();
        this.source = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.mid = in.readString();
        this.idstr = in.readString();
        this.status = in.readParcelable(Status.class.getClassLoader());
        this.reply_comment = in.readParcelable(Comment.class.getClassLoader());
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
