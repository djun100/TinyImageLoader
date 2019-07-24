package com.cy.tinyimageloader;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;


/**
 * Created by Administrator on 2016/5/1 0001.
 */
public class TaskHandler extends Handler{
    public TaskHandler() {
        super(Looper.getMainLooper());
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        //给iamgeView加载bitmap
        TaskResult result =(TaskResult) msg.obj;
        ImageView imageView =result.imageView;
        //判断是否数据错乱
        String uri =(String)imageView.getTag();
        if (uri.equals(result.uri)){
            if(result.bitmap!=null){
//                imageView.setImageBitmap(result.bitmap);
                result.mDisplayer.display(result.bitmap,imageView);
            }else {
//                imageView.setImageResource(R.drawable.ic_error);
                Log.e("tag","数据错乱");
            }

        }else{
            Log.i("tag","不是最新数据");
        }
    }
}
