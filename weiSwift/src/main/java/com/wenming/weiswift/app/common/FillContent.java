package com.wenming.weiswift.app.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cesards.cropimageview.CropImageView;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.entity.Comment;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.common.entity.User;
import com.wenming.weiswift.app.mvp.model.imp.StatusDetailModelImp;
import com.wenming.weiswift.app.imgpreview.ImageDetailsActivity;
import com.wenming.weiswift.app.imgpreview.SaveImageDialog;
import com.wenming.weiswift.app.home.adapter.ImageAdapter;
import com.wenming.weiswift.app.profile.activity.ProfileSwipeActivity;
import com.wenming.weiswift.app.weibodetail.activity.OriginPicTextCommentDetailSwipeActivity;
import com.wenming.weiswift.app.weibodetail.activity.RetweetPicTextCommentDetailSwipeActivity;
import com.wenming.weiswift.app.home.adapter.CommentDetailAdapter;
import com.wenming.weiswift.app.login.fragment.post.PostService;
import com.wenming.weiswift.app.login.fragment.post.idea.IdeaSwipeActivity;
import com.wenming.weiswift.utils.DateUtils;
import com.wenming.weiswift.utils.NetUtil;
import com.wenming.weiswift.utils.SharedPreferencesUtil;
import com.wenming.weiswift.utils.TimeUtils;
import com.wenming.weiswift.widget.emojitextview.EmojiTextView;
import com.wenming.weiswift.widget.emojitextview.WeiBoContentTextUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by wenmingvs on 16/4/21.
 */
public class FillContent {

    public static final int IMAGE_TYPE_LONG_TEXT = 1;//长微博
    public static final int IMAGE_TYPE_LONG_PIC = 2;//比较长的微博（但是不至于像长微博那么长）
    public static final int IMAGE_TYPE_WIDTH_PIC = 3;//比较宽的微博
    public static final int IMAGE_TYPE_GIF = 4;

    private static DisplayImageOptions mAvatorOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.avator_default)
            .showImageForEmptyUri(R.drawable.avator_default)
            .showImageOnFail(R.drawable.avator_default)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .displayer(new CircleBitmapDisplayer(14671839, 1))
            .build();

    /**
     * 设置头像的认证icon，记住要手动刷新icon，不然icon会被recycleriview重用，导致显示出错
     *
     * @param user
     * @param profile_img
     * @param profile_verified
     */
    public static void fillProfileImg(final Context context, final User user, final ImageView profile_img, final ImageView profile_verified) {
        profile_verified.setVisibility(View.GONE);
        profile_verified.setVisibility(View.VISIBLE);

        if (user.verified == true && user.verified_type == 0) {
            profile_verified.setImageResource(R.drawable.avatar_vip);
        } else if (user.verified == true && (user.verified_type == 1 || user.verified_type == 2 || user.verified_type == 3)) {
            profile_verified.setImageResource(R.drawable.avatar_enterprise_vip);
        } else if (user.verified == false && user.verified_type == 220) {
            profile_verified.setImageResource(R.drawable.avatar_grassroot);
        } else {
            profile_verified.setVisibility(View.INVISIBLE);
        }
        ImageLoader.getInstance().displayImage(user.avatar_hd, profile_img, mAvatorOptions);

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileSwipeActivity.class);
                intent.putExtra("screenName", user.screen_name);
                context.startActivity(intent);
            }
        });
    }

    /**
     * 填充顶部微博用户信息数据
     *
     * @param context
     * @param comment
     * @param profile_img
     * @param profile_verified
     * @param profile_name
     * @param profile_time
     * @param weibo_comefrom
     */
    public static void fillTitleBar(Context context, Comment comment, ImageView profile_img, ImageView profile_verified, TextView profile_name, TextView profile_time, TextView weibo_comefrom) {
        fillProfileImg(context, comment.user, profile_img, profile_verified);
        setWeiBoName(profile_name, comment.user);
        setWeiBoTime(context, profile_time, comment);
        setWeiBoComeFrom(weibo_comefrom, comment);
    }

    public static void fillTitleBar(Context context, Status status, ImageView profile_img, ImageView profile_verified, TextView profile_name, TextView profile_time, TextView weibo_comefrom) {
        fillProfileImg(context, status.user, profile_img, profile_verified);
        setWeiBoName(profile_name, status.user);
        setWeiBoTime(context, profile_time, status);
        setWeiBoComeFrom(weibo_comefrom, status);
    }


    public static void setWeiBoName(TextView textView, User user) {
        if (user.remark != null && user.remark.length() > 0) {
            textView.setText(user.remark);
        } else {
            textView.setText(user.name);
        }
    }

    public static void setWeiBoTime(Context context, TextView textView, Status status) {
        Date data = DateUtils.parseDate(status.created_at, DateUtils.WeiBo_ITEM_DATE_FORMAT);
        TimeUtils timeUtils = TimeUtils.instance(context);
        textView.setText(timeUtils.buildTimeString(data.getTime()) + "   ");
    }

    public static void setWeiBoTime(Context context, TextView textView, Comment comment) {
        Date data = DateUtils.parseDate(comment.created_at, DateUtils.WeiBo_ITEM_DATE_FORMAT);
        TimeUtils timeUtils = TimeUtils.instance(context);
        textView.setText(timeUtils.buildTimeString(data.getTime()) + "   ");
    }


    public static void setWeiBoComeFrom(TextView textView, Status status) {
        if (status == null) {
            textView.setText("");
            return;
        }
        if (!TextUtils.isEmpty(status.source)) {
            textView.setText("来自 " + status.source);
        } else {
            textView.setText("");
        }
    }

    public static void setFollowerComeFrom(TextView textView, Status status) {
        if (status == null) {
            textView.setText("");
            return;
        }
        if (!TextUtils.isEmpty(status.source)) {
            textView.setText(status.source);
        } else {
            textView.setText("");
        }
    }

    public static void updateRealtionShip(Context context, User user, ImageView icon, TextView text) {
        boolean isNightMode = (boolean) SharedPreferencesUtil.get(context, "setNightMode", false);
        if (!isNightMode) {
            if (user.follow_me && user.following) {
                icon.setImageResource(R.drawable.card_icon_arrow);
                text.setText("互相关注");
                text.setTextColor(context.getResources().getColor(R.color.friend_item_button_follow_each_other));
            } else if (user.following) {
                icon.setImageResource(R.drawable.card_icon_attention);
                text.setText("已关注");
                text.setTextColor(context.getResources().getColor(R.color.friend_item_button_follow_alreay));
            } else {
                icon.setImageResource(R.drawable.card_icon_addattention);
                text.setText("加关注");
                text.setTextColor(context.getResources().getColor(R.color.friend_item_button_follow_none));
            }
        } else {
            if (user.follow_me && user.following) {
                icon.setImageResource(R.drawable.card_icon_arrow);
                text.setText("互相关注");
                text.setTextColor(context.getResources().getColor(R.color.night_friend_item_button_follow_each_other));
            } else if (user.following) {
                icon.setImageResource(R.drawable.card_icon_attention);
                text.setText("已关注");
                text.setTextColor(context.getResources().getColor(R.color.night_friend_item_button_follow_alreay));
            } else {
                icon.setImageResource(R.drawable.card_icon_addattention);
                text.setText("加关注");
                text.setTextColor(context.getResources().getColor(R.color.night_friend_item_button_follow_none));
            }
        }


    }


    public static void setWeiBoComeFrom(TextView textView, Comment comment) {
        if (comment == null) {
            textView.setText("");
            return;
        }
        if (!TextUtils.isEmpty(comment.source)) {
            textView.setText("来自 " + comment.source);
        } else {
            textView.setText("");
        }
    }

    /**
     * 填充微博转发，评论，赞的数量
     *
     * @param status
     * @param bottombar_retweet
     * @param bottombar_comment
     * @param bottombar_attitude
     * @param comment
     * @param redirect
     * @param feedlike
     */
    public static void fillButtonBar(final Context context, final Status status, LinearLayout bottombar_retweet, LinearLayout bottombar_comment, LinearLayout bottombar_attitude, TextView comment, TextView redirect, TextView feedlike) {
        if (status.comments_count != 0) {
            comment.setText(status.comments_count + "");
        } else {
            comment.setText("评论");
        }

        if (status.reposts_count != 0) {
            redirect.setText(status.reposts_count + "");
        } else {
            redirect.setText("转发");
        }

        if (status.attitudes_count != 0) {
            feedlike.setText(status.attitudes_count + "");
        } else {
            feedlike.setText("赞");
        }

        fillButtonBar(context, status, bottombar_retweet, bottombar_comment, bottombar_attitude);
    }

    public static void fillButtonBar(final Context context, final Status status, LinearLayout bottombar_retweet, LinearLayout bottombar_comment, LinearLayout bottombar_attitude) {
        //如果转发的内容已经被删除,则不允许转发
        if (status.retweeted_status != null && status.retweeted_status.user == null) {
            bottombar_retweet.setEnabled(false);
        } else {
            bottombar_retweet.setEnabled(true);
        }

        bottombar_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.retweeted_status == null) {
                    Intent intent = new Intent(context, OriginPicTextCommentDetailSwipeActivity.class);
                    intent.putExtra("weiboitem", status);
                    ((Activity) context).startActivityForResult(intent, 101);
                } else {
                    Intent intent = new Intent(context, RetweetPicTextCommentDetailSwipeActivity.class);
                    intent.putExtra("weiboitem", status);
                    ((Activity) context).startActivityForResult(intent, 101);
                }

            }
        });
        bottombar_retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IdeaSwipeActivity.class);
                intent.putExtra("ideaType", PostService.POST_SERVICE_REPOST_STATUS);
                intent.putExtra("status", status);
                context.startActivity(intent);
            }
        });
    }

    public static void fillDetailButtonBar(final Context context, final Status status, LinearLayout bottombar_retweet, LinearLayout bottombar_comment, LinearLayout bottombar_attitude) {
        //如果转发的内容已经被删除,则不允许转发
        if (status.retweeted_status != null && status.retweeted_status.user == null) {
            bottombar_retweet.setEnabled(false);
        } else {
            bottombar_retweet.setEnabled(true);
        }

        bottombar_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IdeaSwipeActivity.class);
                intent.putExtra("ideaType", PostService.POST_SERVICE_COMMENT_STATUS);
                intent.putExtra("status", status);
                context.startActivity(intent);
            }
        });

        bottombar_retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IdeaSwipeActivity.class);
                intent.putExtra("ideaType", PostService.POST_SERVICE_REPOST_STATUS);
                intent.putExtra("status", status);
                context.startActivity(intent);
            }
        });

        bottombar_attitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点赞动画
            }
        });
    }


    /**
     * 决定是否隐藏转发，评论，赞的底部的bar，进入weibodetail的时候隐藏他
     *
     * @param visible
     * @param layout
     */
    public static void showButtonBar(int visible, LinearLayout layout) {
        if (visible == View.VISIBLE) {
            layout.setVisibility(View.VISIBLE);
        } else if (visible == View.GONE) {
            layout.setVisibility(View.GONE);
        } else if (visible == View.INVISIBLE) {
            layout.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 填充原创微博文字内容
     */
    public static void fillWeiBoContent(String text, Context context, EmojiTextView weibo_content) {
        weibo_content.setText(WeiBoContentTextUtil.getWeiBoContent(text, context, weibo_content));
        //weibo_content.setText(text);
    }

    /**
     * 填充转发微博文字内容
     */
    public static void fillRetweetContent(Status status, Context context, TextView origin_nameAndcontent) {
        if (status.retweeted_status.user != null) {
            StringBuffer retweetcontent_buffer = new StringBuffer();
            retweetcontent_buffer.setLength(0);
            retweetcontent_buffer.append("@");
            retweetcontent_buffer.append(status.retweeted_status.user.name + " :  ");
            retweetcontent_buffer.append(status.retweeted_status.text);
            origin_nameAndcontent.setText(WeiBoContentTextUtil.getWeiBoContent(retweetcontent_buffer.toString(), context, origin_nameAndcontent));
            //origin_nameAndcontent.setText(retweetcontent_buffer.toString());

        } else {
            origin_nameAndcontent.setText(WeiBoContentTextUtil.getWeiBoContent("抱歉，此微博已被作者删除。查看帮助：#网页链接#", context, origin_nameAndcontent));
            //origin_nameAndcontent.setText("抱歉，此微博已被作者删除。查看帮助：#网页链接#");
        }
    }

    /**
     * 填充微博图片列表,包括原创微博和转发微博中的图片都可以使用
     */
    public static void fillWeiBoImgList(Status status, Context context, RecyclerView recyclerview) {
        ArrayList<String> imageDatas = status.bmiddle_pic_urls;
        if (imageDatas == null || imageDatas.size() == 0) {
            recyclerview.setVisibility(View.GONE);
            return;
        }
        if (recyclerview.getVisibility() == View.GONE) {
            recyclerview.setVisibility(View.VISIBLE);
        }
        GridLayoutManager gridLayoutManager = initGridLayoutManager(imageDatas, context);
        ImageAdapter imageAdapter = new ImageAdapter(status, context);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(imageAdapter);
        recyclerview.setLayoutManager(gridLayoutManager);
        imageAdapter.setData(imageDatas);
        imageAdapter.notifyDataSetChanged();
    }

    /**
     * 根据图片数量，初始化GridLayoutManager，并且设置列数，
     * 当图片 = 1 的时候，显示1列
     * 当图片<=4张的时候，显示2列
     * 当图片>4 张的时候，显示3列
     *
     * @return
     */
    private static GridLayoutManager initGridLayoutManager(ArrayList<String> imageDatas, Context context) {
        GridLayoutManager gridLayoutManager;
        if (imageDatas != null) {
            switch (imageDatas.size()) {
                case 1:
                    gridLayoutManager = new GridLayoutManager(context, 1);
                    break;
                case 2:
                    gridLayoutManager = new GridLayoutManager(context, 2);
                    break;
                case 3:
                    gridLayoutManager = new GridLayoutManager(context, 3);
                    break;
                case 4:
                    gridLayoutManager = new GridLayoutManager(context, 2);
                    break;
                default:
                    gridLayoutManager = new GridLayoutManager(context, 3);
                    break;
            }
        } else {
            gridLayoutManager = new GridLayoutManager(context, 3);
        }
        return gridLayoutManager;
    }


    private static void displayLongPic(File file, Bitmap bitmap, SubsamplingScaleImageView longImg, ImageView imageLable) {
        imageLable.setVisibility(View.VISIBLE);
        imageLable.setImageResource(R.drawable.timeline_image_longimage);
        longImg.setZoomEnabled(false);
        longImg.setPanEnabled(false);
        longImg.setQuickScaleEnabled(false);
        if (NewFeature.timeline_img_quality != NewFeature.thumbnail_quality) {
            longImg.setImage(ImageSource.uri(file.getAbsolutePath()), new ImageViewState(0, new PointF(0, 0), 0));
            longImg.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        } else {
            longImg.setImage(ImageSource.uri(file.getAbsolutePath()));
        }
    }

    public static void displayNorImg(File file, Bitmap bitmap, ImageView norImg, ImageView imageLable) {
        imageLable.setVisibility(View.GONE);
        norImg.setImageBitmap(bitmap);
        norImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public static void displayGif(File file, GifImageView gifImageView, ImageView imageLable) {
        imageLable.setVisibility(View.VISIBLE);
        imageLable.setImageResource(R.drawable.timeline_image_gif);
        try {
            GifDrawable gifDrawable = new GifDrawable(file);
            gifImageView.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            Log.e("wenming", e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 填充微博列表图片
     *
     * @param context
     * @param status
     * @param options
     * @param position
     * @param longImg
     * @param norImg
     * @param gifImg
     * @param imageLabel
     */
    public static void fillImageList(final Context context, final Status status, DisplayImageOptions options, final int position, final SubsamplingScaleImageView longImg, final ImageView norImg, final GifImageView gifImg, final ImageView imageLabel) {
        final ArrayList<String> urllist;
        if (NewFeature.timeline_img_quality == NewFeature.thumbnail_quality) {
            urllist = status.thumbnail_pic_urls;
        } else if (NewFeature.timeline_img_quality == NewFeature.bmiddle_quality) {
            urllist = status.bmiddle_pic_urls;
        } else {
            urllist = status.origin_pic_urls;
        }
        ImageLoader.getInstance().loadImage(urllist.get(position), options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                setLabelForGif(urllist.get(position), imageLabel);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                File file = DiskCacheUtils.findInCache(urllist.get(position), ImageLoader.getInstance().getDiskCache());
                if (file == null) {
                    return;
                }
                if (imageUri.endsWith(".gif")) {
                    gifImg.setVisibility(View.VISIBLE);
                    longImg.setVisibility(View.INVISIBLE);
                    norImg.setVisibility(View.INVISIBLE);
                    displayGif(file, gifImg, imageLabel);
                } else if (ImageUtil.isLongImg(file, bitmap)) {
                    longImg.setVisibility(View.VISIBLE);
                    gifImg.setVisibility(View.INVISIBLE);
                    norImg.setVisibility(View.INVISIBLE);
                    displayLongPic(file, bitmap, longImg, imageLabel);
                } else {
                    norImg.setVisibility(View.VISIBLE);
                    longImg.setVisibility(View.INVISIBLE);
                    gifImg.setVisibility(View.INVISIBLE);
                    displayNorImg(file, bitmap, norImg, imageLabel);
                }
            }
        });
        longImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageDetailsActivity.class);
                intent.putExtra("imagelist_url", status.origin_pic_urls);
                intent.putExtra("image_position", position);
                context.startActivity(intent);
            }
        });
        gifImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageDetailsActivity.class);
                intent.putExtra("imagelist_url", status.bmiddle_pic_urls);
                intent.putExtra("image_position", position);
                context.startActivity(intent);
            }
        });

        norImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageDetailsActivity.class);
                intent.putExtra("imagelist_url", status.origin_pic_urls);
                intent.putExtra("image_position", position);
                context.startActivity(intent);
            }
        });
        //setOnLongClickListener(longImg, gifImg, norImg, context, status, position);
    }

    private static void setOnLongClickListener(SubsamplingScaleImageView longImg, GifImageView gifImg, ImageView norImg, final Context context, final Status status, final int position) {


        longImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ArrayList<String> saveUrlArrayList;
                if (NetUtil.isConnected(context)) {
                    saveUrlArrayList = status.origin_pic_urls;
                } else {
                    saveUrlArrayList = status.bmiddle_pic_urls;
                }
                SaveImageDialog.showDialog(saveUrlArrayList.get(position), context);
                return false;
            }
        });

        gifImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SaveImageDialog.showDialog(status.bmiddle_pic_urls.get(position), context);
                return false;
            }
        });

        norImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ArrayList<String> saveUrlArrayList;
                if (NetUtil.isConnected(context)) {
                    saveUrlArrayList = status.origin_pic_urls;
                } else {
                    saveUrlArrayList = status.bmiddle_pic_urls;
                }
                SaveImageDialog.showDialog(saveUrlArrayList.get(position), context);
                return false;
            }
        });
    }


    /**
     * 为Gif图设置图标，根据url来决定是否设置
     *
     * @param url
     * @param imageLabel
     */
    private static void setLabelForGif(String url, ImageView imageLabel) {
        if (url.endsWith(".gif")) {
            imageLabel.setVisibility(View.VISIBLE);
            imageLabel.setImageResource(R.drawable.timeline_image_gif);
        } else {
            imageLabel.setVisibility(View.GONE);
        }
    }

    /**
     * 根据下载的图片的大小，返回图片的类型
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static int returnImageType(Context context, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //长微博尺寸
        if (height >= width * 3) {
            return IMAGE_TYPE_LONG_TEXT;
        }

        return IMAGE_TYPE_WIDTH_PIC;
    }

    /**
     * 根据url链接判断是否为Gif，返回图片类型
     *
     * @param context
     * @param url
     * @return
     */
    public static int returnImageType(Context context, String url) {
        //长微博尺寸
        if (url.endsWith(".gif")) {
            return IMAGE_TYPE_GIF;
        }

        return IMAGE_TYPE_WIDTH_PIC;
    }


    public static void fillDetailBar(int comments_count, int reposts_count, int attitudes_count, TextView comment, TextView redirect, TextView feedlike) {
        comment.setText("评论 " + comments_count);
        redirect.setText("转发 " + reposts_count);
        feedlike.setText("赞 " + attitudes_count);

    }

    public static void refreshNoneView(Context context, int type, int repostss_count, int comments_count, View noneView) {
        TextView textView = (TextView) noneView.findViewById(R.id.tv_normal_refresh_footer_status);
        if (NetUtil.isConnected(context)) {
            switch (type) {
                case StatusDetailModelImp.COMMENT_PAGE:
                    if (comments_count > 0) {
                        noneView.setVisibility(View.GONE);
                    } else if (comments_count == 0) {
                        noneView.setVisibility(View.VISIBLE);
                        textView.setText("还没有人评论");
                    }
                    break;

                case StatusDetailModelImp.REPOST_PAGE:
                    if (repostss_count > 0) {
                        noneView.setVisibility(View.GONE);
                    } else if (repostss_count == 0) {
                        noneView.setVisibility(View.VISIBLE);
                        textView.setText("还没有人转发");
                    }
                    break;
            }

        } else {
            noneView.setVisibility(View.VISIBLE);
            textView.setText("网络出错啦");
        }

    }


    /**
     * 初始化用于显示评论，转发，赞的viewpager
     *
     * @param context
     * @param commentCount
     * @param commentArrayList
     * @param recyclerView
     * @param commentView
     */

    public static void fillCommentList(Context context, int commentCount, ArrayList<Comment> commentArrayList, final RecyclerView recyclerView, TextView commentView) {
        CommentDetailAdapter commentAdapter = new CommentDetailAdapter(context, commentArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(commentAdapter);
    }

    /**
     * 填充转发中的内容区域
     *
     * @param retweetstatus
     * @param profile_img
     * @param profile_name
     * @param content
     */
    public static void fillMentionCenterContent(Status retweetstatus, ImageView profile_img, TextView profile_name, TextView content) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.message_image_default)
                .showImageForEmptyUri(R.drawable.message_image_default)
                .showImageOnFail(R.drawable.timeline_image_failure)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        profile_img.setVisibility(View.GONE);
        profile_img.setVisibility(View.VISIBLE);

        //转发的内容存在且没有被删除
        if (retweetstatus != null && retweetstatus.user != null) {
            if (retweetstatus.bmiddle_pic != null && retweetstatus.bmiddle_pic.length() > 0) {
                ImageLoader.getInstance().displayImage(retweetstatus.bmiddle_pic, profile_img, options);
            } else {
                ImageLoader.getInstance().displayImage(retweetstatus.user.avatar_hd, profile_img, options);
            }
            profile_name.setVisibility(View.VISIBLE);
            profile_name.setText("@" + retweetstatus.user.name);
            content.setText(retweetstatus.text);
        }
        //转发的内容已经被删除
        else {
            profile_img.setImageResource(R.drawable.photo_filter_image_empty);
            profile_name.setVisibility(View.GONE);
            content.setText(retweetstatus.text);
        }
    }

    /**
     * 设置评论页的回复评论区域的Bg
     *
     * @param context
     * @param layout
     */
    public static void setReplyAreaBg(Context context, View layout) {
        boolean isNightMode = (boolean) SharedPreferencesUtil.get(context, "setNightMode", false);
        if (isNightMode) {

        } else {

        }

    }

    /**
     * 设置评论页的评论内容的center Bg
     *
     * @param layout
     */
    public static void setReplyContentAreaBg(View layout) {

    }

    public static void fillCommentCenterContent(final Context context, Comment comment, LinearLayout bg_layout, LinearLayout comment_weibolayout, EmojiTextView mycomment, final CropImageView mentionitem_img, TextView profile_name, TextView content) {
        //如果存在回复，则需要填充我回复的评论
        if (comment.reply_comment != null) {
            mycomment.setVisibility(View.VISIBLE);
            bg_layout.setBackgroundResource(R.drawable.home_commentcenter_grey_bg_auto);
            comment_weibolayout.setBackgroundResource(R.drawable.home_commentcenter_white_bg_auto);
            String mycommenttext = "@" + comment.reply_comment.user.name + ":" + comment.reply_comment.text;
            fillWeiBoContent(mycommenttext, context, mycomment);
        } else {
            mycomment.setVisibility(View.GONE);
            bg_layout.setBackgroundColor(Color.TRANSPARENT);
            comment_weibolayout.setBackgroundResource(R.drawable.home_commentcenter_white_noreply_bg_auto);
        }

        //填充我所评论的微博的内容，包括微博的主人名，微博图片，微博文本内容
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.message_image_default)
                .showImageForEmptyUri(R.drawable.message_image_default)
                .showImageOnFail(R.drawable.timeline_image_failure)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true)
                .cacheInMemory(true)
                .build();

        mentionitem_img.setVisibility(View.GONE);
        mentionitem_img.setVisibility(View.VISIBLE);

        //评论的微博存在且没有被删除
        if (comment.status != null && comment.status.user != null) {
            //评论的微博是转发微博且包含图片
            if (comment.status.retweeted_status != null && !TextUtils.isEmpty(comment.status.retweeted_status.bmiddle_pic)) {
                ImageLoader.getInstance().displayImage(comment.status.retweeted_status.bmiddle_pic, mentionitem_img, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (returnImageType(context, loadedImage) == IMAGE_TYPE_LONG_TEXT) {
                            mentionitem_img.setCropType(CropImageView.CropType.CENTER_TOP);
                        }
                    }
                });
            }
            //评论的微博是转发微博，但是没有图片
            else if (comment.status.retweeted_status != null && comment.status.retweeted_status.bmiddle_pic == null) {
                ImageLoader.getInstance().displayImage(comment.status.user.avatar_hd, mentionitem_img, options);
            }
            //评论的微博是原创微博，且存在图片
            else if (comment.status.bmiddle_pic != null && !TextUtils.isEmpty(comment.status.bmiddle_pic)) {
                ImageLoader.getInstance().displayImage(comment.status.bmiddle_pic, mentionitem_img, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (returnImageType(context, loadedImage) == IMAGE_TYPE_LONG_TEXT) {
                            mentionitem_img.setCropType(CropImageView.CropType.CENTER_TOP);
                        }
                    }
                });
            }
            //评论的微博是原创微博，但是没有图片
            else {
                ImageLoader.getInstance().displayImage(comment.status.user.avatar_hd, mentionitem_img, options);
            }

            profile_name.setVisibility(View.VISIBLE);
            profile_name.setText("@" + comment.status.user.name);
            content.setText(comment.status.text);
        }
        //评论的微博已经被删除
        else {
            mentionitem_img.setImageResource(R.drawable.photo_filter_image_empty);
            profile_name.setVisibility(View.GONE);
            content.setText(comment.status.text);
        }
    }

    public static void fillFollowerDescription(User user, TextView content) {
        //设置文本内容
        content.setText("");
        if (!TextUtils.isEmpty(user.description)) {
            content.setText(user.description);
        } else if (user.status != null) {
            content.setText(user.status.text);
        }
    }

    /**
     * 填充粉丝的内容
     *
     * @param context
     * @param user
     * @param follwerRelation
     */
    public static void fillFollowerRealtionShip(Context context, User user, ImageView follwerRelation, TextView textView) {
        //设置是否关注了此人
        if (user.following == true) {
            follwerRelation.setImageResource(R.drawable.card_icon_arrow);

        } else {
            follwerRelation.setImageResource(R.drawable.card_icon_addattention);
        }
    }

    public static void fillFriendContent(Context context, User user, ImageView friendImg, ImageView friendVerified, ImageView followme, TextView friendName, TextView friendContent) {
        FillContent.fillProfileImg(context, user, friendImg, friendVerified);
        setWeiBoName(friendName, user);

        if (user.follow_me) {
            followme.setVisibility(View.VISIBLE);
        } else {
            followme.setVisibility(View.INVISIBLE);
        }
        if (user.status != null) {//有些人不发微博
            friendContent.setText(user.status.text);
        }
    }


    public static void fillUserHeadView(final Context context, final User user, final ImageView userCoverimg, ImageView userImg, ImageView userVerified, TextView userName,
                                        ImageView userSex, TextView userFriends,
                                        TextView userFollows, TextView userVerifiedreason) {
        if (!TextUtils.isEmpty(user.cover_image_phone)) {
            ImageLoader.getInstance().displayImage(user.cover_image_phone, userCoverimg, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if (!TextUtils.isEmpty(user.cover_image) && NetUtil.isConnected(context)) {
                        ImageLoader.getInstance().displayImage(user.cover_image, userCoverimg);
                    } else {
                        userCoverimg.setImageResource(R.drawable.cover_image);
                    }
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        } else if (!TextUtils.isEmpty(user.cover_image)) {
            ImageLoader.getInstance().displayImage(user.cover_image, userCoverimg);
        } else {
            userCoverimg.setImageResource(R.drawable.cover_image);
        }

        userCoverimg.setColorFilter(Color.parseColor("#1e000000"));

        fillProfileImg(context, user, userImg, userVerified);
        setWeiBoName(userName, user);

        if (user.gender.equals("m")) {
            userSex.setImageResource(R.drawable.userinfo_icon_male);
        } else if (user.gender.equals("f")) {
            userSex.setImageResource(R.drawable.userinfo_icon_female);
        } else {
            userSex.setVisibility(View.GONE);
        }
        userFriends.setText("关注  " + user.friends_count);
        userFollows.setText("粉丝  " + user.followers_count);
        if (!user.verified) {
            userVerifiedreason.setVisibility(View.GONE);
        } else {
            userVerifiedreason.setText("微博认证：" + user.verified_reason);
        }
    }


}
