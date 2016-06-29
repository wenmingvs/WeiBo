package com.wenming.weiswift.ui.login.fragment.home.userdetail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cesards.cropimageview.CropImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Status;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/1/3.
 */
public class UserPhotoAdapter extends RecyclerView.Adapter<UserPhotoAdapter.ViewHolder> {
    private Context mContext;
    private DisplayImageOptions options;
    private Status mStatus;
    private ArrayList<String> bmiddle_pic_urls = new ArrayList<>();


    public UserPhotoAdapter(ArrayList<String> bmiddle_pic_urls, Context context) {
        this.bmiddle_pic_urls = bmiddle_pic_urls;
        this.mContext = context;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.message_image_default)
                .showImageForEmptyUri(R.drawable.message_image_default)
                .showImageOnFail(R.drawable.message_image_default)
                .imageScaleType(ImageScaleType.NONE_SAFE)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mainfragment_weiboitem_imageitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //FillContent.fillImageList(mContext, msStatus, position, holder.imageItem, holder.imageType);
        ImageLoader.getInstance().displayImage(bmiddle_pic_urls.get(position), ((ViewHolder) holder).imageItem, options);
    }

    @Override
    public int getItemCount() {
        return bmiddle_pic_urls == null ? 0 : bmiddle_pic_urls.size();
    }

    public void setData(ArrayList<String> statuslist) {
        this.bmiddle_pic_urls = statuslist;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CropImageView imageItem;
        public ImageView imageType;

        public ViewHolder(View itemView) {
            super(itemView);
            imageItem = (CropImageView) itemView.findViewById(R.id.imageItem);
            imageType = (ImageView) itemView.findViewById(R.id.imageType);
        }
    }


}
