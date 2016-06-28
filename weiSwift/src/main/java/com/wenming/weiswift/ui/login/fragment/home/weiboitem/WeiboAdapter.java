package com.wenming.weiswift.ui.login.fragment.home.weiboitem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenming.weiswift.R;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.mvp.presenter.WeiBoArrowPresent;
import com.wenming.weiswift.mvp.presenter.imp.WeiBoArrowPresenterImp;
import com.wenming.weiswift.ui.common.FillContent;
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity.OriginPicTextCommentDetailActivity;
import com.wenming.weiswift.ui.login.fragment.home.weiboitemdetail.activity.RetweetPicTextCommentDetailActivity;
import com.wenming.weiswift.widget.emojitextview.EmojiTextView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 2015/12/29.
 */
public abstract class WeiboAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int TYPE_ORINGIN_ITEM = 0;
    private static final int TYPE_RETWEET_ITEM = 3;
    private ArrayList<Status> mDatas;
    private Context mContext;
    private View mView;

    public WeiboAdapter(ArrayList<Status> datas, Context context) {
        this.mDatas = datas;
        this.mContext = context;
    }

    /**
     * 设置图片间距，注意要保证addItemDecoration只被调用一次，多次调用间距会累加
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ORINGIN_ITEM) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.home_weiboitem_original_pictext, parent, false);
            OriginViewHolder originViewHolder = new OriginViewHolder(mView);
            return originViewHolder;
        } else if (viewType == TYPE_RETWEET_ITEM) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.mainfragment_weiboitem_retweet_pictext, parent, false);
            RetweetViewHolder retweetViewHolder = new RetweetViewHolder(mView);
            return retweetViewHolder;
        }
        return null;
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof OriginViewHolder) {
            //如果这条原创微博没有被删除
            if (mDatas.get(position).user != null) {
                ((OriginViewHolder) holder).titlebar_layout.setVisibility(View.VISIBLE);
                ((OriginViewHolder) holder).bottombar_layout.setVisibility(View.VISIBLE);
                ((OriginViewHolder) holder).splitLine.setVisibility(View.GONE);
                ((OriginViewHolder) holder).favoritedelete.setVisibility(View.GONE);
                FillContent.fillTitleBar(mContext, mDatas.get(position), ((OriginViewHolder) holder).profile_img, ((OriginViewHolder) holder).profile_verified, ((OriginViewHolder) holder).profile_name, ((OriginViewHolder) holder).profile_time, ((OriginViewHolder) holder).weibo_comefrom);
                FillContent.fillWeiBoContent(mDatas.get(position).text, mContext, ((OriginViewHolder) holder).weibo_content);
                FillContent.fillButtonBar(mContext, mDatas.get(position), ((OriginViewHolder) holder).bottombar_retweet, ((OriginViewHolder) holder).bottombar_comment, ((OriginViewHolder) holder).bottombar_attitude, ((OriginViewHolder) holder).comment, ((OriginViewHolder) holder).redirect, ((OriginViewHolder) holder).feedlike);
                FillContent.fillWeiBoImgList(mDatas.get(position), mContext, ((OriginViewHolder) holder).imageList);

                ((OriginViewHolder) holder).bottombar_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                ((OriginViewHolder) holder).popover_arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arrowClick(mDatas.get(position), position);
                    }
                });

                //微博背景的点击事件
                ((OriginViewHolder) holder).origin_weibo_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, OriginPicTextCommentDetailActivity.class);
                        intent.putExtra("weiboitem", mDatas.get(position));
                        mContext.startActivity(intent);
                    }
                });
            }
            //如果这条原创微博被删除
            else {
                ((OriginViewHolder) holder).titlebar_layout.setVisibility(View.GONE);
                ((OriginViewHolder) holder).bottombar_layout.setVisibility(View.GONE);
                ((OriginViewHolder) holder).imageList.setVisibility(View.GONE);
                ((OriginViewHolder) holder).splitLine.setVisibility(View.VISIBLE);
                ((OriginViewHolder) holder).favoritedelete.setVisibility(View.VISIBLE);
                FillContent.fillWeiBoContent(mDatas.get(position).text, mContext, ((OriginViewHolder) holder).weibo_content);
                
                ((OriginViewHolder) holder).favoritedelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WeiBoArrowPresent weiBoArrowPresent = new WeiBoArrowPresenterImp(WeiboAdapter.this);
                        weiBoArrowPresent.cancalFavorite(position, mDatas.get(position), mContext, true);
                        //arrowClick(mDatas.get(position), position);
                    }
                });
            }


        } else if (holder instanceof RetweetViewHolder) {
            FillContent.fillTitleBar(mContext, mDatas.get(position), ((RetweetViewHolder) holder).profile_img, ((RetweetViewHolder) holder).profile_verified, ((RetweetViewHolder) holder).profile_name, ((RetweetViewHolder) holder).profile_time, ((RetweetViewHolder) holder).weibo_comefrom);
            FillContent.fillRetweetContent(mDatas.get(position), mContext, ((RetweetViewHolder) holder).origin_nameAndcontent);
            FillContent.fillWeiBoContent(mDatas.get(position).text, mContext, ((RetweetViewHolder) holder).retweet_content);
            FillContent.fillButtonBar(mContext, mDatas.get(position), ((RetweetViewHolder) holder).bottombar_retweet, ((RetweetViewHolder) holder).bottombar_comment, ((RetweetViewHolder) holder).bottombar_attitude, ((RetweetViewHolder) holder).comment, ((RetweetViewHolder) holder).redirect, ((RetweetViewHolder) holder).feedlike);
            FillContent.fillWeiBoImgList(mDatas.get(position).retweeted_status, mContext, ((RetweetViewHolder) holder).retweet_imageList);

            //点击转发的内容
            ((RetweetViewHolder) holder).retweetStatus_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDatas.get(position).retweeted_status.user != null) {
                        Intent intent = new Intent(mContext, OriginPicTextCommentDetailActivity.class);
                        intent.putExtra("weiboitem", mDatas.get(position).retweeted_status);
                        mContext.startActivity(intent);
                    }
                }
            });

            ((RetweetViewHolder) holder).popover_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrowClick(mDatas.get(position), position);
                }
            });

            //微博背景的点击事件
            ((RetweetViewHolder) holder).retweet_weibo_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, RetweetPicTextCommentDetailActivity.class);
                    intent.putExtra("weiboitem", mDatas.get(position));
                    mContext.startActivity(intent);
                }
            });


        }

    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        } else {
            return 0;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position).retweeted_status != null) {
            return TYPE_RETWEET_ITEM;
        } else {
            return TYPE_ORINGIN_ITEM;
        }
    }

    public void setData(ArrayList<Status> data) {
        this.mDatas = data;
    }

    public abstract void arrowClick(Status status, int position);

    public void removeDataItem(int position) {
        mDatas.remove(position);
    }


    public static class OriginViewHolder extends ViewHolder {
        public LinearLayout origin_weibo_layout;
        public LinearLayout titlebar_layout;
        public ImageView profile_img;
        public ImageView profile_verified;
        public ImageView popover_arrow;
        public TextView profile_name;
        public TextView profile_time;
        public TextView weibo_comefrom;
        public EmojiTextView weibo_content;
        public TextView redirect;
        public TextView comment;
        public TextView feedlike;
        public RecyclerView imageList;
        public TextView favoritedelete;
        public ImageView splitLine;
        public LinearLayout bottombar_layout;
        public LinearLayout bottombar_retweet;
        public LinearLayout bottombar_comment;
        public LinearLayout bottombar_attitude;

        public OriginViewHolder(View v) {
            super(v);
            origin_weibo_layout = (LinearLayout) v.findViewById(R.id.origin_weibo_layout);
            titlebar_layout = (LinearLayout) v.findViewById(R.id.titlebar_layout);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_verified = (ImageView) v.findViewById(R.id.profile_verified);
            popover_arrow = (ImageView) v.findViewById(R.id.popover_arrow);
            profile_name = (TextView) v.findViewById(R.id.profile_name);
            profile_time = (TextView) v.findViewById(R.id.profile_time);
            weibo_content = (EmojiTextView) v.findViewById(R.id.weibo_Content);
            weibo_comefrom = (TextView) v.findViewById(R.id.weiboComeFrom);
            redirect = (TextView) v.findViewById(R.id.redirect);
            comment = (TextView) v.findViewById(R.id.comment);
            feedlike = (TextView) v.findViewById(R.id.feedlike);
            splitLine = (ImageView) v.findViewById(R.id.splitLine);
            imageList = (RecyclerView) v.findViewById(R.id.weibo_image);
            favoritedelete = (TextView) v.findViewById(R.id.favorities_delete);
            bottombar_layout = (LinearLayout) v.findViewById(R.id.bottombar_layout);
            bottombar_retweet = (LinearLayout) v.findViewById(R.id.bottombar_retweet);
            bottombar_comment = (LinearLayout) v.findViewById(R.id.bottombar_comment);
            bottombar_attitude = (LinearLayout) v.findViewById(R.id.bottombar_attitude);
        }
    }

    public static class RetweetViewHolder extends ViewHolder {
        public LinearLayout retweet_weibo_layout;
        public ImageView profile_img;
        public ImageView profile_verified;
        public ImageView popover_arrow;
        public TextView profile_name;
        public TextView profile_time;
        public TextView weibo_comefrom;
        public EmojiTextView retweet_content;
        public TextView redirect;
        public TextView comment;
        public TextView feedlike;
        public EmojiTextView origin_nameAndcontent;
        public RecyclerView retweet_imageList;
        public LinearLayout bottombar_layout;
        public LinearLayout bottombar_retweet;
        public LinearLayout bottombar_comment;
        public LinearLayout bottombar_attitude;
        public LinearLayout retweetStatus_layout;


        public RetweetViewHolder(View v) {
            super(v);
            retweet_weibo_layout = (LinearLayout) v.findViewById(R.id.retweet_weibo_layout);
            profile_img = (ImageView) v.findViewById(R.id.profile_img);
            profile_verified = (ImageView) v.findViewById(R.id.profile_verified);
            popover_arrow = (ImageView) v.findViewById(R.id.popover_arrow);
            profile_name = (TextView) v.findViewById(R.id.profile_name);
            profile_time = (TextView) v.findViewById(R.id.profile_time);
            retweet_content = (EmojiTextView) v.findViewById(R.id.retweet_content);
            weibo_comefrom = (TextView) v.findViewById(R.id.weiboComeFrom);
            redirect = (TextView) v.findViewById(R.id.redirect);
            comment = (TextView) v.findViewById(R.id.comment);
            feedlike = (TextView) v.findViewById(R.id.feedlike);
            origin_nameAndcontent = (EmojiTextView) v.findViewById(R.id.origin_nameAndcontent);
            retweet_imageList = (RecyclerView) v.findViewById(R.id.origin_imageList);
            bottombar_layout = (LinearLayout) v.findViewById(R.id.bottombar_layout);
            bottombar_retweet = (LinearLayout) v.findViewById(R.id.bottombar_retweet);
            bottombar_comment = (LinearLayout) v.findViewById(R.id.bottombar_comment);
            bottombar_attitude = (LinearLayout) v.findViewById(R.id.bottombar_attitude);
            retweetStatus_layout = (LinearLayout) v.findViewById(R.id.retweetStatus_layout);
        }
    }

}
