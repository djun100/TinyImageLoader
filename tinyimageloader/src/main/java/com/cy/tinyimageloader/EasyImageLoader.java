package com.cy.tinyimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.cy.tinyimageloader.display.BitmapDisplayer;
import com.cy.tinyimageloader.display.SimpleBitmapDisplayer;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 供外层使用的图片加载类，通过它实现图片加载
 * Created by Wenhuaijun on 2016/4/22 0022.
 */
public class EasyImageLoader {
    private static EasyImageLoader instance=null;
    private Context mContext;
    private static ImageLrucache imageLrucache;
    private static ImageDiskLrucache imageDiskLrucache;
    //创建一个静态的线程池对象
    private static ThreadPoolExecutor THREAD_POOL_EXECUTOR = null;
    //创建一个更新ImageView的UI的Handler
    private static TaskHandler mMainHandler;

    public static EasyImageLoader getInstance(Context context){
        if(instance==null){
            synchronized (EasyImageLoader.class){
                if(instance == null){
                    instance = new EasyImageLoader(context);
                }
            }
        }
        instance.clear();
        return instance;
    }

    private String mUrl;
    private ImageView mImageView;
    private BitmapCallback mCallback;
    private int mWidth;
    private int mHeight;
    private File mFile;
    private Drawable mLoadingDrawable;
    private BitmapDisplayer mDisplayer;

    public void clear(){
        mUrl=null;
        mImageView=null;
        mCallback=null;
        mWidth=0;
        mHeight=0;
        mFile=null;
        mLoadingDrawable=null;
        mDisplayer = new SimpleBitmapDisplayer();
    }

    public EasyImageLoader setUrl(String url) {
        this.mUrl = url;
        return this;
    }

    public EasyImageLoader setImageView(ImageView imageView) {
        this.mImageView = imageView;
        return this;
    }

    public EasyImageLoader setCallback(BitmapCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public EasyImageLoader setWidth(int width) {
        this.mWidth = width;
        return this;
    }

    public EasyImageLoader setHeight(int height) {
        this.mHeight = height;
        return this;
    }

    public EasyImageLoader setFile(File file) {
        mFile = file;
        return this;
    }

    public EasyImageLoader setLoadingDrawable(Drawable loadingDrawable) {
        mLoadingDrawable = loadingDrawable;
        return this;
    }

    public EasyImageLoader setDisplayer(BitmapDisplayer displayer) {
        mDisplayer = displayer;
        return this;
    }

    //私有的构造方法，防止在外部实例化该ImageLoader
    private EasyImageLoader(Context context){
        mContext =context.getApplicationContext();
        THREAD_POOL_EXECUTOR = ImageThreadPoolExecutor.getInstance();
        imageLrucache = new ImageLrucache();
        imageDiskLrucache = new ImageDiskLrucache(context);
        mMainHandler = new TaskHandler();
    }

    public void bindBitmap(){
        //防止加载图片的时候数据错乱
        // mImageView.setTag(TAG_KEY_URI, uri);
        mImageView.setTag(mUrl);

        //设置加载loadding图片
        if (mLoadingDrawable!=null) {
            mImageView.setImageDrawable(mLoadingDrawable);
//            mDisplayer.display(((BitmapDrawable) mLoadingDrawable).getBitmap(),mImageView);
        }

        if (mFile!=null && mFile.isFile()){
            Bitmap myBitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath());
//            mImageView.setImageBitmap(myBitmap);
            mDisplayer.display(myBitmap,mImageView);
            return;
        }
        //从内存缓存中获取bitmap
        Bitmap bitmap = imageLrucache.loadBitmapFromMemCache(mUrl);
        if(bitmap!=null){
//            mImageView.setImageBitmap(bitmap);
            mDisplayer.display(bitmap,mImageView);
            return;
        }
        LoadBitmapTask loadBitmapTask =new LoadBitmapTask(mContext,mMainHandler,mImageView,mUrl,mWidth,mHeight,mCallback,mDisplayer);
       //使用线程池去执行Runnable对象
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);

    }

    /**
     *
     * @param url 图片链接
     * @param callback bitmap回调接口
     * @param reqWidth 需求宽度
     * @param reqHeight 需求高度
     */
    public void getBitmap(final String url, final BitmapCallback callback,int reqWidth,int reqHeight){
        //从内存缓存中获取bitmap
        final Bitmap bitmap = imageLrucache.loadBitmapFromMemCache(url);
        if(bitmap!=null){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onResponse(bitmap);
                }
            });
            return;
        }
        LoadBitmapTask loadBitmapTask =new LoadBitmapTask(mContext,callback,url,reqWidth,reqHeight);
        //使用线程池去执行Runnable对象
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);

    }
    public void getBitmap(final String url,BitmapCallback callback){
       getBitmap(url,callback,0,0);

    }

    //返回内存缓存类
    public static ImageLrucache getImageLrucache(){
        if(imageLrucache==null){
            imageLrucache = new ImageLrucache();
        }
        return imageLrucache;
    }
    //返回本地缓存类
    public static ImageDiskLrucache getImageDiskLrucache(Context context){
        if(imageDiskLrucache==null){
            imageDiskLrucache = new ImageDiskLrucache(context);
        }
        return imageDiskLrucache;
    }
    public interface BitmapCallback{
       public void onResponse(Bitmap bitmap);
       public void onFail();
    }
}
