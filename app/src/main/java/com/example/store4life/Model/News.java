package com.example.store4life.Model;

/**
 * Created by duchieu on 10/10/2016.
 */

public class News {
    public int ID;
    public String Hinh;
    public String TieuDe;
    public String Ngay;
    public String ChiTiet;

    public News(int ID,String TieuDe, String Hinh,String ChiTiet,String Ngay){
        this.ID = ID;
        this.TieuDe = TieuDe;
        this.Hinh = Hinh;
        this.ChiTiet = ChiTiet;
        this.Ngay = Ngay;
    }


}
