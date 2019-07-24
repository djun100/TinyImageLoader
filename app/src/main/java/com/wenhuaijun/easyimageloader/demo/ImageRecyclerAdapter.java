package com.wenhuaijun.easyimageloader.demo;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.wenhuaijun.easyimageloader.R;

/**
 * Created by wenhuaijun on 2016/4/24 0024.
 */
public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageItemViewHolder>{
    private NetImage[] data;
    private boolean isScrolling =false;
    private int layoutManagerType =Constants.StagedGridLayoutStyle;
    public ImageRecyclerAdapter(NetImage[] data,int style) {
        this.data = data;
        this.layoutManagerType =style;
    }

    @Override
    public ImageItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ImageItemViewHolder holder, int position) {
//        holder.setLayoutParams(data[position],layoutManagerType);
        //如果RecyclerView设置了滑动监听则使用下列注释代码，优化图片加载性能，解决卡顿
        /*if(getItemCount()!=0){
            if (isScrolling) {
                holder.imageView.setImageResource(R.drawable.ic_loading);
            }else{
                holder.setData(data[position]);
            }
        }*/
        holder.setData(data[position],layoutManagerType);
    }

    @Override
    public int getItemCount() {
        if(data==null){
            return 0;
        }
        return data.length;
    }

    public NetImage[] getData() {
        return data;
    }
    public void clear(){
        data =null;
        notifyDataSetChanged();
    }

    public int getLayoutManagerType() {
        return layoutManagerType;
    }

    public void setLayoutManagerType(int layoutManagerType) {
        this.layoutManagerType = layoutManagerType;
    }

    public void setData(NetImage[] data) {
        this.data = data;
    }

    public boolean isScrolling() {
        return isScrolling;
    }


    public void setIsScrolling(boolean isScrolling) {
        this.isScrolling = isScrolling;
    }
}
