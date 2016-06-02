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

import com.sina.weibo.sdk.openapi.models.Geo;
import com.sina.weibo.sdk.openapi.models.Visible;
import com.sina.weibo.sdk.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微博结构体。
 *
 * @author SINA
 * @since 2013-11-22
 */
public class Status implements Parcelable {

    private static Pattern mpattern;
    private static Matcher mmatcher;
    /**
     * 微博创建时间
     */
    public String created_at;
    /**
     * 微博ID
     */
    public String id;
    /**
     * 微博MID
     */
    public String mid;
    /**
     * 字符串型的微博ID
     */
    public String idstr;
    /**
     * 微博信息内容
     */
    public String text;
    /**
     * 微博来源
     */
    public String source;
    /**
     * 是否已收藏，true：是，false：否
     */
    public boolean favorited;
    /**
     * 是否被截断，true：是，false：否
     */
    public boolean truncated;
    /**
     * （暂未支持）回复ID
     */
    public String in_reply_to_status_id;
    /**
     * （暂未支持）回复人UID
     */
    public String in_reply_to_user_id;
    /**
     * （暂未支持）回复人昵称
     */
    public String in_reply_to_screen_name;
    /**
     * 缩略图片地址（小图），没有时不返回此字段
     */
    public String thumbnail_pic;
    /**
     * 中等尺寸图片地址（中图），没有时不返回此字段
     */
    public String bmiddle_pic;
    /**
     * 原始图片地址（原图），没有时不返回此字段
     */
    public String original_pic;
    /**
     * 地理信息字段
     */
    public Geo geo;
    /**
     * 微博作者的用户信息字段
     */
    public User user;
    /**
     * 被转发的原微博信息字段，当该微博为转发微博时返回
     */
    public Status retweeted_status;
    /**
     * 转发数
     */
    public int reposts_count;
    /**
     * 评论数
     */
    public int comments_count;
    /**
     * 表态数
     */
    public int attitudes_count;
    /**
     * 暂未支持
     */
    public int mlevel;
    /**
     * 微博的可见性及指定可见分组信息。该 object 中 type 取值，
     * 0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；
     * list_id为分组的组号
     */
    public Visible visible;
    /**
     * 微博配图地址。多图时返回多图链接。无配图返回"[]"
     */
    public ArrayList<String> thumbnail_pic_urls;

    public ArrayList<String> bmiddle_pic_urls;

    public ArrayList<String> origin_pic_urls;

    public int singleImgSizeType;

    /**
     * 微博流内的推广微博ID
     */
    //public Ad ad;
    public static Status parse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return Status.parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Status parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        Status status = new Status();
        status.created_at = jsonObject.optString("created_at");
        status.id = jsonObject.optString("id");
        status.mid = jsonObject.optString("mid");
        status.idstr = jsonObject.optString("idstr");
        status.text = StringFilter(jsonObject.optString("text"));

        status.source = getSource(jsonObject.optString("source"));

        status.favorited = jsonObject.optBoolean("favorited", false);
        status.truncated = jsonObject.optBoolean("truncated", false);

        // Have NOT supported
        status.in_reply_to_status_id = jsonObject.optString("in_reply_to_status_id");
        status.in_reply_to_user_id = jsonObject.optString("in_reply_to_user_id");
        status.in_reply_to_screen_name = jsonObject.optString("in_reply_to_screen_name");

        status.thumbnail_pic = jsonObject.optString("thumbnail_pic");
        status.bmiddle_pic = jsonObject.optString("bmiddle_pic");
        status.original_pic = jsonObject.optString("original_pic");
        status.geo = Geo.parse(jsonObject.optJSONObject("geo"));
        status.user = User.parse(jsonObject.optJSONObject("user"));
        status.retweeted_status = Status.parse(jsonObject.optJSONObject("retweeted_status"));
        status.reposts_count = jsonObject.optInt("reposts_count");
        status.comments_count = jsonObject.optInt("comments_count");
        status.attitudes_count = jsonObject.optInt("attitudes_count");
        status.mlevel = jsonObject.optInt("mlevel", -1);    // Have NOT supported
        status.visible = Visible.parse(jsonObject.optJSONObject("visible"));


        JSONArray picUrlsArray = jsonObject.optJSONArray("pic_urls");
        if (picUrlsArray != null && picUrlsArray.length() > 0) {
            int length = picUrlsArray.length();
            status.thumbnail_pic_urls = new ArrayList<String>(length);
            status.bmiddle_pic_urls = new ArrayList<String>(length);
            status.origin_pic_urls = new ArrayList<String>(length);
            JSONObject tmpObject = null;
            String thumbnailUrl;
            for (int ix = 0; ix < length; ix++) {
                tmpObject = picUrlsArray.optJSONObject(ix);
                if (tmpObject != null) {
                    thumbnailUrl = tmpObject.optString("thumbnail_pic");
                    status.thumbnail_pic_urls.add(thumbnailUrl);
                    status.bmiddle_pic_urls.add(thumbnailUrl.replace("thumbnail", "bmiddle"));
                    LogUtil.d("wenming", thumbnailUrl.replace("thumbnail", "bmiddle"));
                    status.origin_pic_urls.add(thumbnailUrl.replace("thumbnail", "large"));
                }
            }
        }

        if (status.thumbnail_pic_urls != null && status.thumbnail_pic_urls.size() == 1) {
            Random random = new Random();
            status.singleImgSizeType = random.nextInt(3);
        }


        //status.ad = jsonObject.optString("ad", "");
        return status;
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

    private static String getOriginUrl(String thumbnail_url) {
        StringBuffer buffer = new StringBuffer(thumbnail_url);
        buffer.replace(22, 31, "bmiddle");
        // Log.d("wenming", buffer.toString());
        return buffer.toString();
    }


    public Status() {
    }


    public static String StringFilter(String str) {
//        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!");//替换中文标号
//        String regEx = "[『』]"; // 清除掉特殊字符
//        Pattern p = Pattern.compile(regEx);
//        Matcher m = p.matcher(str);
//        return m.replaceAll("").trim();

        return str;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.created_at);
        dest.writeString(this.id);
        dest.writeString(this.mid);
        dest.writeString(this.idstr);
        dest.writeString(this.text);
        dest.writeString(this.source);
        dest.writeByte(this.favorited ? (byte) 1 : (byte) 0);
        dest.writeByte(this.truncated ? (byte) 1 : (byte) 0);
        dest.writeString(this.in_reply_to_status_id);
        dest.writeString(this.in_reply_to_user_id);
        dest.writeString(this.in_reply_to_screen_name);
        dest.writeString(this.thumbnail_pic);
        dest.writeString(this.bmiddle_pic);
        dest.writeString(this.original_pic);
        dest.writeParcelable(this.geo, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.retweeted_status, flags);
        dest.writeInt(this.reposts_count);
        dest.writeInt(this.comments_count);
        dest.writeInt(this.attitudes_count);
        dest.writeInt(this.mlevel);
        dest.writeParcelable(this.visible, flags);
        dest.writeStringList(this.thumbnail_pic_urls);
        dest.writeStringList(this.bmiddle_pic_urls);
        dest.writeStringList(this.origin_pic_urls);
    }

    protected Status(Parcel in) {
        this.created_at = in.readString();
        this.id = in.readString();
        this.mid = in.readString();
        this.idstr = in.readString();
        this.text = in.readString();
        this.source = in.readString();
        this.favorited = in.readByte() != 0;
        this.truncated = in.readByte() != 0;
        this.in_reply_to_status_id = in.readString();
        this.in_reply_to_user_id = in.readString();
        this.in_reply_to_screen_name = in.readString();
        this.thumbnail_pic = in.readString();
        this.bmiddle_pic = in.readString();
        this.original_pic = in.readString();
        this.geo = in.readParcelable(Geo.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.retweeted_status = in.readParcelable(Status.class.getClassLoader());
        this.reposts_count = in.readInt();
        this.comments_count = in.readInt();
        this.attitudes_count = in.readInt();
        this.mlevel = in.readInt();
        this.visible = in.readParcelable(Visible.class.getClassLoader());
        this.thumbnail_pic_urls = in.createStringArrayList();
        this.bmiddle_pic_urls = in.createStringArrayList();
        this.origin_pic_urls = in.createStringArrayList();
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel source) {
            return new Status(source);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };
}
