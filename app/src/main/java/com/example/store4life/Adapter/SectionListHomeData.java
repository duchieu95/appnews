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
import com.example.store4life.Activity.DetailProduct_Activity;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Model.Product;
import com.example.store4life.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;



public class SectionListHomeData extends RecyclerView.Adapter<SectionListHomeData.SingleItemRowHolder> {

    public ArrayList<Product> itemsList;
    public Context mContext;

    int ID_SP;
    public SectionListHomeData(Context context, ArrayList<Product> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }


    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder,final int position) {
        View view = null;
        final Product singleItem = itemsList.get(position);

        holder.tvTitle.setText(singleItem.TenSP);
        holder.tvTien.setText(doitien(singleItem.GiaSP+""));
        ID_SP = singleItem.ID_SP;

        ImageLoader.getInstance().displayImage(AppConfig.URL_FolderUpload+itemsList.get(position).HinhSP, holder.itemImage, new ImageLoadingListener() {
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
                intent.putExtra("TENSP",singleItem.TenSP);
                intent.putExtra("ID_SP",singleItem.ID_SP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle,tvTien;

        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.tvTien = (TextView) view.findViewById(R.id.tvTien);
        }

    }

    public String doitien(String x)
    {
        String chuoi=String.valueOf(x);
        double d=Double.parseDouble(chuoi);
       DecimalFormat f = new DecimalFormat("#,### VNĐ");
        return f.format(d);
    }
}
