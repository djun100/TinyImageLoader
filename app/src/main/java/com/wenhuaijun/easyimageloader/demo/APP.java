package com.wenhuaijun.easyimageloader.demo;

import android.app.Application;

/**
 * Created by wenhuaijun on 2016/4/26 0026.
 */
public class APP extends Application{
  //  private int screenWidth;
    @Override
    public void onCreate() {
        super.onCreate();
        JUtils.initialize(this);
        JUtils.setDebug(true, "heheda");
        //screenWidth =JUtils.getScreenWidth();

    }
}
