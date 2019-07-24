package com.cy.tinyimageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 简单封装的网络请求库，支持GET、POST、自动回调回UI线程、自动解析Json数据成javaBean对象
 * Created by wenhuaijun on 2016/4/24 0024.
 */
public class NetRequest {

    //通过url下载图片，未缩放图片宽高
    public static Bitmap downloadBitmapFromUrl(String  urlString){
        Bitmap bitmap =null;
        HttpURLConnection urlConnection =null;
        BufferedInputStream in =null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),8*1024);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(in!=null){
                    in.close();
                }
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    //通过url下载图片获取字节流写入输出流中
    public static boolean downloadUrlToStream(String imageUrl,OutputStream outputStream){

        HttpURLConnection urlConnection =null;
        BufferedInputStream in =null;
        BufferedOutputStream out =null;
        try {
            final URL url = new URL(imageUrl);
            urlConnection =(HttpURLConnection) url.openConnection();
            in =new BufferedInputStream(urlConnection.getInputStream(),8*1024);
            out =new BufferedOutputStream(outputStream,8*1024);
            int b;
            while ((b =in.read()) !=-1){
                out.write(b);
            }
            //写入成功返回true
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            try {
                if(in!=null){
                    in.close();
                }
                if(out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
