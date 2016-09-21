package com.wenming.weiswift.ui.login.fragment.post.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.wenming.weiswift.entity.Comment;

/**
 * Created by wenmingvs on 2016/6/27.
 */
public class CommentReplyBean implements Parcelable {
    /**
     * 要发送的文本
     */
    public String content;
    /**
     * 要评论的微博或者
     */
    public Comment comment;



    public CommentReplyBean(String content, Comment comment) {
        this.comment = comment;
        this.content = content;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeParcelable(this.comment, flags);
    }

    protected CommentReplyBean(Parcel in) {
        this.content = in.readString();
        this.comment = in.readParcelable(Comment.class.getClassLoader());
    }

    public static final Creator<CommentReplyBean> CREATOR = new Creator<CommentReplyBean>() {
        @Override
        public CommentReplyBean createFromParcel(Parcel source) {
            return new CommentReplyBean(source);
        }

        @Override
        public CommentReplyBean[] newArray(int size) {
            return new CommentReplyBean[size];
        }
    };
}
