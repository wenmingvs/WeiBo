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

import com.sina.weibo.sdk.openapi.models.Tag;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 好友分组信息。
 *
 * @author SINA
 * @date 2013-11-27
 */
public class Group implements Parcelable {
    public static final String GROUP_ID_ALL = "1";

    /**
     * 微博分组ID
     **/
    public String id;
    /**
     * 微博分组字符串ID
     **/
    public String idStr;
    /**
     * 分组名称
     **/
    public String name;
    /**
     * 类型（不公开分组等）
     **/
    public String mode;
    /**
     * 是否公开
     **/
    public int visible;
    /**
     * 喜欢数
     **/
    public int like_count;
    /**
     * 分组成员数
     **/
    public int member_count;
    /**
     * 分组描述
     **/
    public String description;
    /**
     * 分组的Tag 信息
     **/
    public ArrayList<Tag> tags;
    /**
     * 头像信息
     **/
    public String profile_image_url;
    /**
     * 分组所属用户信息
     **/
    public User user;
    /**
     * 分组创建时间
     **/
    public String createAtTime;

    public static Group parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        Group group = new Group();
        group.user = User.parse(jsonObject.optJSONObject("user"));
        group.id = jsonObject.optString("id");
        group.idStr = jsonObject.optString("idstr");
        group.name = jsonObject.optString("name");
        group.mode = jsonObject.optString("mode");
        group.visible = jsonObject.optInt("visible");
        group.like_count = jsonObject.optInt("like_count");
        group.member_count = jsonObject.optInt("member_count");
        group.description = jsonObject.optString("description");
        group.profile_image_url = jsonObject.optString("profile_image_url");
        group.createAtTime = jsonObject.optString("create_time", "");

        JSONArray jsonArray = jsonObject.optJSONArray("tags");
        if (jsonArray != null && jsonObject.length() > 0) {
            int length = jsonArray.length();
            group.tags = new ArrayList<Tag>(length);
            for (int ix = 0; ix < length; ix++) {
                group.tags.add(Tag.parse(jsonArray.optJSONObject(ix)));
            }
        }
        return group;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.idStr);
        dest.writeString(this.name);
        dest.writeString(this.mode);
        dest.writeInt(this.visible);
        dest.writeInt(this.like_count);
        dest.writeInt(this.member_count);
        dest.writeString(this.description);
        dest.writeList(this.tags);
        dest.writeString(this.profile_image_url);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.createAtTime);
    }

    public Group() {
    }

    protected Group(Parcel in) {
        this.id = in.readString();
        this.idStr = in.readString();
        this.name = in.readString();
        this.mode = in.readString();
        this.visible = in.readInt();
        this.like_count = in.readInt();
        this.member_count = in.readInt();
        this.description = in.readString();
        this.tags = new ArrayList<Tag>();
        in.readList(this.tags, Tag.class.getClassLoader());
        this.profile_image_url = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.createAtTime = in.readString();
    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
