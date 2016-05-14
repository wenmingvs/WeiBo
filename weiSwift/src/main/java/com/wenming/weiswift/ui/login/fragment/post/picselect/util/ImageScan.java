package com.wenming.weiswift.ui.login.fragment.post.picselect.util;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.wenming.weiswift.utils.ToastUtil;
import com.wenming.weiswift.ui.login.fragment.post.picselect.bean.AlbumFolderInfo;
import com.wenming.weiswift.ui.login.fragment.post.picselect.bean.ImageInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wenmingvs on 16/5/7.
 */
public abstract class ImageScan {

    private final static String[] IMAGE_PROJECTION = new String[]{
            MediaStore.Images.Media.DATA,//图片路径
            MediaStore.Images.Media.DISPLAY_NAME,//图片文件名，包括后缀名
            MediaStore.Images.Media.TITLE//图片文件名，不包含后缀
    };

    private final static int IMAGE_LOADER_ID = 1000;
    private ArrayList<AlbumFolderInfo> mFolderList;

    public ImageScan(Context context, LoaderManager loaderManager) {
        startScanImg(loaderManager, context);
    }

    private void startScanImg(LoaderManager loaderManager, final Context context) {
        mFolderList = new ArrayList<AlbumFolderInfo>();
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader imageCursorLoader = new CursorLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                return imageCursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data.getCount() == 0) {
                    ToastUtil.showShort(context, "没有扫描到图片");

                } else {
                    int dataColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DATA);//获取属性列的索引位置
                    ArrayList<File> albumFolderList = new ArrayList<>();//图片目录arraylist
                    HashMap<String, ArrayList<File>> albumImageListMap = new HashMap<>();//图片目录的hashmap，防止扫描统一目录下的图片，图片目录重复出现
                    while (data.moveToNext()) {
                        File imageFile = new File(data.getString(dataColumnIndex));//图片文件
                        File albumFolder = imageFile.getParentFile();//图片的父文件夹
                        if (!albumFolderList.contains(albumFolder)) {//图片目录arraylist中没有这个目录，就加入到里面
                            albumFolderList.add(albumFolder);
                        }

                        //检查此目录是否存在于hashmap当中，如果没有就添加进去
                        //<相册的绝对路径,相册底下的所有的图片>
                        String albumPath = albumFolder.getAbsolutePath();
                        ArrayList<File> albumImageFiles = albumImageListMap.get(albumPath);//
                        if (albumImageFiles == null) {
                            albumImageFiles = new ArrayList<>();
                            albumImageListMap.put(albumPath, albumImageFiles);
                        }
                        albumImageFiles.add(imageFile);//添加到对应的相册目录下面
                    }

                    sortByFileLastModified(albumFolderList);//对图片目录做排序

                    Set<String> keySet = albumImageListMap.keySet();
                    for (String key : keySet) {//对图片目录下所有的图片文件做排序
                        ArrayList<File> albumImageList = albumImageListMap.get(key);
                        sortByFileLastModified(albumImageList);
                    }

                    if (albumFolderList != null && albumFolderList.size() > 0 && albumImageListMap != null) {

                        ArrayList<AlbumFolderInfo> albumFolderInfoList = new ArrayList<>();

                        AlbumFolderInfo allImageFolder = createAllImageAlbum(context, albumImageListMap);
                        if (allImageFolder != null) {
                            albumFolderInfoList.add(allImageFolder);
                        }

                        int albumFolderSize = albumFolderList.size();
                        for (int albumFolderPos = 0; albumFolderPos < albumFolderSize; albumFolderPos++) {

                            File albumFolder = albumFolderList.get(albumFolderPos);
                            AlbumFolderInfo albumFolderInfo = new AlbumFolderInfo();

                            String folderName = albumFolder.getName();
                            albumFolderInfo.setFolderName(folderName);

                            String albumPath = albumFolder.getAbsolutePath();
                            List<File> albumImageList = albumImageListMap.get(albumPath);
                            File frontCover = albumImageList.get(0);
                            albumFolderInfo.setFrontCover(frontCover);//设置首张图片

                            List<ImageInfo> imageInfoList = ImageInfo.buildFromFileList(albumImageList);
                            albumFolderInfo.setImageInfoList(imageInfoList);
                            allImageFolder.getImageInfoList().addAll(imageInfoList);//保存到 "全部图片" 目录下

                            albumFolderInfoList.add(albumFolderInfo);
                        }
                        mFolderList = albumFolderInfoList;
                    }
                    onScanFinish(mFolderList);

                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
            }
        };
        loaderManager.initLoader(IMAGE_LOADER_ID, null, loaderCallbacks);//初始化指定id的Loader
    }


    /**
     * 创建一个"全部图片"目录
     *
     * @param albumImageListMap
     * @return
     */
    private AlbumFolderInfo createAllImageAlbum(Context context, Map<String, ArrayList<File>> albumImageListMap) {
        if (albumImageListMap != null) {
            AlbumFolderInfo albumFolderInfo = new AlbumFolderInfo();
            albumFolderInfo.setFolderName("相册胶卷");//设置目录名

            List<ImageInfo> totalImageInfoList = new ArrayList<>();
            albumFolderInfo.setImageInfoList(totalImageInfoList);//设置所有的图片文件

            boolean isFirstAlbum = true; //是否是第一个目录

            Set<String> albumKeySet = albumImageListMap.keySet();
            for (String albumKey : albumKeySet) {//每个目录的图片
                List<File> albumImageList = albumImageListMap.get(albumKey);
                if (isFirstAlbum == true) {
                    File frontCover = albumImageList.get(0);
                    albumFolderInfo.setFrontCover(frontCover);//设置第一张图片
                    isFirstAlbum = false;
                }
            }

            return albumFolderInfo;
        } else {
            return null;
        }
    }

    /**
     * 按照文件的修改时间进行排序，越最近修改的，排得越前
     */
    private void sortByFileLastModified(List<File> files) {
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.lastModified() > rhs.lastModified()) {
                    return -1;
                } else if (lhs.lastModified() < rhs.lastModified()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    public abstract void onScanFinish(ArrayList<AlbumFolderInfo> folderList);


}
