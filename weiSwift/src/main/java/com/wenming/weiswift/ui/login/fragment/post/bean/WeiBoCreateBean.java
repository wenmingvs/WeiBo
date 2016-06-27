package com.wenming.weiswift.ui.login.fragment.post.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.ui.login.fragment.post.picselect.bean.ImageInfo;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 2016/6/27.
 */
public class WeiBoCreateBean implements Parcelable {
    /**
     * 要发送的图片列表
     */
    public ArrayList<ImageInfo> selectImgList;
    /**
     * 要发送的文本
     */
    public String content;
    /**
     * 要转发的微博
     */
    public Status status;

    public WeiBoCreateBean(String content, ArrayList<ImageInfo> selectImgList, Status status) {
        this.selectImgList = selectImgList;
        this.content = content;
        this.status = status;
    }

    public WeiBoCreateBean(String content, ArrayList<ImageInfo> selectImgList) {
        this.content = content;
        this.selectImgList = selectImgList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.selectImgList);
        dest.writeString(this.content);
        dest.writeParcelable(this.status, flags);
    }

    protected WeiBoCreateBean(Parcel in) {
        this.selectImgList = in.createTypedArrayList(ImageInfo.CREATOR);
        this.content = in.readString();
        this.status = in.readParcelable(Status.class.getClassLoader());
    }

    public static final Creator<WeiBoCreateBean> CREATOR = new Creator<WeiBoCreateBean>() {
        @Override
        public WeiBoCreateBean createFromParcel(Parcel source) {
            return new WeiBoCreateBean(source);
        }

        @Override
        public WeiBoCreateBean[] newArray(int size) {
            return new WeiBoCreateBean[size];
        }
    };
}
