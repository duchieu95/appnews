package com.example.store4life.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.store4life.Activity.DetailNew_Activity;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Model.News;
import com.example.store4life.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by duchieu on 10/10/2016.
 */

public class News_Adapter extends RecyclerView.Adapter<News_Adapter.MyViewHolder> {

    public Context context;
    public List<News> List;

    public News_Adapter(Context context,List<News> List) {
        this.context = context;
        this.List = List;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,date;
        public ImageView hinh;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtTieuDe);
            date = (TextView) view.findViewById(R.id.txtNgayDang);
            hinh = (ImageView) view.findViewById(R.id.ImgNews);
            /*view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailNew_Activity.class);
                    intent.putExtra("URL","4life.webstarterz.com");
                    intent.putExtra("TenKM",title.getText());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });*/
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_news, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final News news = List.get(position);
        holder.title.setText(news.TieuDe);
        holder.date.setText(DoiNgay(news.Ngay));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(context,DetailNew_Activity.class);
                intent.putExtra("ID",news.ID+"");
                intent.putExtra("TenKM",news.TieuDe);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);




        ImageLoader.getInstance().displayImage(AppConfig.URL_FolderUpload+List.get(position).Hinh,holder.hinh, new ImageLoadingListener() {
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


    }

    @Override
    public int getItemCount() {
        return List.size();
    }
    public String DoiNgay(String date_s)  {
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd");
        Date date = null;
        try {
            date = dt.parse(date_s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-mm-yyyy");
        return dt1.format(date);
    }
}
