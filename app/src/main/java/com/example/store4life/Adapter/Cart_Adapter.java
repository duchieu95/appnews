package com.example.store4life.Adapter;


import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.store4life.Activity.Cart_Activity;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Database.Database_4life;
import com.example.store4life.Model.Cart;
import com.example.store4life.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.DecimalFormat;
import java.util.List;


public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.Holder_GoHang> {
    private List<Cart> gioHangList;
    public Context context;
    Database_4life db;
    public class Holder_GoHang extends RecyclerView.ViewHolder {
        public ImageView hinh;
        public TextView ten, danhmuc, tongtien;
        public EditText soluong;
        public ImageButton imbGiam,imbTang,btnxoa;
        public Holder_GoHang(View view) {
            super(view);
            hinh = (ImageView) view.findViewById(R.id.imgGiohang);
            ten = (TextView) view.findViewById(R.id.tenTV);
            danhmuc = (TextView) view.findViewById(R.id.DanhMucTV);
            soluong = (EditText) view.findViewById(R.id.edtSoLuong);
            tongtien = (TextView) view.findViewById(R.id.Tongtien);
            imbGiam = (ImageButton) view.findViewById(R.id.bttru);
            imbTang = (ImageButton) view.findViewById(R.id.btcong);
            btnxoa = (ImageButton) view.findViewById(R.id.btnxoa);
        }
    }


    public Cart_Adapter(Context context,List<Cart> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }
    @Override
    public Holder_GoHang onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_cart, parent, false);

        return new Holder_GoHang(itemView);
    }

    @Override
    public void onBindViewHolder(final Holder_GoHang holder, final int position) {
        View v;
        final Cart GioHang = gioHangList.get(position);
        db = new Database_4life(context);
        holder.ten.setText(GioHang.TENSP);
        holder.danhmuc.setText(doitien(GioHang.GIASP+""));
        holder.soluong.setText(GioHang.SOLUONG+"");
        holder.tongtien.setText(doitien(GioHang.TONGTIEN+""));
        holder.btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              db.DeleteItemGioHang(GioHang.IDSP);
                notifyDataSetChanged();
                gioHangList.remove(position);
            }
        });

        ImageLoader.getInstance().displayImage(AppConfig.URL_FolderUpload+gioHangList.get(position).HINHSP,holder.hinh, new ImageLoadingListener() {
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

        holder.imbTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer   s1 = Integer.parseInt(holder.soluong.getText().toString());
                    int tong = s1 + 1;
                    holder.soluong.setText(tong + "");
                    int tongtien = GioHang.GIASP * tong;
                    holder.tongtien.setText(doitien(tongtien+""));
                    holder.soluong.setText(tong+"");
                    db.UpdateSoLuongTongTien(GioHang.IDSP,tong,tongtien);

            }
        });

        holder.imbGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sl =1;
                sl = Integer.parseInt(holder.soluong.getText().toString());

                if(sl <= 1){
                    Toast.makeText(context,"Không Được Giảm",Toast.LENGTH_SHORT).show();
                    holder.imbGiam.setEnabled(false);
                    holder.imbTang.setEnabled(true);
                }else{
                    holder.imbTang.setEnabled(true);
                    int tong = sl - 1 ;
                    holder.soluong.setText(tong+"");
                    int tongtien = GioHang.GIASP * tong;
                    holder.tongtien.setText(doitien(tongtien+""));
                    db.UpdateSoLuongTongTien(GioHang.IDSP,tong,tongtien);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }
    public String doitien(String x)
    {
        String chuoi=String.valueOf(x);
        double d=Double.parseDouble(chuoi);
        DecimalFormat f=new DecimalFormat("#,### VNĐ");
        return f.format(d);
    }
}