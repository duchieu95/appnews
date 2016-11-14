package com.example.store4life.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.store4life.Activity.Check_Status_Activity;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Model.History;
import com.example.store4life.Model.Product;
import com.example.store4life.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by TAI on 11/10/2016.
 */

public class History_Adapter extends RecyclerView.Adapter<History_Adapter.Holder_History> {
    public List<History> LSlist;
    public Context context;
    public History_Adapter (Context context, List<History> LSlist){

        this.context = context;
        this.LSlist = LSlist;
    }
    @Override
    public int getItemCount() {
        return LSlist.size();
    }
    public class Holder_History extends RecyclerView.ViewHolder {
        public TextView TenSP, Gia, NgayMua, TrangThai;
        public ImageView HinhSP;
        public Holder_History(View v) {
            super(v);
            TenSP = (TextView) v.findViewById(R.id.txtTen);
            Gia = (TextView) v.findViewById(R.id.txtGia);
            NgayMua = (TextView) v.findViewById(R.id.txtNgayMua);
            TrangThai = (TextView) v.findViewById(R.id.txtTrangThai);
            HinhSP = (ImageView) v.findViewById(R.id.imgLS);
        }
    }
    @Override
    public Holder_History onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_history, parent, false);

        return new Holder_History(itemView);
    }

    @Override
    public void onBindViewHolder(Holder_History holder, int position) {
        final History ls = LSlist.get(position);
        holder.TenSP.setText("Mã Hóa Đơn: "+ls.ID_HD);
        holder.Gia.setText("Tổng Tiền: "+doitien(ls.TONGTIEN+""));
        holder.NgayMua.setText(DoiNgay(ls.NGAYTAO_HD));

        final int check = ls.TRANGTHAI;
        if(check == 2){
            holder.TrangThai.setText("Trạng Thái: "+"Đã Giao");
        }else if(check == 1){
            holder.TrangThai.setText("Trạng Thái: "+"Đang Giao");
        }else {
            holder.TrangThai.setText("Trạng Thái: "+"Đang Xử Lí");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(context,Check_Status_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ID_HD",ls.ID_HD);
                    context.startActivity(intent);


            }
        });
        int color = Color.parseColor("#FF1493");
        holder.HinhSP.setColorFilter(color);
    }

    public String doitien(String x)
    {
        String chuoi=String.valueOf(x);
        double d=Double.parseDouble(chuoi);
        DecimalFormat f=new DecimalFormat("#,### VNĐ");
        return f.format(d);
    }

    public String DoiNgay(String date_s)  {
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
        Date date = null;
        try {
            date = dt.parse(date_s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return dt1.format(date);
    }
}
