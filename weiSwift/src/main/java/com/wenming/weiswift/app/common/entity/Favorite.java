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

package com.wenming.weiswift.app.common.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.sina.weibo.sdk.openapi.models.Tag;

import java.util.ArrayList;

/**
 * 我喜欢的微博信息结构体。
 *
 * @author SINA
 * @since 2013-11-24
 */
public class Favorite implements Parcelable {

    /**
     * 我喜欢的微博信息
     */
    public Status status;
    /**
     * 我喜欢的微博的 Tag 信息
     */
    public ArrayList<Tag> tags;
    /**
     * 创建我喜欢的微博信息的时间
     */
    public String favorited_time;


    public Favorite() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.status, flags);
        dest.writeList(this.tags);
        dest.writeString(this.favorited_time);
    }

    protected Favorite(Parcel in) {
        this.status = in.readParcelable(Status.class.getClassLoader());
        this.tags = new ArrayList<Tag>();
        in.readList(this.tags, Tag.class.getClassLoader());
        this.favorited_time = in.readString();
    }

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel source) {
            return new Favorite(source);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };
}
