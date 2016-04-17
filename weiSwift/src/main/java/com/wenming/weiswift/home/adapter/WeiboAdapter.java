package com.wenming.weiswift.home.adapter;

import android.content.Context;
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
import com.wenming.weiswift.common.DateUtils;
import com.wenming.weiswift.common.DensityUtil;
import com.wenming.weiswift.common.emojitextview.EmojiTextView;
import com.wenming.weiswift.home.imagelist.ImageAdapter;
import com.wenming.weiswift.home.imagelist.SpaceItemDecoration;
import com.wenming.weiswift.home.util.WeiBoContentTextUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wenmingvs on 2015/12/29.
 */
public class WeiboAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int TYPE_ORINGIN_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_RETWEET_ITEM = 3;
    private static final int TYPE_RETWEET_DELETE = 4;

    protected AnimationDrawable mFooterImag;
    private ArrayList<Status> mData;
    private Context mContext;
    private DisplayImageOptions options;
    private ArrayList<String> mImageDatas;
    private boolean mFirstLoad;
    private ImageAdapter imageAdapter;
    private LinearLayout.LayoutParams mParams;
    private View view;
    private StringBuffer retweetcontent_buffer = new StringBuffer();


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
        if (viewType == TYPE_ORINGIN_ITEM) {
            view = LayoutInflater.from(mContext).inflate(R.layout.mainfragment_original_weiboitem, parent, false);
            OriginViewHolder itemViewHolder = new OriginViewHolder(view);
            itemViewHolder.imageList.addItemDecoration(new SpaceItemDecoration((int) mContext.getResources().getDimension(R.dimen.home_weiboitem_imagelist_space)));
            return itemViewHolder;
        } else {
            if (viewType == TYPE_RETWEET_ITEM) {
                view = LayoutInflater.from(mContext).inflate(R.layout.mainfragment_retweet_weiboitem, parent, false);
                RetweetViewHolder retweetViewHolder = new RetweetViewHolder(view);
                retweetViewHolder.retweet_imageList.addItemDecoration(new SpaceItemDecoration((int) mContext.getResources().getDimension(R.dimen.home_weiboitem_imagelist_space)));
                return retweetViewHolder;
            } else if (viewType == TYPE_FOOTER) {
                view = LayoutInflater.from(mContext).inflate(R.layout.footerview_loading, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                FooterViewHolder footerViewHolder = new FooterViewHolder(view);
                ImageView waitingImg = (ImageView) view.findViewById(R.id.waiting_image);
                mFooterImag = (AnimationDrawable) waitingImg.getDrawable();
                mFooterImag.start();
                return footerViewHolder;

            } else if (viewType == TYPE_HEADER) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.headsearchview, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(mContext, 40));
                view.setLayoutParams(params);
                SearchViewHolder headerViewHolder = new SearchViewHolder(view);
                return headerViewHolder;
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof OriginViewHolder) {
            //微博用户信息
            ImageLoader.getInstance().displayImage(mData.get(position).user.avatar_hd, ((OriginViewHolder) holder).profile_img, options);
            ((OriginViewHolder) holder).profile_name.setText(mData.get(position).user.name);

            Date data = DateUtils.parseDate(mData.get(position).created_at, DateUtils.WeiBo_ITEM_DATE_FORMAT);
            SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
            String time = df.format(data);
            ((OriginViewHolder) holder).profile_time.setText(time + "   ");
            if (mData.get(position).source != null) {
                ((OriginViewHolder) holder).weiboComeFrom.setText("来自 " + mData.get(position).source);
            } else {
                ((OriginViewHolder) holder).weiboComeFrom.setText("");
            }


            //微博文字内容
            ((OriginViewHolder) holder).weibo_Content.setText(WeiBoContentTextUtil.getWeiBoContent(mData.get(position).text, mContext, ((OriginViewHolder) holder).weibo_Content));

            //微博图片内容
            ((OriginViewHolder) holder).imageList.setVisibility(View.GONE);
            ((OriginViewHolder) holder).imageList.setVisibility(View.VISIBLE);

            mImageDatas = mData.get(position).origin_pic_urls;
            GridLayoutManager gridLayoutManager = initGridLayoutManager(mImageDatas);
            imageAdapter = new ImageAdapter(mImageDatas, mContext);
            ((OriginViewHolder) holder).imageList.setHasFixedSize(true);
            ((OriginViewHolder) holder).imageList.setAdapter(imageAdapter);
            ((OriginViewHolder) holder).imageList.setLayoutManager(gridLayoutManager);
            imageAdapter.setData(mImageDatas);


            if (mImageDatas == null || mImageDatas.size() == 0) {
                ((OriginViewHolder) holder).imageList.setVisibility(View.GONE);
            }

            //微博转发，评论，赞的数量
            ((OriginViewHolder) holder).comment.setText(mData.get(position).comments_count + "");
            ((OriginViewHolder) holder).redirect.setText(mData.get(position).reposts_count + "");
            ((OriginViewHolder) holder).feedlike.setText(mData.get(position).attitudes_count + "");

        } else if (holder instanceof RetweetViewHolder) {
            //微博用户信息
            ImageLoader.getInstance().displayImage(mData.get(position).user.avatar_hd, ((RetweetViewHolder) holder).profile_img, options);
            ((RetweetViewHolder) holder).profile_name.setText(mData.get(position).user.name);
            Date data = DateUtils.parseDate(mData.get(position).created_at, DateUtils.WeiBo_ITEM_DATE_FORMAT);
            SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
            String time = df.format(data);
            ((RetweetViewHolder) holder).profile_time.setText(time + "   ");
            ((RetweetViewHolder) holder).weiboComeFrom.setText("来自 " + mData.get(position).source);

            //微博文字内容
            ((RetweetViewHolder) holder).retweet_content.setText(WeiBoContentTextUtil.getWeiBoContent(mData.get(position).text, mContext, ((RetweetViewHolder) holder).retweet_content));

            //微博转发，评论，赞的数量
            ((RetweetViewHolder) holder).comment.setText(mData.get(position).comments_count + "");
            ((RetweetViewHolder) holder).redirect.setText(mData.get(position).reposts_count + "");
            ((RetweetViewHolder) holder).feedlike.setText(mData.get(position).attitudes_count + "");

            //转发的文字
            retweetcontent_buffer.setLength(0);
            retweetcontent_buffer.append("@");
            retweetcontent_buffer.append(mData.get(position).retweeted_status.user.name + " :  ");
            retweetcontent_buffer.append(mData.get(position).retweeted_status.text);
            ((RetweetViewHolder) holder).origin_nameAndcontent.setText(WeiBoContentTextUtil.getWeiBoContent(retweetcontent_buffer.toString(), mContext, ((RetweetViewHolder) holder).origin_nameAndcontent));

            //转发的图片
            ((RetweetViewHolder) holder).retweet_imageList.setVisibility(View.GONE);
            ((RetweetViewHolder) holder).retweet_imageList.setVisibility(View.VISIBLE);

            mImageDatas = mData.get(position).retweeted_status.origin_pic_urls;
            GridLayoutManager gridLayoutManager = initGridLayoutManager(mImageDatas);
            imageAdapter = new ImageAdapter(mImageDatas, mContext);
            ((RetweetViewHolder) holder).retweet_imageList.setHasFixedSize(true);
            ((RetweetViewHolder) holder).retweet_imageList.setAdapter(imageAdapter);
            ((RetweetViewHolder) holder).retweet_imageList.setLayoutManager(gridLayoutManager);
            imageAdapter.setData(mImageDatas);

            if (mImageDatas == null || mImageDatas.size() == 0) {
                ((RetweetViewHolder) holder).retweet_imageList.setVisibility(View.GONE);
            }

        } else if (holder instanceof FooterViewHolder) {
            if (getItemCount() == 1) {
                ((FooterViewHolder) holder).linearLayout.setVisibility(View.GONE);
            } else {
                ((FooterViewHolder) holder).linearLayout.setVisibility(View.VISIBLE);
            }
        }

    }


    /**
     * 根据图片数量返回应该显示的行数
     *
     * @param mData
     * @return
     */
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

    /**
     * 根据图片数量，初始化GridLayoutManager，并且设置列数，
     * 当图片 = 1 的时候，显示1列
     * 当图片<=4张的时候，显示2列
     * 当图片>4 张的时候，显示3列
     *
     * @return
     */
    private GridLayoutManager initGridLayoutManager(ArrayList<String> ImageDatas) {
        GridLayoutManager gridLayoutManager;
        if (mImageDatas != null && mImageDatas.size() == 1) {
            gridLayoutManager = new GridLayoutManager(mContext, 1);
        } else if (mImageDatas != null && mImageDatas.size() <= 4) {
            gridLayoutManager = new GridLayoutManager(mContext, 2);
        } else {
            gridLayoutManager = new GridLayoutManager(mContext, 3);
        }
        return gridLayoutManager;
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else if (position == 0) {
            return TYPE_HEADER;
        } else {
            if (mData.get(position).retweeted_status != null && mData.get(position).retweeted_status.user != null) {
                return TYPE_RETWEET_ITEM;
            } else {
                return TYPE_ORINGIN_ITEM;
            }
        }
    }

    public void setData(ArrayList<Status> data) {
        this.mData = data;
    }

    public static class OriginViewHolder extends ViewHolder {
        public ImageView profile_img;
        public TextView profile_name;
        public TextView profile_time;
        public TextView weiboComeFrom;
        public EmojiTextView weibo_Content;
        public TextView redirect;
        public TextView comment;
        public TextView feedlike;
        public RecyclerView imageList;

        public OriginViewHolder(View v) {
            super(v);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_name = (TextView) v.findViewById(R.id.profile_name);
            profile_time = (TextView) v.findViewById(R.id.profile_time);
            weibo_Content = (EmojiTextView) v.findViewById(R.id.weibo_Content);
            weiboComeFrom = (TextView) v.findViewById(R.id.weiboComeFrom);
            redirect = (TextView) v.findViewById(R.id.redirect);
            comment = (TextView) v.findViewById(R.id.comment);
            feedlike = (TextView) v.findViewById(R.id.feedlike);
            imageList = (RecyclerView) v.findViewById(R.id.weibo_image);
        }
    }

    public static class RetweetViewHolder extends ViewHolder {
        public ImageView profile_img;
        public TextView profile_name;
        public TextView profile_time;
        public TextView weiboComeFrom;
        public TextView retweet_content;
        public TextView redirect;
        public TextView comment;
        public TextView feedlike;
        public TextView origin_nameAndcontent;
        public RecyclerView retweet_imageList;


        public RetweetViewHolder(View v) {
            super(v);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_name = (TextView) v.findViewById(R.id.profile_name);
            profile_time = (TextView) v.findViewById(R.id.profile_time);
            retweet_content = (TextView) v.findViewById(R.id.retweet_content);
            weiboComeFrom = (TextView) v.findViewById(R.id.weiboComeFrom);
            redirect = (TextView) v.findViewById(R.id.redirect);
            comment = (TextView) v.findViewById(R.id.comment);
            feedlike = (TextView) v.findViewById(R.id.feedlike);
            origin_nameAndcontent = (TextView) v.findViewById(R.id.origin_nameAndcontent);
            retweet_imageList = (RecyclerView) v.findViewById(R.id.origin_imageList);
        }
    }

    class FooterViewHolder extends ViewHolder {
        private LinearLayout linearLayout;

        public FooterViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.loadMoreLayout);
        }
    }

    private class SearchViewHolder extends ViewHolder {

        public SearchViewHolder(View itemView) {
            super(itemView);
        }
    }


}
