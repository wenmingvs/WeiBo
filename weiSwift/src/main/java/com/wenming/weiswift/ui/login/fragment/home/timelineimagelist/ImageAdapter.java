package com.wenming.weiswift.ui.login.fragment.home.timelineimagelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.ui.common.FillContent;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by wenmingvs on 16/1/3.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private ArrayList<String> mData;
    private Context mContext;
    private Status mStatus;
    /**
     * 用于加载微博列表图片的配置，进行安全压缩，尽可能的展示图片细节
     */
    private DisplayImageOptions options= new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.message_image_default)
            .showImageForEmptyUri(R.drawable.message_image_default)
            .showImageOnFail(R.drawable.message_image_default)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();;

    public ImageAdapter(Status status, Context context) {
        this.mStatus = status;
        this.mData = status.bmiddle_pic_urls;
        this.mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mainfragment_weiboitem_imageitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FillContent.fillImageList(mContext, mStatus,options, position, holder.longImg, holder.norImg, holder.gifImageView, holder.imageLabel);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setData(ArrayList<String> data) {
        this.mData = data;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SubsamplingScaleImageView longImg;
        public ImageView norImg;
        public GifImageView gifImageView;
        public ImageView imageLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            longImg = (SubsamplingScaleImageView) itemView.findViewById(R.id.longImg);
            norImg = (ImageView) itemView.findViewById(R.id.norImg);
            gifImageView = (GifImageView) itemView.findViewById(R.id.gifView);
            imageLabel = (ImageView) itemView.findViewById(R.id.imageType);
        }
    }


}
