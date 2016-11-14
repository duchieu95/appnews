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

import com.bumptech.glide.Glide;
import com.example.store4life.Activity.DetailProduct_Activity;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Model.Product;
import com.example.store4life.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.DecimalFormat;
import java.util.List;



public class DetailCategory_Adapter extends RecyclerView.Adapter<DetailCategory_Adapter.MyViewHolder>  {

    public Context mContext;
    public List<Product> list;
    public DetailCategory_Adapter (Context mContext, List<Product> list){
        this.mContext = mContext;
        this.list = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TenSP, Gia;
        public ImageView Hinh;

        public MyViewHolder(View view) {
            super(view);
            TenSP = (TextView) view.findViewById(R.id.title);
            Gia = (TextView) view.findViewById(R.id.count);
            Hinh = (ImageView) view.findViewById(R.id.thumbnail);

        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public DetailCategory_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_detailcate, parent, false);
        return new DetailCategory_Adapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final DetailCategory_Adapter.MyViewHolder holder, int position) {
        final Product p = list.get(position);
        holder.TenSP.setText(p.TenSP);
        holder.Gia.setText(doitien(p.GiaSP+""));


        ImageLoader.getInstance().displayImage(AppConfig.URL_FolderUpload+list.get(position).HinhSP,holder.Hinh, new ImageLoadingListener() {
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailProduct_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("TENSP",p.TenSP);
                intent.putExtra("ID_SP",p.ID_SP);
                intent.putExtra("ID_LOAI",p.ID_LOAI);
                mContext.startActivity(intent);
            }
        });
    }
    public String doitien(String x)
    {
        String chuoi=String.valueOf(x);
        double d=Double.parseDouble(chuoi);
        DecimalFormat f=new DecimalFormat("#,### VNĐ");
        return f.format(d);
    }
}
