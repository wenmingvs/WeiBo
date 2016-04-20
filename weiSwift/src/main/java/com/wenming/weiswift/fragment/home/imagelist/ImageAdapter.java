package com.wenming.weiswift.fragment.home.imagelist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wenming.weiswift.R;
import com.wenming.weiswift.fragment.home.imagedetaillist.ImageDetailsActivity;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/1/3.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private ArrayList<String> mData;
    private Context mContext;
    private DisplayImageOptions options;

    public ImageAdapter(ArrayList<String> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.message_image_default)
                .showImageForEmptyUri(R.drawable.message_image_default)
                .showImageOnFail(R.drawable.message_image_default)
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
        ImageLoader.getInstance().displayImage(mData.get(position), holder.imageItem, options);
        holder.imageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageDetailsActivity.class);
                intent.putExtra("imagelist_url", mData);
                intent.putExtra("image_position", position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setData(ArrayList<String> data) {
        this.mData = data;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageItem;

        public ViewHolder(View itemView) {
            super(itemView);
            imageItem = (ImageView) itemView.findViewById(R.id.imageItem);

        }
    }


}
