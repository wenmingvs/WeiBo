package com.wenming.weiswift.app.timeline.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.wenming.weiswift.R;
import com.wenming.weiswift.app.timeline.net.TimeLineHttpHepler;
import com.wenming.weiswift.utils.ScreenUtil;

/**
 * Created by wenmingvs on 16/1/3.
 */
public class VideoImgAdapter extends RecyclerView.Adapter<VideoImgAdapter.ViewHolder> {
    private String mImgUrl;
    private Context mContext;
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    public VideoImgAdapter(String videoImgUrl, Context context) {
        this.mContext = context;
        this.mImgUrl = videoImgUrl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.timeline_imglist_video, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        FrameLayout.LayoutParams norImgLayout = (FrameLayout.LayoutParams) viewHolder.norImg.getLayoutParams();
        norImgLayout.width = ScreenUtil.getScreenWidth(mContext);
        norImgLayout.height = (int) (ScreenUtil.getScreenWidth(mContext) * 0.7);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(mImgUrl, holder.norImg);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (!TextUtils.isEmpty(mImgUrl)) {
            return 1;
        }
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView norImg;

        public ViewHolder(View itemView) {
            super(itemView);
            norImg = (ImageView) itemView.findViewById(R.id.norImg);
        }
    }

    private static void setSingleImgSize(Context context, ImageView norImg) {
        FrameLayout.LayoutParams norImgLayout = (FrameLayout.LayoutParams) norImg.getLayoutParams();
        norImgLayout.width = ScreenUtil.getScreenWidth(context);
        norImgLayout.height = (int) (ScreenUtil.getScreenWidth(context) * 0.7);
    }
}
