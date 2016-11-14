package com.example.store4life.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.store4life.Model.Cart;

import java.util.ArrayList;
import java.util.HashMap;

public class Database_4life extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "4life.sqlite";

    public static String TABLE_TV = "thanh_vien";
    public static String KEY_IDTV = "ID_TV";
    public static String KEY_TENTV = "TEN";
    public static String KEY_HINHTV = "HINH";
    public static String KEY_EMAILTV = "EMAIL";
    public static String KEY_MKTV = "MK";
    public static String KEY_DIACHITV = "DIACHI";
    public static String KEY_SDTTV = "SDT";
    public static String Key_GioiTinh = "GioiTinh";



    public static String TABLE_GIOHANG = "giohang";
    public static String KEY_IDSP = "ID_SP";
    public static String KEY_TENSP = "TENSP";
    public static String KEY_HINHSP = "HINHSP";
    public static String KEY_GIASP = "GIASP";
    public static String KEY_SOLUONG = "SOLUONG";
    public static String KEY_TONGTIEN = "TONGTIEN";
    public static String KEY_IDTVMUA = "ID_TVMUA";




    public Database_4life(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlThanhVien = "CREATE TABLE " + TABLE_TV +" ( " +
                KEY_IDTV +" INTEGER PRIMARY KEY ," +
                KEY_TENTV + " TEXT ," +
                KEY_HINHTV + " TEXT ," +
                KEY_EMAILTV + " TEXT ," +
                KEY_MKTV + " TEXT ," +
                KEY_DIACHITV + " TEXT ," +
                KEY_SDTTV + " TEXT ," +
                Key_GioiTinh + " INTEGER " +
                ")";
        db.execSQL(sqlThanhVien);


        String slqGioHang ="CREATE TABLE " + TABLE_GIOHANG +" ( " +
                KEY_IDSP + " INTEGER PRIMARY KEY ," +
                KEY_TENSP + " TEXT ," +
                KEY_HINHSP + " TEXT ," +
                KEY_GIASP + " INTEGER ," +
                KEY_SOLUONG + " INTEGER ," +
                KEY_TONGTIEN + " INTEGER ," +
                KEY_IDTVMUA +" INTEGER " +
                ")";
       db.execSQL(slqGioHang);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TV);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GIOHANG);
    }

    public void CreateTableUser(Integer IDTV,String TENTV,String HINH,String EMAILTV,String MK,String DIACHI,String SDT,Integer GioiTinh){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT COUNT (*) FROM "+TABLE_TV,null);
        c.moveToFirst();
        int count = c.getInt(0);
        if(count > 0){
            db.close();
        }else{
            ContentValues values=new ContentValues();
            values.put(KEY_IDTV,IDTV);
            values.put(KEY_TENTV,TENTV);
            values.put(KEY_HINHTV,HINH);
            values.put(KEY_EMAILTV,EMAILTV);
            values.put(KEY_MKTV,MK);
            values.put(KEY_DIACHITV,DIACHI);
            values.put(KEY_SDTTV,SDT);
            values.put(Key_GioiTinh,GioiTinh);
            long i = db.insert(TABLE_TV, null, values);
            Log.d("Them Thanh Cong Thanh Vien",i+"");
            db.close();

        }
    }
    public void LogOutUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TV, null, null);
        db.delete(TABLE_GIOHANG,null,null);
        db.close();
        Log.d("Da Xoa",TABLE_TV);
    }

  public HashMap<String,String  > GetUserHeader(){
        HashMap<String, String> user = new HashMap<String, String>();
        String qr = "SELECT  * FROM " + TABLE_TV;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(qr, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("ID_TV", cursor.getString(0));
            user.put("TEN", cursor.getString(1));
            user.put("HINH", cursor.getString(2));
            user.put("EMAIL", cursor.getString(3));
            user.put("MK", cursor.getString(4));
            user.put("DIACHI", cursor.getString(5));
            user.put("SDT", cursor.getString(6));
            user.put("GioiTinh", cursor.getString(7));
        }else{
            cursor.close();
            db.close();
        }
        cursor.close();
        db.close();
        Log.d("Database", "Da Doc User: " + user.toString());
        return user;
    }

    public void UpdateUser(Integer IDTV,String Ten,String HINH,String EMAIL,String MK,String DC,String DT,Integer GioiTinh){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TENTV, Ten);
        values.put(KEY_HINHTV,HINH);
        values.put(KEY_EMAILTV,EMAIL);
        values.put(KEY_MKTV,MK);
        values.put(KEY_DIACHITV,DC);
        values.put(KEY_SDTTV,DT);
        values.put(Key_GioiTinh,GioiTinh);
        long i =db.update(TABLE_TV, values, KEY_IDTV+"=?", new String[]{IDTV + ""});

        Log.d("Them Thanh Cong: " ,i+"");
    }
    public ArrayList<Cart> GetAllCart(){
        ArrayList<Cart> list=new ArrayList<Cart>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM "+TABLE_GIOHANG,null);
        if (c.moveToFirst())
        {
            do{
             int idsp = c.getInt(0);
             String tensp = c.getString(1);
             String hinh = c.getString(2);
             int gia =c.getInt(3);
             int soluong = c.getInt(4);
             int tongtien = c.getInt(5);
             int tvmua = c.getInt(6);
             list.add(new Cart(idsp,tensp,hinh,gia,soluong,tongtien,tvmua));
                db.close();
             Log.d("Da Doc",c+"");
            }while(c.moveToNext());
        }
        db.close();
        return list;
    }

    public boolean InsertCart(Integer IDSP,String TENSP,String HinhSP,Integer GiaSP,Integer SoLuong,Integer TongTien,String IDTVMua){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT COUNT(*) FROM "+TABLE_GIOHANG + " WHERE " + KEY_IDSP +" = " + IDSP,null);
        c.moveToFirst();
        int count = c.getInt(0);
        if(count > 0){
            c=db.rawQuery("SELECT SUM(" +KEY_SOLUONG + ") FROM "+TABLE_GIOHANG + " WHERE " + KEY_IDSP +" = " + IDSP,null);
            c.moveToFirst();
            int sum = c.getInt(0);
            ContentValues values = new ContentValues();
            values.put(KEY_SOLUONG, sum+SoLuong);
            values.put(KEY_TONGTIEN,(sum+SoLuong)*GiaSP);
            long i = db.update(TABLE_GIOHANG, values, KEY_IDSP+"=?", new String[]{IDSP + ""});
            Log.d("Da Update ",i+"");
            db.close();
            return true;
        }else{
            ContentValues values=new ContentValues();
            values.put(KEY_IDSP,IDSP);
            values.put(KEY_TENSP,TENSP);
            values.put(KEY_HINHSP,HinhSP);
            values.put(KEY_GIASP,GiaSP);
            values.put(KEY_SOLUONG,SoLuong);
            values.put(KEY_TONGTIEN,TongTien);
            values.put(KEY_IDTVMUA,IDTVMua);
            long i = db.insert(TABLE_GIOHANG, null, values);
            Log.d("Them Thanh Cong ", i+"");
        }

        return false;
}
    public  HashMap<String,String > TongTien (){
        HashMap<String, String> CART = new HashMap<String, String>();
        String qr="SELECT SUM(" +KEY_TONGTIEN + ") AS TIEN FROM "+TABLE_GIOHANG ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(qr, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            CART.put("TIEN", cursor.getString(0));
        }
        cursor.close();
        db.close();
        Log.d("Database", "Tong Tien : " + qr.toString());
        return CART;
    }
    public ArrayList<Cart> CHECK (){
        ArrayList<Cart> list=new ArrayList<Cart>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT ID_SP, SOLUONG FROM "+TABLE_GIOHANG,null);
        if (c.moveToFirst())
        {
            do{
                int idsp = c.getInt(0);
                int Soluong = c.getInt(1);
                list.add(new Cart(idsp,Soluong));
                Log.d("Da Doc",idsp+"");
            }while(c.moveToNext());
        }

        return list;
    }

    public boolean checkForTables(){
        SQLiteDatabase  db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " +TABLE_GIOHANG, null);
        if(cursor != null){
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if(count > 0){
                return true;
            }
            cursor.close();
        }
        return false;
    }
    public void UpdateSoLuongTongTien(int ID,int SoLuong,int Gia ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SOLUONG, SoLuong);
        values.put(KEY_TONGTIEN,Gia);
        db.update(TABLE_GIOHANG, values, KEY_IDSP+"=?", new String[]{ID + ""});
    }

    public void DeleteGioHang(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GIOHANG,null,null);
        db.close();
        Log.d("Da Xoa",TABLE_GIOHANG);
    }

    public void DeleteItemGioHang(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        long i = db.delete(TABLE_GIOHANG, KEY_IDSP +"=?", new String[]{id + ""});
        Log.d("Da Xoa:" ,i+"");
        db.close();
    }

    public HashMap<String,String > DetailProduct(int id){
        HashMap<String, String> user = new HashMap<String, String>();
        String qr = "SELECT  * FROM " + TABLE_GIOHANG + " WHERE " + KEY_IDSP + "=" + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(qr, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("ID_SP", cursor.getString(0));
            user.put("TENSP", cursor.getString(1));
            user.put("HINHSP", cursor.getString(2));
            user.put("GIASP", cursor.getString(3));
            user.put("SOLUONG", cursor.getString(4));
            user.put("TONGTIEN", cursor.getString(5));
            user.put("ID_TVMUA", cursor.getString(6));
        }
        cursor.close();
        db.close();
        Log.d("Database", "Da Chi Tiet : " + qr.toString());
        return user;
    }
}
