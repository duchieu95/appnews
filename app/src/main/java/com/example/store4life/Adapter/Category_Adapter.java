package com.example.store4life.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Model.Category;
import com.example.store4life.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Category_Adapter extends BaseAdapter {

    public Context mContext;
    ArrayList<Category> list;

    public Category_Adapter(Context mContext, ArrayList<Category> list){
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.custom_category, null);
        TextView tv = (TextView) view.findViewById(R.id.textcategory);
        Category category = list.get(position);
        tv.setText(category.Ten);
        CircleImageView iv = (CircleImageView) view.findViewById(R.id.imgcategory);

        ImageLoader.getInstance().displayImage(AppConfig.URL_FolderUpload+list.get(position).Hinh,iv, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
/*
        int color = Color.parseColor("#FF1493");
        iv.setColorFilter(color);*/
        return view;
    }


}
