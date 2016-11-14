package com.example.store4life.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.store4life.Model.Comment;
import com.example.store4life.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Comment_Adapter extends BaseAdapter {
    public Context context;
    public ArrayList<Comment> list;

    public Comment_Adapter (Context context,ArrayList<Comment> list){
        this.context = context;
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
        Comment comment = list.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView=inflater.inflate(R.layout.custom_danhsachbl, null);
        TextView ten = (TextView) convertView.findViewById(R.id.txtten);
        TextView ngay = (TextView) convertView.findViewById(R.id.txtngay);
        TextView binhluan = (TextView) convertView.findViewById(R.id.txtnoidung);
        ten.setText(comment.TENTV);
        ngay.setText(DoiNgay(comment.Ngay));
        binhluan.setText(comment.NoiDung);
        return convertView;
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
