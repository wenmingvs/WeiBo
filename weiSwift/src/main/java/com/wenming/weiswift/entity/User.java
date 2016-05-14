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

/**
 * 用户信息结构体。
 *
 * @author SINA
 * @since 2013-11-24
 */
public class User implements Parcelable {

    /**
     * 用户UID（int64）
     */
    public String id;
    /**
     * 字符串型的用户 UID
     */
    public String idstr;
    /**
     * 用户昵称
     */
    public String screen_name;
    /**
     * 友好显示名称
     */
    public String name;
    /**
     * 用户所在省级ID
     */
    public int province;
    /**
     * 用户所在城市ID
     */
    public int city;
    /**
     * 用户所在地
     */
    public String location;
    /**
     * 用户个人描述
     */
    public String description;
    /**
     * 用户博客地址
     */
    public String url;
    /**
     * 用户头像地址，50×50像素
     */
    public String profile_image_url;
    /**
     * 用户的微博统一URL地址
     */
    public String profile_url;
    /**
     * 用户的个性化背景(手机)
     */
    public String cover_image_phone;
    /**
     * 用户的个性化背景
     */
    public String cover_image;

    /**
     * 用户的个性化域名
     */
    public String domain;
    /**
     * 用户的微号
     */
    public String weihao;
    /**
     * 性别，m：男、f：女、n：未知
     */
    public String gender;
    /**
     * 粉丝数
     */
    public int followers_count;
    /**
     * 关注数
     */
    public int friends_count;
    /**
     * 微博数
     */
    public int statuses_count;
    /**
     * 收藏数
     */
    public int favourites_count;
    /**
     * 用户创建（注册）时间
     */
    public String created_at;
    /**
     * 暂未支持
     */
    public boolean following;
    /**
     * 是否允许所有人给我发私信，true：是，false：否
     */
    public boolean allow_all_act_msg;
    /**
     * 是否允许标识用户的地理位置，true：是，false：否
     */
    public boolean geo_enabled;
    /**
     * 是否是微博认证用户，即加V用户，true：是，false：否
     */
    public boolean verified;
    /**
     * 暂未支持
     */
    public int verified_type;
    /**
     * 用户备注信息，只有在查询用户关系时才返回此字段
     */
    public String remark;
    /**
     * 用户的最近一条微博信息字段
     */
    public Status status;
    /**
     * 是否允许所有人对我的微博进行评论，true：是，false：否
     */
    public boolean allow_all_comment;
    /**
     * 用户大头像地址
     */
    public String avatar_large;
    /**
     * 用户高清大头像地址
     */
    public String avatar_hd;
    /**
     * 认证原因
     */
    public String verified_reason;
    /**
     * 该用户是否关注当前登录用户，true：是，false：否
     */
    public boolean follow_me;
    /**
     * 用户的在线状态，0：不在线、1：在线
     */
    public int online_status;
    /**
     * 用户的互粉数
     */
    public int bi_followers_count;
    /**
     * 用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
     */
    public String lang;

    /**
     * 注意：以下字段暂时不清楚具体含义，OpenAPI 说明文档暂时没有同步更新对应字段
     */
    public String star;
    public String mbtype;
    public String mbrank;
    public String block_word;

    public static User parse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return User.parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static User parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        User user = new User();
        user.id = jsonObject.optString("id", "");
        user.idstr = jsonObject.optString("idstr", "");
        user.screen_name = jsonObject.optString("screen_name", "");
        user.name = jsonObject.optString("name", "");
        user.province = jsonObject.optInt("province", -1);
        user.city = jsonObject.optInt("city", -1);
        user.location = jsonObject.optString("location", "");
        user.description = jsonObject.optString("description", "");
        user.url = jsonObject.optString("url", "");
        user.profile_image_url = jsonObject.optString("profile_image_url", "");
        user.profile_url = jsonObject.optString("profile_url", "");
        user.cover_image_phone = jsonObject.optString("cover_image_phone", "");
        user.cover_image = jsonObject.optString("cover_image", "");
        user.domain = jsonObject.optString("domain", "");
        user.weihao = jsonObject.optString("weihao", "");
        user.gender = jsonObject.optString("gender", "");
        user.followers_count = jsonObject.optInt("followers_count", 0);
        user.friends_count = jsonObject.optInt("friends_count", 0);
        user.statuses_count = jsonObject.optInt("statuses_count", 0);
        user.favourites_count = jsonObject.optInt("favourites_count", 0);
        user.created_at = jsonObject.optString("created_at", "");
        user.following = jsonObject.optBoolean("following", false);
        user.allow_all_act_msg = jsonObject.optBoolean("allow_all_act_msg", false);
        user.geo_enabled = jsonObject.optBoolean("geo_enabled", false);
        user.verified = jsonObject.optBoolean("verified", false);
        user.verified_type = jsonObject.optInt("verified_type", -1);
        user.remark = jsonObject.optString("remark", "");
        user.status = Status.parse(jsonObject.optJSONObject("status"));
        user.allow_all_comment = jsonObject.optBoolean("allow_all_comment", true);
        user.avatar_large = jsonObject.optString("avatar_large", "");
        user.avatar_hd = jsonObject.optString("avatar_hd", "");
        user.verified_reason = jsonObject.optString("verified_reason", "");
        user.follow_me = jsonObject.optBoolean("follow_me", false);
        user.online_status = jsonObject.optInt("online_status", 0);
        user.bi_followers_count = jsonObject.optInt("bi_followers_count", 0);
        user.lang = jsonObject.optString("lang", "");

        // 注意：以下字段暂时不清楚具体含义，OpenAPI 说明文档暂时没有同步更新对应字段含义
        user.star = jsonObject.optString("star", "");
        user.mbtype = jsonObject.optString("mbtype", "");
        user.mbrank = jsonObject.optString("mbrank", "");
        user.block_word = jsonObject.optString("block_word", "");

        return user;
    }


    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.idstr);
        dest.writeString(this.screen_name);
        dest.writeString(this.name);
        dest.writeInt(this.province);
        dest.writeInt(this.city);
        dest.writeString(this.location);
        dest.writeString(this.description);
        dest.writeString(this.url);
        dest.writeString(this.profile_image_url);
        dest.writeString(this.profile_url);
        dest.writeString(this.cover_image_phone);
        dest.writeString(this.cover_image);
        dest.writeString(this.domain);
        dest.writeString(this.weihao);
        dest.writeString(this.gender);
        dest.writeInt(this.followers_count);
        dest.writeInt(this.friends_count);
        dest.writeInt(this.statuses_count);
        dest.writeInt(this.favourites_count);
        dest.writeString(this.created_at);
        dest.writeByte(following ? (byte) 1 : (byte) 0);
        dest.writeByte(allow_all_act_msg ? (byte) 1 : (byte) 0);
        dest.writeByte(geo_enabled ? (byte) 1 : (byte) 0);
        dest.writeByte(verified ? (byte) 1 : (byte) 0);
        dest.writeInt(this.verified_type);
        dest.writeString(this.remark);
        dest.writeParcelable(this.status, flags);
        dest.writeByte(allow_all_comment ? (byte) 1 : (byte) 0);
        dest.writeString(this.avatar_large);
        dest.writeString(this.avatar_hd);
        dest.writeString(this.verified_reason);
        dest.writeByte(follow_me ? (byte) 1 : (byte) 0);
        dest.writeInt(this.online_status);
        dest.writeInt(this.bi_followers_count);
        dest.writeString(this.lang);
        dest.writeString(this.star);
        dest.writeString(this.mbtype);
        dest.writeString(this.mbrank);
        dest.writeString(this.block_word);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.idstr = in.readString();
        this.screen_name = in.readString();
        this.name = in.readString();
        this.province = in.readInt();
        this.city = in.readInt();
        this.location = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.profile_image_url = in.readString();
        this.profile_url = in.readString();
        this.cover_image_phone = in.readString();
        this.cover_image = in.readString();
        this.domain = in.readString();
        this.weihao = in.readString();
        this.gender = in.readString();
        this.followers_count = in.readInt();
        this.friends_count = in.readInt();
        this.statuses_count = in.readInt();
        this.favourites_count = in.readInt();
        this.created_at = in.readString();
        this.following = in.readByte() != 0;
        this.allow_all_act_msg = in.readByte() != 0;
        this.geo_enabled = in.readByte() != 0;
        this.verified = in.readByte() != 0;
        this.verified_type = in.readInt();
        this.remark = in.readString();
        this.status = in.readParcelable(Status.class.getClassLoader());
        this.allow_all_comment = in.readByte() != 0;
        this.avatar_large = in.readString();
        this.avatar_hd = in.readString();
        this.verified_reason = in.readString();
        this.follow_me = in.readByte() != 0;
        this.online_status = in.readInt();
        this.bi_followers_count = in.readInt();
        this.lang = in.readString();
        this.star = in.readString();
        this.mbtype = in.readString();
        this.mbrank = in.readString();
        this.block_word = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


}
