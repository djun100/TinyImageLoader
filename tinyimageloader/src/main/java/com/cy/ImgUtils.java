package com.cy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;


import com.cy.tinyimageloader.EasyImageLoader;
import com.cy.tinyimageloader.display.BitmapDisplayer;

import java.io.File;

/**
 * @description: 图片工具类
 */

public class ImgUtils {

    public static EasyImageLoader getImageLoader(){
        return EasyImageLoader.getInstance(ContextUtils.getContext());
    }

    public static void load(File file, ImageView iv) {
        getImageLoader().setFile(file).setImageView(iv).bindBitmap();
    }

    public static void load(String url, ImageView iv) {
        getImageLoader().setUrl(url).setImageView(iv).bindBitmap();
    }

    public static void load(String url, ImageView iv, BitmapDisplayer displayer, Drawable loadingDrawable) {
        getImageLoader()
                .setUrl(url)
                .setImageView(iv)
                .setDisplayer(displayer)
                .setLoadingDrawable(loadingDrawable)
                .bindBitmap();
    }

    public static void loadOnProgress(String url, ImageView iv, final OnLoadListener requestListener) {
        getImageLoader().setUrl(url).setImageView(iv).setCallback(new EasyImageLoader.BitmapCallback() {
            @Override
            public void onResponse(Bitmap bitmap) {
                requestListener.onSuccess();
            }

            @Override
            public void onFail() {

            }
        }).bindBitmap();
    }

    public static void clear(ImageView iv) {
//        Glide.with(iv).clear(iv);
//        ImageLoaderUtils.getImageLoader().cancelDisplayTask(iv);
    }


    public static void clearMemory(Context context) {
//        Glide.get(context).clearMemory();
//        ImageLoaderUtils.getImageLoader().clearMemoryCache();
    }
    public static void trimMemory(Context context, int level) {
//        Glide.get(context).trimMemory(level);
    }

    public interface OnLoadListener{
        void onSuccess();
    }
}
