package com.example.store4life.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.store4life.Controller.AppConfig;
import com.example.store4life.Model.Product;
import com.example.store4life.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DetailBill_Adapter extends RecyclerView.Adapter<DetailBill_Adapter.Holder_Bill>  {
    public Context context;
    public List<Product> list;
    public DetailBill_Adapter(Context context,List<Product> list){
        this.context = context;
        this.list = list;
    }
    public class Holder_Bill extends RecyclerView.ViewHolder {
        public TextView TenSP, Gia, NgayMua,SoLuong;
        public ImageView HinhSP;
        public Holder_Bill(View v) {
            super(v);
            TenSP = (TextView) v.findViewById(R.id.txtTen);
            Gia = (TextView) v.findViewById(R.id.txtGia);
            NgayMua = (TextView) v.findViewById(R.id.txtNgayMua);
            SoLuong = (TextView) v.findViewById(R.id.txtsoluong);
            HinhSP = (ImageView) v.findViewById(R.id.imgLS);
        }
    }
    @Override
    public DetailBill_Adapter.Holder_Bill onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_bill, parent, false);

        return new DetailBill_Adapter.Holder_Bill(itemView);
    }

    @Override
    public void onBindViewHolder(DetailBill_Adapter.Holder_Bill holder, int position) {
        Product p = list.get(position);
        holder.TenSP.setText("Tên: "+p.TenSP);
        holder.Gia.setText("Giá: "+doitien(p.GiaSP+""));
        holder.NgayMua.setText("Ngày Mua: "+DoiNgay(p.NGAY));
        holder.SoLuong.setText("Số Lượng: "+p.SOLUONG);
        Glide.with(context).load(AppConfig.URL_FolderUpload+p.HinhSP).into(holder.HinhSP);
    }

    @Override
    public int getItemCount() {
        return list.size();
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
