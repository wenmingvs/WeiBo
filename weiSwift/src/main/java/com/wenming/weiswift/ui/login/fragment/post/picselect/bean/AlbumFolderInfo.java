package com.wenming.weiswift.ui.login.fragment.post.picselect.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.List;

/**
 * 目录信息
 * <p/>
 * Created by Clock on 2016/3/21.
 */
public class AlbumFolderInfo implements Parcelable {


    /**
     * 目录名
     */
    private String folderName;
    /**
     * 包含的所有图片信息
     */
    private List<ImageInfo> imageInfoList;
    /**
     * 第一张图片
     */
    private File frontCover;


    public File getFrontCover() {
        return frontCover;
    }

    public void setFrontCover(File frontCover) {
        this.frontCover = frontCover;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public List<ImageInfo> getImageInfoList() {
        return imageInfoList;
    }

    public void setImageInfoList(List<ImageInfo> imageInfoList) {
        this.imageInfoList = imageInfoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumFolderInfo that = (AlbumFolderInfo) o;

        if (getFolderName() != null ? !getFolderName().equals(that.getFolderName()) : that.getFolderName() != null)
            return false;
        if (getImageInfoList() != null ? !getImageInfoList().equals(that.getImageInfoList()) : that.getImageInfoList() != null)
            return false;
        return !(getFrontCover() != null ? !getFrontCover().equals(that.getFrontCover()) : that.getFrontCover() != null);

    }

    @Override
    public int hashCode() {
        int result = getFolderName() != null ? getFolderName().hashCode() : 0;
        result = 31 * result + (getImageInfoList() != null ? getImageInfoList().hashCode() : 0);
        result = 31 * result + (getFrontCover() != null ? getFrontCover().hashCode() : 0);
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.folderName);
        dest.writeTypedList(imageInfoList);
        dest.writeSerializable(this.frontCover);
    }

    public AlbumFolderInfo() {
    }

    protected AlbumFolderInfo(Parcel in) {
        this.folderName = in.readString();
        this.imageInfoList = in.createTypedArrayList(ImageInfo.CREATOR);
        this.frontCover = (File) in.readSerializable();
    }

    public static final Creator<AlbumFolderInfo> CREATOR = new Creator<AlbumFolderInfo>() {
        @Override
        public AlbumFolderInfo createFromParcel(Parcel source) {
            return new AlbumFolderInfo(source);
        }

        @Override
        public AlbumFolderInfo[] newArray(int size) {
            return new AlbumFolderInfo[size];
        }
    };
}
