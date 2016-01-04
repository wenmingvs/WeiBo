package com.wenming.weiswift.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.sina.weibo.sdk.openapi.models.Status;
import com.wenming.weiswift.R;
import com.wenming.weiswift.util.DensityUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 2015/12/29.
 */
public class WeiboAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    protected AnimationDrawable mFooterImag;
    private ArrayList<Status> mData;
    private Context mContext;
    private DisplayImageOptions options;
    private ArrayList<String> mImageDatas;
    private boolean mFirstLoad;
    private GridLayoutManager gridLayoutManager;
    private ImageAdapter imageAdapter;
    private ViewGroup.LayoutParams mParams;


    public WeiboAdapter(ArrayList<Status> datas, Context context) {
        this.mData = datas;
        this.mContext = context;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.avator_default)
                .showImageForEmptyUri(R.drawable.avator_default)
                .showImageOnFail(R.drawable.avator_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(14671839, 1))
                .build();
        mImageDatas = new ArrayList<String>();
        mFirstLoad = true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.mainfragment_original_weiboitem, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            viewHolder.imageList.addItemDecoration(new SpaceItemDecoration(DensityUtil.dp2px(mContext, 5)));
            return viewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.footerview, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            FooterViewHolder footerViewHolder = new FooterViewHolder(view);

            ImageView waitingImg = (ImageView) view.findViewById(R.id.waiting_image);
            mFooterImag = (AnimationDrawable) waitingImg.getDrawable();
            mFooterImag.start();
            return footerViewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            //微博用户信息
            ImageLoader.getInstance().displayImage(mData.get(position).user.avatar_hd, ((ItemViewHolder) holder).profile_img, options);
            ((ItemViewHolder) holder).profile_name.setText(mData.get(position).user.name);
            ((ItemViewHolder) holder).profile_time.setText("刚刚   ");
            ((ItemViewHolder) holder).weiboComeFrom.setText("来自 " + mData.get(position).source);

            //微博文字内容
            ((ItemViewHolder) holder).weibo_Content.setText(mData.get(position).text);

            //微博图片内容

            mImageDatas = mData.get(position).origin_pic_urls;
            gridLayoutManager = new GridLayoutManager(mContext, 3);
            imageAdapter = new ImageAdapter(mImageDatas, mContext);
            ((ItemViewHolder) holder).imageList.setHasFixedSize(true);
            ((ItemViewHolder) holder).imageList.setAdapter(imageAdapter);
            ((ItemViewHolder) holder).imageList.setLayoutManager(gridLayoutManager);

            if (mImageDatas != null && mImageDatas.size() != 0) {
                mParams = ((ItemViewHolder) holder).imageList.getLayoutParams();
                mParams.height = (DensityUtil.dp2px(mContext, 110f)) * getImgLineCount(mImageDatas) + (DensityUtil.dp2px(mContext, 5f)) * getImgLineCount(mImageDatas);
                mParams.width = (DensityUtil.dp2px(mContext, 110f)) * 3 + (DensityUtil.dp2px(mContext, 5f)) * 2;
                ((ItemViewHolder) holder).imageList.setLayoutParams(mParams);
            } else {
                ((ItemViewHolder) holder).imageList.setVisibility(View.GONE);
            }

            //微博转发，评论，赞的数量
            if (mData.get(position).comments_count != 0) {

                ((ItemViewHolder) holder).comment.setText(mData.get(position).comments_count + "");
            }
            if (mData.get(position).reposts_count != 0) {
                ((ItemViewHolder) holder).redirect.setText(mData.get(position).reposts_count + "");
            }
            if (mData.get(position).attitudes_count != 0) {
                ((ItemViewHolder) holder).feedlike.setText(mData.get(position).attitudes_count + "");
            }
        } else if (holder instanceof FooterViewHolder) {
            if (getItemCount() == 1) {
                ((FooterViewHolder) holder).linearLayout.setVisibility(View.GONE);
            } else {
                ((FooterViewHolder) holder).linearLayout.setVisibility(View.VISIBLE);
            }
        }

    }


    private int getImgLineCount(ArrayList<String> mData) {
        int count = mData.size();
        if (count <= 3) {
            return 1;
        } else if (count <= 6) {
            return 2;
        } else if (count <= 9) {
            return 3;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }

    }

    public void setData(ArrayList<Status> data) {
        this.mData = data;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_img;
        public TextView profile_name;
        public TextView profile_time;
        public TextView weiboComeFrom;
        public TextView weibo_Content;
        public TextView redirect;
        public TextView comment;
        public TextView feedlike;
        public RecyclerView imageList;

        public ItemViewHolder(View v) {
            super(v);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_name = (TextView) v.findViewById(R.id.profile_name);
            profile_time = (TextView) v.findViewById(R.id.profile_time);
            weibo_Content = (TextView) v.findViewById(R.id.weibo_Content);
            weiboComeFrom = (TextView) v.findViewById(R.id.weiboComeFrom);
            redirect = (TextView) v.findViewById(R.id.redirect);
            comment = (TextView) v.findViewById(R.id.comment);
            feedlike = (TextView) v.findViewById(R.id.feedlike);
            imageList = (RecyclerView) v.findViewById(R.id.weibo_image);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;

        public FooterViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.loadMoreLayout);
        }

    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.right = space;
            outRect.top = space;
        }
    }

}
